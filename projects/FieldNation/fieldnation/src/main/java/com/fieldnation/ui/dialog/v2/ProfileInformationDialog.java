package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.ui.ProfilePicView;

import java.io.File;
import java.lang.ref.WeakReference;

import static android.app.Activity.RESULT_OK;
import static com.fieldnation.service.activityresult.ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES;
import static com.fieldnation.service.activityresult.ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES;

/**
 * Created by Shoaib on 13/01/2016.
 */
public class ProfileInformationDialog extends FullScreenDialog {
    private static final String TAG = "ProfileInformationDialog";
    private static final String UID_APP_PICKER_DIALOG = TAG + ".AppPickerDialog";


    // Ui
    private Toolbar _toolbar;
    private View _root;
    private ProfilePicView _picView;
    private TextView _profileIdTextView;
    private TextView _profileNameTextView;
    private EditText _phoneNoEditText;
    private EditText _phoneNoExtEditText;
    private EditText _address1EditText;
    private EditText _address2EditText;
    private EditText _cityEditText;
    private Button _stateButton;
    private EditText _zipCodeEditText;

    // Data
    private Profile _profile;
    private PhotoClient _photos;
    private WeakReference<Drawable> _profilePic = null;
    private boolean _clear = false;
    private ActivityResultClient _activityResultClient;
    private File _tempFile = null;
    private AppPickerDialog.Controller _appPickerDialog;


    public ProfileInformationDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        _root = inflater.inflate(R.layout.dialog_v2_profile_information, container, false);

        _toolbar = (Toolbar) _root.findViewById(R.id.toolbar);

        _picView = (ProfilePicView) _root.findViewById(R.id.pic_view);
        _picView.setProfilePic(R.drawable.missing_circle);

        _profileIdTextView = (TextView) _root.findViewById(R.id.profile_id_textview);
        _profileNameTextView = (TextView) _root.findViewById(R.id.profile_name_textview);
        _phoneNoEditText = (EditText) _root.findViewById(R.id.phone_edittext);
        _phoneNoExtEditText = (EditText) _root.findViewById(R.id.phone_ext_edittext);
        _address1EditText = (EditText) _root.findViewById(R.id.address_1_edittext);
        _address2EditText = (EditText) _root.findViewById(R.id.address_2_edittext);
        _cityEditText = (EditText) _root.findViewById(R.id.city_edittext);
        _stateButton = (Button) _root.findViewById(R.id.state_button);
        _zipCodeEditText = (EditText) _root.findViewById(R.id.zip_code_edittext);

