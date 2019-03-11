package business.gmail;

import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.MessagePartHeader;
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

    // TODO: use watch
    public void getThreads() throws IOException {
        ListThreadsResponse threadsResponse = gmailService.getGmail().users().threads().list("me").setQ("from:noreply@notifications.freelancer.com is:unread").execute();
        List<Thread> threads = threadsResponse.getThreads();
        if (threads.isEmpty()) {
            System.out.println("No threads found.");
        } else {
            System.out.println("Threads:");
            for (Thread thread: threads) {
                Thread threadData = gmailService.getGmail().users().threads().get("me", thread.getId()).execute();
                List<MessagePartHeader> headers =  threadData.getMessages().get(0).getPayload().getHeaders();
                for (MessagePartHeader header: headers)
                    if (header.getName().equals("Subject"))
                        System.out.printf("- %s\n", header.getValue());
            }
        }
    }

}
