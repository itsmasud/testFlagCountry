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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fieldnation.R;
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
                R.array.enum_report_problem_1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        _problem1Spinner.setAdapter(adapter);
        _problem1Spinner.setOnItemSelectedListener(_problem1_onItemClick);


        _problem2Spinner = (MaterialBetterSpinner) v.findViewById(R.id.problem2_spinner);
        _problem2Spinner.setOnItemSelectedListener(_problem2_onItemClick);

        _explanationLayout = (TextInputLayout) v.findViewById(R.id.explanation_layout);
        _explanationEditText = (EditText) v.findViewById(R.id.explanation_edittext);

        _noteTextView = (TextView) v.findViewById(R.id.note_textview);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        return v;
    }

    private void setProblem2Visibility(int visibility) {
        _problem2Spinner.setVisibility(visibility);
    }

    private final AdapterView.OnItemSelectedListener _problem1_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 2) {
                ArrayAdapter adap = ArrayAdapter.createFromResource(getActivity(), R.array.enum_report_problem_missing, android.R.layout.simple_spinner_item);
                adap.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                _problem2Spinner.setAdapter(adap);
                setProblem2Visibility(View.VISIBLE);
            } else if (position == 3) {
                ArrayAdapter adap = ArrayAdapter.createFromResource(getActivity(), R.array.enum_report_problem_approval, android.R.layout.simple_spinner_item);
                adap.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                _problem2Spinner.setAdapter(adap);
                setProblem2Visibility(View.VISIBLE);
            } else if (position == 4) {
                ArrayAdapter adap = ArrayAdapter.createFromResource(getActivity(), R.array.enum_report_problem_payment, android.R.layout.simple_spinner_item);
                adap.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                _problem2Spinner.setAdapter(adap);
                setProblem2Visibility(View.VISIBLE);
            } else if (position == 7) {
                ArrayAdapter adap = ArrayAdapter.createFromResource(getActivity(), R.array.enum_report_problem_site_not_ready, android.R.layout.simple_spinner_item);
                adap.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                _problem2Spinner.setAdapter(adap);
                setProblem2Visibility(View.VISIBLE);
            } else {
                setProblem2Visibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener _problem2_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
