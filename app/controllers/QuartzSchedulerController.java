package controllers;

import business.exceptions.ClientException;
import business.handlers.ErrorHandler;
import business.scheduler.QuartzScheduler;
import org.quartz.SchedulerException;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class QuartzSchedulerController extends Controller {

    private final QuartzScheduler quartzScheduler;
    private final ErrorHandler errorHandler;

    @Inject
    public QuartzSchedulerController(QuartzScheduler quartzScheduler, ErrorHandler errorHandler) {
        this.quartzScheduler = quartzScheduler;
        this.errorHandler = errorHandler;
    }

    public Result status(String status) {
        try {
            if ("start".equals(status)) {
                quartzScheduler.start();
            } else if ("stop".equals(status)) {
                quartzScheduler.stop();
            } else {
                return errorHandler.onClientError(BAD_REQUEST, "scheduler-status-badstatus",
                        "unknown status: '" + status + "'",
                        request().method() + " " + request().uri());
            }
        } catch (SchedulerException e) {
            return errorHandler.onServerError("scheduler-status-exception", e,
                    request().method() + " " + request().uri());
        }
        return ok();
    }

    public Result trigger(String jobName, String jobGroup) {
        try {
            quartzScheduler.triggerJob(jobName, jobGroup);
        } catch (ClientException e) {
            return errorHandler.onClientError(BAD_REQUEST, "scheduler-trigger-" + e.getErrorCode(),
                    e.getMessage(), request().method() + " " + request().uri());
        } catch (SchedulerException e) {
            return errorHandler.onServerError("scheduler-trigger-exception", e,
                    request().method() + " " + request().uri());
        }
        return ok();
    }

}
