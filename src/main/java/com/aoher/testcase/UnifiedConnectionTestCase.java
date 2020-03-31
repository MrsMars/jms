package com.aoher.testcase;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import com.aoher.provider.Provider;
import com.aoher.provider.ProviderLoader;
import org.junit.After;
import org.junit.Before;

import java.security.InvalidParameterException;

public abstract class UnifiedConnectionTestCase extends ProviderLoader {

    private Provider admin;

    protected QueueConnectionFactory queueFactory;
    protected QueueConnection queueConnection;
    protected QueueSession queueSession;
    protected Queue queue;
    protected String queueName;

    protected TopicConnectionFactory topicFactory;
    protected TopicConnection topicConnection;
    protected TopicSession topicSession;
    protected Topic topic;
    protected String topicName;

    protected abstract String getTestName();

    @Before
    public void setUp() {

        try {

            if ((queueName = getTestName()) == null) {
                throw new InvalidParameterException("test name is null");
            }
            if ((topicName = getTestName()) == null) {
                throw new InvalidParameterException("test name is null");
            }
            if ((admin = getProvider()) == null) {
                throw new InvalidParameterException("jms provider is null");
            }

            queueName += "_queue";
            topicName += "_topic";

            if (queueFactory == null)
                queueFactory = admin.createQueueConnectionFactory(qcfName);
            if (topicFactory == null)
                topicFactory = admin.createTopicConnectionFactory(tcfName);

            if (queue == null)
                queue = admin.createQueue(queueName);
            if (topic == null)
                topic = admin.createTopic(topicName);

            queueConnection = queueFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            topicConnection = topicFactory.createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            queueConnection.start();
            topicConnection.start();

        } catch (Exception e) {
            // XXX
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            if (queueConnection != null)
                queueConnection.close();
            if (topicConnection != null)
                topicConnection.close();

            if (admin != null) {
                admin.deleteTopicConnectionFactory(tcfName);
                admin.deleteQueueConnectionFactory(qcfName);
                admin.deleteTopic(topic);
                admin.deleteQueue(queue);
                admin.disconnect();
                admin = null;
            }

        } catch (Exception e) {
            // XXX
            e.printStackTrace();
        } finally {
            queueFactory = null;
            queueConnection = null;
            queueSession = null;
            queue = null;

            topicFactory = null;
            topicConnection = null;
            topicSession = null;
            topic = null;
        }
    }

}