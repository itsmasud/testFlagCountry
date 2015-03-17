package com.fieldnation.service.auth;

import com.fieldnation.service.topics.TopicClient;

/**
 * Created by Michael Carver on 3/17/2015.
 */
public class AuthTopicClient extends TopicClient {

    public AuthTopicClient(Listener listener) {
        super(listener);
    }

    public static abstract class Listener extends TopicClient.Listener {
    }
}
