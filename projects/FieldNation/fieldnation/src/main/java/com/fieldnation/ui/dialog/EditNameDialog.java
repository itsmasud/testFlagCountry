package com.fieldnation.ui.dialog;

/**
 * Created by user on 6/26/2015.
 */
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fieldnation.R;
// ...

public class EditNameDialog extends DialogFragment {

    private EditText mEditText;

    public EditNameDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thank_you, container);
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        getDialog().setTitle("Hello");

        return view;
    }
}
