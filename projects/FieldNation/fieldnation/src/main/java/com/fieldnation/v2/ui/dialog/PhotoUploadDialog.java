package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.filecache.FileCacheClient;
import com.fieldnation.ui.KeyedDispatcher;

/**
 * @author shoaib.ahmed
 */
public class PhotoUploadDialog extends SimpleDialog {
    private static final String TAG = UniqueTag.makeTag("PhotoUploadDialog");

    // State
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
    private FileCacheClient _fileCacheClient;


    // Supplied
    private String _originalFileName;
    private Bitmap _bitmap;
    private boolean _hideImageView = false;
    private int _workOrderId = 0;


    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public PhotoUploadDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
        if (savedState != null) {
            if (savedState.containsKey(STATE_NEW_FILE_NAME))
                _newFileName = savedState.getString(STATE_NEW_FILE_NAME);

            if (savedState.containsKey(STATE_DESCRIPTION))
                _description = savedState.getString(STATE_DESCRIPTION);

            if (savedState.containsKey(STATE_PHOTO))
                _bitmap = savedState.getParcelable(STATE_PHOTO);

            if (savedState.containsKey(STATE_FILE_EXTENSION))
                _extension = savedState.getString(STATE_FILE_EXTENSION);

            if (savedState.containsKey(STATE_HIDE_PHOTO))
                _hideImageView = savedState.getBoolean(STATE_HIDE_PHOTO);
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (!misc.isEmptyOrNull(_newFileName))
            outState.putString(STATE_NEW_FILE_NAME, _newFileName);

        if (!misc.isEmptyOrNull(_description))
            outState.putString(STATE_DESCRIPTION, _description);

        if (!misc.isEmptyOrNull(_extension))
            outState.putString(STATE_FILE_EXTENSION, _extension);

        if (_hideImageView)
            outState.putBoolean(STATE_HIDE_PHOTO, _hideImageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_photo_upload, container, false);

        _imageView = (ImageView) v.findViewById(R.id.photo_imageview);
        _fileNameEditText = (EditText) v.findViewById(R.id.filename_edittext);
        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        return v;
    }

    @Override
    public void onStart() {
        Log.e(TAG, "onStart");
        super.onStart();
        _imageView.setOnClickListener(_photoImageView_onClick);
        _fileNameEditText.setOnEditorActionListener(_onEditor);
        _fileNameEditText.addTextChangedListener(_fileName_textWatcher);
        _descriptionEditText.setOnEditorActionListener(_onEditor);
        _descriptionEditText.addTextChangedListener(_photoDescription_textWatcher);
        _okButton.setOnClickListener(_okButton_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
        _fileCacheClient = new FileCacheClient(_fileCacheClient_listener);
        _fileCacheClient.connect(App.get());
        populateUi();

    }

    @Override
    public void onPause() {
        if (_fileCacheClient != null && _fileCacheClient.isConnected()) {
            _fileCacheClient.disconnect(App.get());
        }
        super.onPause();
    }


    @Override
    public void show(Bundle payload, boolean animate) {
        Log.e(TAG, "override show");
        super.show(payload, animate);
        _originalFileName = payload.getString("fileName");
        _workOrderId = payload.getInt("workOrderId");
        if (_originalFileName.contains(".")) {
            _extension = _originalFileName.substring(_originalFileName.lastIndexOf("."));
        }

        if (payload.containsKey("uri")) {
            FileCacheClient.cacheDeliverableUpload(App.get(), (Uri) payload.getParcelable("uri"));
        } else if (payload.containsKey("filePath")) {
            setPhoto(MemUtils.getMemoryEfficientBitmap(payload.getString("filePath"), 400));
        }
    }

    public void setPhoto(Bitmap bitmap) {
        Log.v(TAG, "setPhoto");
        _bitmap = bitmap;
        _hideImageView = _bitmap == null;

        populateUi();
    }

    private void populateUi() {
        Log.e(TAG, "populateUi");
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
                ToastClient.toast(App.get(), App.get().getString(R.string.dialog_insert_file_name), Toast.LENGTH_LONG);
                return;
            }

            if (!FileUtils.isValidFileName(_newFileName)) {
                ToastClient.toast(App.get(), App.get().getString(R.string.dialog_invalid_file_name), Toast.LENGTH_LONG);
                return;
            }

            if (!misc.isEmptyOrNull(_extension) && !_newFileName.endsWith(_extension)) {
                _newFileName += _extension;
            }
            dismiss(true);
            _onOkDispatcher.dispatch(getUid(), _workOrderId, _newFileName, _description);

        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
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
            _onImageClickDispatcher.dispatch(getUid());
        }
    };

    private final FileCacheClient.Listener _fileCacheClient_listener = new FileCacheClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDeliverableCacheEnd(Uri uri, String filename) {
            setPhoto(MemUtils.getMemoryEfficientBitmap(filename, 400));
        }
    };

    public static void show(Context context, String uid, int workOrderId, String fileName, Uri uri) {
        Log.e(TAG, "show");
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("fileName", fileName);
        params.putParcelable("uri", uri);

        Controller.show(context, uid, PhotoUploadDialog.class, params);
    }

    public static void show(Context context, String uid, int workOrderId, String fileName, String filePath) {
        Log.e(TAG, "show");
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("fileName", fileName);
        params.putString("filePath", filePath);

        Controller.show(context, uid, PhotoUploadDialog.class, params);
    }


    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(int workOrderId, String fileName, String description);
    }

    private static KeyedDispatcher<PhotoUploadDialog.OnOkListener> _onOkDispatcher = new KeyedDispatcher<PhotoUploadDialog.OnOkListener>() {
        @Override
        public void onDispatch(PhotoUploadDialog.OnOkListener listener, Object... parameters) {
            listener.onOk((Integer) parameters[0], (String) parameters[1], (String) parameters[2]);
        }
    };

    public static void addOnOkListener(String uid, PhotoUploadDialog.OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, PhotoUploadDialog.OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }


    /*-**************************-*/
    /*-         Image Click           -*/
    /*-**************************-*/
    public interface OnImageClickListener {
        void onImageClick();
    }

    private static KeyedDispatcher<PhotoUploadDialog.OnImageClickListener> _onImageClickDispatcher = new KeyedDispatcher<OnImageClickListener>() {
        @Override
        public void onDispatch(PhotoUploadDialog.OnImageClickListener listener, Object... parameters) {
            listener.onImageClick();
        }
    };

    public static void addOnImageClickListener(String uid, PhotoUploadDialog.OnImageClickListener onImageClickListener) {
        _onImageClickDispatcher.add(uid, onImageClickListener);
    }

    public static void removeOnImageClickListener(String uid, PhotoUploadDialog.OnImageClickListener onImageClickListener) {
        _onImageClickDispatcher.remove(uid, onImageClickListener);
    }

    public static void removeAllOnImageClickListener(String uid) {
        _onImageClickDispatcher.removeAll(uid);
    }


    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<PhotoUploadDialog.OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<PhotoUploadDialog.OnCancelListener>() {
        @Override
        public void onDispatch(PhotoUploadDialog.OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, PhotoUploadDialog.OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, PhotoUploadDialog.OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }
}