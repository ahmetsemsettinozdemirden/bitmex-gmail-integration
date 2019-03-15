package business.gmail;

import business.exceptions.ClientException;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Threads;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Thread;
import play.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * for more info: https://developers.google.com/gmail/api/quickstart/java
 */
public class GmailHelper {

    // TODO: conf
    private static final String FROM_MAIL = "ahmetozdemirden@std.iyte.edu.tr";

    private final GmailService gmailService;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public GmailHelper(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    public List<String> getTradingViewTips() throws IOException, ClientException {
        List<String> tips = new ArrayList<>();
        List<Gmail> gmails = gmailService.getGmails();
        for (Gmail gmail: gmails) {
            Threads gmailThreads = gmail.users().threads();
            List<Thread> threads = gmailThreads.list("me")
                    .setQ("from:" + FROM_MAIL + " is:unread")
                    .execute().getThreads();
            if (threads != null) {
                for (Thread thread: threads) {
                    Thread threadData = gmailThreads.get("me", thread.getId()).execute();
                    List<MessagePartHeader> headers =  threadData.getMessages().get(0).getPayload().getHeaders();
                    for (MessagePartHeader header: headers) {
                        if (header.getName().equals("Subject")) {
                            logger.debug("- {}", header.getValue());
                            tips.add(header.getValue());
                        }
                    }
                    gmailThreads.trash("me", threadData.getId()).execute();
                }
                return tips;
            }
        }
        throw new ClientException("nogmailservices", "No gmail services exist.");
    }

}
