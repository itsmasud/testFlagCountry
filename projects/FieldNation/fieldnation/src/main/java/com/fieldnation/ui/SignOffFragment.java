package com.fieldnation.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.ForLoopRunnable;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.Stopwatch;
import com.fieldnation.utils.misc;

import java.util.Random;

/**
 * Created by michael.carver on 12/1/2014.
 */
public class SignOffFragment extends FragmentBase {
    private static final String TAG = "SignOffFragment";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // Ui
    private LinearLayout _container;
    private ImageView _companyImageView;
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

    private Button _signOffButton;
    private Button _rejectButton;

    private LoadingView _loadingView;

    // Data
    private Workorder _workorder;
    private Listener _listener;
    private boolean _waitTasks = false;
    private boolean _waitLogs = false;

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
                new AsyncTaskEx<Bundle, Object, Workorder>() {
                    @Override
                    protected Workorder doInBackground(Bundle... params) {
                        return params[0].getParcelable(STATE_WORKORDER);
                    }

                    @Override
                    protected void onPostExecute(Workorder workorder) {
                        super.onPostExecute(workorder);
                        _workorder = workorder;
                    }
                }.executeEx(savedInstanceState);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Stopwatch stopwatch = new Stopwatch();
        if (_workorder != null) {
            outState.putParcelable(STATE_WORKORDER, _workorder);
        }

        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState time " + stopwatch.finish());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signoff, container, false);

        _container = (LinearLayout) v.findViewById(R.id.container);
        _companyImageView = (ImageView) v.findViewById(R.id.company_imageview);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _descriptionTextView = (TextView) v.findViewById(R.id.description_textview);

        _timeDivider = v.findViewById(R.id.time_divider);
        _timeTextView = (TextView) v.findViewById(R.id.time_textview);
        _timeLinearLayout = (LinearLayout) v.findViewById(R.id.time_list);

        _tasksDivider = v.findViewById(R.id.tasks_divider);
        _tasksTextView = (TextView) v.findViewById(R.id.tasks_textview);
        _tasksLinearLayout = (LinearLayout) v.findViewById(R.id.tasks_list);

        _closingNotesDivider = v.findViewById(R.id.closingnotes_divider);
        _closingNotesLabelTextView = (TextView) v.findViewById(R.id.closingnoteslabel_textview);
        _closingNotesTextView = (TextView) v.findViewById(R.id.closingnotes_textview);

        _signOffButton = (Button) v.findViewById(R.id.signoff_button);
        _signOffButton.setOnClickListener(_signOff_onClick);
        _rejectButton = (Button) v.findViewById(R.id.reject_button);
        _rejectButton.setOnClickListener(_reject_onClick);

        _loadingView = (LoadingView) v.findViewById(R.id.loading_view);

        setLoading(true);

        return v;
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            _loadingView.setVisibility(View.VISIBLE);
        } else {
            _loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        Stopwatch stopwatch = new Stopwatch();
        super.onResume();

        Bundle bundle = getArguments();
        if (bundle != null) {
            new AsyncTaskEx<Bundle, Object, Workorder>() {
                @Override
                protected Workorder doInBackground(Bundle... params) {
                    return params[0].getParcelable(SignOffActivity.INTENT_PARAM_WORKORDER);
                }

                @Override
                protected void onPostExecute(Workorder workorder) {
                    super.onPostExecute(workorder);
                    _workorder = workorder;
                    populateUi();
                }
            }.executeEx(bundle);
        }
        Log.v(TAG, "onResume time " + stopwatch.finish());
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_signOffButton == null)
            return;

        if (_workorder == null)
            return;

        Stopwatch stopwatch = new Stopwatch(true);

        _titleTextView.setText(_workorder.getTitle());
        if (_workorder.getFullWorkDescription() != null)
            _descriptionTextView.setText(misc.htmlify(_workorder.getFullWorkDescription()));
        //_descriptionTextView.setLinksClickable(false);

        final LoggedWork[] logs = _workorder.getLoggedWork();
        if (logs != null && logs.length > 0) {
            _waitLogs = true;
            _timeLinearLayout.setVisibility(View.VISIBLE);
            _timeTextView.setVisibility(View.VISIBLE);
            _timeDivider.setVisibility(View.VISIBLE);

            _timeLinearLayout.removeAllViews();
            ForLoopRunnable r = new ForLoopRunnable(logs.length, new Handler()) {
                private LoggedWork[] _logs = logs;

                @Override
                public void next(int i) {
                    if (getActivity() == null) {
                        return;
                    }

                    LoggedWork work = _logs[i];
                    WorklogTile v = new WorklogTile(getActivity());
                    v.setWorklog(work, _workorder.getPay().isPerDeviceRate());
                    _timeLinearLayout.addView(v);
                }

                @Override
                public void finish(int count) throws Exception {
                    super.finish(count);
                    if (!_waitTasks) {
                        setLoading(false);
                    }
                    _waitLogs = false;
                }
            };
            _container.postDelayed(r, new Random().nextInt(1000));

        } else {
            _timeLinearLayout.setVisibility(View.GONE);
            _timeTextView.setVisibility(View.GONE);
            _timeDivider.setVisibility(View.GONE);
        }

        final Task[] tasks = _workorder.getTasks();
        if (tasks != null && tasks.length > 0) {
            _waitTasks = true;
            _tasksDivider.setVisibility(View.VISIBLE);
            _tasksTextView.setVisibility(View.VISIBLE);
            _tasksLinearLayout.setVisibility(View.VISIBLE);

            _tasksLinearLayout.removeAllViews();

            ForLoopRunnable r = new ForLoopRunnable(tasks.length, new Handler()) {
                private Task[] _tasks = tasks;

                @Override
                public void next(int i) {
                    if (getActivity() == null)
                        return;

                    Task task = _tasks[i];

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

                @Override
                public void finish(int count) throws Exception {
                    super.finish(count);
                    if (!_waitLogs) {
                        setLoading(false);
                    }
                    _waitTasks = false;

                }
            };
            _container.postDelayed(r, new Random().nextInt(1000));
        } else {
            _tasksDivider.setVisibility(View.GONE);
            _tasksTextView.setVisibility(View.GONE);
            _tasksLinearLayout.setVisibility(View.GONE);
            setLoading(false);
        }

        if (!misc.isEmptyOrNull(_workorder.getClosingNotes())) {
            _closingNotesTextView.setVisibility(View.VISIBLE);
            _closingNotesDivider.setVisibility(View.VISIBLE);

            _closingNotesTextView.setText(_workorder.getClosingNotes());

        } else {
            _closingNotesTextView.setVisibility(View.GONE);
            _closingNotesDivider.setVisibility(View.GONE);
            _closingNotesLabelTextView.setVisibility(View.GONE);
        }
        Log.v(TAG, "pop time " + stopwatch.finish());

        if (!_waitTasks && !_waitLogs)
            setLoading(false);
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _signOff_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.signOffOnClick();
            }
        }
    };

    private final View.OnClickListener _reject_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.rejectOnClick();
        }
    };

    public interface Listener {
        void signOffOnClick();

        void rejectOnClick();
    }
}
