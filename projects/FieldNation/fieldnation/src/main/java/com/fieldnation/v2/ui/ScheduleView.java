package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.v2.data.model.Schedule;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mc on 7/11/17.
 */

public class ScheduleView extends LinearLayout {
    private static final String TAG = "ScheduleView";

    // Ui
    private KeyValuePairView _schedule1View;
    private KeyValuePairView _schedule2View;

    // Data
    private Schedule _schedule;

    // Listeners
    private OnRenderListener _onRenderListener;

    public ScheduleView(Context context) {
        super(context);
        init();
    }

    public ScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_schedule, this);

        if (isInEditMode())
            return;

        _schedule1View = findViewById(R.id.schedule1_view);
        _schedule2View = findViewById(R.id.schedule2_view);

        populateUi();
    }

    public void set(Schedule schedule) {
        _schedule = schedule;
        populateUi();
    }

    private void populateUi() {
        if (_schedule1View == null)
            return;

        if (_schedule == null)
            return;

        try {
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(getContext().getResources().getStringArray(R.array.schedule_capital_case_am_pm_array));

            switch (_schedule.getServiceWindow().getMode()) {
                case BETWEEN: {
                    Calendar sCal = _schedule.getServiceWindow().getStart().getCalendar();
                    Calendar eCal = _schedule.getServiceWindow().getEnd().getCalendar();

                    SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d, yyyy @ h:mm a", Locale.getDefault());
                    sdf.setDateFormatSymbols(symbols);

                    _schedule1View.set("Start After", sdf.format(sCal.getTime()));
                    _schedule2View.set("Finish By", sdf.format(eCal.getTime()));
                    if (_onRenderListener != null) _onRenderListener.onRender(true);
                    break;
                }
                case EXACT: {
                    Calendar sCal = _schedule.getServiceWindow().getStart().getCalendar();

                    SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d, yyyy", Locale.getDefault());
                    sdf.setDateFormatSymbols(symbols);

                    _schedule1View.set("Date", sdf.format(sCal.getTime()));

                    sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    sdf.setDateFormatSymbols(symbols);

                    _schedule2View.set("Time", sdf.format(sCal.getTime()));
                    if (_onRenderListener != null) _onRenderListener.onRender(true);
                    break;
                }
                case HOURS: {
                    Calendar sCal = _schedule.getServiceWindow().getStart().getCalendar();
                    Calendar eCal = _schedule.getServiceWindow().getEnd().getCalendar();

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                    sdf.setDateFormatSymbols(symbols);

                    _schedule1View.set("Between", sdf.format(sCal.getTime()) + " - " + sdf.format(eCal.getTime()));

                    sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    sdf.setDateFormatSymbols(symbols);

                    _schedule2View.set("During", sdf.format(sCal.getTime()) + " - " + sdf.format(eCal.getTime()));
                    if (_onRenderListener != null) _onRenderListener.onRender(true);
                    break;
                }
                default:
                    if (_onRenderListener != null) _onRenderListener.onRender(false);
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            if (_onRenderListener != null) _onRenderListener.onRender(false);
        }
    }

    public void setOnRenderListener(OnRenderListener onRenderListener) {
        _onRenderListener = onRenderListener;
    }

    public void removeOnRenderListener() {
        _onRenderListener = null;
    }

    public interface OnRenderListener {
        void onRender(boolean success);
    }
}
