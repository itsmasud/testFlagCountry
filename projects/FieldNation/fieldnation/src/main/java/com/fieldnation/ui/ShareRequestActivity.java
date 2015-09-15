package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.fieldnation.ForLoopRunnable;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.UploadingDocument;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.ui.workorder.MyWorkActivity;
import com.fieldnation.ui.workorder.detail.DocumentView;
import com.fieldnation.utils.Stopwatch;
import com.fieldnation.utils.misc;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by shoaib.ahmed on Sept/08/2015.
 */
public class ShareRequestActivity extends AuthFragmentActivity {
    private static final String TAG = "ShareRequestActivity";

    private static final String STATE_PROFILE = "STATE_PROFILE";
    private static final String STATE_IS_AUTH = "STATE_IS_AUTH";
    private static final String STATE_SHOWING_DIALOG = "STATE_SHOWING_DIALOG";

    // UI
    private OverScrollView _scrollView;
    private LinearLayout _reviewList;

    private Profile _profile = null;
    private boolean _isAuth = false;
    private boolean _calledMyWork = false;
    private GlobalTopicClient _globalClient;
    private AuthTopicClient _authClient;

    public ShareRequestActivity() {
        super();
        Log.v(TAG, "Construct");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_uploading_documents_list);

        _reviewList = (LinearLayout) findViewById(R.id.review_list);

//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(STATE_IS_AUTH)) {
//                _isAuth = savedInstanceState.getBoolean(STATE_IS_AUTH);
//            }
//            if (savedInstanceState.containsKey(STATE_PROFILE)) {
//                _profile = savedInstanceState.getParcelable(STATE_PROFILE);
//            }
//        }


        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

//        if (Intent.ACTION_SEND.equals(action) && type != null) {
//            if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//                handleSendMultipleImages(intent); // Handle multiple images being sent
//            }
//        }

        //make sure it's an action and type we can handle
        if(action.equals(Intent.ACTION_SEND)){
            //content is being shared
            Log.v(TAG, "file received");
        }
        else if(action.equals(Intent.ACTION_MAIN)){
            //app has been launched directly, not from share list
            Log.v(TAG, "file received");

        }


        else if(action.equals(Intent.ACTION_SEND_MULTIPLE)){
            //app has been launched directly, not from share list
            Log.v(TAG, "file received");

        }

        else{
            Log.v(TAG, "file received" + action);
        }



        Log.v(TAG, "onCreate");
        }

        @Override
        protected void onSaveInstanceState (Bundle outState){
            outState.putBoolean(STATE_IS_AUTH, _isAuth);
            if (_profile != null) {
                outState.putParcelable(STATE_PROFILE, _profile);
            }
            super.onSaveInstanceState(outState);
        }

        @Override
        protected void onResume () {
            Log.v(TAG, "onResume");
            super.onResume();
            startService(new Intent(this, AuthTopicService.class));
            _globalClient = new GlobalTopicClient(_globalTopic_listener);
            _globalClient.connect(this);
            _authClient = new AuthTopicClient(_authTopic_listener);
            _authClient.connect(this);

            AuthTopicClient.dispatchRequestCommand(this);
        }

        @Override
        protected void onPause () {
            _globalClient.disconnect(this);
            _authClient.disconnect(this);
            super.onPause();
        }


//    private void populateUi() {
//
//        if (_profile == null)
//            return;
//
//
//        Stopwatch stopwatch = new Stopwatch(true);
//        final UploadingDocument[] docs = _workorder.getDocuments();
//        if (docs != null && docs.length > 0) {
//            Log.v(TAG, "_reviewList.getChildCount() " + _reviewList.getChildCount());
//            Log.v(TAG, "docs.length " + docs.length);
//
//            if (_reviewList.getChildCount() > docs.length) {
//                _reviewList.removeViews(docs.length - 1, _reviewList.getChildCount() - docs.length);
//            }
//
//            ForLoopRunnable r = new ForLoopRunnable(docs.length, new Handler()) {
//                private final UploadingDocument[] _docs = docs;
//
//                @Override
//                public void next(int i) throws Exception {
//                    ShareRequestRowView v = null;
//                    if (i < _reviewList.getChildCount()) {
//                        v = (ShareRequestRowView) _reviewList.getChildAt(i);
//                    } else {
//                        v = new ShareRequestRowView(ShareRequestActivity.this);
//                        _reviewList.addView(v);
//                    }
//                    UploadingDocument doc = _docs[i];
//                    v.setData(doc);
//                }
//            };
//            _reviewList.postDelayed(r, new Random().nextInt(1000));
//        } else {
//            _reviewList.removeAllViews();
//        }
//        Log.v(TAG, "pop docs time " + stopwatch.finish());
//    }


    private void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            Log.v(TAG, "file received " );
        }
    }

    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_globalTopic_listener.onConnected");
            _globalClient.registerGotProfile();
        }

        @Override
        public void onGotProfile(Profile profile) {
            Log.v(TAG, "_globalTopic_listener.onGotProfile");
            if (profile != null)
                Log.v(TAG, profile.toJson().display());
            _profile = profile;
//            doNextStep();
        }
    };

    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            _authClient.registerAuthState();
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            _isAuth = true;
//            doNextStep();
        }

        @Override
        public void onNotAuthenticated() {
            //Todo: If application is not logged-in, need to show login screen
            AuthTopicClient.dispatchRequestCommand(ShareRequestActivity.this);
        }
    };

    public static void startNew(Context context) {
        Intent intent = new Intent(context, ShareRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
