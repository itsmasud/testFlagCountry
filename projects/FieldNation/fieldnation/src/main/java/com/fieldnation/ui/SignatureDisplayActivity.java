package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.utils.misc;

/**
 * Created by michael.carver on 12/9/2014.
 */
public class SignatureDisplayActivity extends AuthActionBarActivity {
    private static final String TAG = "ui.SignatureDisplayActivity";

    // State
    private static final String STATE_SIGNATURE = "STATE_SIGNATURE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_SIGNATURE_ID = "STATE_SIGNATURE_ID";

    // Intent Params
    public static final String INTENT_PARAM_SIGNATURE = "ui.SignatureDisplayActivity:INTENT_PARAM_SIGNATURE";
    public static final String INTENT_PARAM_WORKORDER = "ui.SignatureDisplayActivity:INTENT_PARAM_WORKORDER";

    // Web
    public static int WEB_GET_SIGNATURE = 1;

    // Ui
    private TextView _titleTextView;
    private TextView _descriptionTextView;

    private View _timeDivider;
    private TextView _timeTextView;
    private LinearLayout _timeLinearLayout;

    private View _tasksDivider;
    private TextView _tasksTextView;
    private LinearLayout _tasksLinearLayout;

    private View _closingNotesDivider;
    private TextView _closingNotesLabelTextView;
    private TextView _closingNotesTextView;

    private SignatureView _signatureView;
    private TextView _nameTextView;
    private Button _doneButton;

    private LoadingView _loadingView;

    // Data
    private Signature _signature;
    private Workorder _wo;
    private long _signatureId = -1;

    // Service
    private WorkorderService _service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_display);

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _descriptionTextView = (TextView) findViewById(R.id.description_textview);

        _timeDivider = findViewById(R.id.time_divider);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _timeLinearLayout = (LinearLayout) findViewById(R.id.time_list);

        _tasksDivider = findViewById(R.id.tasks_divider);
        _tasksTextView = (TextView) findViewById(R.id.tasks_textview);
        _tasksLinearLayout = (LinearLayout) findViewById(R.id.tasks_list);

        _closingNotesDivider = findViewById(R.id.closingnotes_divider);
        _closingNotesLabelTextView = (TextView) findViewById(R.id.closingnoteslabel_textview);
        _closingNotesTextView = (TextView) findViewById(R.id.closingnotes_textview);

        _signatureView = (SignatureView) findViewById(R.id.signature_view);
        _nameTextView = (TextView) findViewById(R.id.name_textview);

        _doneButton = (Button) findViewById(R.id.done_button);
        _doneButton.setOnClickListener(_done_onClick);

        _loadingView = (LoadingView) findViewById(R.id.loading_view);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_SIGNATURE))
                _signature = savedInstanceState.getParcelable(STATE_SIGNATURE);

            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _wo = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_SIGNATURE_ID))
                _signatureId = savedInstanceState.getLong(STATE_SIGNATURE_ID);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(INTENT_PARAM_SIGNATURE))
                _signatureId = extras.getLong(INTENT_PARAM_SIGNATURE);

            if (extras.containsKey(INTENT_PARAM_WORKORDER))
                _wo = extras.getParcelable(INTENT_PARAM_WORKORDER);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_signature != null)
            outState.putParcelable(STATE_SIGNATURE, _signature);

        if (_wo != null)
            outState.putParcelable(STATE_WORKORDER, _wo);

        outState.putLong(STATE_SIGNATURE_ID, _signatureId);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        if (_service == null || isNew) {
            _service = new WorkorderService(this, username, authToken, _resultReceiver);
            getData();
        }
    }

    private void getData() {
        if (_service == null)
            return;

        _loadingView.setVisibility(View.VISIBLE);

        startService(
                _service.getSignature(WEB_GET_SIGNATURE, _wo.getWorkorderId(), _signatureId, true));
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateUi(false);
    }

    private void populateUi(boolean isCached) {
        if (_signature == null)
            return;

        if (_doneButton == null)
            return;

        _signatureView.setSignatureJson(_signature.getSignature(), true);
        _nameTextView.setText(_signature.getPrintName());

        _titleTextView.setText(_wo.getTitle());
        _descriptionTextView.setText(misc.htmlify(_wo.getFullWorkDescription()));
        _descriptionTextView.setLinksClickable(false);
        _descriptionTextView.setLinkTextColor(0xFF000000);

        LoggedWork[] logs = _signature.getWorklog();
        if (logs != null && logs.length > 0) {
            _timeLinearLayout.setVisibility(View.VISIBLE);
            _timeTextView.setVisibility(View.VISIBLE);
            _timeDivider.setVisibility(View.VISIBLE);

            _timeLinearLayout.removeAllViews();
            for (int i = 0; i < logs.length; i++) {
                LoggedWork work = logs[i];
                WorklogTile v = new WorklogTile(this);
                v.setWorklog(work, _wo.getPay().isPerDeviceRate());
                _timeLinearLayout.addView(v);
            }
        } else {
            _timeLinearLayout.setVisibility(View.GONE);
            _timeTextView.setVisibility(View.GONE);
            _timeDivider.setVisibility(View.GONE);
        }

        Task[] tasks = _wo.getTasks();
        if (tasks != null && tasks.length > 0) {
            _tasksDivider.setVisibility(View.VISIBLE);
            _tasksTextView.setVisibility(View.VISIBLE);
            _tasksLinearLayout.setVisibility(View.VISIBLE);

            _tasksLinearLayout.removeAllViews();
            for (int i = 0; i < tasks.length; i++) {
                Task task = tasks[i];

                String display = "";
                if (task.getTypeId() != null) {
                    TaskType type = task.getTaskType();
                    display = type.getDisplay(this);
                } else {
                    display = task.getType();
                }

                TaskRowSimpleView v = new TaskRowSimpleView(this);
                if (misc.isEmptyOrNull(task.getDescription())) {
                    v.setText(display);
                } else {
                    v.setText(display + "\n" + task.getDescription());
                }
                _tasksLinearLayout.addView(v);
            }
        } else {
            _tasksDivider.setVisibility(View.GONE);
            _tasksTextView.setVisibility(View.GONE);
            _tasksLinearLayout.setVisibility(View.GONE);
        }

        if (!misc.isEmptyOrNull(_signature.getClosingNotes())) {
            _closingNotesTextView.setVisibility(View.VISIBLE);
            _closingNotesDivider.setVisibility(View.VISIBLE);

            _closingNotesTextView.setText(_signature.getClosingNotes());

        } else {
            _closingNotesTextView.setVisibility(View.GONE);
            _closingNotesDivider.setVisibility(View.GONE);
            _closingNotesLabelTextView.setVisibility(View.GONE);
        }

        _loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
    }

    /*-*************************************-*/
    /*-                 Events              -*/
    /*-*************************************-*/
    private View.OnClickListener _done_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private WebResultReceiver _resultReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public Context getContext() {
            return SignatureDisplayActivity.this;
        }

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_GET_SIGNATURE) {
                String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));

                try {
                    JsonObject obj = new JsonObject(data);

                    Signature signature = Signature.fromJson(obj);

                    _signature = signature;

                    populateUi(resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };


    public static Intent startIntent(Context context, long signatureId, Workorder workorder) {
        Intent intent = new Intent(context, SignatureDisplayActivity.class);
        intent.putExtra(INTENT_PARAM_SIGNATURE, signatureId);
        intent.putExtra(INTENT_PARAM_WORKORDER, workorder);
        return intent;
    }

}