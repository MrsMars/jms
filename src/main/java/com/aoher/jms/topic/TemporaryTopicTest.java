package com.aoher.jms.topic;

import com.aoher.testcase.TopicConnectionTestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import javax.jms.TemporaryTopic;
import javax.jms.TopicSubscriber;

/**
 * Test the <code>javax.jms.TemporaryTopic</code> features.
 */
public class TemporaryTopicTest extends TopicConnectionTestCase{

    private TemporaryTopic tempTopic;
    private TopicSubscriber tempSubscriber;

    @Rule
    public TestName name = new TestName();

    @Override
    protected String getTestName() {
        return name.getMethodName();
    }

    @Test
    public
}
