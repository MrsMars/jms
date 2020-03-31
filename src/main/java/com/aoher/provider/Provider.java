package com.aoher.provider;

import javax.jms.*;
import java.net.ConnectException;

/**
 * Simple JMS provider interface. <br />
 * JMS Provider has to implement this simple interface to be able to use the test suite.
 */
public interface Provider {

    /**
     * Returns the name of the JMS Provider.
     *
     * @return name of the JMS Provider
     */
    String getName();

    /**
     * Creates a <code>ConnectionFactory</code>.
     *
     * @since JMS 1.1
     * @param name
     *            of the <code>ConnectionFactory</code>
     */
    ConnectionFactory createConnectionFactory(String name) throws ConnectException;

    /**
     * Creates a <code>QueueConnectionFactory</code>.
     *
     * @param name
     *            of the <code>QueueConnectionFactory</code>
     */
    QueueConnectionFactory createQueueConnectionFactory(String name) throws ConnectException;

    /**
     * Creates a <code>TopicConnectionFactory</code>.
     *
     * @param name
     *            of the <code>TopicConnectionFactory</code>
     */
    TopicConnectionFactory createTopicConnectionFactory(String name) throws ConnectException;

    /**
     * Creates a <code>Queue</code>.
     *
     * @param name
     *            of the <code>Queue</code>
     */
    Queue createQueue(String name) throws ConnectException, JMSException;

    /**
     * Creates a <code>Topic</code>.
     *
     * @param name
     *            of the <code>Topic</code>
     */
    Topic createTopic(String name) throws ConnectException, JMSException;

    /**
     * Removes the <code>Queue</code>
     *
     * @param queue
     */
    void deleteQueue(Destination queue) throws ConnectException, JMSException;

    /**
     * Removes the <code>Topic</code>
     *
     * @param topic
     */
    void deleteTopic(Destination topic) throws ConnectException, JMSException;

    /**
     * Removes the <code>ConnectionFactory</code> of name <code>name</code> from JNDI and deletes it
     *
     * @since JMS 1.1
     * @param name
     *            JNDI name of the <code>ConnectionFactory</code>
     */
    void deleteConnectionFactory(String name);

    /**
     * Removes the <code>QueueConnectionFactory</code> of name <code>name</code> from JNDI and deletes it
     *
     * @param name
     *            JNDI name of the <code>QueueConnectionFactory</code>
     */
    void deleteQueueConnectionFactory(String name);

    /**
     * Removes the <code>TopicConnectionFactory</code> of name <code>name</code> from JNDI and deletes it
     *
     * @param name
     *            JNDI name of the <code>TopicConnectionFactory</code>
     */
    void deleteTopicConnectionFactory(String name);

    void disconnect() throws JMSException;
}
