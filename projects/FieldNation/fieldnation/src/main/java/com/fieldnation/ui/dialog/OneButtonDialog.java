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

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 2/6/2015.
 */
public class OneButtonDialog extends DialogFragmentBase {
    private static final String TAG = UniqueTag.makeTag("OneButtonDialog");

    private static final int TEXT_MAX_SIZE = 500;

    //Ui
    private TextView _titleTextView;

    private TextView _bodyTextView;
    private Button _button;

    private Button _prevButton;
    private Button _nextButton;

    // Data
    private String _title;
    private String _body;
    private int _bodyPosition = 0;
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

        _prevButton = (Button) v.findViewById(R.id.prev_button);
        _prevButton.setOnClickListener(_prev_onClick);
        _nextButton = (Button) v.findViewById(R.id.next_button);
        _nextButton.setOnClickListener(_next_onClick);

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

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    private void populateUi() {
        _titleTextView.setText(_title);

        if (_body.length() < TEXT_MAX_SIZE) {
            _prevButton.setVisibility(View.GONE);
            _nextButton.setVisibility(View.GONE);
            _bodyTextView.setText(misc.linkifyHtml(_body.trim(), Linkify.ALL));
            _bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else if ((_bodyPosition + 1) * TEXT_MAX_SIZE > _body.length()) {
            _prevButton.setVisibility(View.VISIBLE);
            _nextButton.setVisibility(View.VISIBLE);
            _bodyTextView.setText(misc.linkifyHtml(_body.substring(_bodyPosition * TEXT_MAX_SIZE).trim(), Linkify.ALL));
            _bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            _prevButton.setVisibility(View.VISIBLE);
            _nextButton.setVisibility(View.VISIBLE);
            _bodyTextView.setText(misc.linkifyHtml(_body.substring(_bodyPosition * TEXT_MAX_SIZE, (_bodyPosition + 1) * TEXT_MAX_SIZE).trim(), Linkify.ALL));
            _bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        // _bodyTextView.setText(_body);
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

    private final View.OnClickListener _prev_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_bodyPosition > 0) {
                _bodyPosition--;
            }
            populateUi();
        }
    };

    private final View.OnClickListener _next_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((_bodyPosition + 1) * TEXT_MAX_SIZE < _body.length()) {
                _bodyPosition++;
            }
            populateUi();
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
