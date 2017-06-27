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
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
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

    // Clients
    private PermissionsClient _permissionsClient;
    private ActivityResultClient _activityResultClient;

    public GetFileDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_get_file, container, false);
        _items = v.findViewById(R.id.apps_listview);
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

        if (savedState.containsKey("_sourceUri"))
            _sourceUri = savedState.getParcelable("_sourceUri");
        if (savedState.containsKey("_cameraIntent"))
            _cameraIntent = savedState.getParcelable("_cameraIntent");
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_sourceUri != null)
            outState.putParcelable("_sourceUri", _sourceUri);
        if (_cameraIntent != null)
            outState.putParcelable("_cameraIntent", _cameraIntent);
    }

    @Override
    public void onPause() {
        if (_activityResultClient != null) _activityResultClient.disconnect(App.get());
        super.onPause();
    }

    @Override
    public void onStop() {
        if (_permissionsClient != null) _permissionsClient.disconnect(App.get());
        super.onStop();
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
                int grant = PermissionsClient.checkSelfPermission(App.get(), Manifest.permission.CAMERA);
                File f = new File(App.get().getPicturePath() + "/IMAGE-" + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                _sourceUri = App.getUriFromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, _sourceUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                if (grant == PackageManager.PERMISSION_DENIED) {
                    _permissionsClient = new PermissionsClient(_permissionsClient_listener);
                    _permissionsClient.connect(App.get());
                    PermissionsClient.requestPermissions(App.get(), new String[]{Manifest.permission.CAMERA}, new boolean[]{false});
                    _cameraIntent = intent;
                } else {
                    ActivityResultClient.startActivityForResult(App.get(), intent, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
                }
            }
        }
    };

    public static void show(Context context, String uid, GetFileIntent[] intents) {
        Bundle params = new Bundle();
        params.putParcelableArray("intents", intents);

        Controller.show(context, uid, GetFileDialog.class, params);
    }

    private final PermissionsClient.ResponseListener _permissionsClient_listener = new PermissionsClient.ResponseListener() {
        @Override
        public PermissionsClient getClient() {
            return _permissionsClient;
        }

        @Override
        public void onComplete(String permission, int grantResult) {
            if (permission.equals(Manifest.permission.CAMERA) && _cameraIntent != null) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    ActivityResultClient.startActivityForResult(App.get(), _cameraIntent, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
                    _cameraIntent = null;
                } else {
                    ToastClient.toast(App.get(), "Camera Permission denied. Please try again.", Toast.LENGTH_SHORT);
                }
            }
        }
    };

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

                _onFileDispatcher.dispatch(getUid(), fileUris);
                dismiss(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.logException(ex);
            }

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