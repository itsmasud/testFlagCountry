package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
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
import android.widget.ListView;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Michael on 2/6/2015.
 */
public class OneButtonScrollingDialog extends DialogFragmentBase {
    private static final String TAG = UniqueTag.makeTag("OneButtonScrollingDialog");

    private static final int TEXT_MAX_SIZE = 800;

    //Ui
    private TextView _titleTextView;
    private ListView _bodyListView;
    private Button _button;

    // Data
    private String _title;
    private String _body;
    private List<String> _sections = null;
    private String _buttonText;
    private Listener _listener;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static OneButtonScrollingDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, OneButtonScrollingDialog.class);
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
        View v = inflater.inflate(R.layout.dialog_one_button_scrolling, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _button = (Button) v.findViewById(R.id.button);
        _button.setOnClickListener(_button_onClick);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _bodyListView = (ListView) v.findViewById(R.id.body_listview);
        _bodyListView.setDivider(null);
        _bodyListView.setDividerHeight(0);
        _bodyListView.setAdapter(_adapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("title"))
                _title = savedInstanceState.getString("title");

            if (savedInstanceState.containsKey("body")) {
                _body = savedInstanceState.getString("body");
                _sections = splitBody(_body);
            }

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
            window.setLayout((display.getWidth() * 9) / 10, (display.getHeight() * 9) / 10);
        } else {
            window.setLayout((display.getWidth() * 9) / 10, (display.getHeight() * 9) / 10);
        }
        populateUi();
    }

    private void populateUi() {
        _titleTextView.setText(_title);
        _button.setText(_buttonText);
    }

    public void setData(String title, String body, String buttonText, Listener listener) {
        Log.v(TAG, "setData");
        _title = title;
        _buttonText = buttonText;
        _listener = listener;
        _body = body;

        _sections = splitBody(_body);

        _adapter.notifyDataSetChanged();
        if (_titleTextView != null)
            reset();
    }

    public List<String> splitBody(String body) {
        List<String> list = new LinkedList<>();
        StringTokenizer tok = new StringTokenizer(body, "\n");
        StringBuilder line = new StringBuilder(TEXT_MAX_SIZE);

        int runningLength = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            line.append(word);
            line.append(" ");
            runningLength += word.length();

            if (runningLength >= TEXT_MAX_SIZE) {
                list.add(line.toString().trim());
                line = new StringBuilder(TEXT_MAX_SIZE);
                runningLength = 0;
            }
        }
        if (runningLength > 0) {
            list.add(line.toString().trim());
        }

        return list;
    }

    private final BaseAdapter _adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if (_sections == null)
                return 0;

            Log.v(TAG, _sections.size() + "");

            return _sections.size();
        }

        @Override
        public Object getItem(int position) {
            Log.v(TAG, _sections.get(position));
            return _sections.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = null;

            if (convertView == null) {
                v = new TextView(parent.getContext(), null, R.attr.dialogBodyNoPaddingStyle);
            } else if (convertView instanceof TextView) {
                v = (TextView) convertView;
            } else {
                v = new TextView(parent.getContext(), null, R.attr.dialogBodyNoPaddingStyle);
            }

            try {
                v.setText(misc.linkifyHtml(((String) getItem(position)).trim(), Linkify.ALL));
                v.setMovementMethod(LinkMovementMethod.getInstance());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return v;
        }
    };

    private final View.OnClickListener _button_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onButtonClick();
            }
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
