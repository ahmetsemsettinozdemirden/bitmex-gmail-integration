package business.gmail;

import business.exceptions.ClientException;
import business.settings.SettingsService;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Thread;
import play.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Includes Gmail business logic like checking emails.
 * for more info: https://developers.google.com/gmail/api/quickstart/java
 */
public class GmailHelper {

    private final GmailService gmailService;
    private final SettingsService settingsService;
    private final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    public GmailHelper(GmailService gmailService, SettingsService settingsService) {
        this.gmailService = gmailService;
        this.settingsService = settingsService;
    }

    /**
     * Checks all emails from repository and filters all TradingView tips. Then deletes email.
     * @return TradingView tips.
     * @throws IOException Connection error.
     * @throws ClientException Gmail account error.
     */
    public List<String> getTradingViewTips() throws IOException, ClientException {
        List<String> tips = new ArrayList<>();
        List<Gmail> gmails = gmailService.getGmails();
        for (Gmail gmail: gmails) {
            Gmail.Users.Threads gmailThreads = gmail.users().threads();
            List<Thread> threads = gmailThreads.list("me")
                    .setQ("from:" + settingsService.getSetting("fromMail").getValue() + " is:unread")
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
            }
            return tips;
        }
        throw new ClientException("nogmailservices", "No gmail services exist.");
    }

}
