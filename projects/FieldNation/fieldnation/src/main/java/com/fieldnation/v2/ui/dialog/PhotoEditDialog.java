package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.service.data.filecache.FileCacheClient;

/**
 * Created by mc on 6/21/17.
 */

public class PhotoEditDialog extends FullScreenDialog {
    private static final String TAG = "PhotoEditDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private ImageView _imageView;
    private ProgressBar _progressBar;

    // Clients
    private FileCacheClient _fileCacheClient;

    // Supplied Data
    private Uri _uri;
    private String _path;
    private String _name;
    private Bitmap _bitmap;


    public PhotoEditDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_photo_edit, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setTitle("Profile Photo");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);
        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setTitle("SAVE");

        _imageView = v.findViewById(R.id.imageView);
        _progressBar = v.findViewById(R.id.progressBar);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        _fileCacheClient = new FileCacheClient(_fileCacheClient_listener);
        _fileCacheClient.connect(App.get());
        populateUi();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        if (params.containsKey("uri")) {
            _uri = params.getParcelable("uri");
            FileCacheClient.cacheDeliverableUpload(App.get(), _uri);
        } else if (params.containsKey("path")) {
            _path = params.getString("path");
            setPhoto(MemUtils.getMemoryEfficientBitmap(_path, 400));
        }
        _name = params.getString("name");
    }

    public void setPhoto(Bitmap bitmap) {
        Log.v(TAG, "setPhoto");
        _bitmap = bitmap;

        populateUi();
    }

    private void populateUi() {
        if (_imageView == null)
            return;

        if (_bitmap != null) {
            _progressBar.setVisibility(View.GONE);
            _imageView.setVisibility(View.VISIBLE);
            _imageView.setImageBitmap(_bitmap);
        } else {
            _progressBar.setVisibility(View.VISIBLE);
            _imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
    }

    @Override
    public void onPause() {
        if (_fileCacheClient != null) _fileCacheClient.disconnect(App.get());
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);
    }

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            _onSaveDispatcher.dispatch(getUid(), _name, _path, _uri);
            dismiss(true);
            return true;
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
    public void cancel() {
        _onCancelDispatcher.dispatch(getUid(), _name, _path, _uri);
        super.cancel();
    }

    private final FileCacheClient.Listener _fileCacheClient_listener = new FileCacheClient.Listener() {
        @Override
        public void onConnected() {
            _fileCacheClient.subDeliverableCache();
        }

        @Override
        public void onDeliverableCacheEnd(Uri uri, String filePath) {
            Log.v(TAG, "onDeliverableCacheEnd " + filePath);
            Log.v(TAG, "onDeliverableCacheEnd " + uri);
            Log.v(TAG, "onDeliverableCacheEnd " + _path);
            Log.v(TAG, "onDeliverableCacheEnd " + _uri);

            if (_path != null && filePath != null) {
                if (!_path.equals(filePath)) {
                    Log.v(TAG, "onDeliverableCacheEnd filepath mismatch, skipping");
                    return;
                }
            }

            if (_uri != null && uri != null) {
                if (!_uri.toString().equals(uri.toString())) {
                    Log.v(TAG, "onDeliverableCacheEnd uri mismatch, skipping");
                    return;
                }
            }

            _path = filePath;
            setPhoto(MemUtils.getMemoryEfficientBitmap(filePath, 400));
        }
    };

    public static void show(Context context, String uid, String path, String name) {
        Bundle params = new Bundle();
        params.putString("path", path);
        params.putString("name", name);

        Controller.show(context, uid, PhotoEditDialog.class, params);
    }

    public static void show(Context context, String uid, Uri uri, String name) {
        Bundle params = new Bundle();
        params.putParcelable("uri", uri);
        params.putString("name", name);

        Controller.show(context, uid, PhotoEditDialog.class, params);
    }

    /*-*************************-*/
    /*-         Save            -*/
    /*-*************************-*/
    public interface OnSaveListener {
        void onSave(String name, String path, Uri uri);
    }

    private static KeyedDispatcher<OnSaveListener> _onSaveDispatcher = new KeyedDispatcher<OnSaveListener>() {
        @Override
        public void onDispatch(OnSaveListener listener, Object... parameters) {
            listener.onSave((String) parameters[0], (String) parameters[1], (Uri) parameters[2]);
        }
    };

    public static void addOnSaveListener(String uid, OnSaveListener onSaveListener) {
        _onSaveDispatcher.add(uid, onSaveListener);
    }

    public static void removeOnSaveListener(String uid, OnSaveListener onSaveListener) {
        _onSaveDispatcher.remove(uid, onSaveListener);
    }

    public static void removeAllOnSaveListener(String uid) {
        _onSaveDispatcher.removeAll(uid);
    }

    /*-*************************-*/
    /*-         Cancel          -*/
    /*-*************************-*/
    public interface OnCancelListener {
        void onCancel(String name, String path, Uri uri);
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel((String) parameters[0], (String) parameters[1], (Uri) parameters[2]);
        }
    };

    public static void addOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }
}
