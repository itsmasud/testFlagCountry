package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.fntools.KeyedDispatcher;

/**
 * Created by mc on 10/27/16.
 */

public class RequestBundleDialog extends SimpleDialog {
    private static final String TAG = "RequestBundleDialog";

    // State
    private static final String STATE_SPINNER_POSITION = "STATE_SPINNER_POSITION";

    private final static int INVALID_NUMBER = -1;

    // Type
    private static final String PARAM_TYPE = "type";
    public static final int TYPE_ACCEPT = 0;
    public static final int TYPE_REQUEST = 1;

    private static final String PARAM_WORK_ORDER_ID = "workOrderId";
    private static final String PARAM_BUNDLE_ID = "bundleId";
    private static final String PARAM_BUNDLE_SIZE = "bundleSize";

    // Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private TextView _expiresTextView;
    private CheckBox _expiresCheckBox;
    private HintSpinner _expireSpinner;
    private View _dividerView;
    private TextView _termsWarningTextView;
    private Button _cancelButton;
    private Button _okButton;

    // Data
    private int _type = TYPE_ACCEPT;
    private long _bundleId = 0;
    private int _bundleSize = 0;
    private long _workOrderId = 0;

    private int _currentPosition = 1;
    private long _expiringDurationSeconds = -1;
    private int[] _durations;
    private boolean _expires;

    public RequestBundleDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_accept_bundle, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);
        _expiresTextView = (TextView) v.findViewById(R.id.expires_textview);
        _dividerView = v.findViewById(R.id.divider);
        _expiresCheckBox = (CheckBox) v.findViewById(R.id.expires_checkbox);
        _expireSpinner = (HintSpinner) v.findViewById(R.id.expire_duration_spinner);
        _termsWarningTextView = (TextView) v.findViewById(R.id.termswarning_textview);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _okButton = (Button) v.findViewById(R.id.ok_button);

        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(getView().getContext(), R.array.expire_duration_titles, R.layout.view_counter_offer_reason_spinner_item);
        adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _expireSpinner.setAdapter(adapter);

        _durations = getView().getContext().getResources().getIntArray(R.array.expire_duration_values);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton.setOnClickListener(_ok_onClick);
        _expiresCheckBox.setOnClickListener(_expires_onClick);
        _expireSpinner.setOnItemSelectedListener(_expireSpinner_selected);

        _termsWarningTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _type = payload.getInt(PARAM_TYPE);
        _bundleId = payload.getLong(PARAM_BUNDLE_ID);
        _bundleSize = payload.getInt(PARAM_BUNDLE_SIZE);
        _workOrderId = payload.getLong(PARAM_WORK_ORDER_ID);

        super.show(payload, animate);

        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey(STATE_SPINNER_POSITION))
            _currentPosition = savedState.getInt(STATE_SPINNER_POSITION);
        super.onRestoreDialogState(savedState);
    }

    private void populateUi() {
        switch (_type) {
            case TYPE_ACCEPT: {
                _titleTextView.setText(R.string.accept_bundle);
                _bodyTextView.setText("If you accept this bundle you are accepting all " + _bundleSize + " work orders.");
                setExpirationVisibility(false);
                _okButton.setText(R.string.btn_accept);

                SpannableString spanned = new SpannableString("By accepting these work orders, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
                spanned.setSpan(_standardTerms_onClick, 92, 132, spanned.getSpanFlags(_standardTerms_onClick));
                spanned.setSpan(_pqap_onClick, 141, 174, spanned.getSpanFlags(_pqap_onClick));
                _termsWarningTextView.setText(spanned);
                _termsWarningTextView.setVisibility(View.VISIBLE);

                break;
            }
            case TYPE_REQUEST: {
                _titleTextView.setText(R.string.request_bundle);
                _bodyTextView.setText("If you request this bundle you are requesting all " + _bundleSize + " work orders.");
                setExpirationVisibility(true);
                _okButton.setText(R.string.btn_request);

                SpannableString spanned = new SpannableString("By requesting these work orders, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
                spanned.setSpan(_standardTerms_onClick, 93, 133, spanned.getSpanFlags(_standardTerms_onClick));
                spanned.setSpan(_pqap_onClick, 142, 175, spanned.getSpanFlags(_pqap_onClick));
                _termsWarningTextView.setText(spanned);
                _termsWarningTextView.setVisibility(View.VISIBLE);

                if (_currentPosition != INVALID_NUMBER) {
                    _expiringDurationSeconds = _durations[_currentPosition];
                    _expireSpinner.setSelection(_currentPosition);
                }

                break;
            }
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        outState.putInt(STATE_SPINNER_POSITION, _currentPosition);

        super.onSaveDialogState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setExpirationVisibility(boolean visible) {
        _expiresCheckBox.setVisibility(visible ? View.VISIBLE : View.GONE);
        _expireSpinner.setVisibility(visible ? View.VISIBLE : View.GONE);
        _expiresTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private final ClickableSpan _standardTerms_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=workorder"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };

    private final ClickableSpan _pqap_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=qualityassurance"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };

    @Override
    public void cancel() {
        super.cancel();
        _onCanceledDispatcher.dispatch(getUid(), _workOrderId);
    }

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
        }
    };

    private final AdapterView.OnItemSelectedListener _expireSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _currentPosition = position;
            _expiringDurationSeconds = _durations[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _currentPosition = 1;
        }
    };

    private final View.OnClickListener _expires_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _expires = _expiresCheckBox.isChecked();

            if (!_expires) {
                _expiringDurationSeconds = INVALID_NUMBER;
            }
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
    public static void show(Context context, String uid, long bundleId, int bundleSize, long workOrderId, int type) {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_BUNDLE_ID, bundleId);
        bundle.putLong(PARAM_WORK_ORDER_ID, workOrderId);
        bundle.putInt(PARAM_BUNDLE_SIZE, bundleSize);
        bundle.putInt(PARAM_TYPE, type);
        Controller.show(context, uid, RequestBundleDialog.class, bundle);
    }

    /*-**************************************-*/
    /*-         Requested Listener           -*/
    /*-**************************************-*/
    public interface OnRequestedListener {
        void onRequested(long workOrderId);
    }

    private static KeyedDispatcher<OnRequestedListener> _onRequestedDispatcher = new KeyedDispatcher<OnRequestedListener>() {
        @Override
        public void onDispatch(OnRequestedListener listener, Object... parameters) {
            listener.onRequested((Long) parameters[0]);
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
        void onCanceled(long workOrderId);
    }

    private static KeyedDispatcher<OnCanceledListener> _onCanceledDispatcher = new KeyedDispatcher<OnCanceledListener>() {
        @Override
        public void onDispatch(OnCanceledListener listener, Object... parameters) {
            listener.onCanceled((Long) parameters[0]);
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
