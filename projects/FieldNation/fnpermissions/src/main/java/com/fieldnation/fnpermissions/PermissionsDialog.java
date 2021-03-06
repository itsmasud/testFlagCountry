package com.fieldnation.fnpermissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fieldnation.fnactivityresult.ActivityClient;
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
    private PermissionsTuple _permissionTuple;
    private Parcelable _extraData;

    public PermissionsDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_permissions, container, false);

        _imageView = v.findViewById(R.id.imageView);
        _titleTextView = v.findViewById(R.id.title_textview);
        _descriptionTextView = v.findViewById(R.id.description_textviews);
        _accessButton = v.findViewById(R.id.access_button);

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

        _permissionTuple = params.getParcelable("permissionsTuple");
        _extraData = params.getParcelable("extraData");

        populateUi();
    }

    private void populateUi() {
        switch (_permissionTuple.permission) {
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

            case Manifest.permission.CALL_PHONE:
                _imageView.setImageResource(R.drawable.phone);
                _titleTextView.setText(R.string.dialog_phone_title);
                _descriptionTextView.setText(misc.htmlify(getContext().getString(R.string.dialog_phone_body)));
                break;

            default:
                Log.v(TAG, "Permission " + _permissionTuple.permission + " not supported by this dialog!");
                break;
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        State.setPermissionDenied(getContext(), _permissionTuple.permission);
        PermissionsClient.onComplete(_permissionTuple.permission, PackageManager.PERMISSION_DENIED, _extraData);
        PermissionsRequestHandler.requesting = false;
    }

    @Override
    public boolean isCancelable() {
        return !_permissionTuple.required;
    }

    private final View.OnClickListener _access_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PermissionsRequestHandler.requesting = false;
            if (_permissionTuple.secondTry && _permissionTuple.required) {
                ActivityClient.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getContext().getPackageName(), null)));
            } else {
                _permissionTuple.secondTry(true).save(getContext());
                PermissionsClient.requestPermissions(new String[]{_permissionTuple.permission}, new boolean[]{_permissionTuple.required}, _extraData);
            }
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, PermissionsTuple permissionsTuple, Parcelable extraData) {
        Log.v(TAG, "static show");
        Bundle params = new Bundle();
        params.putParcelable("permissionsTuple", permissionsTuple);
        params.putParcelable("extraData", extraData);
        Controller.show(context, uid, PermissionsDialog.class, params);
    }
}
