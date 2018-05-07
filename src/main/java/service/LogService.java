package service;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;

import javax.ejb.Stateless;

/**
 * @author Thom van de Pas on 14-3-2018
 */
@Stateless
public class LogService {

    public void logOverloadRequestError(String message) {
        Sentry.init();

        SentryClient sentryClient = SentryClientFactory.sentryClient();
        Sentry.capture(message);
    }
}
