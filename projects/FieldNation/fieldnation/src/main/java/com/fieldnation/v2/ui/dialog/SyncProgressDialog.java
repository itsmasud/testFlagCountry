package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by michaelcarver on 2/13/18.
 */

public class SyncProgressDialog extends SimpleDialog {
    private static final String TAG = "SyncProgressDialog";

    private ProgressBar _progressBar;
    private TextView _progressTextView;
    private Button _cancelButton;

    private int _total = -1;
    private int _remain = -1;
    private boolean _running = false;
    private Handler _handler = new Handler();
    private boolean _scheduled = false;

    public SyncProgressDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_sync_progress, container, false);

        _progressBar = v.findViewById(R.id.progressbar_view);
        _progressTextView = v.findViewById(R.id.progress_textview);

        _cancelButton = v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (_total == -1)
            _total = WebTransaction.getSyncing().size();

        _cancelButton.setOnClickListener(_cancel_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        _running = true;
        scheduleNext();
    }

    @Override
    public void onPause() {
        _running = false;
        super.onPause();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        populateUi();
    }

    private void populateUi() {
        if (_progressBar == null)
            return;

        if (_total == -1 || _remain == -1) {
            _progressBar.setIndeterminate(true);
            _progressTextView.setText("Starting up...");
        } else if (_remain == 0) {
            // Done!
            AppMessagingClient.setOfflineMode(App.OfflineState.NORMAL);
            dismiss(true);
        } else {
            _progressBar.setMax(_total);
            _progressBar.setProgress(_total - _remain);
            _progressBar.setIndeterminate(false);
            _progressTextView.setText((_total - _remain) + " / " + _total);
        }
    }

    private void scheduleNext() {
        if (_running && !_scheduled) {
            _scheduled = true;
            _handler.postDelayed(_checker, 500);
        }
    }

    private final Runnable _checker = new Runnable() {
        @Override
        public void run() {
            _remain = WebTransaction.getSyncing().size();
            populateUi();

            _scheduled = false;
            scheduleNext();
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AppMessagingClient.setOfflineMode(App.OfflineState.SYNC);
            Log.v(TAG, "_cancel_onClick");
            dismiss(true);
        }
    };


    public static void show(Context context) {
        Controller.show(context, TAG, SyncProgressDialog.class, null);
    }
}
