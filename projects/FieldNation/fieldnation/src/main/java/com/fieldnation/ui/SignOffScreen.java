package com.fieldnation.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.workorder.detail.TimeLogRowView;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by michael.carver on 12/1/2014.
 */
public class SignOffScreen extends RelativeLayout {
    private static final String TAG = "SignOffScreen";

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
    private WorkOrder _workOrder;
    private Listener _listener;
    private boolean _waitTasks = false;
    private boolean _waitLogs = false;


    /*-*************----------**************-*/
    /*-             Life Cycle              -*/
    /*-*************----------**************-*/

    public SignOffScreen(Context context) {
        super(context);
        init();
    }

    public SignOffScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignOffScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_signoff, this);

        if (isInEditMode()) return;

        _container = findViewById(R.id.container);
        _companyImageView = findViewById(R.id.company_imageview);
        _titleTextView = findViewById(R.id.title_textview);
        _descriptionTextView = findViewById(R.id.description_textview);

        _timeDivider = findViewById(R.id.time_divider);
        _timeTextView = findViewById(R.id.time_textview);
        _timeLinearLayout = findViewById(R.id.time_list);

        _tasksDivider = findViewById(R.id.tasks_divider);
        _tasksTextView = findViewById(R.id.tasks_textview);
        _tasksLinearLayout = findViewById(R.id.tasks_list);

        _closingNotesDivider = findViewById(R.id.closingnotes_divider);
        _closingNotesLabelTextView = findViewById(R.id.closingnoteslabel_textview);
        _closingNotesTextView = findViewById(R.id.closingnotes_textview);

        _signOffButton = findViewById(R.id.signoff_button);
        _signOffButton.setOnClickListener(_signOff_onClick);
        _rejectButton = findViewById(R.id.reject_button);
        _rejectButton.setOnClickListener(_reject_onClick);

        _loadingView = findViewById(R.id.loading_view);

        setLoading(true);
    }

    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;

        populateUi();
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            _loadingView.setVisibility(View.VISIBLE);
        } else {
            _loadingView.setVisibility(View.GONE);
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_signOffButton == null)
            return;

        if (_workOrder == null)
            return;

        Stopwatch stopwatch = new Stopwatch(true);

        _titleTextView.setText(_workOrder.getTitle());
        if (_workOrder.getDescription() != null)
            _descriptionTextView.setText(misc.htmlify(_workOrder.getDescription().getHtml()));
        //_descriptionTextView.setLinksClickable(false);

        final TimeLog[] timeLogs = _workOrder.getTimeLogs().getResults();
        if (timeLogs.length > 0) {
            _waitLogs = true;
            _timeLinearLayout.setVisibility(View.VISIBLE);
            _timeTextView.setVisibility(View.VISIBLE);
            _timeDivider.setVisibility(View.VISIBLE);

            _timeLinearLayout.removeAllViews();
            ForLoopRunnable r = new ForLoopRunnable(timeLogs.length, new Handler()) {
                private final TimeLog[] _logs = timeLogs;

                @Override
                public void next(int i) {
                    TimeLog work = _logs[i];
                    TimeLogRowView v = new TimeLogRowView(getContext());
                    v.setData(_workOrder, work);
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
            _container.postDelayed(r, 10);

        } else {
            _timeLinearLayout.setVisibility(View.GONE);
            _timeTextView.setVisibility(View.GONE);
            _timeDivider.setVisibility(View.GONE);
        }

        final Task[] tasks = _workOrder.getTasks().getResults();
        if (tasks.length > 0) {
            _waitTasks = true;
            _tasksDivider.setVisibility(View.VISIBLE);
            _tasksTextView.setVisibility(View.VISIBLE);
            _tasksLinearLayout.setVisibility(View.VISIBLE);
            final List<View> taskView = new LinkedList<>();

            ForLoopRunnable r = new ForLoopRunnable(tasks.length, new Handler()) {
                private final Task[] _tasks = tasks;

                @Override
                public void next(int i) {
                    Task task = _tasks[i];

                    String display = "";
                    if (task.getType().getId() == 7) {
                        display = task.getDescription();
                    } else if (task.getType().getId() == 8) {
                        display = "Call " + task.getPhone() + "\n" + task.getLabel();
                    } else if (task.getType().getId() == 9) {
                        display = "Email " + task.getEmail() + "\n" + task.getLabel();
                    } else if (task.getType().getId() == 10) {
                        display = "Complete Task\n" + task.getLabel();
                    } else {
                        if (misc.isEmptyOrNull(task.getLabel()) || task.getLabel().equals(task.getType().getName()))
                            display = task.getType().getName();
                        else
                            display = task.getType().getName() + "\n" + task.getLabel();
                    }

                    TaskRowSimpleView v = new TaskRowSimpleView(getContext());
                    v.setText(display);
                    taskView.add(v);
                }

                @Override
                public void finish(int count) throws Exception {
                    super.finish(count);
                    if (!_waitLogs) {
                        _tasksLinearLayout.removeAllViews();
                        for (View v : taskView) {
                            _tasksLinearLayout.addView(v);
                        }
                        setLoading(false);
                    }
                    _waitTasks = false;

                }
            };
            _container.postDelayed(r, new Random().nextInt(10));
        } else {
            _tasksDivider.setVisibility(View.GONE);
            _tasksTextView.setVisibility(View.GONE);
            _tasksLinearLayout.setVisibility(View.GONE);
            setLoading(false);
        }

        if (!misc.isEmptyOrNull(_workOrder.getClosingNotes())) {
            _closingNotesTextView.setVisibility(View.VISIBLE);
            _closingNotesDivider.setVisibility(View.VISIBLE);

            _closingNotesTextView.setText(_workOrder.getClosingNotes());

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
