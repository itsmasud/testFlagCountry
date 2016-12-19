package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fndialog.Dialog;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.ReportProblemListFactory;
import com.fieldnation.service.data.workorder.ReportProblemType;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

/**
 * Created by Michael on 10/5/2016.
 */

public class ReportProblemDialog extends SimpleDialog {
    private static final String TAG = "ReportProblemDialog";

    // State keys
    private static final String PARAM_WORKORDER_ID = "workOrderId";
    private static final String PARAM_WORKORDER = "workOrder";

    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_PRIMARY_POS = "STATE_PRIMARY_POS";
    private static final String STATE_SECONDARY_POS = "STATE_SECONDARY_POS";
    private static final String STATE_SELECTED_PROBLEM = "STATE_SELECTED_PROBLEM";
    private static final String STATE_OK_ENABLED = "STATE_OK_ENABLED";

    private static final String[] TIMEFRAMES = new String[]{"5", "10", "15", "Other"};

    // Ui
    private TextView _titleTextView;
    private ProgressBar _loadingBar;
    private HintSpinner _primarySpinner;
    private HintSpinner _secondarySpinner;
    private HintSpinner _timeframeSpinner;
    private TextInputLayout _timeframeLayout;
    private EditText _timeframeEditText;
    private TextInputLayout _explanationLayout;
    private EditText _explanationEditText;
    private TextView _noteTextView;
    private Button _cancelButton;
    private Button _okButton;

    // Data
    private ReportProblemType[] _primaryList = null;
    private ReportProblemType[] _secondaryList = null;
    private long _workOrderId;
    private WorkorderClient _workorderClient;

    // Saved Data
    private Workorder _workorder;
    private int _primaryPosition = -1;
    private int _secondaryPosition = -1;
    private int _timeframePosition = -1;
    private ReportProblemType _selectedProblem = null;
    private boolean _clear = false;

