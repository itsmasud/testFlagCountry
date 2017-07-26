package com.fieldnation.v2.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.service.data.filecache.FileCacheClient;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.UCropView;

/**
 * Created by mc on 6/21/17.
 */

public class PhotoEditDialog extends FullScreenDialog {
    private static final String TAG = "PhotoEditDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private UCropView _uCropView;
    private ProgressBar _progressBar;

    // Clients
    private FileCacheClient _fileCacheClient;

    // Supplied Data
    private Uri _sourceUri;
    private Uri _cachedUri;
    private String _name;


    public PhotoEditDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        View v = inflater.inflate(R.layout.dialog_photo_edit, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setTitle("Profile Photo");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);
        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText("SAVE");

        _uCropView = v.findViewById(R.id.ucrop);
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
            _sourceUri = params.getParcelable("uri");
            FileCacheClient.cacheFileUpload(App.get(), _sourceUri.toString(), _sourceUri);
        }
        _name = params.getString("name");
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey("_cachedUri"))
            _cachedUri = savedState.getParcelable("_cachedUri");

        super.onRestoreDialogState(savedState);
    }

    @Override
    public void onPause() {
        if (_fileCacheClient != null) _fileCacheClient.disconnect(App.get());
        super.onPause();
    }

    @Override
    public void onStop() {
        ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        super.onStop();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_cachedUri != null)
            outState.putParcelable("_cachedUri", _cachedUri);

        super.onSaveDialogState(outState);
    }

    private void populateUi() {
        if (_uCropView == null)
            return;

        if (_sourceUri != null) {
            _progressBar.setVisibility(View.GONE);
            _uCropView.setVisibility(View.VISIBLE);
            try {
                _uCropView.getCropImageView().setImageUri(_cachedUri, _cachedUri);
                _uCropView.getOverlayView().setShowCropFrame(true);
                _uCropView.getOverlayView().setShowCropGrid(true);
                _uCropView.getOverlayView().setTargetAspectRatio(1.0F);
                _uCropView.getOverlayView().setCircleDimmedLayer(true);
                _uCropView.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH);
                _uCropView.setEnabled(true);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else {
            _progressBar.setVisibility(View.VISIBLE);
            _uCropView.setVisibility(View.INVISIBLE);
        }
    }

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            _uCropView.setEnabled(false);
            _progressBar.setVisibility(View.VISIBLE);
            _uCropView.getCropImageView().cropAndSaveImage(Bitmap.CompressFormat.JPEG, 99, new BitmapCropCallback() {
                @Override
                public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                    _onSaveDispatcher.dispatch(getUid(), _name, resultUri);
                    dismiss(true);
                }

                @Override
                public void onCropFailure(@NonNull Throwable t) {
                    ToastClient.toast(App.get(), "Failure cropping image", Toast.LENGTH_SHORT);
                    Log.v(TAG, t);
                    _uCropView.setEnabled(true);
                    _progressBar.setVisibility(View.GONE);
                }

            });
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
        _onCancelDispatcher.dispatch(getUid(), _name, _cachedUri);
        super.cancel();
    }

    private final FileCacheClient.Listener _fileCacheClient_listener = new FileCacheClient.Listener() {
        @Override
        public void onConnected() {
            _fileCacheClient.subFileCache();
        }

        @Override
        public void onFileCacheEnd(String tag, Uri uri, boolean success) {

            if (!tag.equals(_sourceUri.toString())) {
                Log.v(TAG, "onFileCacheEnd uri mismatch, skipping");
                return;
            }

            _cachedUri = uri;
            populateUi();
        }
    };

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
        void onSave(String name, Uri uri);
    }

    private static KeyedDispatcher<OnSaveListener> _onSaveDispatcher = new KeyedDispatcher<OnSaveListener>() {
        @Override
        public void onDispatch(OnSaveListener listener, Object... parameters) {
            listener.onSave((String) parameters[0], (Uri) parameters[1]);
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
        void onCancel(String name, Uri uri);
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel((String) parameters[0], (Uri) parameters[1]);
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
