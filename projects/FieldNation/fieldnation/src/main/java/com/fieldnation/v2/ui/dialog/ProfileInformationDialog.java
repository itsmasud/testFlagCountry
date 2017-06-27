package com.fieldnation.v2.ui.dialog;

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
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.ImageUtils;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.filecache.FileCacheClient;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.v2.ui.GetFileIntent;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Shoaib on 13/01/2016.
 */
public class ProfileInformationDialog extends FullScreenDialog {
    private static final String TAG = "ProfileInformationDialog";

    // Dialogs
    private static final String DIALOG_GET_FILE = TAG + ".getFileDialog";
    private static final String DIALOG_EDIT_PHOTO = TAG + ".editPhotoDialog";

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
    private FileCacheClient _fileCacheClient;
    private String _tempFileName = null;

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

        _toolbar = _root.findViewById(R.id.toolbar);
        _toolbar.setTitle(_root.getResources().getString(R.string.dialog_profile_information_title));
        _toolbar.setNavigationIcon(R.drawable.back_arrow);

        _picView = _root.findViewById(R.id.pic_view);
        _picView.setProfilePic(R.drawable.missing_circle);

        _profileIdTextView = _root.findViewById(R.id.profile_id_textview);
        _profileNameTextView = _root.findViewById(R.id.profile_name_textview);
        _phoneNoEditText = _root.findViewById(R.id.phone_edittext);
        _phoneNoExtEditText = _root.findViewById(R.id.phone_ext_edittext);
        _address1EditText = _root.findViewById(R.id.address_1_edittext);
        _address2EditText = _root.findViewById(R.id.address_2_edittext);
        _cityEditText = _root.findViewById(R.id.city_edittext);
        _stateButton = _root.findViewById(R.id.state_button);
        _zipCodeEditText = _root.findViewById(R.id.zip_code_edittext);

        return _root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _picView.setOnClickListener(_pic_onClick);

        _photos = new PhotoClient(_photo_listener);
        _photos.connect(App.get());

        _fileCacheClient = new FileCacheClient(_fileCacheClient_listener);
        _fileCacheClient.connect(App.get());

        GetFileDialog.addOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        PhotoEditDialog.addOnSaveListener(DIALOG_EDIT_PHOTO, _photoEdit_onSave);
        PhotoEditDialog.addOnCancelListener(DIALOG_EDIT_PHOTO, _photoEdit_onCancel);

        _profile = App.get().getProfile();
        populateUi();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        if (_photos != null) _photos.disconnect(App.get());

        if (_fileCacheClient != null) _fileCacheClient.disconnect(App.get());

