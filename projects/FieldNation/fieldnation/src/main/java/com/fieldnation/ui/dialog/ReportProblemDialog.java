package com.fieldnation.ui.dialog;

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
import com.fieldnation.service.data.workorder.ReportProblemType;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.misc;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by Michael Carver on 11/25/2015.
 */
public class ReportProblemDialog extends DialogFragmentBase {
    private static final String TAG = "ReportProblemDialog";

    // Ui
    private MaterialBetterSpinner _problem1Spinner;
    private MaterialBetterSpinner _problem2Spinner;
    private TextInputLayout _explanationLayout;
    private EditText _explanationEditText;
    private TextView _noteTextView;
    private Button _cancelButton;
    private Button _okButton;

    // Data
    private int _spinner1Position = -1;
    private int _spinner2Position = -1;
    private Listener _listener;

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

        _problem1Spinner = (MaterialBetterSpinner) v.findViewById(R.id.problem1_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.enum_report_problem_1,
                R.layout.view_spinner_item);

        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);

        _problem1Spinner.setAdapter(adapter);
        _problem1Spinner.setOnItemClickListener(_problem1_onItemClick);

        _problem2Spinner = (MaterialBetterSpinner) v.findViewById(R.id.problem2_spinner);
        _problem2Spinner.setOnItemClickListener(_problem2_onItemClick);

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

    private void setProblem2Visibility(int visibility) {
        _problem2Spinner.setVisibility(visibility);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_noteTextView == null)
            return;

        setProblem2Visibility(View.GONE);
        _noteTextView.setVisibility(View.GONE);
        switch (_spinner1Position) {
            case 0: { // I can\'t make my assignment
                _noteTextView.setText(R.string.once_submitted_you_will_be_removed);
                _noteTextView.setVisibility(View.VISIBLE);
                _explanationEditText.requestFocus();
                _okButton.setEnabled(true);
                break;
            }
            case 1: { // I\'m going to be late
                _explanationEditText.requestFocus();
                _okButton.setEnabled(true);
                break;
            }
            case 2: { // I don\'t have what I need
                ArrayAdapter adap = ArrayAdapter.createFromResource(getActivity(),
                        R.array.enum_report_problem_missing,
                        R.layout.view_spinner_item);

                adap.setDropDownViewResource(
                        android.support.design.R.layout.support_simple_spinner_dropdown_item);

                _problem2Spinner.setAdapter(adap);
                setProblem2Visibility(View.VISIBLE);
                _problem2Spinner.requestFocus();
                _problem2Spinner.setHint(R.string.what_are_you_missing);
                _problem2Spinner.setText("");
                _problem2Spinner.dismissDropDown();
                _okButton.setEnabled(false);
                break;
            }
            case 3: { // Approval
                ArrayAdapter adap = ArrayAdapter.createFromResource(getActivity(),
                        R.array.enum_report_problem_approval,
                        R.layout.view_spinner_item);

                adap.setDropDownViewResource(
                        android.support.design.R.layout.support_simple_spinner_dropdown_item);

                _problem2Spinner.setAdapter(adap);
                setProblem2Visibility(View.VISIBLE);
                _problem2Spinner.requestFocus();
                _problem2Spinner.setHint(R.string.what_about_approval);
                _problem2Spinner.setText("");
                _problem2Spinner.dismissDropDown();
                _okButton.setEnabled(false);
                break;
            }
            case 4: { // Payment
                ArrayAdapter adap = ArrayAdapter.createFromResource(getActivity(),
                        R.array.enum_report_problem_payment,
                        R.layout.view_spinner_item);

                adap.setDropDownViewResource(
                        android.support.design.R.layout.support_simple_spinner_dropdown_item);

                _problem2Spinner.setAdapter(adap);
                setProblem2Visibility(View.VISIBLE);
                _problem2Spinner.requestFocus();
                _problem2Spinner.setHint(R.string.what_about_payment);
                _problem2Spinner.setText("");
                _problem2Spinner.dismissDropDown();
                _okButton.setEnabled(false);
                break;
            }
            case 5: { // Buyer unresponsive
                _explanationEditText.requestFocus();
                _okButton.setEnabled(true);
                break;
            }
            case 6: { // Scope of work
                _explanationEditText.requestFocus();
                _okButton.setEnabled(true);
                break;
            }
            case 7: { // Site is not ready
                ArrayAdapter adap = ArrayAdapter.createFromResource(getActivity(),
                        R.array.enum_report_problem_site_not_ready, R.layout.view_spinner_item);

                adap.setDropDownViewResource(
                        android.support.design.R.layout.support_simple_spinner_dropdown_item);

                _problem2Spinner.setAdapter(adap);
                setProblem2Visibility(View.VISIBLE);
                _problem2Spinner.requestFocus();
                _problem2Spinner.setHint(R.string.what_about_site);
                _problem2Spinner.setText("");
                _problem2Spinner.dismissDropDown();
                _okButton.setEnabled(false);
                break;
            }
            case 8: { // Other
                _explanationEditText.requestFocus();
                _okButton.setEnabled(true);
                break;
            }
            default: {
                _problem1Spinner.clearFocus();
                _okButton.setEnabled(false);
                setProblem2Visibility(View.GONE);
                break;
            }
        }
    }

    private final AdapterView.OnItemClickListener _problem1_onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _spinner1Position = position;
            populateUi();
        }
    };

    private final AdapterView.OnItemClickListener _problem2_onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _spinner2Position = position;
            _explanationEditText.requestFocus();
            _okButton.setEnabled(true);
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "problem1=" + _spinner1Position + ", problem2=" + _spinner2Position);

            String explanation = null;

            if (!misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
                explanation = _explanationEditText.getText().toString();
            }

            if (_listener == null) {
                return;
            }

            switch (_spinner1Position) {
                case 0: { // I can\'t make my assignment
                    ToastClient.toast(App.get(), R.string.sorry_to_hear_that_you_have_been_removed, Toast.LENGTH_LONG);
                    _listener.onReportAProblem(explanation, ReportProblemType.CANNOT_MAKE_ASSIGNMENT);
                    break;
                }
                case 1: { // I\'m going to be late
                    ToastClient.toast(App.get(), R.string.thanks_for_the_heads_up, Toast.LENGTH_LONG);
                    _listener.onReportAProblem(explanation, ReportProblemType.WILL_BE_LATE);
                    break;
                }
                case 2: { // I don\'t have what I need
                    ToastClient.toast(App.get(), R.string.buyer_has_been_notified, Toast.LENGTH_LONG);
                    switch (_spinner2Position) {
                        case 0:
                            _listener.onReportAProblem(explanation, ReportProblemType.DO_NOT_HAVE_SHIPMENT);
                            break;
                        case 1:
                            _listener.onReportAProblem(explanation, ReportProblemType.DO_NOT_HAVE_INFO);
                            break;
                        case 2:
                            _listener.onReportAProblem(explanation, ReportProblemType.DO_NOT_HAVE_RESPONSE);
                            break;
                        case 3:
                            _listener.onReportAProblem(explanation, ReportProblemType.DO_NOT_HAVE_OTHER);
                            break;
                    }
                    break;
                }
                case 3: { // Approval
                    ToastClient.toast(App.get(), R.string.buyer_has_been_notified, Toast.LENGTH_LONG);
                    switch (_spinner2Position) {
                        case 0:
                            _listener.onReportAProblem(explanation, ReportProblemType.APPROVAL_NOT_YET);
                            break;
                        case 1:
                            _listener.onReportAProblem(explanation, ReportProblemType.APPROVAL_DISAGREEMENT);
                            break;
                    }
                    break;
                }
                case 4: { // Payment
                    switch (_spinner2Position) {
                        case 0:
                            ToastClient.toast(App.get(), R.string.support_has_been_notified, Toast.LENGTH_LONG);
                            _listener.onReportAProblem(explanation, ReportProblemType.PAYMENT_NOT_RECEIVED);
                            break;
                        case 1:
                            ToastClient.toast(App.get(), R.string.buyer_has_been_notified, Toast.LENGTH_LONG);
                            _listener.onReportAProblem(explanation, ReportProblemType.PAYMENT_NOT_ACCURATE);
                            break;
                    }
                    break;
                }
                case 5: { // Buyer unresponsive
                    ToastClient.toast(App.get(), R.string.buyer_and_support_have_been_notified, Toast.LENGTH_LONG);
                    _listener.onReportAProblem(explanation, ReportProblemType.BUYER_UNRESPONSIVE);
                    break;
                }
                case 6: { // Scope of work
                    ToastClient.toast(App.get(), R.string.buyer_has_been_notified, Toast.LENGTH_LONG);
                    _listener.onReportAProblem(explanation, ReportProblemType.SCOPE_OF_WORK);
                    break;
                }
                case 7: { // Site is not ready
                    ToastClient.toast(App.get(), R.string.buyer_has_been_notified, Toast.LENGTH_LONG);
                    switch (_spinner2Position) {
                        case 0:
                            _listener.onReportAProblem(explanation, ReportProblemType.SITE_NOT_READY_CONTACT);
                            break;
                        case 1:
                            _listener.onReportAProblem(explanation, ReportProblemType.SITE_NOT_READY_PRIOR_WORK);
                            break;
                        case 2:
                            _listener.onReportAProblem(explanation, ReportProblemType.SITE_NOT_READY_ACCESS);
                            break;
                        case 3:
                            _listener.onReportAProblem(explanation, ReportProblemType.SITE_NOT_READY_OTHER);
                            break;
                    }
                    break;
                }
                case 8: { // Other
                    ToastClient.toast(App.get(), R.string.buyer_has_been_notified, Toast.LENGTH_LONG);
                    _listener.onReportAProblem(explanation, ReportProblemType.OTHER);
                    break;
                }
                default: {
                    break;
                }
            }
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
