package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.fieldnation.ui.FnSpinner;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 11/25/2015.
 */
public class ReportProblemDialog extends DialogFragmentBase {
    private static final String TAG = "ReportProblemDialog";
    private static final int MILLISECOND_PER_DAY = 24 * 3600 * 1000;

    // Ui
    private FnSpinner _primarySpinner;
    private FnSpinner _secondarySpinner;
    private TextInputLayout _explanationLayout;
    private EditText _explanationEditText;
    private TextView _noteTextView;
    private Button _cancelButton;
    private Button _okButton;

    // Data
    private int _primaryPosition = -1;
    private int _secondaryPosition = -1;
    private Listener _listener;
    private Workorder _workorder;
    private boolean _clear = true;
    private ReportProblemType[] _primaryList = null;
    private ReportProblemType[] _secondaryList = null;
    private ArrayAdapter<ReportProblemType> _primaryAdapter = null;
    private ArrayAdapter<ReportProblemType> _secondaryAdapter = null;
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

        _primarySpinner = (FnSpinner) v.findViewById(R.id.problem1_spinner);
        _primaryAdapter = new ArrayAdapter<ReportProblemType>(App.get(), R.layout.view_spinner_item);
        _primaryAdapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _primarySpinner.setAdapter(_primaryAdapter);
        _primarySpinner.setOnItemClickListener(_problem1_onItemClick);


        _secondarySpinner = (FnSpinner) v.findViewById(R.id.problem2_spinner);
        _secondaryAdapter = new ArrayAdapter<ReportProblemType>(App.get(), R.layout.view_spinner_item);
        _secondaryAdapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _secondarySpinner.setAdapter(_secondaryAdapter);
        _secondarySpinner.setOnItemClickListener(_problem2_onItemClick);

        _explanationLayout = (TextInputLayout) v.findViewById(R.id.explanation_layout);
        _explanationEditText = (EditText) v.findViewById(R.id.explanation_edittext);

        _noteTextView = (TextView) v.findViewById(R.id.note_textview);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        populateUi();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        if (_clear) {
            _primarySpinner.setText(getResources().getString(R.string.dialog_report_problem_spinner_1));
            _explanationEditText.setText("");
            _clear = false;
        }
        populateUi();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.v(TAG, "dismiss");
        _clear = true;
        _primaryPosition = -1;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.v(TAG, "onDismiss");
        super.onDismiss(dialog);
        _clear = true;
        _primaryPosition = -1;
    }

    private void setProblem2Visibility(int visibility) {
        _secondarySpinner.setVisibility(visibility);
    }

    public void setListener(Listener listener, Workorder workorder) {
        _listener = listener;
        _workorder = workorder;
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
                _primaryAdapter.clear();
            } else if (pList != _primaryList) {
                _primaryList = pList;
                _primaryAdapter.clear();
                for (ReportProblemType t : _primaryList) {
                    _primaryAdapter.add(t);
                }
            }
        }

        setProblem2Visibility(View.GONE);
        _noteTextView.setVisibility(View.GONE);

        if (_primaryList == null) {
            return;
        }

        if (_primaryPosition >= 0) {
            ReportProblemType[] sList = ReportProblemListFactory.getSecondaryList(_workorder, _primaryList[_primaryPosition]);

            if (sList == null) {
                _secondaryList = null;
                _secondaryAdapter.clear();
            } else if (sList != _secondaryList) {
                _secondaryList = sList;
                _secondaryAdapter.clear();
                for (ReportProblemType t : _secondaryList) {
                    _secondaryAdapter.add(t);
                }
            }
        }

        if (_secondaryList != null) {
            _secondarySpinner.setVisibility(View.VISIBLE);
            _secondarySpinner.clearFocus();
            _secondarySpinner.dismissDropDown();
        }

        if (_selectedProblem == null) {
            return;
        }

        switch (_selectedProblem) {
            case CANNOT_MAKE_ASSIGNMENT:
                _noteTextView.setText(R.string.once_submitted_you_will_be_removed);
                _noteTextView.setVisibility(View.VISIBLE);
                _explanationEditText.requestFocus();
                _okButton.setEnabled(true);
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
                _explanationEditText.requestFocus();
                _okButton.setEnabled(true);
                break;
            case SITE_NOT_READY:
                _secondarySpinner.setHint(R.string.what_about_site);
                _secondarySpinner.setText("");
                _secondarySpinner.dismissDropDown();
                _okButton.setEnabled(false);
                break;
            case MISSING:
                _secondarySpinner.setHint(R.string.what_are_you_missing);
                _secondarySpinner.setText("");
                _okButton.setEnabled(false);
                break;
            case APPROVAL:
                break;
            default: {
                //_primarySpinner.clearFocus();
                _okButton.setEnabled(false);
                setProblem2Visibility(View.GONE);
                break;
            }
        }
    }

    private final AdapterView.OnItemClickListener _problem1_onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _primaryPosition = position;
            _selectedProblem = _primaryAdapter.getItem(position);
            populateUi();
        }
    };

    private final AdapterView.OnItemClickListener _problem2_onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _secondaryPosition = position;
            _explanationEditText.requestFocus();
            _okButton.setEnabled(true);
            _selectedProblem = _secondaryAdapter.getItem(position);
            populateUi();
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

    public interface Listener {
        void onReportAProblem(String explanation, ReportProblemType type);
    }
}
