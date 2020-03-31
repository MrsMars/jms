package com.aoher.provider;

import com.aoher.provider.impl.ActiveMQProvider;

public abstract class ProviderLoader {

    //QueueFactory Name
    protected static final String QCF_NAME = "testQCF";

    //TopicFactory Name
    protected static final String TCF_NAME = "testTCF";

    //Timeout
    public static final long TIMEOUT = 10000;

    public static Provider getProvider() {
        return new ActiveMQProvider();
    }
}
