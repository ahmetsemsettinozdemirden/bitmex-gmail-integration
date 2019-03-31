package business.scheduler;

import business.exceptions.ClientException;
import business.scheduler.jobs.GmailToBitmexJob;
import business.settings.SettingsService;
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
import java.util.concurrent.CompletableFuture;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Construction and management of Quartz Scheduler instance. Main business logic job scheduled in this class.
 */
@Singleton
public class QuartzScheduler {

    private final Injector guice;
    private final ApplicationLifecycle lifecycle;
    private final SettingsService settingsService;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    private Scheduler scheduler;

    @Inject
    public QuartzScheduler(Injector guice, ApplicationLifecycle lifecycle, SettingsService settingsService) {
        this.guice = guice;
        this.lifecycle = lifecycle;
        this.settingsService = settingsService;
    }

    /**
     * Starts Quartz scheduler instance.
     * @throws SchedulerException Already started or any error with scheduler.
     */
    public void start() throws SchedulerException {
        checkSchedulerInitialized();
        scheduler.start();
    }

    /**
     * Pauses Quartz scheduler instance.
     * @throws SchedulerException Already stopped or any error with scheduler.
     */
    public void stop() throws SchedulerException {
        checkSchedulerInitialized();
        scheduler.standby();
    }

    /**
     * Triggers a already scheduled job.
     * @param jobName Name of the job.
     * @param jobGroup Group of the job.
     * @throws SchedulerException On job error.
     * @throws ClientException Job does not exist.
     */
    public void triggerJob(String jobName, String jobGroup) throws SchedulerException, ClientException {
        checkSchedulerInitialized();
        if (!scheduler.checkExists(JobKey.jobKey(jobName, jobGroup)))
            throw new ClientException("jobnotfound", "jobName or jobGroup is wrong!");
        scheduler.triggerJob(JobKey.jobKey(jobName, jobGroup));
    }

    private void checkSchedulerInitialized() {
        if (scheduler == null)
            throw new RuntimeException("Quartz Scheduler is not initialized! Make sure that initialized() is successfully completed.");
    }

    /**
     * Constructs Quartz scheduler instance and schedules Bitmex job for given time interval.
     * @throws SchedulerException On scheduler error.
     */
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
        scheduleJob(GmailToBitmexJob.class, Integer.parseInt(settingsService.getSetting("timeInterval").getValue()));
        logger.debug("job names: " + scheduler.getJobKeys(GroupMatcher.anyJobGroup()));

        scheduler.start();
        logger.info("QuartzWorldScheduler started.");
    }

    private void scheduleJob(Class<? extends Job> clazz, int seconds) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        scheduler.scheduleJob(
                newJob(clazz)
                        .withIdentity(clazz.getSimpleName(), "default")
                        .usingJobData(jobDataMap).build(),
                newTrigger()
                        .withIdentity(clazz.getSimpleName(), "default")
                        .withSchedule(repeatSecondlyForever(seconds))
                        .build());
    }

}
