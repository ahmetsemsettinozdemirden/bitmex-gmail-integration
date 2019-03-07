package business.scheduler;

import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import play.Logger;

import javax.inject.Inject;

public class GuiceJobFactory implements JobFactory {

    private final Injector guice;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public GuiceJobFactory(final Injector guice) {
        this.guice = guice;
    }

    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) {
        // Get the job detail so we can get the job class
        JobDetail jobDetail = triggerFiredBundle.getJobDetail();
        Class jobClass = jobDetail.getJobClass();

        try {
            // Get a new instance of that class from Guice so we can do dependency injection
            return (Job) guice.getInstance(jobClass);
        } catch (Exception e) {
            // Something went wrong.  Print out the stack trace here so SLF4J doesn't hide it.
            logger.error("new quartz job instantiation error", e);

            // Rethrow the exception as an UnsupportedOperationException
            throw new UnsupportedOperationException(e);
        }
    }
}