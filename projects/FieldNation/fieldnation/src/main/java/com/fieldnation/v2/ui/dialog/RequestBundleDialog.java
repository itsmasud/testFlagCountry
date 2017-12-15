package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.v2.ui.ListItemSummaryView;
import com.fieldnation.v2.ui.ListItemTwoHorizView;

/**
 * Created by mc on 10/27/16.
 */

public class RequestBundleDialog extends FullScreenDialog {
    private static final String TAG = "RequestBundleDialog";

    // Dialogs
    private static final String DIALOG_UID_EXPIRE = TAG + ".expireDialog";

    private final static int INVALID_NUMBER = -1;

    // Type
    private static final String PARAM_TYPE = "type";
    public static final int TYPE_ACCEPT = 0;
    public static final int TYPE_REQUEST = 1;

    private static final String PARAM_WORK_ORDER_ID = "workOrderId";
    private static final String PARAM_BUNDLE_ID = "bundleId";
    private static final String PARAM_BUNDLE_SIZE = "bundleSize";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private ListItemTwoHorizView _expiresView;
    private ListItemSummaryView _bundleView;
    private TextView _termsTextView;

    private RefreshView _refreshView;

    // Data
    private int _type = TYPE_ACCEPT;
    private long _bundleId = 0;
    private int _bundleSize = 0;
    private int _workOrderId = 0;

    private long _expiringDurationSeconds = -1;
    private boolean _expires;

