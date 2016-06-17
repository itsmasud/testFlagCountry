package com.fieldnation.ui.dialog;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.FileUtils;
import com.fieldnation.utils.misc;

import java.io.File;


/**
 * @author shoaib.ahmed
 */
public class PhotoUploadDialog extends DialogFragmentBase {
    private static final String TAG = "PhotoUploadDialog";

    // State
    private static final String STATE_ORIGINAL_FILE_NAME = "STATE_ORIGINAL_FILE_NAME";
    private static final String STATE_NEW_FILE_NAME = "STATE_NEW_FILE_NAME";
    private static final String STATE_PHOTO_DESCRIPTION = "STATE_PHOTO_DESCRIPTION";

    // UI
    private TextView _titleTextView;
    private ImageView _photoImageView;
    private EditText _fileNameEditText;
    private EditText _photoDescriptionEditText;
    private Button _okButton;
    private Button _cancelButton;
    private ProgressBar _progressBar;


    // Data user entered
    private String _newFileName;
    private String _photoDescription;

    // Supplied
    private Listener _listener;
    private String _title;
    private String _originalFileName;
    private Bitmap _bitmap;
    private boolean isDeliverableAnImage = false;


    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static PhotoUploadDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, PhotoUploadDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Log.v(TAG, "onCreate");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_ORIGINAL_FILE_NAME))
                _originalFileName = savedInstanceState.getString(STATE_ORIGINAL_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_NEW_FILE_NAME))
                _newFileName = savedInstanceState.getString(STATE_NEW_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_PHOTO_DESCRIPTION))
                _photoDescription = savedInstanceState.getString(STATE_PHOTO_DESCRIPTION);
        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Log.v(TAG, "onSaveInstanceState");
        if (!misc.isEmptyOrNull(_originalFileName))
            outState.putString(STATE_ORIGINAL_FILE_NAME, _originalFileName);

        if (!misc.isEmptyOrNull(_newFileName))
            outState.putString(STATE_NEW_FILE_NAME, _newFileName);

        if (!misc.isEmptyOrNull(_photoDescription))
            outState.putString(STATE_PHOTO_DESCRIPTION, _photoDescription);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_photo_upload, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);

        _photoImageView = (ImageView) v.findViewById(R.id.photo_imageview);
        _photoImageView.setOnClickListener(_photoImageView_onClick);

        _fileNameEditText = (EditText) v.findViewById(R.id.filename_edittext);
        _fileNameEditText.setOnEditorActionListener(_onEditor);
        _fileNameEditText.addTextChangedListener(_fileName_textWatcher);

        _photoDescriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _photoDescriptionEditText.setOnEditorActionListener(_onEditor);
        _photoDescriptionEditText.addTextChangedListener(_photoDescription_textWatcher);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(CharSequence title, String filename) {
        _title = (String) title;
        show();
    }

    public void setPhoto(Bitmap bitmap) {
        Log.v(TAG, "setPhoto");
        _bitmap = bitmap;
        populateUi();
    }

    private void populateUi() {
        if (getActivity() == null)
            return;

        if (_photoImageView == null)
            return;

        if (_bitmap != null) {
            _progressBar.setVisibility(View.GONE);
            _photoImageView.setVisibility(View.VISIBLE);
            _photoImageView.setImageDrawable(new BitmapDrawable(getActivity().getResources(), _bitmap));
        } else {
//            _progressBar.setVisibility(View.VISIBLE);
//            _photoImageView.setVisibility(View.INVISIBLE);
        }

        _photoDescriptionEditText.setText(misc.isEmptyOrNull(_photoDescription) ? "" : _photoDescription);
        _fileNameEditText.setText(misc.isEmptyOrNull(_newFileName) ? _originalFileName : _newFileName);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final TextView.OnEditorActionListener _onEditor = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v == _fileNameEditText) {
                    _fileNameEditText.requestFocus();
                    handled = true;

                } else if (v == _photoDescriptionEditText) {
                    _photoDescriptionEditText.requestFocus();
                    handled = true;
                }
            }

            return handled;
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener == null) return;

            if (misc.isEmptyOrNull(_newFileName)) {
                _fileNameEditText.setText(_originalFileName);
                ToastClient.toast(App.get(), getString(R.string.dialog_insert_file_name), Toast.LENGTH_LONG);
                return;
            }

            if (misc.isEmptyOrNull(_newFileName.substring(0, _newFileName.lastIndexOf('.')))) {
                ToastClient.toast(App.get(), getString(R.string.dialog_insert_filename_before_extension), Toast.LENGTH_LONG);
                return;
            }

            if (!FileUtils.isValidFileName(_newFileName)) {
                ToastClient.toast(App.get(), getString(R.string.dialog_invalid_file_name), Toast.LENGTH_LONG);
                return;
            }

            if (misc.isEmptyOrNull(_newFileName)) {
                _fileNameEditText.setText(_originalFileName);
                ToastClient.toast(App.get(), getString(R.string.dialog_insert_file_name), Toast.LENGTH_LONG);
                return;
            }

            PhotoUploadDialog.this.dismiss();
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private final View.OnClickListener _photoImageView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private final TextWatcher _fileName_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            _newFileName = _fileNameEditText.getText().toString().trim();

            if (misc.isEmptyOrNull(_newFileName)) {
                _okButton.setEnabled(false);
            } else {
                _okButton.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher _photoDescription_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            _photoDescription = _photoDescriptionEditText.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public interface Listener {
        void onOk(Uri uri, File file, String filename, String photoDescription);

        void onCancel();
    }
}