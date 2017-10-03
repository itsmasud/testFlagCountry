package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.filecache.FileCacheClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionSystem;
import com.fieldnation.v2.data.client.AttachmentHelper;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.Task;

import java.io.File;

/**
 * @author shoaib.ahmed
 */
public class PhotoUploadDialog extends FullScreenDialog {
    private static final String TAG = "PhotoUploadDialog";

    // State
    private static final String STATE_NEW_FILE_NAME = "STATE_NEW_FILE_NAME";
    private static final String STATE_FILE_EXTENSION = "STATE_FILE_EXTENSION";
    private static final String STATE_DESCRIPTION = "STATE_DESCRIPTION";
    private static final String STATE_HIDE_PHOTO = "STATE_HIDE_PHOTO";
    private static final String STATE_CACHED_URI = "STATE_CACHED_URI";
    private static final String STATE_CACHED_SIZE = "STATE_CACHED_SIZE";
    private static final String STATE_SOURCE_URI = "STATE_SOURCE_URI";

    // Mode
    private static final int MODE_NORMAL = 0;
    private static final int MODE_RETRY = 1;

    // UI
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private ImageView _imageView;
    private EditText _fileNameEditText;
    private EditText _descriptionEditText;
    private ProgressBar _progressBar;
    private RelativeLayout _noPreviewLayout;
    private TextView _iconTextView;

    // Data user entered
    private String _newFileName;
    private String _description;
    private String _extension;

    // Supplied
    private String _originalFileName;
    private Bitmap _bitmap;
    private boolean _hideImageView = false;
    private int _workOrderId = 0;
    private Task _task;
    private AttachmentFolder _slot;
    private Uri _sourceUri;
    private Uri _cachedUri = null;
    private long _cacheSize = 0;
    private int _mode = MODE_NORMAL;
    private WebTransaction _webTransaction;
    private TransactionParams _transactionParams;
    private JsonObject _methodParams;
    private JsonObject _httpBuilder;


