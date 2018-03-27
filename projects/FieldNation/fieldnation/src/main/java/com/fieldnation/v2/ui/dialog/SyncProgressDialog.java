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

import java.util.List;

/**
 * Created by michaelcarver on 2/13/18.
 */

public class SyncProgressDialog extends SimpleDialog {
    private static final String TAG = "SyncProgressDialog";

    // Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private ProgressBar _progressBar;
    private TextView _progressTextView;
    private Button _cancelButton;

    private int _total = -1;
    private int _remain = -1;
    private int _zombie = -1;
    private boolean _running = false;
    private Handler _handler = new Handler();
    private boolean _scheduled = false;
    private boolean _netWorkDown = false;

    public SyncProgressDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_sync_progress, container, false);

        _titleTextView = v.findViewById(R.id.title_textview);
        _bodyTextView = v.findViewById(R.id.body_textview);

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
        _appClient.subNetworkState();
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
    public void onStop() {
        _appClient.unsubNetworkState();

        super.onStop();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        populateUi();
    }

    private void populateUi() {
        if (_progressBar == null)
            return;

        Log.v(TAG, "t" + _total + " r" + _remain + " z" + _zombie);

        _titleTextView.setText("Sync in Progress");
        _bodyTextView.setText("Your unsynced activity is now being uploaded to Field Nation");
        _cancelButton.setText("CANCEL");

        if (_total == -1 || _remain == -1) {
            _progressBar.setVisibility(View.VISIBLE);
            _progressTextView.setVisibility(View.VISIBLE);
            _progressBar.setIndeterminate(true);
            _progressTextView.setText("Starting up...");
        } else if (_netWorkDown) {
            _titleTextView.setText("Sync Failed");
            _bodyTextView.setText("Please check your network connection and try again");
            _progressBar.setVisibility(View.GONE);
            _progressTextView.setVisibility(View.GONE);
            _cancelButton.setText("CLOSE");
        } else if (_remain == 0) {
            if (_zombie > 0) {
                _titleTextView.setText("Sync Failed");
                _bodyTextView.setText("Please check your network connection and try again");
                _progressBar.setVisibility(View.GONE);
                _progressTextView.setVisibility(View.GONE);
                _cancelButton.setText("CLOSE");
            } else {
                _titleTextView.setText("Sync Complete");
                _bodyTextView.setText("Your unsynced activity has been uploaded to Field Nation");
                _progressBar.setVisibility(View.GONE);
                _progressTextView.setVisibility(View.GONE);
                _cancelButton.setText("CLOSE");
            }

        } else {
            _progressBar.setVisibility(View.VISIBLE);
            _progressTextView.setVisibility(View.VISIBLE);
            _progressBar.setMax(_total);
            _progressBar.setProgress(_total - (_remain));
            _progressBar.setIndeterminate(false);
            _progressTextView.setText((_total - (_remain)) + " / " + _total);
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
            Log.v(TAG, "Checker run");
            List<WebTransaction> list = WebTransaction.getSyncing();
            _zombie = 0;
            for (WebTransaction webTransaction : list) {
                if (webTransaction.isZombie())
                    _zombie++;
            }
            _remain = list.size() - _zombie;

            populateUi();
            _scheduled = false;
            scheduleNext();
        }
    };

    private final AppMessagingClient _appClient = new AppMessagingClient() {
        @Override
        public void onNetworkDisconnected() {
            _netWorkDown = true;
            populateUi();
        }

        @Override
        public void onNetworkConnected() {
            _netWorkDown = false;
            populateUi();
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v(TAG, "_cancel_onClick");
            if (_remain == 0) {
                if (_zombie > 0) {
                    List<WebTransaction> list = WebTransaction.getSyncing();
                    for (WebTransaction webTransaction : list) {
                        webTransaction.requeue(0);
                    }
                    AppMessagingClient.setOfflineMode(App.OfflineState.SYNC);
                } else
                    AppMessagingClient.setOfflineMode(App.OfflineState.NORMAL);
            } else {
                AppMessagingClient.setOfflineMode(App.OfflineState.SYNC);
            }
            dismiss(true);
        }
    };

    public static void show(Context context) {
        Controller.show(context, TAG, SyncProgressDialog.class, null);
    }
}
