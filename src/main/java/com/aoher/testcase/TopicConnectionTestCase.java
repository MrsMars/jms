package com.aoher.testcase;

import com.aoher.provider.Provider;
import com.aoher.provider.ProviderLoader;
import org.junit.After;
import org.junit.Before;

import javax.jms.*;
import java.security.InvalidParameterException;

public abstract class TopicConnectionTestCase extends ProviderLoader {

    private Provider admin;
    private String topicName;

    protected Topic publisherTopic;
    protected TopicPublisher publisher;
    protected TopicConnectionFactory publisherTCF;
    protected TopicConnection publisherConnection;
    protected TopicSession publisherSession;

    protected Topic subscriberTopic;
    protected TopicSubscriber subscriber;
    protected TopicConnectionFactory subscriberTCF;
    protected TopicConnection subscriberConnection;
    protected TopicSession subscriberSession;

    protected abstract String getTestName();

    @Before
    public void setUp() {
        try {

            if ((topicName = getTestName()) == null) {
                throw new InvalidParameterException("test name is null");
            }
            if ((admin = getProvider()) == null) {
                throw new InvalidParameterException("jms provider is null");
            }

            if (publisherTCF == null)
                publisherTCF = admin.createTopicConnectionFactory(topicName);
            if (publisherTopic == null)
                publisherTopic = admin.createTopic(topicName);

            publisherConnection = publisherTCF.createTopicConnection();
            if (publisherConnection.getClientID() == null) {
                publisherConnection.setClientID("publisherConnection");
            }

            publisherSession = publisherConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            publisher = publisherSession.createPublisher(publisherTopic);

            if (subscriberTCF == null)
                subscriberTCF = admin.createTopicConnectionFactory(topicName);
            if (subscriberTopic == null)
                subscriberTopic = admin.createTopic(topicName);

            subscriberConnection = subscriberTCF.createTopicConnection();
            if (subscriberConnection.getClientID() == null) {
                subscriberConnection.setClientID("subscriberConnection");
            }

            subscriberSession = subscriberConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            subscriber = subscriberSession.createSubscriber(subscriberTopic);

            publisherConnection.start();
            subscriberConnection.start();

        } catch (Exception e) {
            // XXX
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            if (publisherConnection != null)
                publisherConnection.close();
            if (subscriberConnection != null)
                subscriberConnection.close();

            if (admin != null) {
                admin.deleteTopicConnectionFactory(topicName);
                admin.deleteTopic(subscriberTopic);
                admin.disconnect();
                admin = null;
            }
        } catch (Exception e) {
            // XXX
            e.printStackTrace();
        } finally {
            publisherTopic = null;
            publisher = null;
            publisherTCF = null;
            publisherSession = null;
            publisherConnection = null;

            subscriberTopic = null;
            subscriber = null;
            subscriberTCF = null;
            subscriberSession = null;
            subscriberConnection = null;
        }
    }
}