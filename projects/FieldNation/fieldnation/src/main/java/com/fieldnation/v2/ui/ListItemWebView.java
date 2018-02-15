package com.fieldnation.v2.ui;

import android.content.Context;
import android.text.Html;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 11/10/17.
 */

public class ListItemWebView extends RelativeLayout {
    private static final String TAG = "ListItemWebView";

    private static final int COLLAPSED_HEIGHT = 250;

    // Ui
    private TextView _titleTextView;
    private WebView _webView;
    private IconFontTextView _button;

    // Data
    private String _title;
    private String _data;
    private boolean _collapsed = true;
    private boolean _lastCollapsed = true;
    private boolean _switchCollapsed = true;

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
        _titleTextView.setOnClickListener(_toggle_onClick);

        _webView = findViewById(R.id.data_webview);

        _button = findViewById(R.id.expand_button);
        _button.setOnClickListener(_toggle_onClick);

        _ccw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_ccw);
        _cw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_cw);

        int fontSize = getResources().getInteger(R.integer.textSizeWorkorderDescription);
        WebSettings webSettings = null;
        webSettings = _webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultFontSize(fontSize);

        populateUi();
    }

    public void setData(String title, String data) {
        Log.v(TAG, "setData");
        _title = title;
        _data = data.replaceAll("<del>", "<span style=\"color:#FFFFFF; background-color:#000000\">");
        _data = _data.replaceAll("</del>", "</span>");
        _data = Html.toHtml(misc.linkifyHtml(_data, Linkify.ALL));
        populateUi();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                _switchCollapsed = true;
                refreshWebViewLayout();
            }
        }, 1000);
    }

    private void populateUi() {
        Log.v(TAG, "populateUi");
        if (_title != null)
            _titleTextView.setText(_title);

        if (_data != null) {
            _webView.loadDataWithBaseURL(null, _data, "text/html", "utf-8", null);
        }
    }

    private void refreshWebViewLayout() {
        if (!_switchCollapsed)
            return;
        Log.v(TAG, "refreshWebViewLayout");

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) _webView.getLayoutParams();

        // move to collapsed state
        if (_collapsed) {
            Log.v(TAG, "collapsed");
            if (_title == null) {
                _titleTextView.setVisibility(GONE);

                if (layoutParams.height == LayoutParams.WRAP_CONTENT) {
                    if (_webView.getHeight() < COLLAPSED_HEIGHT) {
                        _button.setVisibility(GONE);
                    } else {
                        _button.setVisibility(VISIBLE);
                        layoutParams.height = COLLAPSED_HEIGHT;
                        _webView.setLayoutParams(layoutParams);
                        if (_lastCollapsed != _collapsed)
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    _button.startAnimation(_ccw);
                                }
                            });
                    }
                }
            } else {
                _button.setVisibility(VISIBLE);
                _webView.setVisibility(GONE);
                _titleTextView.setVisibility(VISIBLE);
                if (_lastCollapsed != _collapsed)
                    post(new Runnable() {
                        @Override
                        public void run() {
                            _button.startAnimation(_ccw);
                        }
                    });
            }
            // move to expanded
        } else {
            Log.v(TAG, "!collapsed");
            if (_title == null) {
                if (layoutParams.height != LayoutParams.WRAP_CONTENT) {
                    layoutParams.height = LayoutParams.WRAP_CONTENT;
                    _webView.setLayoutParams(layoutParams);
                    if (_lastCollapsed != _collapsed)
                        post(new Runnable() {
                            @Override
                            public void run() {
                                _button.startAnimation(_cw);
                            }
                        });
                }
            } else {
                _button.setVisibility(VISIBLE);
                _titleTextView.setVisibility(VISIBLE);
                _webView.setVisibility(VISIBLE);
                if (_lastCollapsed != _collapsed)
                    post(new Runnable() {
                        @Override
                        public void run() {
                            _button.startAnimation(_cw);
                        }
                    });
            }
        }
        _lastCollapsed = _collapsed;
        _switchCollapsed = false;
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final OnClickListener _toggle_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _collapsed = !_collapsed;
            _switchCollapsed = true;
            refreshWebViewLayout();
        }
    };
}
