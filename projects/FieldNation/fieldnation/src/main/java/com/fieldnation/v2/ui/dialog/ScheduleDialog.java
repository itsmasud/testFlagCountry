package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.ui.ScheduleCoView;
import com.fieldnation.v2.data.model.Schedule;

/**
 * Created by mc on 6/29/17.
 */

public class ScheduleDialog extends FullScreenDialog {
    private static final String TAG = "ScheduleDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private ScheduleCoView _scheduleCoView;

    public ScheduleDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_schedule, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle("Change Schedule");

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_ok);

        _scheduleCoView = v.findViewById(R.id.schedule_layout);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
    }

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            _onCompleteDispatcher.dispatch(getUid(), _scheduleCoView.getSchedule());
            dismiss(true);
            return true;
        }
    };

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, Schedule schedule) {
        Bundle params = new Bundle();
        params.putParcelable("schedule", schedule);
        Controller.show(context, uid, ScheduleDialog.class, params);
    }

    /*-****************************-*/
    /*-         Complete           -*/
    /*-****************************-*/
    public interface OnCompleteListener {
        void onComplete(Schedule schedule);
    }

    private static KeyedDispatcher<OnCompleteListener> _onCompleteDispatcher = new KeyedDispatcher<OnCompleteListener>() {
        @Override
        public void onDispatch(OnCompleteListener listener, Object... parameters) {
            listener.onComplete((Schedule) parameters[0]);
        }
    };

    public static void addOnCompleteListener(String uid, OnCompleteListener onCompleteListener) {
        _onCompleteDispatcher.add(uid, onCompleteListener);
    }

    public static void removeOnCompleteListener(String uid, OnCompleteListener onCompleteListener) {
        _onCompleteDispatcher.remove(uid, onCompleteListener);
    }

    public static void removeAllOnCompleteListener(String uid) {
        _onCompleteDispatcher.removeAll(uid);
    }

}
