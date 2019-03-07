package business.scheduler.jobs;

import business.main.GmailToBitmex;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.inject.Inject;

public class GmailToBitmexJob implements Job {

    public final GmailToBitmex gmailToBitmex;

    @Inject
    public GmailToBitmexJob(GmailToBitmex gmailToBitmex) {
        this.gmailToBitmex = gmailToBitmex;
    }

    @Override
    public void execute(JobExecutionContext context) {
        gmailToBitmex.execute();
    }

}
