package com.aoher.testcase;

import com.aoher.provider.Provider;
import com.aoher.provider.ProviderLoader;

import javax.jms.*;
import java.security.InvalidParameterException;

public abstract class QueueConnectionTestCase extends ProviderLoader {

    private Provider admin;
    private String queueName;

    protected Queue senderQueue;
    protected QueueSender sender;
    protected QueueConnectionFactory senderQCF;
    protected QueueConnection senderConnection;
    protected QueueSession senderSession;

    protected Queue receiverQueue;
    protected QueueReceiver receiver;
    protected QueueConnectionFactory receiverQCF;
    protected QueueConnection receiverConnection;
    protected QueueSession receiverSession;

    protected abstract String getTestName();

    @Before
    public void setUp() {
        try {

            if ((queueName = getTestName()) == null) {
                throw new InvalidParameterException("test name is null");
            }
            if ((admin = getProvider()) == null) {
                throw new InvalidParameterException("jms provider is null");
            }

            if (senderQCF == null)
                senderQCF = admin.createQueueConnectionFactory(qcfName);
            if (senderQueue == null)
                senderQueue = admin.createQueue(queueName);

            senderConnection = senderQCF.createQueueConnection();
            senderSession = senderConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            sender = senderSession.createSender(senderQueue);

            if (receiverQCF == null)
                receiverQCF = admin.createQueueConnectionFactory(qcfName);
            if (receiverQueue == null)
                receiverQueue = admin.createQueue(queueName);

            receiverConnection = receiverQCF.createQueueConnection();
            receiverSession = receiverConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            receiver = receiverSession.createReceiver(receiverQueue);

            senderConnection.start();
            receiverConnection.start();

        } catch (Exception e) {
            // XXX
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            if (senderConnection != null)
                senderConnection.close();

            if (receiverConnection != null)
                receiverConnection.close();

            if (admin != null) {
                admin.deleteQueueConnectionFactory(qcfName);
                admin.deleteQueue(senderQueue);
                admin.disconnect();
                admin = null;
            }
        } catch (Exception e) {
            // XXX
            e.printStackTrace();
        } finally {
            senderQueue = null;
            sender = null;
            senderQCF = null;
            senderSession = null;
            senderConnection = null;

            receiverQueue = null;
            receiver = null;
            receiverQCF = null;
            receiverSession = null;
            receiverConnection = null;
        }
    }
}
