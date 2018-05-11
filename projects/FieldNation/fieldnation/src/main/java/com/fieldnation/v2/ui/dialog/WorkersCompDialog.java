package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.Button;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.v2.data.client.UsersWebApi;


/**
 * Created by Shoaib Ahmed on 09/05/18.
 */

public class WorkersCompDialog extends FullScreenDialog {
    private static final String TAG = "WorkersCompDialog";

    // Params
    public static final String PARAM_DIALOG_TYPE_ACCEPT = "accept";
    public static final String PARAM_DIALOG_TYPE_REQUEST = "request";

    // Ui
    private Toolbar _toolbar;
    private WebView _webView;
    private Button _submitButton;

    // Data
    private String _dialogType = null;

    public WorkersCompDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_web_view, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _webView = v.findViewById(R.id.webview);
        _submitButton = v.findViewById(R.id.submit_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        getView().addOnAttachStateChangeListener(_onAttachState);
        _submitButton.setOnClickListener(_submit_onClick);

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
        _toolbar.setTitle(params.getString("title"));

        _dialogType = params.getString("dialogType");

        String html = params.getString("html");

        if (params.containsKey("replacingString")) {
            html = html.replaceAll("\\u007B" + "messages, number, percent" + "\\u007D", params.getString("replacingString"));
        }

        if (!params.getBoolean("skipFormatting")) {
            html = html.replaceAll("<del>", "<span style=\"color:#FFFFFF; background-color:#000000\">");
            html = html.replaceAll("</del>", "</span>");
            html = Html.toHtml(misc.linkifyHtml(html, Linkify.ALL));
        }
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

    @Override
    public void dismiss(boolean animate) {
        _onClosedDispatcher.dispatch(getUid());
        super.dismiss(animate);
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    private final View.OnClickListener _submit_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_dialogType.equals(PARAM_DIALOG_TYPE_ACCEPT)){
                _onAcceptedDispatcher.dispatch(getUid());
            }else if (_dialogType.equals(PARAM_DIALOG_TYPE_REQUEST)){
                _onRequestedDispatcher.dispatch(getUid());
            }
            dismiss(true);

            UsersWebApi.getUser(App.get(), App.getProfileId(), false, WebTransaction.Type.NORMAL);
        }
    };

    public static void show(Context context, String dialogType, String title, String html, String replacingString) {
        Bundle params = new Bundle();
        params.putString("title", title);
        params.putString("html", html);
        params.putString("replacingString", replacingString);
        params.putString("dialogType", dialogType);
        params.putBoolean("skipFormatting", false);

        Controller.show(context, null, WorkersCompDialog.class, params);
    }

    public static void show(Context context, String dialogType, String title, String html, String replacingString, boolean skipFormatting) {
        Bundle params = new Bundle();
        params.putString("title", title);
        params.putString("html", html);
        params.putString("replacingString", replacingString);
        params.putString("dialogType", dialogType);
        params.putBoolean("skipFormatting", skipFormatting);

        Controller.show(context, null, WorkersCompDialog.class, params);
    }

    public static void show(Context context, String uid, String dialogType, String title, String html, String replacingString) {
        Bundle params = new Bundle();
        params.putString("title", title);
        params.putString("html", html);
        params.putString("replacingString", replacingString);
        params.putString("dialogType", dialogType);
        params.putBoolean("skipFormatting", false);

        Controller.show(context, uid, WorkersCompDialog.class, params);
    }

    public static void show(Context context, String uid, String dialogType, String title, String html, boolean skipFormatting, String replacingString) {
        Bundle params = new Bundle();
        params.putString("title", title);
        params.putString("html", html);
        params.putString("replacingString", replacingString);
        params.putString("dialogType", dialogType);
        params.putBoolean("skipFormatting", skipFormatting);

        Controller.show(context, uid, WorkersCompDialog.class, params);
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

    /*-****************************-*/
    /*-           Accept           -*/
    /*-****************************-*/
    public interface OnAcceptListener {
        void onAccept();
    }

    private static KeyedDispatcher<WorkersCompDialog.OnAcceptListener> _onAcceptedDispatcher = new KeyedDispatcher<WorkersCompDialog.OnAcceptListener>() {
        @Override
        public void onDispatch(WorkersCompDialog.OnAcceptListener listener, Object... parameters) {
            listener.onAccept();
        }
    };

    public static void addOnAcceptedListener(String uid, WorkersCompDialog.OnAcceptListener onAcceptedListener) {
        _onAcceptedDispatcher.add(uid, onAcceptedListener);
    }

    public static void removeOnAcceptedListener(String uid, WorkersCompDialog.OnAcceptListener onAcceptedListener) {
        _onAcceptedDispatcher.remove(uid, onAcceptedListener);
    }

    public static void removeAllOnAcceptedListener(String uid) {
        _onAcceptedDispatcher.removeAll(uid);
    }

    /*-****************************-*/
    /*-         Request            -*/
    /*-****************************-*/
    public interface OnRequestListener {
        void onRequest();
    }

    private static KeyedDispatcher<WorkersCompDialog.OnRequestListener> _onRequestedDispatcher = new KeyedDispatcher<WorkersCompDialog.OnRequestListener>() {
        @Override
        public void onDispatch(WorkersCompDialog.OnRequestListener listener, Object... parameters) {
            listener.onRequest();
        }
    };

    public static void addOnRequestedListener(String uid, WorkersCompDialog.OnRequestListener onRequestedListener) {
        _onRequestedDispatcher.add(uid, onRequestedListener);
    }

    public static void removeOnRequestedListener(String uid, WorkersCompDialog.OnRequestListener onRequestedListener) {
        _onRequestedDispatcher.remove(uid, onRequestedListener);
    }

    public static void removeAllOnRequestedListener(String uid) {
        _onRequestedDispatcher.removeAll(uid);
    }
}
