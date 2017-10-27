package com.fieldnation.v2.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Task;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Created by Shoaib on 6/10/17.
 */

public class TaskRowView extends RelativeLayout {
    private static final String TAG = UniqueTag.makeTag("TaskRowView");

    // Ui
    private TextView _keyTextView;
    private TextView _valueTextView;
    private TextView _rightValueTextView;
    private ProgressBar _progressBar;

    // Data
    private Task _task;
    private boolean _progressVisible = false;

    private final HashSet<String> _uploadingFiles = new HashSet<>();
    private final Hashtable<String, Integer> _uploadingProgress = new Hashtable<>();


    public TaskRowView(Context context) {
        super(context);
        init();
    }

    public TaskRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_task_row, this);

        if (isInEditMode())
            return;

        _keyTextView = findViewById(R.id.key);
        _valueTextView = findViewById(R.id.value);
        _rightValueTextView = findViewById(R.id.value_right);
        _progressBar = findViewById(R.id.progressBar);

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setData(Task task) {
        _task = task;
        populateUi();
    }

    public void setProgress(Integer progress) {
        if (_progressBar == null)
            return;

        _progressBar.setVisibility(VISIBLE);
        if (progress == null) {
            _progressBar.setIndeterminate(true);
            return;
        }

        _progressBar.setIndeterminate(false);
        _progressBar.setMax(100);
        _progressBar.setProgress(progress);
    }

    public void setProgressVisible(boolean visible) {
        _progressVisible = visible;
        populateUi();
    }


    private void populateUi() {
        if (_keyTextView == null)
            return;

        if (_task == null)
            return;

        if (_uploadingFiles.size() > 0) {

            if (_uploadingFiles.size() == 1) {
                _keyTextView.setText(_task.getType().getName() + "\nUploading: " + _uploadingFiles.iterator().next());
            } else if (_uploadingFiles.size() > 1) {
                _keyTextView.setText(_task.getType().getName() + "\nUploading " + _uploadingFiles.size() + " files");
            }

            int progress = 0;
            for (Integer val : _uploadingProgress.values()) {
                progress += val;
            }

            if (_uploadingProgress.size() > 0) {
                int pos = progress / _uploadingProgress.size();

                if (pos == 100) {
                    _progressBar.setIndeterminate(true);
                    _progressBar.setVisibility(VISIBLE);
                } else {
                    _progressBar.setIndeterminate(false);
                    _progressBar.setProgress(progress / _uploadingProgress.size());
                    _progressBar.setVisibility(VISIBLE);
                }
            } else {
                _progressBar.setIndeterminate(true);
                _progressBar.setVisibility(VISIBLE);
            }

        } else {
            _progressBar.setVisibility(GONE);
            drawRowByType(getType(_task));
        }
        updateView();
    }

    private String getFormattedTime(Calendar cal) {
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setAmPmStrings(getResources().getStringArray(R.array.schedule_capital_case_am_pm_array));

        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mm a", Locale.getDefault());
        sdf.setDateFormatSymbols(symbols);

        return sdf.format(cal.getTime()) + DateUtils.getDeviceTimezone(cal);
    }

    public TaskTypeEnum getType(Task task) {
        return TaskTypeEnum.fromTypeId(task.getType().getId());
    }

    private void drawRowByType(TaskTypeEnum taskType) {
        try {
            switch (taskType) {
                case SET_ETA: // set eta
                    _keyTextView.setText(_task.getType().getName());
                    _valueTextView.setVisibility(GONE);
                    if (_task.getEta().getStart().getUtc() == null) {
                        _rightValueTextView.setVisibility(GONE);
                    } else {
                        _rightValueTextView.setVisibility(VISIBLE);
                        _rightValueTextView.setText(getFormattedTime(_task.getEta().getStart().getCalendar()));
                    }
                    break;

                case CLOSING_NOTES: // closing notes
                    _keyTextView.setText(_task.getLabel());
                    if (misc.isEmptyOrNull(_task.getClosingNotes()))
                        _valueTextView.setVisibility(GONE);
                    else {
                        _valueTextView.setText(_task.getClosingNotes());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    _rightValueTextView.setVisibility(GONE);
                    break;

                case CHECK_IN: // check in
                    _keyTextView.setText(_task.getLabel());
                    _valueTextView.setVisibility(GONE);
                    if (_task.getTimeLog().getIn().getCreated().getUtc() == null) {
                        _rightValueTextView.setVisibility(GONE);
                    } else {
                        _rightValueTextView.setVisibility(VISIBLE);
                        _rightValueTextView.setText(getFormattedTime(_task.getTimeLog().getIn().getCreated().getCalendar()));
                    }
                    break;

                case CHECK_OUT: // check out
                    _keyTextView.setText(_task.getLabel());
                    _valueTextView.setVisibility(GONE);
                    if (_task.getTimeLog().getOut().getCreated().getUtc() == null) {
                        _rightValueTextView.setVisibility(GONE);
                    } else {
                        _rightValueTextView.setVisibility(VISIBLE);
                        _rightValueTextView.setText(getFormattedTime(_task.getTimeLog().getOut().getCreated().getCalendar()));
                    }
                    break;

                case UPLOAD_FILE: // upload file
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;

                case UPLOAD_PICTURE: // upload picture
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;

                case CUSTOM_FIELD: // custom field
                    if (_task.getCustomField().getId() == null) {
                        setOnClickListener(null);
                    }

                    _keyTextView.setText(_task.getLabel());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getDescription())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getDescription());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;

                case PHONE: // phone
                    _keyTextView.setText("Call " + (_task.getPhone() == null ? "" : _task.getPhone()));
                    _rightValueTextView.setVisibility(GONE);

                    // support for classic web
                    if (_task.getPhone() == null) {
                        _valueTextView.setVisibility(VISIBLE);
                        _valueTextView.setText("No number associated");
                        break;
                    }

                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;

                case EMAIL: // email
                    _keyTextView.setText("Email " + _task.getEmail());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;

                case UNIQUE_TASK: // unique task
                    _keyTextView.setText("Complete tasks");
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;

                case SIGNATURE: // signature
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;

                case SHIPMENT: // shipment
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;

                case DOWNLOAD:
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }

                    if (_progressVisible) {
                        _progressBar.setVisibility(VISIBLE);
                        _progressBar.setIndeterminate(true);
                    } else {
                        _progressBar.setIndeterminate(false);
                        _progressBar.setVisibility(GONE);
                    }
                    break;
            }
        } catch (Exception e) {
            Log.v(TAG, e);
        }
    }

    private void updateView() {
        if (_task != null
                && (_task.getActionsSet().contains(Task.ActionsEnum.EDIT)
                || _task.getActionsSet().contains(Task.ActionsEnum.COMPLETE))) {
            _keyTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.fn_dark_text));
            setEnabled(true);
        } else {
            _keyTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.fn_light_text_50));
            setEnabled(false);
        }
    }
}
