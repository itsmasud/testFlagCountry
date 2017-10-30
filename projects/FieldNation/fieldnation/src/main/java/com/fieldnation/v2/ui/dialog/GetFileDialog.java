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
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.trackers.DeliverableTracker;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fnactivityresult.ActivityResultListener;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fnpermissions.PermissionsResponseListener;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.filecache.FileCacheClient;
import com.fieldnation.v2.ui.GetFileIntent;
import com.fieldnation.v2.ui.GetFilePackage;
import com.fieldnation.v2.ui.GetFilePackageAdapter;
import com.fieldnation.v2.ui.GetFilePackageRowView;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GetFileDialog extends SimpleDialog {
    private static final String TAG = "GetFileDialog";

    // Ui
    private ListView _items;

    private View _loadingLayout;
    private ProgressBar _loadingProgress;
    private TextView _loadingTextView;
    private TextView _loadingBytesTextView;

    // Data
    private List<GetFilePackage> _activityList = new LinkedList<>();
    private Set<String> _packages = new HashSet<>();
    private Uri _sourceUri;
    private Intent _cameraIntent = null;

    private boolean _isCancelable = true;
    private Hashtable<String, UriIntent> caching = new Hashtable<>();
    private List<UriIntent> cached = new LinkedList<>();
    private Hashtable<String, Long> progress = new Hashtable<>();
    private String _uiUUID = null;

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
        _loadingLayout = v.findViewById(R.id.loading_layout);
        _loadingTextView = v.findViewById(R.id.loading_title);
        _loadingProgress = v.findViewById(R.id.loading_progress);
        _loadingBytesTextView = v.findViewById(R.id.loadingBytes_textview);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        _items.setAdapter(new GetFilePackageAdapter(_activityList, _app_onClick));
        _permissionsListener.sub();
        _activityResultListener.sub();
        _fileCacheClient.sub();
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

        _uiUUID = payload.getString("uiUUID");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Get File Dialog"))
                .build());

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

        if (savedState.containsKey("cached")) {
            Parcelable[] parcelables = savedState.getParcelableArray("cached");
            for (Parcelable parcelable : parcelables) {
                cached.add((UriIntent) parcelable);
            }
        }

        if (savedState.containsKey("progress")) {
            Bundle bundle = savedState.getBundle("progress");

            Set<String> keys = bundle.keySet();
            for (String key : keys) {
                progress.put(key, bundle.getLong(key));
            }
        }

        if (savedState.containsKey("caching")) {
            Bundle bundle = savedState.getBundle("caching");

            Set<String> keys = bundle.keySet();
            for (String key : keys) {
                caching.put(key, (UriIntent) bundle.getParcelable(key));
            }
        }
        _isCancelable = savedState.getBoolean("_isCancelable");

        if (!_isCancelable) {
            _items.setVisibility(View.GONE);
            _loadingLayout.setVisibility(View.VISIBLE);
            _loadingProgress.setIndeterminate(false);

            long sum = 0;
            for (Long val : progress.values()) {
                sum += val;
            }

            _loadingBytesTextView.setVisibility(View.VISIBLE);
            _loadingBytesTextView.setText(misc.humanReadableBytes(sum) + " bytes copied...");
            _loadingProgress.setProgress(cached.size());
            _loadingProgress.setMax(cached.size() + caching.size());
            _loadingTextView.setText(
                    getContext().getString(R.string.preparing_files_num,
                            cached.size(),
                            cached.size() + caching.size()));
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        Log.v(TAG, "onSaveDialogState");
        if (_sourceUri != null)
            outState.putParcelable("_sourceUri", _sourceUri);
        if (_cameraIntent != null)
            outState.putParcelable("_cameraIntent", _cameraIntent);
        if (cached.size() > 0) {
            outState.putParcelableArray("cached", cached.toArray(new Parcelable[cached.size()]));
        }
        if (progress.size() > 0) {
            Bundle bundle = new Bundle();

            Set<String> keys = progress.keySet();
            for (String key : keys) {
                bundle.putLong(key, progress.get(key));
            }
            outState.putBundle("progress", bundle);
        }

        if (caching.size() > 0) {
            Bundle bundle = new Bundle();
            Set<String> keys = caching.keySet();
            for (String key : keys) {
                bundle.putParcelable(key, caching.get(key));
            }
            outState.putBundle("caching", bundle);
        }

        outState.putBoolean("_isCancelable", _isCancelable);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Get File Dialog"))
                .build());

        _permissionsListener.unsub();
        _activityResultListener.unsub();
        _fileCacheClient.unsub();
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

            if (intent != null && intent.getComponent() != null) {
                Log.v(TAG, intent.getComponent().toString());
            }

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

    public static void show(Context context, String uid, String uiUUID) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        GetFileIntent intent1 = new GetFileIntent(intent, "Get Content");

        if (App.get().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            GetFileIntent intent2 = new GetFileIntent(intent, "Take Picture");
            GetFileDialog.show(App.get(), uid, uiUUID, new GetFileIntent[]{intent1, intent2});
        } else {
            GetFileDialog.show(App.get(), uid, uiUUID, new GetFileIntent[]{intent1});
        }
    }

    private static void show(Context context, String uid, String uiUUID, GetFileIntent[] intents) {
        Log.v(TAG, "static show");
        Bundle params = new Bundle();
        params.putParcelableArray("intents", intents);
        params.putString("uiUUID", uiUUID);

        Controller.show(context, uid, GetFileDialog.class, params);
    }

    @Override
    public boolean isCancelable() {
        return _isCancelable;
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
                    fileUris.add(new UriIntent(_uiUUID, _sourceUri));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ClipData clipData = data.getClipData();

                        if (clipData != null) {
                            int count = clipData.getItemCount();
                            Intent intent = new Intent();
                            Uri uri = null;

                            if (count == 1) {
                                _sourceUri = null;// TODO not sure this is corrects
                                fileUris.add(new UriIntent(_uiUUID, data.getData()));
                            } else {
                                for (int i = 0; i < count; ++i) {
                                    uri = clipData.getItemAt(i).getUri();
                                    fileUris.add(new UriIntent(_uiUUID, new Intent().setData(uri)));
                                }
                            }
                        } else {
                            Log.v(TAG, "Single local/ non-local file upload");
                            if (data.getData() != null) _sourceUri = data.getData();
                            if (_sourceUri != null)
                                fileUris.add(new UriIntent(_uiUUID, _sourceUri));
                        }
                    } else {
                        Log.v(TAG, "Android version is pre-4.3");
                        if (data.getData() != null) _sourceUri = data.getData();
                        if (_sourceUri != null) fileUris.add(new UriIntent(_uiUUID, _sourceUri));
                    }
                }

                Log.v(TAG, "Starting caching");


                _isCancelable = false;
                _loadingLayout.setVisibility(View.VISIBLE);
                _items.setVisibility(View.GONE);
                _loadingProgress.setIndeterminate(false);
                _loadingProgress.setMax(fileUris.size());
                _loadingProgress.setProgress(0);
                _loadingBytesTextView.setVisibility(View.GONE);
                _loadingTextView.setText(getContext().getString(R.string.preparing_files_num, 1, fileUris.size()));

                for (UriIntent ui : fileUris) {
                    if (ui.uri != null) {
                        caching.put(ui.uri.toString(), ui);
                        FileCacheClient.cacheFileUpload(ui.uuid, ui.uri.toString(), ui.uri);
                    } else if (ui.intent != null && ui.intent.getData() != null) {
                        caching.put(ui.intent.getData().toString(), ui);
                        FileCacheClient.cacheFileUpload(ui.uuid, ui.intent.getData().toString(), ui.intent.getData());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.logException(ex);
            }
            return true;
        }
    };

    public static class UriIntent implements Parcelable {
        public Uri uri = null;
        public Intent intent = null;
        public UUIDGroup uuid = null;

        private UriIntent(String parentUUID, Uri uri) {
            this.uri = uri;
            this.uuid = new UUIDGroup(parentUUID, UUID.randomUUID().toString());
        }

        private UriIntent(String parentUUID, Intent intent) {
            this.intent = intent;
            this.uuid = new UUIDGroup(parentUUID, UUID.randomUUID().toString());
        }

        private UriIntent(Bundle bundle) {
            this.uri = bundle.getParcelable("uri");
            this.intent = bundle.getParcelable("intent");
            this.uuid = bundle.getParcelable("uuid");
        }

        public static final Parcelable.Creator<UriIntent> CREATOR = new Parcelable.Creator<UriIntent>() {
            @Override
            public UriIntent createFromParcel(Parcel source) {
                try {
                    Bundle bundle = source.readBundle(getClass().getClassLoader());
                    return new UriIntent(bundle);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
                return null;
            }

            @Override
            public UriIntent[] newArray(int size) {
                return new UriIntent[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("uri", uri);
            bundle.putParcelable("intent", intent);
            bundle.putParcelable("uuid", uuid);
            dest.writeBundle(bundle);
        }
    }

    private final FileCacheClient _fileCacheClient = new FileCacheClient() {

        @Override
        public void onFileCacheProgress(UUIDGroup uuid, String tag, long size) {
            progress.put(tag, size);

            long sum = 0;
            for (Long val : progress.values()) {
                sum += val;
            }

            _loadingBytesTextView.setVisibility(View.VISIBLE);
            _loadingBytesTextView.setText(misc.humanReadableBytes(sum) + " bytes copied...");
        }

        @Override
        public void onFileCacheEnd(UUIDGroup uuid, String tag, Uri uri, long size, boolean success) {
            if (caching.containsKey(tag)) {
                cached.add(caching.remove(tag));

                _loadingProgress.setProgress(cached.size());
                _loadingProgress.setMax(cached.size() + caching.size());
                _loadingTextView.setText(
                        getContext().getString(R.string.preparing_files_num,
                                cached.size(),
                                cached.size() + caching.size()));

                progress.put(tag, size);

                long sum = 0;
                for (Long val : progress.values()) {
                    sum += val;
                }

                _loadingBytesTextView.setVisibility(View.VISIBLE);
                _loadingBytesTextView.setText(misc.humanReadableBytes(sum) + " bytes copied...");

                if (caching.size() == 0 && cached.size() > 0) {
                    _isCancelable = true;
                    _onFileDispatcher.dispatch(getUid(), cached);
                    dismiss(false);
                }
            }
        }
    };

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