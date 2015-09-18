package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.GpsLocationService;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.misc;

import java.util.ArrayList;
import java.util.List;

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
    private RefreshView _refreshView;
    private LinearLayout _sendToLayout;
    private Spinner _workorderSpinner;
    private Spinner _tasksSpinner;
    private Button _okButton;
    private Button _cancelButton;


    // Data
    private WorkorderClient _workorderClient;

    // state data
    private WorkorderDataSelector _displayView = WorkorderDataSelector.ASSIGNED;

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
        setContentView(R.layout.activity_share_request);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _scrollView = (OverScrollView) findViewById(R.id.scroll_view);
        _scrollView.setOnOverScrollListener(_refreshView);

        _workorderSpinner = (Spinner) findViewById(R.id.workorder_spinner);
        _workorderSpinner.setOnItemSelectedListener(_workorder_selected);

        _tasksSpinner = (Spinner) findViewById(R.id.tasks_spinner);
        _tasksSpinner.setOnItemSelectedListener(_tasks_selected);


        _sendToLayout = (LinearLayout) findViewById(R.id.sendTo_layout);

        _okButton = (Button) findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

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
        if (action.equals(Intent.ACTION_SEND)) {
            //content is being shared
            Log.v(TAG, "file received");
        } else if (action.equals(Intent.ACTION_MAIN)) {
            //app has been launched directly, not from share list
            Log.v(TAG, "file received");

        } else if (action.equals(Intent.ACTION_SEND_MULTIPLE)) {
            //app has been launched directly, not from share list
            Log.v(TAG, "file received");

        } else {
            Log.v(TAG, "file received" + action);
        }


        Log.v(TAG, "onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_IS_AUTH, _isAuth);
        if (_profile != null) {
            outState.putParcelable(STATE_PROFILE, _profile);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        startService(new Intent(this, AuthTopicService.class));
        _globalClient = new GlobalTopicClient(_globalTopic_listener);
        _globalClient.connect(this);
        _authClient = new AuthTopicClient(_authTopic_listener);
        _authClient.connect(this);

        AuthTopicClient.dispatchRequestCommand(this);

//        if (_workorderClient != null && _workorderClient.isConnected())
//            _adapter.refreshPages();

        setLoading(true);
    }

    @Override
    protected void onPause() {
        _globalClient.disconnect(this);
        _authClient.disconnect(this);
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    private void setLoading(boolean loading) {
        Log.v(TAG, "setLoading()");
        // misc.printStackTrace("setLoading(" + loading + ")");
        if (_refreshView != null) {
            if (loading) {
                _refreshView.startRefreshing();
            } else {
                _refreshView.refreshComplete();
            }
        }
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


    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
//            if (_workorder != null) {
//                _workorder.dispatchOnChange();
//            }
        }
    };


    private final AdapterView.OnItemSelectedListener _workorder_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Todo: Populate _tasksSpinner when the
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    private final AdapterView.OnItemSelectedListener _tasks_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Todo: not sure
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


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

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/
//    private final WorkorderClient.Listener _workorderData_listener = new WorkorderClient.Listener() {
//        @Override
//        public void onConnected() {
//            Log.v(TAG, "_workorderData_listener.onConnected");
//            _workorderClient.subList(_displayView);
//            _workorderClient.subGet(false);
//            _workorderClient.subActions();
//            _adapter.refreshPages();
//        }
//
//        @Override
//        public void onList(List<Workorder> list, WorkorderDataSelector selector, int page, boolean failed) {
//            Log.v(TAG, "_workorderData_listener.onList");
//            if (!selector.equals(_displayView))
//                return;
//            if (list != null)
//                addPage(page, list);
//        }
//
//        @Override
//        public void onAction(long workorderId, String action, boolean failed) {
//            _adapter.refreshPages();
//        }
//    };

    public static void startNew(Context context) {
        Intent intent = new Intent(context, ShareRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
