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

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;

/**
 * Created by Michael on 2/6/2015.
 */
public class OneButtonDialog extends DialogFragmentBase {
    private static final String TAG = UniqueTag.makeTag("OneButtonDialog");

    //Ui
    private TextView _titleTextView;
    private TextView _bodyTextView;
    private Button _button;

    // Data
    private String _title;
    private String _body;
    private String _buttonText;
    private Listener _listener;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static OneButtonDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, OneButtonDialog.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("title", _title);
        outState.putString("body", _body);
        outState.putString("buttonText", _buttonText);
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
        View v = inflater.inflate(R.layout.dialog_one_button, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _button = (Button) v.findViewById(R.id.button);
        _button.setOnClickListener(_button_onClick);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("title"))
                _title = savedInstanceState.getString("title");

            if (savedInstanceState.containsKey("body"))
                _body = savedInstanceState.getString("body");

            if (savedInstanceState.containsKey("buttonText"))
                _buttonText = savedInstanceState.getString("buttonText");
        }
//        reset();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        _titleTextView.setText(_title);
        // _bodyTextView.setText(_body);
        if (_body != null) {
            _bodyTextView.setText(misc.linkifyHtml(_body, Linkify.ALL));
            _bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        _button.setText(_buttonText);
    }

    public void setData(String title, String body, String buttonText, Listener listener) {
        Log.v(TAG, "setData");
        _body = body;
        _title = title;
        _buttonText = buttonText;
        _listener = listener;

        if (_titleTextView != null)
            reset();
    }

    private final View.OnClickListener _button_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onButtonClick();
        }
    };

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (_listener != null)
            _listener.onCancel();
    }

    public interface Listener {
        void onButtonClick();

        void onCancel();
    }
}
