package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.fieldnation.R;

import static android.view.View.OnClickListener;

/**
 * Created by michael.carver on 11/18/2014.
 */
public class TermsDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.TermsDialog";

    //Ui
    private Button _okButton;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static TermsDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, TermsDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_terms, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        return v;
    }

    private OnClickListener _ok_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

}

