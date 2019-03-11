package business.gmail;

import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyThreadRequest;
import com.google.api.services.gmail.model.Thread;
import play.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * for more info: https://developers.google.com/gmail/api/quickstart/java
 */
public class GmailHelper {

    private final GmailService gmailService;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public GmailHelper(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    public String getTradingViewTip() throws IOException {
        ListThreadsResponse threadsResponse = gmailService.getGmail().users().threads().list("me")
                .setQ("from:ahmetozdemirden@std.iyte.edu.tr is:unread").execute();
        List<Thread> threads = threadsResponse.getThreads();
        if (threads == null || threads.isEmpty()) {
            throw new RuntimeException("no new messages");
        } else {
            String msg = "";
            for (Thread thread: threads) {
                Thread threadData = gmailService.getGmail().users().threads().get("me", thread.getId()).execute();
                List<MessagePartHeader> headers =  threadData.getMessages().get(0).getPayload().getHeaders();
                for (MessagePartHeader header: headers) {
                    if (header.getName().equals("Subject")) {
                        logger.debug("- {}\n", header.getValue());
                        msg = header.getValue();
                    }
                }
                gmailService.getGmail().users().threads().trash("me", threadData.getId()).execute();
            }
            return msg;
        }
    }

}
