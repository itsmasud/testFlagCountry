package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
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
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.ui.ProfilePicView;

import java.lang.ref.WeakReference;

/**
 * Created by Shoaib on 13/01/2016.
 */
public class ProfileInformationDialog extends FullScreenDialog {
    private static final String TAG = "ProfileInformationDialog";

    // State
    private static final String STATE_MESSAGE = "ProfileInformationDialog:Message";
    private static final String STATE_SOURCE = "ProfileInformationDialog:Source";

    // Ui
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
    private Listener _listener;
    private String _source;
    private Profile _profile;
    private PhotoClient _photos;
    private WeakReference<Drawable> _profilePic = null;
    private boolean _clear = false;

    public ProfileInformationDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
//    public static ProfileInformationDialog getInstance(FragmentManager fm, String tag) {
//        Log.v(TAG, "getInstance");
//        return getInstance(fm, tag, ProfileInformationDialog.class);
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.v(TAG, "onCreate");
//        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        Log.v(TAG, "onSaveInstanceState");
//        super.onSaveInstanceState(outState);
//    }


    @Override
    public void onAdded() {
        super.onAdded();


//        if (_clear) {
//            _explanationEditText.setText("");
//            _explanationEditText.setHint(getString(R.string.dialog_explanation_default));
//            _clear = false;
//            return;
//        }

        _picView.setOnClickListener(_pic_onClick);

        PicChooserDialog.setListener(_picChooserDialog_listener);

        _photos = new PhotoClient(_photo_listener);
        _photos.connect(App.get());


        _profile = App.get().getProfile();
        populateUi();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        _root = inflater.inflate(R.layout.dialog_v2_profile_information, container, false);

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


//    public void show(String source) {
//        _source = source;
//        _clear = true;
//        super.show();
//    }

    public void setListener(Listener listener) {
        _listener = listener;
    }


    private void populateUi() {

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
            PicChooserDialog.Controller.show(App.get());
        }
    };



    private PicChooserDialog.Listener _picChooserDialog_listener = new PicChooserDialog.Listener() {
        @Override
        public void onCamera() {
            Log.e(TAG, "inside _picChooserDialog_listener#onCamera");
        }

        @Override
        public void onGallery() {
            Log.e(TAG, "inside _picChooserDialog_listener#onGallery");
        }
    };

    public static abstract class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, ProfileInformationDialog.class);
        }

        public static void show(Context context) {
            show(context, ProfileInformationDialog.class, null);
        }

        public static void dismiss(Context context) {
            dismiss(context, ProfileInformationDialog.class);
        }
    }





    public interface Listener {
        void onOk(String message);

        void onCancel();
    }


}
