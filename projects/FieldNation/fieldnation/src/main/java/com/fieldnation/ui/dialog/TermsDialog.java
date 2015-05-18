package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.utils.misc;

import static android.view.View.OnClickListener;

/**
 * Created by michael.carver on 11/18/2014.
 */
public class TermsDialog extends DialogFragmentBase {
    private static final String TAG = "TermsDialog";

    private static final String DEFAULT_TITLE = "Terms and Conditions";
    private static final String DEFAULT_BODY = "<b>Service Fees</b><br/>A 10% service fee will be deducted from your final total payment amount from this work order.<br/><br/><b>Terms</b><br/>All the work orders that have been approved this week will be processed next week. You can view your payment status from the My Payments option from your account.";

    //Ui
    private Button _okButton;
    private TextView _titleTextView;
    private TextView _bodyTextView;

    // Data
    private String _title = DEFAULT_TITLE;
    private String _body = DEFAULT_BODY;

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

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        _titleTextView.setText(_title);
        _bodyTextView.setText(misc.linkifyHtml(_body, Linkify.ALL));
        _bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void show() {
        _body = DEFAULT_BODY;
        _title = DEFAULT_TITLE;
        super.show();
    }

    public void show(String title, String body) {
        _body = body;
        _title = title;
        super.show();
    }

    private final OnClickListener _ok_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

}

