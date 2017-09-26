package com.fieldnation.v2.ui.dialog;

import android.Manifest;
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
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fnactivityresult.ActivityResultListener;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fnpermissions.PermissionsResponseListener;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.filecache.FileCacheClient;
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
    private Uri _sourceUri;
    private Intent _cameraIntent = null;

    public GetFileDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_get_file, container, false);
        _items = v.findViewById(R.id.apps_listview);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        _items.setAdapter(new GetFilePackageAdapter(_activityList, _app_onClick));
        _permissionsListener.sub();
        _activityResultListener.sub();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        Log.v(TAG, "show");
        Parcelable[] intents = payload.getParcelableArray("intents");
        if (intents != null)
            for (Parcelable parcelable : intents) {
                addIntent((GetFileIntent) parcelable);
            }

        super.show(payload, animate);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        super.onRestoreDialogState(savedState);
        if (savedState.containsKey("_sourceUri"))
            _sourceUri = savedState.getParcelable("_sourceUri");
        if (savedState.containsKey("_cameraIntent"))
            _cameraIntent = savedState.getParcelable("_cameraIntent");
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");
        if (_sourceUri != null)
            outState.putParcelable("_sourceUri", _sourceUri);
        if (_cameraIntent != null)
            outState.putParcelable("_cameraIntent", _cameraIntent);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop");
        _permissionsListener.unsub();
        _activityResultListener.unsub();
        super.onStop();
    }

    @Override
    public void dismiss(boolean animate) {
        Log.v(TAG, "dismiss");
        super.dismiss(animate);
    }

    private void addIntent(GetFileIntent appIntent) {
        PackageManager pm = App.get().getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(appIntent.getIntent(), 0);

        for (int i = 0; i < infos.size(); i++) {
            ResolveInfo info = infos.get(i);

            try {
                if (!_packages.contains(info.activityInfo.packageName + "." + info.activityInfo.name)
                        && info.activityInfo.exported) {
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
                ActivityClient.startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
            } else {
                int grant = PermissionsClient.checkSelfPermission(App.get(), Manifest.permission.CAMERA);
                File f = new File(App.get().getPicturePath() + "/IMAGE-" + misc.longToHex(System.currentTimeMillis(), 8) + ".jpg");
                Log.v(TAG, "GetFileDialog " + f.toString());
                _sourceUri = App.getUriFromFile(f);
                Log.v(TAG, "GetFileDialog " + _sourceUri.toString());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, _sourceUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                List<ResolveInfo> resolveInfos = getContext().getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo resolveInfo : resolveInfos) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getContext().grantUriPermission(packageName, _sourceUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                if (grant == PackageManager.PERMISSION_DENIED) {
                    _cameraIntent = intent;
                    PermissionsClient.requestPermissions(new String[]{Manifest.permission.CAMERA}, new boolean[]{false});
                } else {
                    ActivityClient.startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
                }
            }
        }
    };

    public static void show(Context context, String uid, GetFileIntent[] intents) {
        Log.v(TAG, "static show");
        Bundle params = new Bundle();
        params.putParcelableArray("intents", intents);

        Controller.show(context, uid, GetFileDialog.class, params);
    }

    private final PermissionsResponseListener _permissionsListener = new PermissionsResponseListener() {
        @Override
        public void onComplete(String permission, int grantResult) {
            Log.v(TAG, "PermissionsResponseListener.onComplete");
            if (permission.equals(Manifest.permission.CAMERA) && _cameraIntent != null) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    ActivityClient.startActivityForResult(_cameraIntent, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
                    _cameraIntent = null;
                } else {
                    ToastClient.toast(App.get(), "Camera Permission denied. Please try again.", Toast.LENGTH_SHORT);
                }
            }
        }
    };

    private final ActivityResultListener _activityResultListener = new ActivityResultListener() {

        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.v(TAG, "_activityResultClient_listener.onActivityResult() resultCode= " + resultCode);
            Log.v(TAG, "onActivityResult() resultCode= " + resultCode);
            Log.v(TAG, "onActivityResult() requestCode= " + requestCode);

            if ((requestCode != ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES
                    && requestCode != ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES)
                    || resultCode != Activity.RESULT_OK) {
                return false;
            }

            try {
                List<UriIntent> fileUris = new LinkedList<>();

                if (data == null) {
                    Log.e(TAG, "uploading an image using camera");
                    fileUris.add(new UriIntent(_sourceUri));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ClipData clipData = data.getClipData();

                        if (clipData != null) {
                            int count = clipData.getItemCount();
                            Intent intent = new Intent();
                            Uri uri = null;

                            if (count == 1) {
                                _sourceUri = null;// TODO not sure this is corrects
                                fileUris.add(new UriIntent(data.getData()));
                            } else {
                                for (int i = 0; i < count; ++i) {
                                    uri = clipData.getItemAt(i).getUri();
                                    fileUris.add(new UriIntent(new Intent().setData(uri)));
                                }
                            }
                        } else {
                            Log.v(TAG, "Single local/ non-local file upload");
                            if (data.getData() != null) _sourceUri = data.getData();
                            if (_sourceUri != null) fileUris.add(new UriIntent(_sourceUri));
                        }
                    } else {
                        Log.v(TAG, "Android version is pre-4.3");
                        if (data.getData() != null) _sourceUri = data.getData();
                        if (_sourceUri != null) fileUris.add(new UriIntent(_sourceUri));
                    }
                }

                Log.v(TAG, "Dispatch _onFileDispatcher");
                for (UriIntent ui : fileUris) {
                    if (ui.uri != null) {
                        FileCacheClient.cacheFileUpload(App.get(), "", ui.uri);
                    } else if (ui.intent != null && ui.intent.getData() != null) {
                        FileCacheClient.cacheFileUpload(App.get(), "", ui.intent.getData());
                    }
                }

                _onFileDispatcher.dispatch(getUid(), fileUris);
                dismiss(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.logException(ex);
            }
            return true;
        }
    };

    public static class UriIntent {
        public Uri uri = null;
        public Intent intent = null;

        private UriIntent(Uri uri) {
            this.uri = uri;
        }

        private UriIntent(Intent intent) {
            this.intent = intent;
        }
    }

    /*-***********************************/
    /*-         File Listener           -*/
    /*-***********************************/
    public interface OnFileListener {
        void onFile(List<UriIntent> fileResult);
    }

    private static KeyedDispatcher<OnFileListener> _onFileDispatcher = new KeyedDispatcher<OnFileListener>() {
        @Override
        public void onDispatch(OnFileListener listener, Object... parameters) {
            listener.onFile((List<UriIntent>) parameters[0]);
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