package business.scheduler;

import business.exceptions.ClientException;
import business.scheduler.jobs.GmailToBitmexJob;
import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Singleton
public class QuartzScheduler {

    private final Injector guice;
    private final ApplicationLifecycle lifecycle;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    private Scheduler scheduler;

    @Inject
    public QuartzScheduler(Injector guice, ApplicationLifecycle lifecycle) {
        this.guice = guice;
        this.lifecycle = lifecycle;
    }

    public void start() throws SchedulerException {
        checkSchedulerInitialized();
        scheduler.start();
    }

    public void stop() throws SchedulerException {
        checkSchedulerInitialized();
        scheduler.standby();
    }

    public void triggerJob(String jobName, String jobGroup) throws SchedulerException, ClientException {
        checkSchedulerInitialized();
        if (scheduler.checkExists(JobKey.jobKey(jobName, jobGroup)))
            throw new ClientException("jobnotfound", "jobName or jobGroup is wrong!");
        scheduler.triggerJob(JobKey.jobKey(jobName, jobGroup));
    }

    private void checkSchedulerInitialized() {
        if (scheduler == null)
            throw new RuntimeException("Quartz Scheduler is not initialized! Make sure that initialized() is successfully completed.");
    }

    public void initialize() throws SchedulerException {

        if (scheduler != null)
            throw new RuntimeException("Quartz Scheduler already initialized!");

        logger.info("QuartzWorldScheduler is starting...");
        lifecycle.addStopHook(() -> CompletableFuture.runAsync(() -> {
            try {
                scheduler.shutdown();
                logger.info("scheduler successfully shutdown.");
            } catch (Exception e) {
                logger.error("scheduler shutdown error", e);
            }
        }));

        logger.info("Construct scheduler...");
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.setJobFactory(guice.getInstance(GuiceJobFactory.class));
        scheduler.clear();

        logger.info("Schedule jobs...");
        scheduleJob(GmailToBitmexJob.class, "0 * * * * ?"); // TODO: seconds instead of cron
        logger.debug("job names: " + scheduler.getJobKeys(GroupMatcher.anyJobGroup()));

        scheduler.start();
        logger.info("QuartzWorldScheduler started.");
    }

    private void scheduleJob(Class<? extends Job> clazz, String cron) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        scheduler.scheduleJob(
                newJob(clazz)
                        .withIdentity(clazz.getName(), "default")
                        .usingJobData(jobDataMap).build(),
                newTrigger()
                        .withIdentity(clazz.getName(), "default")
                        .withSchedule(cronSchedule(cron).inTimeZone(TimeZone.getTimeZone("GMT+03:00")))
                        .build());
    }

}
