package com.fieldnation.auth.client;

import android.os.Bundle;

import com.fieldnation.service.topics.TopicClient;

/**
 * Created by Michael Carver on 3/5/2015.
 */
public class AuthTopicClient extends TopicClient  implements AuthTopicConstants {


    public AuthTopicClient(Listener listener) {
        super(listener);
    }


    public abstract class Listener implements TopicClient.Listener {

        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onRegistered(String topicId) {

        }

        @Override
        public void onEvent(String topicId, Bundle payload) {

        }
    }
}
