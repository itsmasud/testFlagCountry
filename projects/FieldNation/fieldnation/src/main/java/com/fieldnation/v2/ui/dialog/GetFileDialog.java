package com.fieldnation.v2.ui.dialog;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.v2.ui.GetFileIntent;
import com.fieldnation.v2.ui.GetFilePackage;
import com.fieldnation.v2.ui.GetFilePackageAdapter;
import com.fieldnation.v2.ui.GetFilePackageRowView;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GetFileDialog extends SimpleDialog {
    private static final String TAG = "GetFileDialog";

    // Ui
    private ListView _items;

    // Data
    private List<GetFilePackage> _activityList = new LinkedList<>();
    private Set<String> _packages = new HashSet<>();
    private ActivityResultClient _activityResultClient;
    private File _tempFile;
    private Uri _tempUri;

    public GetFileDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_get_file, container, false);
        _items = (ListView) v.findViewById(R.id.apps_listview);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _items.setAdapter(new GetFilePackageAdapter(_activityList, _app_onClick));
    }

    @Override
    public void onResume() {
        _activityResultClient = new ActivityResultClient(_activityResultClient_onListener);
        _activityResultClient.connect(App.get());

        super.onResume();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        Parcelable[] intents = payload.getParcelableArray("intents");
        if (intents != null)
            for (Parcelable parcelable : intents) {
                addIntent((GetFileIntent) parcelable);
            }


        super.show(payload, animate);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);

        if (savedState.containsKey("_tempFile"))
            _tempFile = new File(savedState.getString("_tempFile"));

        if (savedState.containsKey("_tempUri"))
            _tempUri = Uri.parse(savedState.getString("_tempUri"));
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_tempFile != null)
            outState.putString("_tempFile", _tempFile.getAbsolutePath());
        if (_tempUri != null)
            outState.putString("_tempUri", _tempUri.toString());
    }

    @Override
    public void onPause() {
        if (_activityResultClient != null) _activityResultClient.disconnect(App.get());

        super.onPause();
    }

    private void addIntent(GetFileIntent appIntent) {
        PackageManager pm = App.get().getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(appIntent.getIntent(), 0);

        for (int i = 0; i < infos.size(); i++) {
            ResolveInfo info = infos.get(i);

            try {
                if (!_packages.contains(info.activityInfo.packageName + "." + info.activityInfo.name)) {
                    GetFilePackage data = new GetFilePackage();

                    data.postfix = appIntent.getPostfix();
                    data.intent = appIntent.getIntent();
                    data.resolveInfo = info;
                    data.appName = pm.getApplicationLabel(info.activityInfo.applicationInfo).toString();
                    data.icon = info.loadIcon(pm);

                    _activityList.add(data);
                    _packages.add(info.activityInfo.packageName + "." + info.activityInfo.name);
                }
            } catch (Exception ex) {
                Log.v(TAG, info.toString());
                Log.v(TAG, ex);
            }
        }
    }

    private final GetFilePackageRowView.OnClickListener _app_onClick = new GetFilePackageRowView.OnClickListener() {
        @Override
        public void onClick(GetFilePackageRowView row, Intent intent) {
            Log.v(TAG, "AppPickerRowView.OnClickListener _app_onClick");

            if (intent.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                Log.v(TAG, "onClick: " + intent.toString());
                ActivityResultClient.startActivityForResult(App.get(), intent, ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
            } else {
                _tempFile = new File(App.get().getTempFolder() + "/IMAGE-" + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(_tempFile));
                ActivityResultClient.startActivityForResult(App.get(), intent, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
            }
        }
    };

    public static void show(Context context, String uid, GetFileIntent[] intents) {
        Bundle params = new Bundle();
        params.putParcelableArray("intents", intents);

        Controller.show(context, uid, GetFileDialog.class, params);
    }

    private final ActivityResultClient.Listener _activityResultClient_onListener = new ActivityResultClient.ResultListener() {
        @Override
        public void onConnected() {
            _activityResultClient.subOnActivityResult();
        }

        @Override
        public ActivityResultClient getClient() {
            return _activityResultClient;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.v(TAG, "_activityResultClient_listener.onActivityResult() resultCode= " + resultCode);
            Log.v(TAG, "onActivityResult() resultCode= " + resultCode);
            Log.v(TAG, "onActivityResult() requestCode= " + requestCode);

            if ((requestCode != ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES
                    && requestCode != ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES)
                    || resultCode != Activity.RESULT_OK) {
                return;
            }

            try {
                List<FileUriIntent> fileUris = new LinkedList<>();

                if (data == null) {
                    Log.e(TAG, "uploading an image using camera");
                    _tempUri = null;
                    fileUris.add(new FileUriIntent(_tempFile));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ClipData clipData = data.getClipData();

                        if (clipData != null) {
                            int count = clipData.getItemCount();
                            Intent intent = new Intent();
                            Uri uri = null;

                            if (count == 1) {
                                _tempUri = data.getData();
                                _tempFile = null;
                                fileUris.add(new FileUriIntent(data.getData()));
                            } else {
                                for (int i = 0; i < count; ++i) {
                                    uri = clipData.getItemAt(i).getUri();
                                    fileUris.add(new FileUriIntent(new Intent().setData(uri)));
                                }
                            }
                        } else {
                            Log.v(TAG, "Single local/ non-local file upload");
                            _tempUri = data.getData();
                            if (_tempUri != null) {
                                fileUris.add(new FileUriIntent(_tempUri));
                            } else if (_tempFile != null) {
                                fileUris.add(new FileUriIntent(_tempFile));
                            }
                        }
                    } else {
                        Log.v(TAG, "Android version is pre-4.3");
                        _tempUri = data.getData();
                        if (_tempUri != null) {
                            fileUris.add(new FileUriIntent(_tempUri));
                        } else if (_tempFile != null) {
                            fileUris.add(new FileUriIntent(_tempFile));
                        }
                    }
                }

                _onFileDispatcher.dispatch(getUid(), fileUris);
                dismiss(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.logException(ex);
            }

        }
    };

    public static class FileUriIntent {
        public Uri uri = null;
        public File file = null;
        public Intent intent = null;

        private FileUriIntent(File file) {
            this.file = file;
        }

        private FileUriIntent(Uri uri) {
            this.uri = uri;
        }

        private FileUriIntent(Intent intent) {
            this.intent = intent;
        }
    }

    /*-***********************************/
    /*-         File Listener           -*/
    /*-***********************************/
    public interface OnFileListener {
        void onFile(List<FileUriIntent> fileResult);
    }

    private static KeyedDispatcher<OnFileListener> _onFileDispatcher = new KeyedDispatcher<OnFileListener>() {
        @Override
        public void onDispatch(OnFileListener listener, Object... parameters) {
            listener.onFile((List<FileUriIntent>) parameters[0]);
        }
    };

    public static void addOnFileListener(String uid, OnFileListener onFileListener) {
        _onFileDispatcher.add(uid, onFileListener);
    }

    public static void removeOnFileListener(String uid, OnFileListener onFileListener) {
        _onFileDispatcher.remove(uid, onFileListener);
    }

    public static void removeAllOnFileListener(String uid) {
        _onFileDispatcher.removeAll(uid);
    }

}