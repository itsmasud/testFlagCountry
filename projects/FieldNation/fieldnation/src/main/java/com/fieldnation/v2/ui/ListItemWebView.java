package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 11/10/17.
 */

public class ListItemWebView extends RelativeLayout {
    private static final String TAG = "ListItemWebView";

    // Ui
    private TextView _titleTextView;
    private WebView _webView;
    private IconFontTextView _button;

    // Data
    private String _title;
    private String _data;

    // Animations
    private Animation _ccw;
    private Animation _cw;

    public ListItemWebView(Context context) {
        super(context);
        init();
    }

    public ListItemWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_webview_item, this, true);

        if (isInEditMode()) return;

        _titleTextView = findViewById(R.id.title_textview);
        _webView = findViewById(R.id.data_webview);
        _button = findViewById(R.id.expand_button);
        _button.setOnClickListener(_readMore_onClick);

        _ccw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_ccw);
        _cw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_cw);

        populateUi();
    }

    public void setData(String title, String data) {
        _title = title;
        _data = data;

        populateUi();
    }

    private void populateUi() {
        _titleTextView.setText(_title);

        int fontSize = getResources().getInteger(R.integer.textSizeWorkorderDescription);
        WebSettings _webSettings = _webView.getSettings();
        _webSettings.setDefaultFontSize(fontSize);
        _webView.loadData(_data, "text/html", "utf-8");
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final OnClickListener _readMore_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_webView.getVisibility() == VISIBLE) {
                _webView.setVisibility(GONE);
                _titleTextView.setVisibility(VISIBLE);
                _button.startAnimation(_ccw);
            } else {
                _webView.setVisibility(VISIBLE);
                _titleTextView.setVisibility(GONE);
                _button.startAnimation(_cw);
            }
        }
    };
}
