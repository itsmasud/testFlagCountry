package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntools.KeyedDispatcher;

/**
 * Created by mc on 8/11/17.
 */

public class WebViewDialog extends FullScreenDialog {
    private static final String TAG = "WebViewDialog";

    // Ui
    private Toolbar _toolbar;
    private WebView _webView;

    public WebViewDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_web_view, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _webView = v.findViewById(R.id.webview);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        getView().addOnAttachStateChangeListener(_onAttachState);

        final int fontSize = getContext().getResources().getInteger(R.integer.textSizeReleaseNote);
        WebSettings webSettings = null;

        webSettings = _webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultFontSize(fontSize);
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        String html = params.getString("html");
        _toolbar.setTitle(params.getString("title"));
        _webView.loadData(html, "text/html", "utf-8");
    }

    private View.OnAttachStateChangeListener _onAttachState = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            WebStorage.getInstance().deleteAllData();
            _webView.destroy();
        }
    };

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    @Override
    public void dismiss(boolean animate) {
        _onClosedDispatcher.dispatch(getUid());
        super.dismiss(animate);
    }

    public static void show(Context context, String title, String html) {
        Bundle params = new Bundle();
        params.putString("title", title);
        params.putString("html", html);

        Controller.show(context, null, WebViewDialog.class, params);
    }

    public static void show(Context context, String uid, String title, String html) {
        Bundle params = new Bundle();
        params.putString("title", title);
        params.putString("html", html);

        Controller.show(context, uid, WebViewDialog.class, params);
    }

    /*-*************************-*/
    /*-         Closed          -*/
    /*-*************************-*/
    public interface OnClosedListener {
        void onClosed();
    }

    private static KeyedDispatcher<OnClosedListener> _onClosedDispatcher = new KeyedDispatcher<OnClosedListener>() {
        @Override
        public void onDispatch(OnClosedListener listener, Object... parameters) {
            listener.onClosed();
        }
    };

    public static void addOnClosedListener(String uid, OnClosedListener onClosedListener) {
        _onClosedDispatcher.add(uid, onClosedListener);
    }

    public static void removeOnClosedListener(String uid, OnClosedListener onClosedListener) {
        _onClosedDispatcher.remove(uid, onClosedListener);
    }

    public static void removeAllOnClosedListener(String uid) {
        _onClosedDispatcher.removeAll(uid);
    }
}