    private static int getIcon(String ext) {
        switch (ext) {
            // audio
            case ".aa":
            case ".aac":
            case ".act":
            case ".aiff":
            case ".amr":
            case ".ape":
            case ".au":
            case ".awb":
            case ".dct":
            case ".dvf":
            case ".flac":
            case ".gsm":
            case ".iklax":
            case ".ivs":
            case ".m4a":
            case ".m4b":
            case ".m4p":
            case ".mmf":
            case ".mp3":
            case ".mpc":
            case ".msv":
            case ".ogg":
            case ".oga":
            case ".mogg":
            case ".opus":
            case ".ra":
            case ".rm":
            case ".raw":
            case ".sln":
            case ".tta":
            case ".vox":
            case ".wav":
            case ".wma":
            case ".wv":
            case ".webm":
            case ".8svx":
            case ".midi":
                return R.string.icon_file_audio;

            // code
            case ".php":
            case ".xml":
            case ".c":
            case ".bat":
            case ".cpp":
            case ".cs":
            case ".java":
            case ".html":
            case ".js":
                return R.string.icon_file_code;

            // spreadsheet
            case ".xls":
            case ".ods":
            case ".wks":
            case ".wku":
            case ".wq1":
            case ".wq2":
            case ".xlr":
            case ".xlsb":
            case ".xlsm":
            case ".xlshtml":
            case ".xlsmhtml":
            case ".xlsx":
            case ".xlthtml":
            case ".xltm":
            case ".xltx":
                return R.string.icon_file_spreadsheet;

            // text
            case ".txt":
            case ".pdf":
            case ".doc":
            case ".docx":
            case ".rtf":
                return R.string.icon_file_text;

            // video
            case ".264":
            case ".3g2":
            case ".3gp2":
            case ".3gpp":
            case ".3gpp2":
            case ".aaf":
            case ".amv":
            case ".avi":
            case ".m2v":
            case ".mov":
            case ".mp4":
            case ".mpeg":
                return R.string.icon_file_video;

            // zip
            case ".zip":
            case ".rar":
            case ".7z":
            case ".arc":
            case ".jar":
                return R.string.icon_file_zip;

            default:
                return R.string.icon_doc_generic;
        }
    }

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public PhotoUploadDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_photo_upload, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_submit);

        _imageView = v.findViewById(R.id.photo_imageview);
        _fileNameEditText = v.findViewById(R.id.filename_edittext);
        _descriptionEditText = v.findViewById(R.id.description_edittext);
        _progressBar = v.findViewById(R.id.progressBar);
        _noPreviewLayout = v.findViewById(R.id.previewUnavailable_view);
        _iconTextView = v.findViewById(R.id.icon_textview);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _imageView.setOnClickListener(_preview_onClick);
        _fileNameEditText.setOnEditorActionListener(_onEditor);
        _fileNameEditText.addTextChangedListener(_fileName_textWatcher);
        _descriptionEditText.setOnEditorActionListener(_onEditor);
        _descriptionEditText.addTextChangedListener(_photoDescription_textWatcher);
        _noPreviewLayout.setOnClickListener(_preview_onClick);
        _progressBar.setOnClickListener(_preview_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        _fileCacheClient.sub();
        populateUi();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        if (payload.containsKey("webTransactionId")) {
            try {
                _mode = MODE_RETRY;
                _webTransaction = WebTransaction.get(payload.getLong("webTransactionId"));
                _transactionParams = TransactionParams.fromJson(new JsonObject(_webTransaction.getListenerParams()));
                _methodParams = new JsonObject(_transactionParams.methodParams);
                _httpBuilder = new JsonObject(_webTransaction.getRequestString());

                _originalFileName = _methodParams.getString("attachment.file.name");
                _description = _methodParams.has("attachment.notes") ? _methodParams.getString("attachment.notes") : "";
                _workOrderId = _methodParams.getInt("workOrderId");
                if (_originalFileName.contains(".")) {
                    _extension = _originalFileName.substring(_originalFileName.lastIndexOf("."));
                }

                _sourceUri = _cachedUri = StoredObject.get(App.get(), _methodParams.getLong("storedObjectId")).getUri();
                setPhoto(MemUtils.getMemoryEfficientBitmap(getContext(), _cachedUri, 400));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else {
            _mode = MODE_NORMAL;
            _originalFileName = payload.getString("fileName");
            _workOrderId = payload.getInt("workOrderId");
            if (_originalFileName.contains(".")) {
                _extension = _originalFileName.substring(_originalFileName.lastIndexOf("."));
            }

            if (payload.containsKey("task"))
                _task = payload.getParcelable("task");
            if (payload.containsKey("slot"))
                _slot = payload.getParcelable("slot");

            if (payload.containsKey("uri")) {
                _sourceUri = payload.getParcelable("uri");
                Log.v(TAG, "uri: " + _sourceUri);
                FileCacheClient.cacheFileUpload(App.get(), _sourceUri.toString(), _sourceUri);
            }
        }
        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
        if (savedState != null) {
            if (savedState.containsKey(STATE_NEW_FILE_NAME))
                _newFileName = savedState.getString(STATE_NEW_FILE_NAME);

            if (savedState.containsKey(STATE_DESCRIPTION))
                _description = savedState.getString(STATE_DESCRIPTION);

            if (savedState.containsKey(STATE_FILE_EXTENSION))
                _extension = savedState.getString(STATE_FILE_EXTENSION);

            if (savedState.containsKey(STATE_HIDE_PHOTO))
                _hideImageView = savedState.getBoolean(STATE_HIDE_PHOTO);

            if (savedState.containsKey(STATE_CACHED_URI))
                _cachedUri = savedState.getParcelable(STATE_CACHED_URI);

            if (savedState.containsKey(STATE_SOURCE_URI))
                _sourceUri = savedState.getParcelable(STATE_SOURCE_URI);

            if (savedState.containsKey(STATE_CACHED_SIZE))
                _cacheSize = savedState.getLong(STATE_CACHED_SIZE);
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

        if (_cachedUri != null)
            outState.putParcelable(STATE_CACHED_URI, _cachedUri);

        if (_sourceUri != null)
            outState.putParcelable(STATE_SOURCE_URI, _sourceUri);

        if (_cacheSize > 0)
            outState.putLong(STATE_CACHED_SIZE, _cacheSize);
    }

    @Override
    public void onPause() {
        _fileCacheClient.unsub();
        super.onPause();
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
            _noPreviewLayout.setVisibility(View.GONE);
        } else if (_hideImageView) {
            _progressBar.setVisibility(View.GONE);
            _imageView.setVisibility(View.INVISIBLE);
            _noPreviewLayout.setVisibility(View.VISIBLE);
            _iconTextView.setText(getIcon(_extension));
        } else {
            _progressBar.setVisibility(View.VISIBLE);
            _imageView.setVisibility(View.INVISIBLE);
            _noPreviewLayout.setVisibility(View.GONE);
        }

        _descriptionEditText.setText(misc.isEmptyOrNull(_description) ? "" : _description);
        _fileNameEditText.setText(misc.isEmptyOrNull(_newFileName) ? _originalFileName : _newFileName);
        _toolbar.setTitle(misc.isEmptyOrNull(_newFileName) ? _originalFileName : _newFileName);

        if (_mode == MODE_NORMAL)
            _finishMenu.setText("SUBMIT");
        else if (_mode == MODE_RETRY)
            _finishMenu.setText("RETRY");
    }

    @Override
    public void cancel() {
        _onCancelDispatcher.dispatch(getUid());
        super.cancel();
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
                _menu_onClick.onMenuItemClick(null);
            }

            return handled;
        }
    };

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.v(TAG, "_okButton_onClick");
            if (misc.isEmptyOrNull(_newFileName)) {
                _fileNameEditText.setText(_originalFileName);
                ToastClient.toast(App.get(), R.string.dialog_insert_file_name, Toast.LENGTH_LONG);
                return false;
            }

            if (!FileUtils.isValidFileName(_newFileName)) {
                ToastClient.toast(App.get(), R.string.dialog_invalid_file_name, Toast.LENGTH_LONG);
                return false;
            }

            if (_cachedUri == null) {
                ToastClient.toast(App.get(), "Please wait until the preview has loaded.", Toast.LENGTH_SHORT);
                return false;
            }

            if (_cacheSize > 100000000) {
                ToastClient.toast(App.get(), "File is over 100mb limit. Cannot upload.", Toast.LENGTH_SHORT);
                return false;
            }

            if (!misc.isEmptyOrNull(_extension) && !_newFileName.endsWith(_extension)) {
                _newFileName += _extension;
            }

            if (_mode == MODE_RETRY) {
                try {
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("AttachmentRetry")
                                    .label((misc.isEmptyOrNull(getUid()) ? TAG : getUid()) + " - task")
                                    .action("start")
                                    .build());

                    Attachment attachment = Attachment.fromJson(_methodParams.getJsonObject("attachment"));
                    attachment.notes(_description).file(new com.fieldnation.v2.data.model.File().name(_newFileName));

                    _methodParams.put("attachment", attachment.getJson());
                    _transactionParams.methodParams = _methodParams.toString();
                    _webTransaction.setListenerParams(_transactionParams.toJson().toByteArray());

                    _httpBuilder.put("multipart.fields.attachment.value", attachment.getJson());
                    _httpBuilder.put("multipart.files.file.filename", _newFileName);
                    _webTransaction.setRequest(_httpBuilder.toString());
                    _webTransaction.requeue(0);
                    WebTransactionSystem.getInstance();

                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

            } else if (_mode == MODE_NORMAL) {
                if (_task != null) {
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("AttachmentUpload")
                                    .label((misc.isEmptyOrNull(getUid()) ? TAG : getUid()) + " - task")
                                    .action("start")
                                    .build());
                    try {
                        Attachment attachment = new Attachment();
                        attachment.folderId(_task.getAttachments().getId()).notes(_description).file(new com.fieldnation.v2.data.model.File().name(_newFileName));

                        // TODO: API cant take notes with the attachment
                        AttachmentHelper.addAttachment(App.get(), _workOrderId, attachment, _newFileName, _cachedUri);
                    } catch (Exception e) {
                        Log.v(TAG, e);
                    }
                }

                if (_slot != null) {
                    Log.v(TAG, getUid() + " slot attached");
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("AttachmentUpload")
                                    .label((misc.isEmptyOrNull(getUid()) ? TAG : getUid()) + " - slot")
                                    .action("start")
                                    .build());

                    try {
                        Attachment attachment = new Attachment();
                        attachment.folderId(_slot.getId()).notes(_description).file(new com.fieldnation.v2.data.model.File().name(_newFileName));

                        AttachmentHelper.addAttachment(App.get(), _workOrderId, attachment, _newFileName, _cachedUri);
                    } catch (Exception e) {
                        Log.v(TAG, e);
                    }
                }
            }
            _onOkDispatcher.dispatch(getUid());
            dismiss(true);
            return true;
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _onCancelDispatcher.dispatch(getUid());
            dismiss(true);
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
                _finishMenu.setEnabled(false);
            } else {
                _finishMenu.setEnabled(true);
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

    private final View.OnClickListener _preview_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_cachedUri == null) {
                ToastClient.toast(App.get(), "Can't show preview yet.", Toast.LENGTH_SHORT);
                return;
            }

            Intent intent;
            intent = new Intent(Intent.ACTION_VIEW);
            File f = new File(_cachedUri.getPath());
            intent.setDataAndType(App.getUriFromFile(f), MimeTypeMap.getSingleton().getMimeTypeFromExtension(_extension.substring(1)));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                if (App.get().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    ActivityClient.startActivity(intent);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
                ToastClient.toast(App.get(), "Could not find an app to open this file", Toast.LENGTH_SHORT);
            }
        }
    };

    private final FileCacheClient _fileCacheClient = new FileCacheClient() {
        @Override
        public void onFileCacheEnd(String tag, Uri uri, long size, boolean success) {
            Log.v(TAG, "onFileCacheEnd tag: " + tag);
            Log.v(TAG, "onFileCacheEnd uri: " + uri);
            if (!tag.equals(_sourceUri.toString())) {
                Log.v(TAG, "onFileCacheEnd uri mismatch, skipping");
                return;
            }

            if (size > 100000000) {
                ToastClient.toast(App.get(), "Warning, file over 100mb limit. Too large to upload.", Toast.LENGTH_SHORT);
            }

            _cacheSize = size;
            _cachedUri = uri;
            try {
                setPhoto(MemUtils.getMemoryEfficientBitmap(getContext(), _cachedUri, 400));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    public static void show(Context context, String uid, int workOrderId, Task task, String fileName, Uri uri) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("fileName", fileName);
        params.putParcelable("uri", uri);
        params.putParcelable("task", task);

        Controller.show(context, uid, PhotoUploadDialog.class, params);
    }

    public static void show(Context context, String uid, int workOrderId, AttachmentFolder slot, String fileName, Uri uri) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("fileName", fileName);
        params.putParcelable("uri", uri);
        params.putParcelable("slot", slot);

        Controller.show(context, uid, PhotoUploadDialog.class, params);
    }

    public static void show(Context context, String uid, long webTransactionId) {
        Bundle params = new Bundle();
        params.putLong("webTransactionId", webTransactionId);

        Controller.show(context, uid, PhotoUploadDialog.class, params);
    }

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk();
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk();
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }

    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }

}