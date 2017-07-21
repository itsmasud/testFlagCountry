package com.fieldnation.fnpermissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

/**
 * Created by mc on 6/8/17.
 */

public class PermissionsDialog extends FullScreenDialog {
    private static final String TAG = "PermissionsDialog";

    // Ui
    private ImageView _imageView;
    private TextView _titleTextView;
    private TextView _descriptionTextView;
    private TextView _accessButton;

    // Data
    private String _permission;
    private boolean _required;
    private boolean _secondTry;

    public PermissionsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_permissions, container, false);

        _imageView = (ImageView) v.findViewById(R.id.imageView);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _descriptionTextView = (TextView) v.findViewById(R.id.description_textviews);
        _accessButton = (TextView) v.findViewById(R.id.access_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().setClickable(true);

        _accessButton.setOnClickListener(_access_onClick);
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);

        _permission = params.getString("permission");
        _required = params.getBoolean("required");
        _secondTry = params.getBoolean("secondTry");

        populateUi();
    }

    private void populateUi() {
        switch (_permission) {
            case Manifest.permission.ACCESS_COARSE_LOCATION:
            case Manifest.permission.ACCESS_FINE_LOCATION:
                _imageView.setImageResource(R.drawable.location);
                _titleTextView.setText(R.string.dialog_location_title);
                _descriptionTextView.setText(misc.htmlify(getContext().getString(R.string.dialog_location_body)));
                break;

            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                _imageView.setImageResource(R.drawable.storage);
                _titleTextView.setText(R.string.dialog_storage_title);
                _descriptionTextView.setText(misc.htmlify(getContext().getString(R.string.dialog_storage_body)));
                break;

            case Manifest.permission.CAMERA:
                _imageView.setImageResource(R.drawable.camera);
                _titleTextView.setText(R.string.dialog_camera_title);
                _descriptionTextView.setText(misc.htmlify(getContext().getString(R.string.dialog_camera_body)));
                break;

            default:
                Log.v(TAG, "Permission " + _permission + " not supported by this dialog!");
                break;
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        PermissionsClient.setPermissionDenied(_permission);
        PermissionsClient.onComplete(getContext(), _permission, PackageManager.PERMISSION_DENIED);
    }

    @Override
    public boolean isCancelable() {
        return !_required;
    }

    private final View.OnClickListener _access_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (_secondTry && _required) {
                ActivityResultClient.startActivity(getContext(), new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getContext().getPackageName(), null)));
            } else {
                PermissionsClient.requestPermissions(getContext(), new String[]{_permission}, new boolean[]{_required}, PermissionsClient.SECOND_REQUEST);
            }
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, String permission, boolean required, boolean secondTry) {
        Bundle params = new Bundle();
        params.putString("permission", permission);
        params.putBoolean("required", required);
        params.putBoolean("secondTry", secondTry);
        Controller.show(context, uid, PermissionsDialog.class, params);
    }
}
