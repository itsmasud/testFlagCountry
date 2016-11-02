package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;

import java.io.File;

/**
 * @author shoaib.ahmed
 */
public class PhotoUploadDialog extends DialogFragmentBase {
    private static final String TAG = UniqueTag.makeTag("PhotoUploadDialog");

    // State
    private static final String STATE_ORIGINAL_FILE_NAME = "STATE_ORIGINAL_FILE_NAME";
    private static final String STATE_NEW_FILE_NAME = "STATE_NEW_FILE_NAME";
    private static final String STATE_FILE_EXTENSION = "STATE_FILE_EXTENSION";
    private static final String STATE_DESCRIPTION = "STATE_DESCRIPTION";
    private static final String STATE_PHOTO = "STATE_PHOTO";
    private static final String STATE_HIDE_PHOTO = "STATE_HIDE_PHOTO";

    // UI
    private ImageView _imageView;
    private EditText _fileNameEditText;
    private EditText _descriptionEditText;
    private Button _okButton;
    private Button _cancelButton;
    private ProgressBar _progressBar;

    // Data user entered
    private String _newFileName;
    private String _description;
    private String _extension;

    // Supplied
    private Listener _listener;
    private String _originalFileName;
    private Bitmap _bitmap;
    private boolean _hideImageView = false;
    File _file;
    Intent _data;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static PhotoUploadDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, PhotoUploadDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_ORIGINAL_FILE_NAME))
                _originalFileName = savedInstanceState.getString(STATE_ORIGINAL_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_NEW_FILE_NAME))
                _newFileName = savedInstanceState.getString(STATE_NEW_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_DESCRIPTION))
                _description = savedInstanceState.getString(STATE_DESCRIPTION);

            if (savedInstanceState.containsKey(STATE_PHOTO))
                _bitmap = savedInstanceState.getParcelable(STATE_PHOTO);

            if (savedInstanceState.containsKey(STATE_FILE_EXTENSION))
                _extension = savedInstanceState.getString(STATE_FILE_EXTENSION);

            if (savedInstanceState.containsKey(STATE_HIDE_PHOTO))
                _hideImageView = savedInstanceState.getBoolean(STATE_HIDE_PHOTO);
        }
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (!misc.isEmptyOrNull(_originalFileName))
            outState.putString(STATE_ORIGINAL_FILE_NAME, _originalFileName);

        if (!misc.isEmptyOrNull(_newFileName))
            outState.putString(STATE_NEW_FILE_NAME, _newFileName);

        if (!misc.isEmptyOrNull(_description))
            outState.putString(STATE_DESCRIPTION, _description);

        if (_bitmap != null)
            outState.putParcelable(STATE_PHOTO, _bitmap);

        if (!misc.isEmptyOrNull(_extension))
            outState.putString(STATE_FILE_EXTENSION, _extension);

        if (_hideImageView)
            outState.putBoolean(STATE_HIDE_PHOTO, _hideImageView);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_photo_upload, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _imageView = (ImageView) v.findViewById(R.id.photo_imageview);
        _imageView.setOnClickListener(_photoImageView_onClick);

        _fileNameEditText = (EditText) v.findViewById(R.id.filename_edittext);
        _fileNameEditText.setOnEditorActionListener(_onEditor);
        _fileNameEditText.addTextChangedListener(_fileName_textWatcher);

        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _descriptionEditText.setOnEditorActionListener(_onEditor);
        _descriptionEditText.addTextChangedListener(_photoDescription_textWatcher);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        return v;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_ORIGINAL_FILE_NAME))
                _originalFileName = savedInstanceState.getString(STATE_ORIGINAL_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_NEW_FILE_NAME))
                _newFileName = savedInstanceState.getString(STATE_NEW_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_DESCRIPTION))
                _description = savedInstanceState.getString(STATE_DESCRIPTION);

            if (savedInstanceState.containsKey(STATE_PHOTO))
                _bitmap = savedInstanceState.getParcelable(STATE_PHOTO);

            if (savedInstanceState.containsKey(STATE_FILE_EXTENSION))
                _extension = savedInstanceState.getString(STATE_FILE_EXTENSION);
        }
        populateUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        populateUi();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        _extension = null;
        _newFileName = null;
        _fileNameEditText.setText("");
        _descriptionEditText.setText("");
        super.onDismiss(dialog);
        _bitmap = null;
        _hideImageView = false;
        _description = null;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(String fileName) {
        _originalFileName = fileName;

        if (fileName.contains(".")) {
            _extension = fileName.substring(fileName.lastIndexOf("."));
        }

        super.show();
        populateUi();
    }


    public void setPhoto(Bitmap bitmap) {
        Log.v(TAG, "setPhoto");
        _bitmap = bitmap;
        _hideImageView = _bitmap == null;

        populateUi();
    }

    private void populateUi() {
        if (_imageView == null)
            return;

        if (_bitmap != null) {
            _progressBar.setVisibility(View.GONE);
            _imageView.setVisibility(View.VISIBLE);
            _imageView.setImageBitmap(_bitmap);
        } else if (_hideImageView) {
            _progressBar.setVisibility(View.GONE);
            _imageView.setVisibility(View.GONE);
        } else {
            _progressBar.setVisibility(View.VISIBLE);
            _imageView.setVisibility(View.INVISIBLE);
        }

        _descriptionEditText.setText(misc.isEmptyOrNull(_description) ? "" : _description);
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
                _descriptionEditText.requestFocus();
                handled = true;
            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                _okButton_onClick.onClick(v);
            }

            return handled;
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (misc.isEmptyOrNull(_newFileName)) {
                _fileNameEditText.setText(_originalFileName);
                ToastClient.toast(App.get(), getString(R.string.dialog_insert_file_name), Toast.LENGTH_LONG);
                return;
            }

            if (!FileUtils.isValidFileName(_newFileName)) {
                ToastClient.toast(App.get(), getString(R.string.dialog_invalid_file_name), Toast.LENGTH_LONG);
                return;
            }

            if (!misc.isEmptyOrNull(_extension) && !_newFileName.endsWith(_extension)) {
                _newFileName += _extension;
            }

            if (_listener != null) {
                _listener.onOk(_newFileName, _description);
                dismiss();
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
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
            _description = _descriptionEditText.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final View.OnClickListener _photoImageView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener == null) return;
            _listener.onImageClick();
        }
    };

    public interface Listener {
        void onOk(String filename, String photoDescription);

        void onImageClick();
    }
}