        return _root;
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");
        if (_tempFile != null)
            outState.putString("TEMP_FILE", _tempFile.toString());
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        if (savedState.containsKey("TEMP_FILE"))
            _tempFile = new File(savedState.getString("TEMP_FILE"));
    }

    @Override
    public void onAdded() {
        super.onAdded();
        Log.v(TAG, "onAdded");
        _toolbar.setTitle(_root.getResources().getString(R.string.dialog_profile_information_title));
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _picView.setOnClickListener(_pic_onClick);


        _appPickerDialog = new AppPickerDialog.Controller(App.get(), UID_APP_PICKER_DIALOG);
        _appPickerDialog.setListener(_appPickerDialog_listener);


        _photos = new PhotoClient(_photo_listener);
        _photos.connect(App.get());

        _activityResultClient = new ActivityResultClient(_activityResultClient_listener);
        _activityResultClient.connect(App.get());

        _profile = App.get().getProfile();
        populateUi();

    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        Log.v(TAG, "onRemoved");
        if (_photos != null && _photos.isConnected())
            _photos.disconnect(App.get());

        if (_activityResultClient != null && _activityResultClient.isConnected())
            _activityResultClient.disconnect(App.get());

    }

    private void populateUi() {
        Log.v(TAG, "populateUi");

        if (_profile == null) return;

        _phoneNoEditText.post(new Runnable() {
            @Override
            public void run() {
                misc.hideKeyboard(_phoneNoEditText);
            }
        });


        if (_profile.getUserId() != null)
            _profileIdTextView.setText(_profile.getUserId().toString());

        String fullName = "";
        if (_profile.getFirstname() != null)
            fullName = _profile.getFirstname();
        if (_profile.getLastname() != null)
            fullName += " " + _profile.getLastname();

        if (fullName != null)
            _profileNameTextView.setText(fullName);
        else _profileNameTextView.setVisibility(View.GONE);

        if (_profile.getPhone() != null)
            _phoneNoEditText.setText(_profile.getPhone());

        if (_profile.getPhoneExt() != null)
            _phoneNoExtEditText.setText(_profile.getPhoneExt());

        if (_profile.getAddress1() != null)
            _address1EditText.setText(_profile.getAddress1());

        if (_profile.getAddress2() != null)
            _address2EditText.setText(_profile.getAddress2());

        if (_profile.getCity() != null)
            _cityEditText.setText(_profile.getCity());

        if (_profile.getState() != null)
            _stateButton.setText(_profile.getState());

        if (_profile.getZip() != null)
            _zipCodeEditText.setText(_profile.getZip());


        if (_photos.isConnected() && (_profilePic == null || _profilePic.get() == null)) {
            _picView.setProfilePic(R.drawable.missing_circle);
            String url = _profile.getPhoto().getThumb();
            if (!misc.isEmptyOrNull(url)) {
                PhotoClient.get(App.get(), url, true, false);
                _photos.subGet(url, true, false);
            }
        } else if (_profilePic != null && _profilePic.get() != null) {
            _picView.setProfilePic(_profilePic.get());
        }
    }


    /*-*****************************-*/
    /*-				Events			-*/
    /*-*****************************-*/

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };
    private final PhotoClient.Listener _photo_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
            populateUi();
        }

        @Override
        public void onGet(String url, BitmapDrawable bitmapDrawable, boolean isCircle, boolean failed) {
            if (bitmapDrawable == null) {
                _picView.setProfilePic(R.drawable.missing_circle);
                return;
            }

            Drawable pic = bitmapDrawable;
            _profilePic = new WeakReference<>(pic);
            _picView.setProfilePic(pic);
        }
    };

    private final View.OnClickListener _pic_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            AppPickerDialog.addIntent(intent, "Get Content");

            if (App.get().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                AppPickerDialog.addIntent(intent, "Take Picture");
            }
            AppPickerDialog.Controller.show(App.get(), UID_APP_PICKER_DIALOG);

        }
    };


    private final ActivityResultClient.Listener _activityResultClient_listener = new ActivityResultClient.ResultListener() {
        @Override
        public void onConnected() {
            _activityResultClient.subOnActivityResult(RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
            _activityResultClient.subOnActivityResult(RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
        }

        @Override
        public ActivityResultClient getClient() {
            return _activityResultClient;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            try {
                if ((requestCode != RESULT_CODE_GET_ATTACHMENT_DELIVERABLES
                        && requestCode != RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES)
                        || resultCode != RESULT_OK) {
                    return;
                }

                _activityResultClient.clearOnActivityResult(RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
                _activityResultClient.clearOnActivityResult(RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
//                setLoading(true);

                if (requestCode == RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES && resultCode == RESULT_OK) {
                    _picView.setProfilePic(MemUtils.getMemoryEfficientBitmap(_tempFile.toString(), _picView.getWidth()));
                    return;
                }

                if (requestCode == RESULT_CODE_GET_ATTACHMENT_DELIVERABLES && resultCode == RESULT_OK) {
                    _tempFile = new File(FileUtils.getFilePathFromUri(App.get(), data.getData()));
                    _picView.setProfilePic(MemUtils.getMemoryEfficientBitmap(_tempFile.toString(), _picView.getWidth()));
                }


            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };


    private final AppPickerDialog.ControllerListener _appPickerDialog_listener = new AppPickerDialog.ControllerListener() {

        @Override
        public void onOk(Intent pack) {

            if (pack.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                Log.v(TAG, "onClick: " + pack.toString());
                ActivityResultClient.startActivityForResult(App.get(), pack, ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
            } else {
                File temppath = new File(App.get().getTempFolder() + "/IMAGE-"
                        + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                _tempFile = temppath;
                Log.v(TAG, "onClick: " + temppath.getAbsolutePath());

                // removed because this doesn't work on my motorola
                pack.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
                ActivityResultClient.startActivityForResult(App.get(), pack, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
            }
        }
    };


    public static abstract class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, ProfileInformationDialog.class, null);
        }

        public static void show(Context context) {
            show(context, null, ProfileInformationDialog.class, null);
        }

        public static void dismiss(Context context) {
            dismiss(context, null);
        }
    }
}