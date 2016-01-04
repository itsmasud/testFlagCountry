package com.fieldnation.ui.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.fieldnation.Log;
import com.fieldnation.R;

/**
 * Created by Michael Carver on 6/12/2015.
 */
public class HelpDialog extends DialogFragmentBase {
    private static final String TAG = "HelpDialog";

    // Ui
    private Button _callButton;
    private Button _emailButton;
    private Button _faqButton;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static HelpDialog getInstance(FragmentManager fm, String tag) {
        Log.v(TAG, "getInstance");
        return getInstance(fm, tag, HelpDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_help, container, false);

        _callButton = (Button) v.findViewById(R.id.call_button);
        _callButton.setOnClickListener(_call_onClick);
        _emailButton = (Button) v.findViewById(R.id.email_button);
        _emailButton.setOnClickListener(_email_onClick);
        _faqButton = (Button) v.findViewById(R.id.faq_button);
        _faqButton.setOnClickListener(_faq_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    private final View.OnClickListener _call_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:18775734353"));
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "No phone app is available", Toast.LENGTH_LONG).show();
            }
        }
    };

    private final View.OnClickListener _email_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@fieldnation.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Help support");
            intent.putExtra(Intent.EXTRA_TEXT, "Tell us how we can help you");
            startActivity(intent);
        }
    };

    private final View.OnClickListener _faq_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
