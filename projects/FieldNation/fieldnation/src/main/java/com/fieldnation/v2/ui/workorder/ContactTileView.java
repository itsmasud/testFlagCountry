package com.fieldnation.v2.ui.workorder;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fnpermissions.PermissionsResponseListener;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.ApatheticOnClickListener;

import java.net.URLEncoder;

/**
 * Created by Michael Carver on 5/26/2015.
 */
public class ContactTileView extends RelativeLayout {
    private static final String TAG = "ContactTileView";

    private TextView _nameTextView;
    private TextView _phoneTextView;
    private TextView _titleTextView;

    private String _name;
    private String _phone;
    private String _phoneExt;
    private String _title;

    public ContactTileView(Context context) {
        super(context);
        init();
    }

    public ContactTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_tile, this);

        if (isInEditMode())
            return;

        _nameTextView = findViewById(R.id.name_textview);
        _phoneTextView = findViewById(R.id.phone_textview);
        _titleTextView = findViewById(R.id.title_textview);

        setOnClickListener(_this_onClick);
        setOnLongClickListener(_this_onLongClick);
    }

    public void setData(String name, String phone, String phoneExt, String title) {
        _name = name;
        _phone = phone;
        _phoneExt = phoneExt;
        _title = title;

        populateUi();
    }

    private void populateUi() {
        if (_nameTextView == null)
            return;

        if (_name == null) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);

        _nameTextView.setText(_name);

        if (!misc.isEmptyOrNull(_phone)) {
            _phoneTextView.setText(_phone + (misc.isEmptyOrNull(_phoneExt) ? "" : " x" + _phoneExt));
            _phoneTextView.setVisibility(VISIBLE);
        } else {
            _phoneTextView.setVisibility(GONE);
        }

        if (_title != null) {
            _titleTextView.setText(_title);
            _titleTextView.setVisibility(VISIBLE);
        } else {
            _titleTextView.setVisibility(GONE);
        }
    }

    private final View.OnClickListener _this_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (!misc.isEmptyOrNull(_phone)) {
                try {
                    int permissionCheck = PermissionsClient.checkSelfPermission(App.get(), Manifest.permission.CALL_PHONE);
                    if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                        _permissionsListener.sub();
                        PermissionsClient.requestPermissions(
                                new String[]{Manifest.permission.CALL_PHONE},
                                new boolean[]{false});
                        return;
                    }

                    // TODO Save this for when we upgrade to Android 6+
                    Intent callIntent = new Intent(Intent.ACTION_CALL);

                    String phone = _phone;
                    if (!misc.isEmptyOrNull(_phoneExt)) {
                        phone += "," + _phoneExt;
                    }
                    callIntent.setData(Uri.parse("tel:" + URLEncoder.encode(phone, "UTF-8")));

                    if (getContext().getPackageManager().queryIntentActivities(callIntent, 0).size() > 0) {
                        getContext().startActivity(callIntent);
                    } else {
                        ToastClient.toast(getContext(),
                                "Couldn't call number: "
                                        + _phone + (misc.isEmptyOrNull(_phoneExt) ? "" : " x" + _phoneExt), Toast.LENGTH_LONG);
                    }

                } catch (Exception ex) {
                    Log.logException(ex);
                }
            }
        }
    };

    private final PermissionsResponseListener _permissionsListener = new PermissionsResponseListener() {
        @Override
        public void onComplete(String permission, int grantResult) {
            if (permission.equals(Manifest.permission.CALL_PHONE)) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    _this_onClick.onClick(ContactTileView.this);
                    _permissionsListener.unsub();
                } else {
                    ToastClient.toast(getContext(), "Couldn't call number: " + _phone + (misc.isEmptyOrNull(_phoneExt) ? "" : " x" + _phoneExt) + ". Permissions denied.", Toast.LENGTH_LONG);
                    _permissionsListener.unsub();
                }
            }
        }
    };

    private final OnLongClickListener _this_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            String phone = _phone;
            if (!misc.isEmptyOrNull(_phoneExt)) {
                phone += "," + _phoneExt;
            }

            ClipboardManager clipboard = (android.content.ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = android.content.ClipData.newPlainText(_name, phone);
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), R.string.toast_copied_to_clipboard, Toast.LENGTH_LONG);
            return true;
        }
    };

}
