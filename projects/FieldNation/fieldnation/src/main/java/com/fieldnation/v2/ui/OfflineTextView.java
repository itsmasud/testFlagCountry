package com.fieldnation.v2.ui;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;

public class OfflineTextView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "OfflineTextView";

    private static final String DIALOG_OFFLINE_ASK = TAG + ".offlienAskDialog";
    private static final String DIALOG_SYNC_ASK = TAG + ".syncAskDialog";

    public OfflineTextView(Context context) {
        super(context);
        init();
    }

    public OfflineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OfflineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(_offline_onClick);
        populateUi();
    }

    private void populateUi() {
        setVisibility(App.get().getOfflineState() == App.OfflineState.OFFLINE ? VISIBLE : GONE);
    }

    @Override
    protected void onAttachedToWindow() {
        _offline.subOfflineMode();
        TwoButtonDialog.addOnPrimaryListener(DIALOG_OFFLINE_ASK, _offlineAsk_onPrimary);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_SYNC_ASK, _syncAsk_onPrimary);
        TwoButtonDialog.addOnSecondaryListener(DIALOG_SYNC_ASK, _syncAsk_onSecondary);

        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        _offline.unsubOfflineMode();
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_OFFLINE_ASK, _offlineAsk_onPrimary);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_SYNC_ASK, _syncAsk_onPrimary);
        TwoButtonDialog.removeOnSecondaryListener(DIALOG_SYNC_ASK, _syncAsk_onSecondary);
    }

    private final AppMessagingClient _offline = new AppMessagingClient() {
        @Override
        public void onOfflineMode(App.OfflineState state) {
            // updateUI
            populateUi();
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _offlineAsk_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            // yes please!

            if (WebTransaction.getWorkOrderCount(WebTransaction.getSyncing()) > 0) {
                TwoButtonDialog.show(App.get(), DIALOG_SYNC_ASK, "Sync Activity",
                        "Would you like to upload your unsynced activity list of "
                                + WebTransaction.getWorkOrderCount(WebTransaction.getSyncing())
                                + " work orders including all attachments? Data rates may apply.",
                        "SYNC ACTIVITY", "NOT NOW", false, null);
            } else {
                AppMessagingClient.setOfflineMode(App.OfflineState.NORMAL);
            }
        }
    };

    private final View.OnClickListener _offline_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TwoButtonDialog.show(App.get(), DIALOG_OFFLINE_ASK, "Offline Mode",
                    "You are currently only viewing your offline content. Would you like to go online?",
                    "GO ONLINE", "CANCEL", false, null);
            Log.v(TAG, "_offline_onClick");
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _syncAsk_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setOfflineMode(App.OfflineState.UPLOADING);
        }
    };

    private final TwoButtonDialog.OnSecondaryListener _syncAsk_onSecondary = new TwoButtonDialog.OnSecondaryListener() {
        @Override
        public void onSecondary(Parcelable extraData) {
            AppMessagingClient.setOfflineMode(App.OfflineState.SYNC);
        }
    };
}
