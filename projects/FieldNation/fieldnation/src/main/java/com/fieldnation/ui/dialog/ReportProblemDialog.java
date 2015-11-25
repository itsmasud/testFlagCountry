package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 11/25/2015.
 */
public class ReportProblemDialog extends DialogFragmentBase {
    private static final String TAG = "ReportProblemDialog";

    private Spinner _directionSpinner;
    private ArrayAdapter<CharSequence> _directionAdapter;

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

        _directionSpinner = (Spinner) v.findViewById(R.id.carrier_spinner);

//        _directionAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.direction_list,
//                R.layout.view_spinner_item);
        _directionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.direction_list,
                android.R.layout.simple_spinner_item);

        _directionAdapter.setDropDownViewResource(
//                android.support.design.R.layout.support_simple_spinner_dropdown_item);
                android.R.layout.simple_spinner_dropdown_item);
        _directionSpinner.setAdapter(_directionAdapter);

        return v;
    }
}
