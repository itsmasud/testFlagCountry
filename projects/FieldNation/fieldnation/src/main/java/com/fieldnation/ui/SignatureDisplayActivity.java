package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by michael.carver on 12/9/2014.
 */
public class SignatureDisplayActivity extends AuthSimpleActivity {
    private static final String TAG = "SignatureDisplayActivity";

    // State
    private static final String STATE_SIGNATURE = "STATE_SIGNATURE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_SIGNATURE_ID = "STATE_SIGNATURE_ID";

    // Intent Params
    public static final String INTENT_PARAM_SIGNATURE = "ui.SignatureDisplayActivity:INTENT_PARAM_SIGNATURE";
    public static final String INTENT_PARAM_WORKORDER = "ui.SignatureDisplayActivity:INTENT_PARAM_WORKORDER";

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


    // Data
    private Signature _signature;
    private WorkOrder _workOrder;
    private long _signatureId = -1;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_signature_display;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(INTENT_PARAM_SIGNATURE))
                _signatureId = extras.getLong(INTENT_PARAM_SIGNATURE);

            if (extras.containsKey(INTENT_PARAM_WORKORDER))
                _workOrder = extras.getParcelable(INTENT_PARAM_WORKORDER);

            populateUi();
        } else if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_SIGNATURE))
                _signature = savedInstanceState.getParcelable(STATE_SIGNATURE);

            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workOrder = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_SIGNATURE_ID))
                _signatureId = savedInstanceState.getLong(STATE_SIGNATURE_ID);

            populateUi();
        } else {
            finish();
        }
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_signature != null)
            outState.putParcelable(STATE_SIGNATURE, _signature);

        if (_workOrder != null)
            outState.putParcelable(STATE_WORKORDER, _workOrder);

        outState.putLong(STATE_SIGNATURE_ID, _signatureId);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateUi();
    }

    private void populateUi() {
        if (_doneButton == null)
            return;

        if (_workOrder == null)
            return;

        if (_workOrder.getSignatures() == null
                || _workOrder.getSignatures().getResults() == null
                || _workOrder.getSignatures().getResults().length == 0)
            return;

        for (Signature signature : _workOrder.getSignatures().getResults()) {
            if (signature.getId() == _signatureId) {
                _signature = signature;
                break;
            }
        }

        if (_signature.getFormat().equals("svg")) {
            _signatureView.setSignatureSvg(_signature.getData(), true);
        } else {
            _signatureView.setSignatureJson(_signature.getData(), true);
        }

        _nameTextView.setText(_signature.getName());

        _titleTextView.setText(_workOrder.getTitle());
        if (_workOrder.getDescription() != null) {
            _descriptionTextView.setText(misc.htmlify(_workOrder.getDescription().getHtml()));
            _descriptionTextView.setLinksClickable(false);
            _descriptionTextView.setLinkTextColor(getResources().getColor(R.color.fn_dark_text));
        }

/* TODO
        final LoggedWork[] logs = _signature.getWorklog();
        if (logs != null && logs.length > 0) {
            _timeLinearLayout.setVisibility(View.VISIBLE);
            _timeTextView.setVisibility(View.VISIBLE);
            _timeDivider.setVisibility(View.VISIBLE);

            _timeLinearLayout.removeAllViews();
            ForLoopRunnable r = new ForLoopRunnable(logs.length, new Handler()) {
                private final LoggedWork[] _logs = logs;

                @Override
                public void next(int i) throws Exception {
                    LoggedWork work = _logs[i];
//TODO                    TimeLogRowView v = new TimeLogRowView(SignatureDisplayActivity.this);
//                    v.setData(_workOrder, work);
//                    _timeLinearLayout.addView(v);
                }
            };
            _timeLinearLayout.postDelayed(r, new Random().nextInt(1000));
        } else {
*/
        _timeLinearLayout.setVisibility(View.GONE);
        _timeTextView.setVisibility(View.GONE);
        _timeDivider.setVisibility(View.GONE);
//        }


        Task[] tasks = _workOrder.getTasks().getResults();
        if (tasks != null && tasks.length > 0) {
            _tasksDivider.setVisibility(View.VISIBLE);
            _tasksTextView.setVisibility(View.VISIBLE);
            _tasksLinearLayout.setVisibility(View.VISIBLE);

            _tasksLinearLayout.removeAllViews();
            for (Task task : tasks) {
                String display = task.getLabel();
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

    }

    /*-*************************************-*/
    /*-                 Events              -*/
    /*-*************************************-*/
    private final View.OnClickListener _done_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public static void startIntent(Context context, long signatureId, WorkOrder workOrder) {
        Intent intent = new Intent(context, SignatureDisplayActivity.class);
        intent.putExtra(INTENT_PARAM_SIGNATURE, signatureId);
        intent.putExtra(INTENT_PARAM_WORKORDER, workOrder);
        intent.setExtrasClassLoader(WorkOrder.class.getClassLoader());
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}