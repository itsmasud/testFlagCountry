package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
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

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.utils.misc;

/**
 * Created by Michael on 2/6/2015.
 */
public class TwoButtonDialog extends DialogFragmentBase {
    private static final String TAG = UniqueTag.makeTag("OneButtonDialog");

    //Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private Button _positiveButton;
    private Button _negativeButton;

    // Data
    private String _title;
    private String _body;
    private String _positiveText;
    private String _negativeText;
    private Listener _listener;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static TwoButtonDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, TwoButtonDialog.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("title", _title);
        outState.putString("body", _body);
        outState.putString("positiveText", _positiveText);
        outState.putString("negativeText", _negativeText);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_two_button, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _positiveButton = (Button) v.findViewById(R.id.positive_button);
        _positiveButton.setOnClickListener(_positive_onClick);

        _negativeButton = (Button) v.findViewById(R.id.negative_button);
        _negativeButton.setOnClickListener(_negative_onClick);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("title"))
                _title = savedInstanceState.getString("title");

            if (savedInstanceState.containsKey("body"))
                _body = savedInstanceState.getString("body");

            if (savedInstanceState.containsKey("positiveText"))
                _positiveText = savedInstanceState.getString("positiveText");

            if (savedInstanceState.containsKey("negativeText"))
                _negativeText = savedInstanceState.getString("negativeText");

            reset();
        }

        return v;
    }

    @Override
    public void onStop() {
        dismiss();
        super.onStop();
    }

    @Override
    public void reset() {
        Log.v(TAG, "reset");
        super.reset();

        _titleTextView.setText(_title);
        if (_body != null) {
            _bodyTextView.setText(misc.linkifyHtml(_body, Linkify.ALL));
            _bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        _positiveButton.setText(_positiveText);
        if (_negativeText == null) {
            _negativeButton.setVisibility(View.GONE);
        } else {
            _negativeButton.setVisibility(View.VISIBLE);
            _negativeButton.setText(_negativeText);
        }

    }

    public void setData(String title, String body, String positiveText, String negativeText, Listener listener) {
        Log.v(TAG, "setData");
        _body = body;
        _title = title;
        _positiveText = positiveText;
        _negativeText = negativeText;
        _listener = listener;

        if (_titleTextView != null)
            reset();
    }

    public void setData(String title, String body, String positiveText, Listener listener) {
        Log.v(TAG, "setData");
        _body = body;
        _title = title;
        _positiveText = positiveText;
        _negativeText = null;
        _listener = listener;

        if (_titleTextView != null)
            reset();
    }

    private final View.OnClickListener _positive_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onPositive();
        }
    };

    private final View.OnClickListener _negative_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onNegative();
        }
    };

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (_listener != null)
            _listener.onCancel();
    }

    public interface Listener {
        void onPositive();

        void onNegative();

        void onCancel();
    }
}
