package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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
    private ListView _bodyListView;
    // private TextView _bodyTextView;
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

        new TextView(v.getContext(), null, R.attr.buttonFlatStyle);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyListView = (ListView) v.findViewById(R.id.body_listview);
        _bodyListView.setAdapter(_adapter);
        //_bodyTextView = (TextView) v.findViewById(R.id.body_textview);

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
        Dialog d = getDialog();
        if (d == null)
            return;

        Window window = d.getWindow();

        Display display = window.getWindowManager().getDefaultDisplay();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.setLayout((display.getWidth() * 9) / 10, (display.getHeight() * 7) / 10);
        } else {
            window.setLayout((display.getWidth() * 9) / 10, (display.getHeight() * 9) / 10);
        }
        populateUi();
    }

    private void populateUi() {
        _titleTextView.setText(_title);

//        if (_body.length() < TEXT_MAX_SIZE) {
//            //_prevButton.setVisibility(View.GONE);
//            //_nextButton.setVisibility(View.GONE);
//            //_bodyTextView.setText(misc.linkifyHtml(_body.trim(), Linkify.ALL));
//            //_bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        } else if ((_bodyPosition + 1) * TEXT_MAX_SIZE > _body.length()) {
//            //_prevButton.setVisibility(View.VISIBLE);
//            //_nextButton.setVisibility(View.VISIBLE);
//            //_bodyTextView.setText(misc.linkifyHtml(_body.substring(_bodyPosition * TEXT_MAX_SIZE).trim(), Linkify.ALL));
//            //_bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        } else {
//            //_prevButton.setVisibility(View.VISIBLE);
//            //_nextButton.setVisibility(View.VISIBLE);
//            //_bodyTextView.setText(misc.linkifyHtml(_body.substring(_bodyPosition * TEXT_MAX_SIZE, (_bodyPosition + 1) * TEXT_MAX_SIZE).trim(), Linkify.ALL));
//            //_bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        }
        // _bodyTextView.setText(_body);
        _button.setText(_buttonText);
    }

    public void setData(String title, String body, String buttonText, Listener listener) {
        Log.v(TAG, "setData");
        _body = body;
        _title = title;
        _buttonText = buttonText;
        _listener = listener;

        _adapter.notifyDataSetChanged();

        if (_titleTextView != null)
            reset();
    }

    private BaseAdapter _adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if (_body == null)
                return 0;

            int count = _body.length() / TEXT_MAX_SIZE;

            if (count * TEXT_MAX_SIZE < _body.length())
                count++;

            return count;
        }

        @Override
        public Object getItem(int position) {
            if (position + 1 == getCount()) {
                return _body.substring(position * TEXT_MAX_SIZE);
            }
            return _body.substring(position * TEXT_MAX_SIZE, (position + 1) * TEXT_MAX_SIZE);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = null;

            if (convertView == null) {
                v = new TextView(parent.getContext(), null, R.attr.dialogBodyStyle);
            } else if (convertView instanceof TextView) {
                v = (TextView) convertView;
            } else {
                v = new TextView(parent.getContext(), null, R.attr.dialogBodyStyle);
            }

            v.setText(misc.linkifyHtml(((String) getItem(position)).trim(), Linkify.ALL));
            v.setMovementMethod(LinkMovementMethod.getInstance());
            return v;
        }
    };

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
