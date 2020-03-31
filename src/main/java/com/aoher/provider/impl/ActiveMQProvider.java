package com.aoher.provider.impl;

import com.aoher.provider.Provider;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class ActiveMQProvider implements Provider {

    private static final String NAME = "activemq";

    private Map<String, Object> factoryMap = new HashMap<>();
    private Map<String, Object> queueMap = new HashMap<>();
    private Map<String, Object> topicMap = new HashMap<>();

    private QueueConnection queueConnection = null;
    private QueueSession queueSession = null;
    private TopicConnection topicConnection = null;
    private TopicSession topicSession = null;

    public ActiveMQProvider() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ConnectionFactory createConnectionFactory(String name) {
        return (ConnectionFactory) factoryMap.computeIfAbsent(name, e -> factoryMap.put(name, new ActiveMQProvider()));
    }

    @Override
    public QueueConnectionFactory createQueueConnectionFactory(String name) {
        return (QueueConnectionFactory) factoryMap.computeIfAbsent(name, e -> factoryMap.put(name, new ActiveMQProvider()));
    }

    @Override
    public TopicConnectionFactory createTopicConnectionFactory(String name) {
        return (TopicConnectionFactory) factoryMap.computeIfAbsent(name, e -> factoryMap.put(name, new ActiveMQProvider()));
    }

    @Override
    public Queue createQueue(String name) throws JMSException {
        Object result = queueMap.get(name);
        if (result == null) {
            if (queueSession == null) {
                QueueConnectionFactory factory = new ActiveMQConnectionFactory();
                queueConnection = factory.createQueueConnection();
                queueSession = queueConnection.createQueueSession(false, AUTO_ACKNOWLEDGE);
            }
            result = queueSession.createQueue(name);
            queueMap.put(name, result);
        }
        return (Queue) result;
    }

    @Override
    public Topic createTopic(String name) throws JMSException {
        Object result = topicMap.get(name);
        if (result == null) {
            if (topicSession == null) {
                TopicConnectionFactory factory = new ActiveMQConnectionFactory();
                topicConnection = factory.createTopicConnection();
                topicSession = topicConnection.createTopicSession(false, AUTO_ACKNOWLEDGE);
            }
            result = topicSession.createTopic(name);
            queueMap.put(name, result);
        }
        return (Topic) result;
    }

    @Override
    public void deleteQueue(Destination queue) throws JMSException {
        ActiveMQDestination destImpl = (ActiveMQDestination) queue;
        queueMap.remove(destImpl);

        if (queueConnection != null) {
            ActiveMQConnection conn = (ActiveMQConnection) queueConnection;
            conn.destroyDestination((ActiveMQDestination) queue);
        }
    }

    @Override
    public void deleteTopic(Destination topic) throws JMSException {
        ActiveMQDestination destImpl = (ActiveMQDestination) topic;
        topicMap.remove(destImpl);

        if (topicConnection != null) {
            ActiveMQConnection conn = (ActiveMQConnection) topicConnection;
            conn.destroyDestination((ActiveMQDestination) topic);
        }
    }

    @Override
    public void deleteConnectionFactory(String name) {
        factoryMap.remove(name);
    }

    @Override
    public void deleteQueueConnectionFactory(String name) {
        factoryMap.remove(name);
    }

    @Override
    public void deleteTopicConnectionFactory(String name) {
        factoryMap.remove(name);
    }

    @Override
    public void disconnect() throws JMSException {
        if (queueConnection != null) {
            queueConnection.close();
            queueConnection = null;
        }

        if (topicConnection != null) {
            topicConnection.close();
            topicConnection = null;
        }
    }
}
