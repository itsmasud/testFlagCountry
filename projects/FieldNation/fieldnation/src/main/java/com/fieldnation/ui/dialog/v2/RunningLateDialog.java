package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.service.data.workorder.WorkorderClient;

import java.util.Calendar;

/**
 * Created by Michael on 10/5/2016.
 */

public class RunningLateDialog extends SimpleDialog {
    private static final String TAG = "RunningLateDialog";

    private static final String PARAM_WORKORDER = "workOrder";

    // Ui
    private TextView _bodyTextView;
    private Button _time1Button;
    private Button _time2Button;
    private Button _time3Button;
    private Button _otherButton;
    private Button _callButton;
    private Button _cancelButton;

    // Data
    private WorkOrder _workOrder;

    public RunningLateDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_running_late, container, false);

        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);
        _time1Button = (Button) v.findViewById(R.id.time1_button);
        _time2Button = (Button) v.findViewById(R.id.time2_button);
        _time3Button = (Button) v.findViewById(R.id.time3_button);
        _otherButton = (Button) v.findViewById(R.id.other_button);
        _callButton = (Button) v.findViewById(R.id.call_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();

        _time1Button.setTag(5);
        _time1Button.setOnClickListener(_time_onClick);
        _time2Button.setTag(10);
        _time2Button.setOnClickListener(_time_onClick);
        _time3Button.setTag(15);
        _time3Button.setOnClickListener(_time_onClick);

        _otherButton.setOnClickListener(_other_onClick);
        _callButton.setOnClickListener(_call_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _workOrder = payload.getParcelable(PARAM_WORKORDER);

        Calendar cal = null;
        try {
            if (_workOrder.getSchedule() != null) {
                if (_workOrder.getSchedule().getEstimate() != null && _workOrder.getSchedule().getEstimate().getArrival() != null) {
                    cal = ISO8601.toCalendar(_workOrder.getSchedule().getEstimate().getArrival());
                } else if (_workOrder.getSchedule().getExact() != null) {
                    cal = ISO8601.toCalendar(_workOrder.getSchedule().getExact());
                } else if (_workOrder.getSchedule().getBegin() != null) {
                    cal = ISO8601.toCalendar(_workOrder.getSchedule().getBegin());
                } else if (_workOrder.getSchedule().getRange() != null && _workOrder.getSchedule().getRange().getBegin() != null) {
                    cal = ISO8601.toCalendar(_workOrder.getSchedule().getRange().getBegin());
                }

                _bodyTextView.setText(
                        _workOrder.getTitle()
                                + " is scheduled to begin at "
                                + DateUtils.formatTime2(cal)
                                + ". Please estimate the amount of time you'll be late:");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.show(payload, animate);
    }

    private final View.OnClickListener _time_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ToastClient.toast(App.get(), "Late arrival notification sent", Toast.LENGTH_SHORT);
            WorkorderClient.actionWilLBeLate(App.get(), _workOrder.getId(), "", ((Integer) v.getTag()) * 60);
            dismiss(true);
        }
    };

    private final View.OnClickListener _other_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO other time picker
        }
    };

    private final View.OnClickListener _call_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO need to implement call buyer
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };


    public static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, RunningLateDialog.class);
        }

        public static void show(Context context, WorkOrder workOrder) {
            Bundle params = new Bundle();
            params.putParcelable(PARAM_WORKORDER, workOrder);

            show(context, RunningLateDialog.class, params);
        }
    }

}