        GetFileDialog.removeOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        PhotoEditDialog.removeOnSaveListener(DIALOG_EDIT_PHOTO, _photoEdit_onSave);
        PhotoEditDialog.removeOnCancelListener(DIALOG_EDIT_PHOTO, _photoEdit_onCancel);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_tempFileName != null)
            outState.putString("_tempFileName", _tempFileName);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState != null) {
            if (savedState.containsKey("_tempFileName"))
                _tempFileName = savedState.getString("_tempFileName");
        }
    }

    @Override
    public void dismiss(boolean animate) {
        Log.v(TAG, "dismiss");
        super.dismiss(animate);
    }

    @Override
    public void cancel() {
        Log.v(TAG, "cancel");
        super.cancel();
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
            _profileIdTextView.setText("ID: " + _profile.getUserId().toString());

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


        if (_tempFileName != null) {
            try {
                _profilePic = new WeakReference<>((Drawable) new BitmapDrawable(ImageUtils.extractCircle(MemUtils.getMemoryEfficientBitmap(_tempFileName, 400))));
                if (_profilePic.get() != null)
                    _picView.setProfilePic(_profilePic.get());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

        } else if (_photos.isConnected() && (_profilePic == null || _profilePic.get() == null)) {
            _picView.setProfilePic(R.drawable.missing_circle);
            String url = _profile.getPhoto().getLarge();
            if (!misc.isEmptyOrNull(url)) {
                PhotoClient.get(App.get(), url, true, false);
                _photos.subGet(url, true, false);
            }
        } else if (_profilePic != null && _profilePic.get() != null) {
            _picView.setProfilePic(_profilePic.get());
        }
    }

    /*-*************************-*/
    /*-         Events			-*/
    /*-*************************-*/
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

    private final PhotoEditDialog.OnSaveListener _photoEdit_onSave = new PhotoEditDialog.OnSaveListener() {
        @Override
        public void onSave(String name, String path, Uri uri) {
            if (path != null) {
                FileCacheClient.cacheFileUpload(App.get(), Uri.fromFile(new File(path)));
                ProfileClient.uploadProfilePhoto(App.get(), _profile.getUserId(), path, name);
            } else if (uri != null) {
                FileCacheClient.cacheFileUpload(App.get(), uri);
                ProfileClient.uploadProfilePhoto(App.get(), _profile.getUserId(), name, uri);
            }
        }
    };

    private final PhotoEditDialog.OnCancelListener _photoEdit_onCancel = new PhotoEditDialog.OnCancelListener() {
        @Override
        public void onCancel(String name, String path, Uri uri) {

        }
    };

    private final GetFileDialog.OnFileListener _getFile_onFile = new GetFileDialog.OnFileListener() {
        @Override
        public void onFile(List<GetFileDialog.FileUriIntent> fileResult) {
            if (fileResult.size() == 0)
                return;

            if (fileResult.size() > 1)
                return;

            GetFileDialog.FileUriIntent fui = fileResult.get(0);

            if (fui.file != null) {
                Log.v(TAG, "Image uploading taken by camera");
                //FileCacheClient.cacheFileUpload(App.get(), Uri.fromFile(fui.file));
                //ProfileClient.uploadProfilePhoto(App.get(), _profile.getUserId(), fui.file.getAbsolutePath(), fui.file.getName());
                PhotoEditDialog.show(App.get(), DIALOG_EDIT_PHOTO, fui.file.getAbsolutePath(), fui.file.getName());
            } else if (fui.uri != null) {
                Log.v(TAG, "Single local/ non-local file upload");

                String mime = App.get().getContentResolver().getType(fui.uri);
                if (mime != null && mime.contains("image")) {
                    PhotoEditDialog.show(App.get(), DIALOG_EDIT_PHOTO, fui.uri, FileUtils.getFileNameFromUri(App.get(), fui.uri));
                } else {
                    FileCacheClient.cacheFileUpload(App.get(), fui.uri);
                    ProfileClient.uploadProfilePhoto(App.get(), _profile.getUserId(), FileUtils.getFileNameFromUri(App.get(), fui.uri), fui.uri);
                }
            }
        }
    };

    private final View.OnClickListener _pic_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            GetFileIntent intent1 = new GetFileIntent(intent, "Get Content");

            if (App.get().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                GetFileIntent intent2 = new GetFileIntent(intent, "Take Picture");
                GetFileDialog.show(App.get(), DIALOG_GET_FILE, new GetFileIntent[]{intent1, intent2});
            } else {
                GetFileDialog.show(App.get(), DIALOG_GET_FILE, new GetFileIntent[]{intent1});
            }
        }
    };

    private final FileCacheClient.Listener _fileCacheClient_listener = new FileCacheClient.Listener() {
        @Override
        public void onConnected() {
            _fileCacheClient.subFileCache();
        }

        @Override
        public void onFileCacheEnd(Uri uri, String filename) {
            _profilePic = null;
            _tempFileName = filename;
            populateUi();
        }
    };

    public static void show(Context context) {
        Controller.show(context, "ProfileInformationDialog", ProfileInformationDialog.class, null);
    }

    public static void dismiss(Context context) {
        Controller.dismiss(context, null);
    }
}