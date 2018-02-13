package com.fieldnation.v2.ui.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.service.crawler.WebCrawlerService;

/**
 * Created by michaelcarver on 2/13/18.
 */

public class DownloadProgressDialog extends SimpleDialog {
    private static final String TAG = "DownloadProgressDialog";

    private ProgressBar _progressBar;
    private TextView _progressTextView;
    private Button _cancelButton;


    public DownloadProgressDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_offline_download_progress, container, false);

        _progressBar = v.findViewById(R.id.progressbar_view);
        _progressTextView = v.findViewById(R.id.progress_textview);

        _cancelButton = v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        return v;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        getContext().registerReceiver(broadcastReceiver, new IntentFilter(
                WebCrawlerService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(broadcastReceiver);

        super.onPause();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        if (App.get().getOfflineState() == App.OfflineState.NORMAL)
            AppMessagingClient.setOfflineMode(App.OfflineState.DOWNLOADING);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra(WebCrawlerService.UPDATE_OFFLINE_MODE);

            int total = bundle.getInt(WebCrawlerService.TOTAL_ASSIGNED_WOS);
            int remain = bundle.getInt(WebCrawlerService.TOTAL_LEFT_DOWNLOADING);

            if (_progressBar == null || _progressTextView == null)
                return;

            _progressBar.setMax(total);
            _progressBar.setProgress(total - remain);

            _progressTextView.setText(total - remain + " / " + total);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO stop the crawler

        }
    };


    public static void show(Context context, String uid) {
        Controller.show(context, uid, DownloadProgressDialog.class, null);
    }

    /*-************************************-*/
    /*-         Primary Listener           -*/
    /*-************************************-*/
    public interface OnPrimaryListener {
        void onPrimary(Parcelable extraData);
    }

    private static KeyedDispatcher<OnPrimaryListener> _onPrimaryDispatcher = new KeyedDispatcher<OnPrimaryListener>() {
        @Override
        public void onDispatch(OnPrimaryListener listener, Object... parameters) {
            listener.onPrimary((Parcelable) parameters[0]);
        }
    };

    public static void addOnPrimaryListener(String uid, OnPrimaryListener onPrimaryListener) {
        _onPrimaryDispatcher.add(uid, onPrimaryListener);
    }

    public static void removeOnPrimaryListener(String uid, OnPrimaryListener onPrimaryListener) {
        _onPrimaryDispatcher.remove(uid, onPrimaryListener);
    }

    public static void removeAllOnPrimaryListener(String uid) {
        _onPrimaryDispatcher.removeAll(uid);
    }

}
