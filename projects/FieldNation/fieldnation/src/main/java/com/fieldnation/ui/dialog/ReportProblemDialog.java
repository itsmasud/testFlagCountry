package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.ReportProblemListFactory;
import com.fieldnation.service.data.workorder.ReportProblemType;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 11/25/2015.
 */
public class ReportProblemDialog extends DialogFragmentBase {
    private static final String TAG = "ReportProblemDialog";

    // State keys
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_PRIMARY_POS = "STATE_PRIMARY_POS";
    private static final String STATE_SECONDARY_POS = "STATE_SECONDARY_POS";
    private static final String STATE_SELECTED_PROBLEM = "STATE_SELECTED_PROBLEM";
    private static final String STATE_OK_ENABLED = "STATE_OK_ENABLED";

    // Ui
    private HintSpinner _primarySpinner;
    private HintSpinner _secondarySpinner;
    private TextInputLayout _explanationLayout;
    private EditText _explanationEditText;
    private TextView _noteTextView;
    private Button _cancelButton;
    private Button _okButton;

    // Data
    private Listener _listener;
    private ReportProblemType[] _primaryList = null;
    private ReportProblemType[] _secondaryList = null;
//    private HintArrayAdapter _primaryAdapter = null;
//    private HintArrayAdapter _secondaryAdapter = null;

    // Saved Data
    private Workorder _workorder;
    private int _primaryPosition = -1;
    private int _secondaryPosition = -1;
    private ReportProblemType _selectedProblem = null;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static ReportProblemDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ReportProblemDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_report_problem, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _primarySpinner = (HintSpinner) v.findViewById(R.id.primary_spinner);
        _primarySpinner.setOnItemSelectedListener(_problem1_onItemClick);

        _secondarySpinner = (HintSpinner) v.findViewById(R.id.secondary_spinner);
        _secondarySpinner.setOnItemSelectedListener(_problem2_onItemClick);

        _explanationLayout = (TextInputLayout) v.findViewById(R.id.explanation_layout);
        _explanationEditText = (EditText) v.findViewById(R.id.explanation_edittext);
        _explanationEditText.addTextChangedListener(_textEditText_watcherListener);

        _noteTextView = (TextView) v.findViewById(R.id.note_textview);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        populateUi();

        return v;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState == null)
            return;

        if (savedInstanceState.containsKey(STATE_PRIMARY_POS))
            _primaryPosition = savedInstanceState.getInt(STATE_PRIMARY_POS);

        if (savedInstanceState.containsKey(STATE_SECONDARY_POS))
            _secondaryPosition = savedInstanceState.getInt(STATE_SECONDARY_POS);

        if (savedInstanceState.containsKey(STATE_WORKORDER))
            _workorder = (Workorder) savedInstanceState.getParcelable(STATE_WORKORDER);

        if (savedInstanceState.containsKey(STATE_SELECTED_PROBLEM))
            _selectedProblem = ReportProblemType.values()[savedInstanceState.getInt(STATE_SELECTED_PROBLEM)];

        if (savedInstanceState.containsKey(STATE_OK_ENABLED))
            _okButton.setEnabled(savedInstanceState.getBoolean(STATE_OK_ENABLED));

        if (_workorder != null) {
            _primaryList = ReportProblemListFactory.getPrimaryList(_workorder);
            if (_primaryList != null) {
                getPrimaryAdapter().clear();
                getPrimaryAdapter().addAll(_primaryList);

                if (_primaryPosition != -1) {
                    getPrimarySpinner().setSelection(_primaryPosition);
                    _secondaryList = ReportProblemListFactory.getSecondaryList(_workorder, _primaryList[_primaryPosition]);
                    if (_secondaryList != null) {
                        getSecondaryAdapter().clear();
                        getSecondaryAdapter().addAll(_secondaryList);
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

    private HintSpinner getPrimarySpinner() {
        if (_primarySpinner != null && _primarySpinner.getAdapter() == null) {
            HintArrayAdapter adapter = new HintArrayAdapter(getActivity(), R.layout.view_spinner_item);

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
            HintArrayAdapter adapter = new HintArrayAdapter(getActivity(), R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _secondarySpinner.setAdapter(adapter);
        }
        return _secondarySpinner;
    }

    private HintArrayAdapter getSecondaryAdapter() {
        return (HintArrayAdapter) getSecondarySpinner().getAdapter();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_PRIMARY_POS, _primaryPosition);
        outState.putInt(STATE_SECONDARY_POS, _secondaryPosition);
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);
        if (_selectedProblem != null)
            outState.putInt(STATE_SELECTED_PROBLEM, _selectedProblem.ordinal());
        if (_okButton != null)
            outState.putBoolean(STATE_OK_ENABLED, _okButton.isEnabled());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        _primaryPosition = -1;
        _secondaryPosition = -1;
        _selectedProblem = null;
        _explanationEditText.setText(null);
        _secondaryList = null;
        _primaryList = null;
        getPrimarySpinner().setHint(R.string.dialog_report_problem_spinner_1);
        getPrimarySpinner().clearSelection();
        getSecondarySpinner().setHint(R.string.dialog_report_problem_spinner_1);
        getSecondarySpinner().clearSelection();
        super.onDismiss(dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        populateUi();
        _textEditText_watcherListener.onTextChanged(
                _explanationEditText.getText().toString(),
                0, _explanationEditText.getText().toString().length(),
                _explanationEditText.getText().toString().length());
    }

    public void show(Workorder workorder) {
        _workorder = workorder;
        super.show();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_noteTextView == null)
            return;

        if (_workorder == null)
            return;

        { // put in a block to limit the scope of pList
            ReportProblemType[] pList = ReportProblemListFactory.getPrimaryList(_workorder);

            if (pList == null) {
                _primaryList = null;
                getPrimaryAdapter().clear();
                getPrimarySpinner().clearSelection();
            } else if (pList != _primaryList) {
                _primaryList = pList;
                getPrimaryAdapter().clear();
                getPrimaryAdapter().addAll(_primaryList);
                getPrimarySpinner().clearSelection();
            }
        }

        _noteTextView.setVisibility(View.GONE);

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
                getSecondaryAdapter().addAll(_secondaryList);
                getSecondarySpinner().clearSelection();
            }
        }

        if (_secondaryList != null) {
            getSecondarySpinner().setVisibility(View.VISIBLE);
            getSecondarySpinner().clearFocus();
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
            _explanationEditText.requestFocus();
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

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "problem1=" + _primaryPosition + ", problem2=" + _secondaryPosition);

            String explanation = null;

            if (!misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
                explanation = _explanationEditText.getText().toString();
            }

            if (_listener == null) {
                return;
            }
            switch (_selectedProblem) {
                case CANNOT_MAKE_ASSIGNMENT:
                    ToastClient.toast(App.get(), R.string.sorry_to_hear_that_you_have_been_removed, Toast.LENGTH_LONG);
                    break;

                case WILL_BE_LATE:
                    ToastClient.toast(App.get(), R.string.thanks_for_the_heads_up, Toast.LENGTH_LONG);
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
                    break;

                case BUYER_UNRESPONSIVE:
                    ToastClient.toast(App.get(), R.string.buyer_and_support_have_been_notified, Toast.LENGTH_LONG);
                    break;

                case PAYMENT_NOT_RECEIVED:
                    ToastClient.toast(App.get(), R.string.support_has_been_notified, Toast.LENGTH_LONG);
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
            _listener.onReportAProblem(explanation, _selectedProblem);
            dismiss();
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
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

    public interface Listener {
        void onReportAProblem(String explanation, ReportProblemType type);
    }
}
