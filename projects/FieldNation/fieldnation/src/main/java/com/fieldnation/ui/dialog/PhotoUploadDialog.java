package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.FileUtils;
import com.fieldnation.utils.misc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author shoaib.ahmed
 */
public class PhotoUploadDialog extends DialogFragmentBase {
    private static final String TAG = "PhotoUploadDialog";

    // State
    private static final String STATE_TASKID = "STATE_TASKID";
    private static final String STATE_FILE_PATH = "STATE_FILE_PATH";
    private static final String STATE_ORIGINAL_FILE_NAME = "STATE_ORIGINAL_FILE_NAME";
    private static final String STATE_NEW_FILE_NAME = "STATE_NEW_FILE_NAME";
    private static final String STATE_PHOTO_DESCRIPTION = "STATE_PHOTO_DESCRIPTION";
    private static final String STATE_INTENT = "STATE_INTENT";


    // UI
    private ImageView _photoImageView;
    private EditText _fileNameEditText;
    private EditText _photoDescriptionEditText;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private String _title;
    private long _taskId = 0;
    private boolean _clear = false;
    private String _originalFileName;
    private File _file;
    private String _filePath;
    private Intent _data;
    private String _newFileName;
    private Bitmap _bitmap;
    private String _photoDescription;


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
            if (savedInstanceState.containsKey(STATE_TASKID))
                _taskId = savedInstanceState.getLong(STATE_TASKID);

            if (savedInstanceState.containsKey(STATE_FILE_PATH))
                _filePath = savedInstanceState.getString(STATE_FILE_PATH);

            if (savedInstanceState.containsKey(STATE_ORIGINAL_FILE_NAME))
                _originalFileName = savedInstanceState.getString(STATE_ORIGINAL_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_NEW_FILE_NAME))
                _newFileName = savedInstanceState.getString(STATE_NEW_FILE_NAME);

            if (savedInstanceState.containsKey(STATE_PHOTO_DESCRIPTION))
                _photoDescription = savedInstanceState.getString(STATE_PHOTO_DESCRIPTION);

            if (savedInstanceState.containsKey(STATE_INTENT))
                _data = savedInstanceState.getParcelable(STATE_INTENT);

        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Log.v(TAG, "onSaveInstanceState");
        if (_taskId != 0)
            outState.putLong(STATE_TASKID, _taskId);

        if (!misc.isEmptyOrNull(_originalFileName))
            outState.putString(STATE_ORIGINAL_FILE_NAME, _originalFileName);

        if (!misc.isEmptyOrNull(_newFileName))
            outState.putString(STATE_NEW_FILE_NAME, _newFileName);

        if (!misc.isEmptyOrNull(_photoDescription))
            outState.putString(STATE_PHOTO_DESCRIPTION, _photoDescription);

        if (_data != null)
            outState.putParcelable(STATE_INTENT, _data);

        if (_filePath != null)
            outState.putString(STATE_FILE_PATH, _filePath);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_photo_upload, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

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

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
//        Log.v(TAG, "onResume");

        if (_clear) {
            _clear = false;
            _photoImageView.setImageDrawable(null);
            _fileNameEditText.setText("");
            _photoDescriptionEditText.setText("");
        } else {
            if (_data != null) {
                setData(_data);
                return;
            } else if (!misc.isEmptyOrNull(_filePath)) {
                _file = new File(_filePath);
                setData(_file);
                return;
            }
        }

        populateUi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.v(TAG, "onDestroy");
        _clear = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        Log.v(TAG, "onDismiss");
        _clear = true;
    }

    @Override
    public void dismiss() {
//        Log.v(TAG, "dismiss");
        _clear = true;
        super.dismiss();
    }


    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setData(Intent data) {
        _file = null;
        _data = data;

        populateFileInfo(data.getData());
        populateUi();
    }

    public void setData(File file) {
        _data = null;
        _file = file;
        _filePath = file.getAbsolutePath();
        _originalFileName = file.getName();

        populateUi();
    }

    public void show(CharSequence title, long taskId) {
        Log.e(TAG, "show");
        _title = (String) title;
        _taskId = taskId;
        _clear = true;
        show();
    }

    private void populateUi() {
        if (_taskId == 0)
            return;

        if (_photoImageView == null)
            return;

        if (_file != null) {
            Log.e(TAG, "populateUi _file");
            _photoImageView.setImageBitmap(BitmapFactory.decodeFile(_file.getAbsolutePath()));
        } else if (_data != null && _data.getData().getAuthority() != null) {
            _photoImageView.setImageBitmap(_bitmap);
        }

        _photoDescriptionEditText.setText(misc.isEmptyOrNull(_photoDescription) ? "" : _photoDescription);
        _fileNameEditText.setText(misc.isEmptyOrNull(_newFileName) ? _originalFileName : _newFileName);
    }

    private void populateFileInfo(Uri uri) {

        _originalFileName = FileUtils.getFileNameFromUri(App.get(), uri);

        _filePath = FileUtils.getFilePathFromUri(App.get(), uri);

        if (misc.isEmptyOrNull(_filePath)) {
            InputStream is = null;
            try {
                is = App.get().getContentResolver().openInputStream(uri);
                _bitmap = BitmapFactory.decodeStream(is);
                // TODO tried to use FileUtils.writeStream to created file but it didn't work
                createBitmapInTemp(_bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            _file = new File(_filePath);
        }

    }

    private void createBitmapInTemp(final Bitmap bitmap) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.v(TAG, "inside handler");

                _file = new File(App.get().getTempFolder() + File.separator + _originalFileName);
                OutputStream os = null;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(_file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
                    os.close();
//                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
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

    private File renameFileAndSaveToTempFolder(String newFileName) {
        File newFile = new File(FileUtils.getFileAbsolutePathWithOriginalExtension(App.get().getTempFolder(), newFileName, _originalFileName));

        if (newFile.exists())
            return newFile;

        try {
            FileUtils.copyFile(_file, newFile);
            return newFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

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

            if (_file != null) {
                File newFileName = renameFileAndSaveToTempFolder(_fileNameEditText.getText().toString().trim());
                _listener.onOk(_taskId, newFileName, _photoDescription);
            }

            if (_data == null) {
                File newFileName = renameFileAndSaveToTempFolder(_fileNameEditText.getText().toString().trim());
                _listener.onOk(_taskId, newFileName, _photoDescription);
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
            Intent intent;
            if (_data == null) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(_file), "image/*");

            } else {
                intent = new Intent(Intent.ACTION_VIEW, _data.getData());
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                if (App.get().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    App.get().startActivity(intent);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
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
        void onOk(long taskId, File file, String photoDescription);

        void onOk(long taskId, Intent data, String photoDescription);

        void onCancel();
    }

}