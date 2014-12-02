package com.fieldnation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

/**
 * Created by michael.carver on 12/1/2014.
 */
public class SignOffFragment extends FragmentBase {
    private static final String TAG = "ui.SignOffFragment";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // Ui
    private ImageView _companyImageView;
    private TextView _titleTextView;
    private TextView _descriptionTextView;
    private View _timeDivider;
    private TextView _timeTextView;
    private LinearLayout _timeLinearLayout;

    private View _tasksDivider;
    private TextView _tasksTextView;
    private LinearLayout _tasksLinearLayout;
    private Button _signOffButton;


    // Data
    private Workorder _workorder;
    private Listener _listener;

    /*-*************----------**************-*/
    /*-             Life Cycle              -*/
    /*-*************----------**************-*/
    public static SignOffFragment getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, SignOffFragment.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null) {
            outState.putParcelable(STATE_WORKORDER, _workorder);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signoff, container, false);

        _companyImageView = (ImageView) v.findViewById(R.id.company_imageview);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _descriptionTextView = (TextView) v.findViewById(R.id.description_textview);
        _timeDivider = v.findViewById(R.id.time_divider);
        _timeTextView = (TextView) v.findViewById(R.id.time_textview);
        _timeLinearLayout = (LinearLayout) v.findViewById(R.id.time_list);
        _tasksDivider = v.findViewById(R.id.tasks_divider);
        _tasksTextView = (TextView) v.findViewById(R.id.tasks_textview);
        _tasksLinearLayout = (LinearLayout) v.findViewById(R.id.tasks_list);
        _signOffButton = (Button) v.findViewById(R.id.signoff_button);
        _signOffButton.setOnClickListener(_signOff_onClick);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();
        if (bundle != null) {
            _workorder = bundle.getParcelable(SignOffActivity.PARAM_WORKORDER);
        }

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_signOffButton == null)
            return;

        if (_workorder == null)
            return;

        _titleTextView.setText(_workorder.getTitle());
        _descriptionTextView.setText(misc.linkifyHtml(_workorder.getFullWorkDescription(), Linkify.ALL));
        _descriptionTextView.setLinksClickable(false);

        LoggedWork[] logs = _workorder.getLoggedWork();
        if (logs != null && logs.length > 0) {
            _timeLinearLayout.setVisibility(View.VISIBLE);
            _timeTextView.setVisibility(View.VISIBLE);
            _timeDivider.setVisibility(View.VISIBLE);

            _timeLinearLayout.removeAllViews();
            for (int i = 0; i < logs.length; i++) {
                LoggedWork work = logs[i];
                WorklogTile v = new WorklogTile(getActivity());
                v.setWorklog(work, _workorder.getPay().isPerDeviceRate());
                _timeLinearLayout.addView(v);
            }
        } else {
            _timeLinearLayout.setVisibility(View.GONE);
            _timeTextView.setVisibility(View.GONE);
            _timeDivider.setVisibility(View.GONE);
        }

        Task[] tasks = _workorder.getTasks();
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
                    display = type.getDisplay(getActivity());
                } else {
                    display = task.getType();
                }

                TaskRowSimpleView v = new TaskRowSimpleView(getActivity());
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
    }


    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private View.OnClickListener _signOff_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.signOffOnClick();
            }
        }
    };

    public interface Listener {
        public void signOffOnClick();
    }
}