    public RequestBundleDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_accept_bundle, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);

        _expiresView = v.findViewById(R.id.expires_view);
        _bundleView = v.findViewById(R.id.bundle_view);

        _termsTextView = v.findViewById(R.id.termswarning_textview);

        _refreshView = v.findViewById(R.id.refresh_view);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        ExpireDialog.addOnOkListener(DIALOG_UID_EXPIRE, _expireDialog_onOk);

    }

    @Override
    public void onResume() {
        super.onResume();

        _expiresView.setOnClickListener(_expires_onClick);
        _termsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _type = payload.getInt(PARAM_TYPE);
        _bundleId = payload.getLong(PARAM_BUNDLE_ID);
        _bundleSize = payload.getInt(PARAM_BUNDLE_SIZE);
        _workOrderId = payload.getInt(PARAM_WORK_ORDER_ID);

        super.show(payload, animate);

        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);

        if (savedState.containsKey("_expiringDurationSeconds"))
            _expiringDurationSeconds = savedState.getLong("_expiringDurationSeconds");

        if (savedState.containsKey("_expires"))
            _expires = savedState.getBoolean("expires");

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);
        if (_expires)
            outState.putBoolean("_expires", _expires);

        if (_expiringDurationSeconds != -1)
            outState.putLong("_expiringDurationSeconds", _expiringDurationSeconds);
    }

    @Override
    public void onStop() {
        ExpireDialog.removeOnOkListener(DIALOG_UID_EXPIRE, _expireDialog_onOk);
        super.onStop();
    }

    private void populateUi() {
        switch (_type) {
            case TYPE_ACCEPT: {
                _toolbar.setTitle(R.string.accept_bundle);
                _bundleView.setTitle("Work Orders in Bundle");
                _bundleView.setCount(_bundleSize + "");
                _bundleView.setCountBg(R.drawable.round_rect_gray);
                setExpirationVisibility(false);
                _finishMenu.setText(R.string.btn_accept);

                SpannableString spanned = new SpannableString("By accepting these work orders, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
                spanned.setSpan(_standardTerms_onClick, 92, 132, spanned.getSpanFlags(_standardTerms_onClick));
                spanned.setSpan(_pqap_onClick, 141, 174, spanned.getSpanFlags(_pqap_onClick));
                _termsTextView.setText(spanned);
                _termsTextView.setVisibility(View.VISIBLE);

                break;
            }
            case TYPE_REQUEST: {
                _toolbar.setTitle(R.string.request_bundle);
                _bundleView.setTitle("Work Orders in Bundle");
                _bundleView.setCount(_bundleSize + "");
                _bundleView.setCountBg(R.drawable.round_rect_gray);
                setExpirationVisibility(true);
                _finishMenu.setText(R.string.btn_request);

                SpannableString spanned = new SpannableString("By requesting these work orders, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
                spanned.setSpan(_standardTerms_onClick, 93, 133, spanned.getSpanFlags(_standardTerms_onClick));
                spanned.setSpan(_pqap_onClick, 142, 175, spanned.getSpanFlags(_pqap_onClick));
                _termsTextView.setText(spanned);
                _termsTextView.setVisibility(View.VISIBLE);

                if (_expiringDurationSeconds == -1) {
                    _expiresView.set("Expire Request", "Never");
                } else {
                    _expiresView.set("Expire Request", misc.convertMsToHuman(_expiringDurationSeconds * 1000));
                }

                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setExpirationVisibility(boolean visible) {
        _expiresView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private final ExpireDialog.OnOkListener _expireDialog_onOk = new ExpireDialog.OnOkListener() {
        @Override
        public void onOk(String title, int milliseconds) {
            _expiringDurationSeconds = milliseconds / 1000;
            populateUi();
        }
    };

    private final ClickableSpan _standardTerms_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=workorder"));
            ActivityClient.startActivity(intent);
        }
    };

    private final ClickableSpan _pqap_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=qualityassurance"));
            ActivityClient.startActivity(intent);
        }
    };

    @Override
    public void cancel() {
        super.cancel();
        _onCanceledDispatcher.dispatch(getUid(), _workOrderId);
    }

    private final View.OnClickListener _toolbar_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new ApatheticOnMenuItemClickListener() {
        @Override
        public boolean onSingleMenuItemClick(MenuItem item) {
            switch (_type) {
//                case TYPE_ACCEPT:
//                    WorkorderClient.actionAcceptAssignment(App.get(), _workOrderId, null, null, null, false);
//                    _onAcceptedDispatcher.dispatch(getUid(), _workOrderId);
//                    break;
                case TYPE_REQUEST:
                    if (_expires && _expiringDurationSeconds > -1) {
                        WorkorderClient.actionRequest(App.get(), _workOrderId, _expiringDurationSeconds);
                    } else {
                        WorkorderClient.actionRequest(App.get(), _workOrderId, -1);
                    }
                    _onRequestedDispatcher.dispatch(getUid(), _workOrderId);
                    break;
            }
            dismiss(true);
            return true;
        }
    };

    private final View.OnClickListener _expires_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            ExpireDialog.show(App.get(), DIALOG_UID_EXPIRE);
        }
    };


    /**
     * @param context     Application context
     * @param uid         the uid of the dialog
     * @param bundleId    The id of the bundle we're worried about
     * @param bundleSize  The number of work orders in the bundle
     * @param workOrderId An id of one of the work orders in the bundle.
     * @param type        One of {@link #TYPE_ACCEPT} or {@link #TYPE_REQUEST}
     */
    public static void show(Context context, String uid, long bundleId, int bundleSize, int workOrderId, int type) {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_BUNDLE_ID, bundleId);
        bundle.putInt(PARAM_WORK_ORDER_ID, workOrderId);
        bundle.putInt(PARAM_BUNDLE_SIZE, bundleSize);
        bundle.putInt(PARAM_TYPE, type);
        Controller.show(context, uid, RequestBundleDialog.class, bundle);
    }

    /*-**************************************-*/
    /*-         Requested Listener           -*/
    /*-**************************************-*/
    public interface OnRequestedListener {
        void onRequested(int workOrderId);
    }

    private static KeyedDispatcher<OnRequestedListener> _onRequestedDispatcher = new KeyedDispatcher<OnRequestedListener>() {
        @Override
        public void onDispatch(OnRequestedListener listener, Object... parameters) {
            listener.onRequested((Integer) parameters[0]);
        }
    };

    public static void addOnRequestedListener(String uid, OnRequestedListener onRequestedListener) {
        _onRequestedDispatcher.add(uid, onRequestedListener);
    }

    public static void removeOnRequestedListener(String uid, OnRequestedListener onRequestedListener) {
        _onRequestedDispatcher.remove(uid, onRequestedListener);
    }

    public static void removeAllOnRequestedListener(String uid) {
        _onRequestedDispatcher.removeAll(uid);
    }


    /*-*************************************-*/
    /*-         Canceled Listener           -*/
    /*-*************************************-*/
    public interface OnCanceledListener {
        void onCanceled(int workOrderId);
    }

    private static KeyedDispatcher<OnCanceledListener> _onCanceledDispatcher = new KeyedDispatcher<OnCanceledListener>() {
        @Override
        public void onDispatch(OnCanceledListener listener, Object... parameters) {
            listener.onCanceled((Integer) parameters[0]);
        }
    };

    public static void addOnCanceledListener(String uid, OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.add(uid, onCanceledListener);
    }

    public static void removeOnCanceledListener(String uid, OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.remove(uid, onCanceledListener);
    }

    public static void removeAllOnCanceledListener(String uid) {
        _onCanceledDispatcher.removeAll(uid);
    }
}