    public ReportProblemDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_report_problem, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _loadingBar = (ProgressBar) v.findViewById(R.id.loadingBar);
        _primarySpinner = (HintSpinner) v.findViewById(R.id.primary_spinner);
        _secondarySpinner = (HintSpinner) v.findViewById(R.id.secondary_spinner);
        _timeframeSpinner = (HintSpinner) v.findViewById(R.id.timeframe_spinner);
        _timeframeLayout = (TextInputLayout) v.findViewById(R.id.timeframe_layout);
        _timeframeEditText = (EditText) v.findViewById(R.id.timeframe_edittext);
        _explanationLayout = (TextInputLayout) v.findViewById(R.id.explanation_layout);
        _explanationEditText = (EditText) v.findViewById(R.id.explanation_edittext);
        _noteTextView = (TextView) v.findViewById(R.id.note_textview);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _okButton = (Button) v.findViewById(R.id.ok_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _primarySpinner.setOnItemSelectedListener(_problem1_onItemClick);
        _secondarySpinner.setOnItemSelectedListener(_problem2_onItemClick);
        _timeframeSpinner.setOnItemSelectedListener(_timeframe_onItemClick);
        _timeframeEditText.addTextChangedListener(_timeframeEditText_watcher);
        _explanationEditText.addTextChangedListener(_textEditText_watcherListener);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton.setOnClickListener(_ok_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        setLoading(true);
        _workorder = null;
        _workOrderId = payload.getLong(PARAM_WORKORDER_ID);

        if (payload.containsKey(PARAM_WORKORDER)) {
            _workorder = (Workorder) payload.getParcelable(PARAM_WORKORDER);
        } else {
            _workorderClient = new WorkorderClient(_workorderClient_listener);
            _workorderClient.connect(App.get());
            WorkorderClient.get(App.get(), _workOrderId, false);
        }

        populateUi();
        super.show(payload, animate);
    }

    private void setLoading(boolean loading) {
        if (loading) {
            _titleTextView.setVisibility(View.GONE);
            _loadingBar.setVisibility(View.VISIBLE);
            _primarySpinner.setVisibility(View.GONE);
            _secondarySpinner.setVisibility(View.GONE);
            _timeframeSpinner.setVisibility(View.GONE);
            _timeframeLayout.setVisibility(View.GONE);
            _explanationLayout.setVisibility(View.GONE);
            _noteTextView.setVisibility(View.GONE);
            _cancelButton.setVisibility(View.GONE);
            _okButton.setVisibility(View.GONE);
        } else {
            _titleTextView.setVisibility(View.VISIBLE);
            _loadingBar.setVisibility(View.GONE);
            _primarySpinner.setVisibility(View.VISIBLE);
            _secondarySpinner.setVisibility(View.GONE);
            _timeframeSpinner.setVisibility(View.GONE);
            _timeframeLayout.setVisibility(View.GONE);
            _explanationLayout.setVisibility(View.VISIBLE);
            _noteTextView.setVisibility(View.VISIBLE);
            _cancelButton.setVisibility(View.VISIBLE);
            _okButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismiss(boolean animate) {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        super.dismiss(animate);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);

        if (savedState == null)
            return;

        if (savedState.containsKey(STATE_PRIMARY_POS))
            _primaryPosition = savedState.getInt(STATE_PRIMARY_POS);

        if (savedState.containsKey(STATE_SECONDARY_POS))
            _secondaryPosition = savedState.getInt(STATE_SECONDARY_POS);

        if (savedState.containsKey(STATE_WORKORDER))
            _workorder = savedState.getParcelable(STATE_WORKORDER);

        if (savedState.containsKey(STATE_SELECTED_PROBLEM))
            _selectedProblem = ReportProblemType.values()[savedState.getInt(STATE_SELECTED_PROBLEM)];

        if (savedState.containsKey(STATE_OK_ENABLED))
            _okButton.setEnabled(savedState.getBoolean(STATE_OK_ENABLED));

        if (_workorder != null) {
            _primaryList = ReportProblemListFactory.getPrimaryList(_workorder);
            if (_primaryList != null) {
                getPrimaryAdapter().clear();
                getPrimaryAdapter().addAll((Object[]) _primaryList);

                if (_primaryPosition != -1) {
                    getPrimarySpinner().setSelection(_primaryPosition);
                    _secondaryList = ReportProblemListFactory.getSecondaryList(_workorder, _primaryList[_primaryPosition]);
                    if (_secondaryList != null) {
                        getSecondaryAdapter().clear();
                        getSecondaryAdapter().addAll((Object[]) _secondaryList);
                    }
                    if (_secondaryPosition != -1 && _secondaryList != null) {
                        getSecondarySpinner().setSelection(_secondaryPosition);
                    } else {
                        getSecondarySpinner().clearSelection();
                    }
                } else {
                    getPrimarySpinner().clearSelection();
                }
            } else {
                getPrimarySpinner().clearSelection();
            }
        } else {
            getPrimarySpinner().clearSelection();
        }
        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        outState.putInt(STATE_PRIMARY_POS, _primaryPosition);
        outState.putInt(STATE_SECONDARY_POS, _secondaryPosition);
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);
        if (_selectedProblem != null)
            outState.putInt(STATE_SELECTED_PROBLEM, _selectedProblem.ordinal());
        if (_okButton != null)
            outState.putBoolean(STATE_OK_ENABLED, _okButton.isEnabled());

        super.onSaveDialogState(outState);
    }

    private HintSpinner getPrimarySpinner() {
        if (_primarySpinner != null && _primarySpinner.getAdapter() == null) {
            HintArrayAdapter adapter = new HintArrayAdapter(getView().getContext(), R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _primarySpinner.setAdapter(adapter);
        }
        return _primarySpinner;
    }

    private HintArrayAdapter getPrimaryAdapter() {
        return (HintArrayAdapter) getPrimarySpinner().getAdapter();
    }

    private HintSpinner getSecondarySpinner() {
        if (_secondarySpinner != null && _secondarySpinner.getAdapter() == null) {
            HintArrayAdapter adapter = new HintArrayAdapter(getView().getContext(), R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _secondarySpinner.setAdapter(adapter);
        }
        return _secondarySpinner;
    }

    private HintArrayAdapter getSecondaryAdapter() {
        return (HintArrayAdapter) getSecondarySpinner().getAdapter();
    }

    private HintSpinner getTimeFrameSpinner() {
        if (_timeframeSpinner != null && _timeframeSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromArray(getView().getContext(),
                    TIMEFRAMES, R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _timeframeSpinner.setAdapter(adapter);
        }
        return _timeframeSpinner;
    }

    private HintArrayAdapter getTimeFrameAdapter() {
        return (HintArrayAdapter) _timeframeSpinner.getAdapter();
    }

    private void populateUi() {
        if (_noteTextView == null)
            return;

        if (_workorder == null)
            return;

        setLoading(false);

        { // put in a block to limit the scope of pList
            ReportProblemType[] pList = ReportProblemListFactory.getPrimaryList(_workorder);

            if (pList == null) {
                _primaryList = null;
                getPrimaryAdapter().clear();
                getPrimarySpinner().clearSelection();
            } else if (pList != _primaryList) {
                _primaryList = pList;
                getPrimaryAdapter().clear();
                getPrimaryAdapter().addAll((Object[]) _primaryList);
                getPrimarySpinner().clearSelection();
            }
        }

        getTimeFrameSpinner();

        _noteTextView.setVisibility(View.GONE);
        _timeframeSpinner.setVisibility(View.GONE);

        if (_primaryList == null) {
            return;
        }

        if (_primaryPosition >= 0) {
            ReportProblemType[] sList = ReportProblemListFactory.getSecondaryList(_workorder, _primaryList[_primaryPosition]);

            if (sList == null) {
                _secondaryList = null;
                getSecondarySpinner().setVisibility(View.GONE);
                getSecondaryAdapter().clear();
            } else if (sList != _secondaryList) {
                _secondaryList = sList;
                getSecondaryAdapter().clear();
                getSecondaryAdapter().addAll((Object[]) _secondaryList);
                getSecondarySpinner().clearSelection();
            }
        }

        if (_secondaryList != null) {
            getSecondarySpinner().setVisibility(View.VISIBLE);
            getSecondarySpinner().clearFocus();
        }

        if (!misc.isEmptyOrNull(_explanationEditText.getText().toString()) && _selectedProblem != null) {
            _okButton.setEnabled(true);
        } else {
            _okButton.setEnabled(false);
        }

        if (_selectedProblem == null) {
            return;
        }

        switch (_selectedProblem) {
            case CANNOT_MAKE_ASSIGNMENT:
                _noteTextView.setText(R.string.once_submitted_you_will_be_removed);
                _noteTextView.setVisibility(View.VISIBLE);
                _explanationEditText.requestFocus();
                misc.showKeyboard(_explanationEditText);
                break;

            case WILL_BE_LATE:
                _timeframeSpinner.setVisibility(View.VISIBLE);

                if (_timeframePosition == -1 || _timeframePosition == 3) {
                    _okButton.setEnabled(false);
                } else {
                    _okButton.setEnabled(true);
                }

                if (_timeframePosition == 3) {
                    _timeframeLayout.setVisibility(View.VISIBLE);
                    if (misc.isEmptyOrNull(_timeframeEditText.getText().toString())) {
                        _okButton.setEnabled(false);
                    }
                } else {
                    _timeframeLayout.setVisibility(View.GONE);
                }

                break;

            case DO_NOT_HAVE_SHIPMENT:
            case DO_NOT_HAVE_INFO:
            case DO_NOT_HAVE_RESPONSE:
            case DO_NOT_HAVE_OTHER:
            case BUYER_UNRESPONSIVE:
            case SCOPE_OF_WORK:
            case SITE_NOT_READY_CONTACT:
            case SITE_NOT_READY_PRIOR_WORK:
            case SITE_NOT_READY_ACCESS:
            case SITE_NOT_READY_OTHER:
            case OTHER:
            case APPROVAL_NOT_YET:
            case APPROVAL_DISAGREEMENT:
            case PAYMENT_NOT_RECEIVED:
            case PAYMENT_NOT_ACCURATE:
            case APPROVAL:
                _explanationEditText.requestFocus();
                misc.showKeyboard(_explanationEditText);
                break;

            case SITE_NOT_READY:
                getSecondarySpinner().setHint(R.string.what_about_site);
                getSecondarySpinner().clearSelection();
                _okButton.setEnabled(false);
                break;

            case MISSING:
                getSecondarySpinner().setHint(R.string.what_are_you_missing);
                getSecondarySpinner().clearSelection();
                _okButton.setEnabled(false);
                break;

            default: {
                _okButton.setEnabled(false);
                getSecondarySpinner().setVisibility(View.GONE);
                break;
            }
        }
    }

    private final AdapterView.OnItemSelectedListener _problem1_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _primaryPosition = position;
            _selectedProblem = (ReportProblemType) getPrimaryAdapter().getItem(position);
            _textEditText_watcherListener.onTextChanged(
                    _explanationEditText.getText().toString(),
                    0, _explanationEditText.getText().toString().length(),
                    _explanationEditText.getText().toString().length());
            populateUi();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final AdapterView.OnItemSelectedListener _problem2_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _secondaryPosition = position;
            misc.showKeyboard(_explanationEditText);
            _selectedProblem = (ReportProblemType) getSecondaryAdapter().getItem(position);
            _textEditText_watcherListener.onTextChanged(
                    _explanationEditText.getText().toString(),
                    0, _explanationEditText.getText().toString().length(),
                    _explanationEditText.getText().toString().length());
            populateUi();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final AdapterView.OnItemSelectedListener _timeframe_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _timeframePosition = position;
            Log.v(TAG, "onItemSelected " + position);
            populateUi();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            misc.hideKeyboard(_okButton);
            Log.v(TAG, "problem1=" + _primaryPosition + ", problem2=" + _secondaryPosition);

            String explanation = null;

            if (!misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
                explanation = _explanationEditText.getText().toString();
            }

            switch (_selectedProblem) {
                case CANNOT_MAKE_ASSIGNMENT:
                    CancelWarningDialog.Controller.show(App.get(), _workorder.getWorkorderId(), explanation);
                    break;

                case WILL_BE_LATE:
                    if (_timeframePosition == 3) {
                        try {
                            WorkorderClient.actionRunningLate(App.get(), _workOrderId, explanation, Integer.parseInt(_timeframeEditText.getText().toString()) * 60);
                            ToastClient.toast(App.get(), R.string.thanks_for_the_heads_up, Toast.LENGTH_LONG);
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                            ToastClient.toast(App.get(), "Please enter a number for the delay", Toast.LENGTH_LONG);
                        }
                    } else {
                        try {
                            String delay = TIMEFRAMES[_timeframePosition];
                            WorkorderClient.actionRunningLate(App.get(), _workOrderId, explanation, Integer.parseInt(delay) * 60);
                            ToastClient.toast(App.get(), R.string.thanks_for_the_heads_up, Toast.LENGTH_LONG);
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                            ToastClient.toast(App.get(), "Please enter a number for the delay", Toast.LENGTH_LONG);
                        }
                    }
                    break;

                case SCOPE_OF_WORK:
                case SITE_NOT_READY_CONTACT:
                case SITE_NOT_READY_PRIOR_WORK:
                case SITE_NOT_READY_ACCESS:
                case SITE_NOT_READY_OTHER:
                case OTHER:
                case APPROVAL_NOT_YET:
                case APPROVAL_DISAGREEMENT:
                case PAYMENT_NOT_ACCURATE:
                case DO_NOT_HAVE_SHIPMENT:
                case DO_NOT_HAVE_INFO:
                case DO_NOT_HAVE_RESPONSE:
                case DO_NOT_HAVE_OTHER:
                    ToastClient.toast(App.get(), R.string.buyer_has_been_notified, Toast.LENGTH_LONG);
                    WorkorderClient.actionReportProblem(App.get(), _workOrderId, explanation, _selectedProblem);
                    break;

                case BUYER_UNRESPONSIVE:
                    ToastClient.toast(App.get(), R.string.buyer_and_support_have_been_notified, Toast.LENGTH_LONG);
                    WorkorderClient.actionReportProblem(App.get(), _workOrderId, explanation, _selectedProblem);
                    break;

                case PAYMENT_NOT_RECEIVED:
                    ToastClient.toast(App.get(), R.string.support_has_been_notified, Toast.LENGTH_LONG);
                    WorkorderClient.actionReportProblem(App.get(), _workOrderId, explanation, _selectedProblem);
                    break;

                case SITE_NOT_READY:
                    break;
                case MISSING:
                    break;
                case APPROVAL:
                    break;
                default:
                    break;
            }

            dismiss(true);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            misc.hideKeyboard(v);
        }
    };

    private final TextWatcher _textEditText_watcherListener = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (_primaryPosition < 0) return;
            if (getSecondarySpinner().isShown() && _secondaryPosition < 0) return;
            if (_explanationEditText.getText().toString().trim().length() > 0) {
                _okButton.setEnabled(true);
            } else {
                _okButton.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher _timeframeEditText_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            _timeframeSpinner.setVisibility(View.VISIBLE);

            if (_timeframePosition == -1 || _timeframePosition == 3) {
                _okButton.setEnabled(false);
            } else {
                _okButton.setEnabled(true);
            }

            if (_timeframePosition == 3) {
                _timeframeLayout.setVisibility(View.VISIBLE);
                if (misc.isEmptyOrNull(_timeframeEditText.getText().toString())) {
                    _okButton.setEnabled(false);
                }
            } else {
                _timeframeLayout.setVisibility(View.GONE);
            }

            if (_primaryPosition < 0) return;
            if (getSecondarySpinner().isShown() && _secondaryPosition < 0) return;
            if (_explanationEditText.getText().toString().trim().length() > 0) {
                _okButton.setEnabled(true);
            } else {
                _okButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClient.subGet(_workOrderId, false);
        }

        @Override
        public void onGet(long workorderId, Workorder workorder, boolean failed, boolean isCached) {
            _workorder = workorder;
            populateUi();
        }
    };

    public static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context, Class<? extends Dialog> klass) {
            super(context, klass, null);
        }

        public static void show(Context context, long workOrderId) {
            Bundle params = new Bundle();
            params.putLong(PARAM_WORKORDER_ID, workOrderId);
            show(context, null, ReportProblemDialog.class, params);
        }

        public static void show(Context context, Workorder workorder) {
            Bundle params = new Bundle();
            params.putLong(PARAM_WORKORDER_ID, workorder.getWorkorderId());
            params.putParcelable(PARAM_WORKORDER, workorder);
            show(context, null, ReportProblemDialog.class, params);
        }
    }
}
