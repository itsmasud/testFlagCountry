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
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.v2.data.client.AttachmentService;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.ui.AppPickerAdapter;
import com.fieldnation.v2.ui.AppPickerIntent;
import com.fieldnation.v2.ui.AppPickerPackage;
import com.fieldnation.v2.ui.AppPickerRowView;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AppPickerDialog extends SimpleDialog {
    private static final String TAG = "AppPickerDialog";
    private static int INVALID_NUMBER = -1;

    // Dialog tags
    private static final String DIALOG_PHOTO_UPLOAD = TAG + ".photoUploadDialog";


    // Ui
    private ListView _items;
    private View _root;

    // Data
    private List<AppPickerPackage> _activityList = new LinkedList<>();
    private Set<String> _packages = new HashSet<>();
    private ActivityResultClient _activityResultClient;
    private File _tempFile;
    private Uri _tempUri;
    private int _workOrderId = INVALID_NUMBER;
    private Task _task;
    private AttachmentFolder _slot;

    public AppPickerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _root = inflater.inflate(R.layout.dialog_v2_app_picker, container, false);
        _items = (ListView) _root.findViewById(R.id.apps_listview);
        return _root;
    }

    @Override
    public void onStart() {
        super.onStart();
        _items.setAdapter(new AppPickerAdapter(_activityList, _app_onClick));
    }

    @Override
    public void onResume() {
        super.onResume();
        _activityResultClient = new ActivityResultClient(_activityResultClient_onListener);
        _activityResultClient.connect(App.get());
    }

    @Override
    public void onPause() {
        if (_activityResultClient != null) {
            _activityResultClient.disconnect(App.get());
        }
        super.onPause();
    }


    @Override
    public void show(Bundle payload, boolean animate) {
        Parcelable[] intents = payload.getParcelableArray("AppPickerIntents");
        if (intents != null)
            for (Parcelable parcelable : intents) {
                addIntent((AppPickerIntent) parcelable);
            }

        _workOrderId = payload.getInt("workOrderId", INVALID_NUMBER);
        if (payload.containsKey("task"))
            _task = payload.getParcelable("task");

        if (payload.containsKey("slot"))
            _slot = payload.getParcelable("slot");

        super.show(payload, animate);
    }

    private void addIntent(AppPickerIntent appIntent) {
        PackageManager pm = App.get().getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(appIntent.getIntent(), 0);

        for (int i = 0; i < infos.size(); i++) {
            ResolveInfo info = infos.get(i);

            try {
                if (!_packages.contains(info.activityInfo.packageName + "." + info.activityInfo.name)) {
                    AppPickerPackage data = new AppPickerPackage();

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

    private final AppPickerRowView.OnClickListener _app_onClick = new AppPickerRowView.OnClickListener() {
        @Override
        public void onClick(AppPickerRowView row, Intent intent) {

            if (_workOrderId == -1) {
                _onOkDispatcher.dispatch(getUid(), intent);
                dismiss(true);
            }

            if (intent.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                Log.v(TAG, "onClick: " + intent.toString());
                ActivityResultClient.startActivityForResult(App.get(), intent, ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES);
            } else {
                File temppath = new File(App.get().getTempFolder() + "/IMAGE-"
                        + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                _tempFile = temppath;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
                ActivityResultClient.startActivityForResult(App.get(), intent, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES);
            }
//            setLoading(true);
//            dismiss(true);
        }
    };

    public static void show(Context context, String uid, AppPickerIntent[] intents) {
        Bundle params = new Bundle();
        params.putParcelableArray("AppPickerIntents", intents);

        Controller.show(context, uid, AppPickerDialog.class, params);
    }

    public static void show(Context context, String uid, AppPickerIntent[] intents, int workOrderId, Task task) {
        Bundle params = new Bundle();
        params.putParcelableArray("AppPickerIntents", intents);
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("task", task);

        Controller.show(context, uid, AppPickerDialog.class, params);
    }

    public static void show(Context context, String uid, AppPickerIntent[] intents, int workOrderId, AttachmentFolder slot) {
        Bundle params = new Bundle();
        params.putParcelableArray("AppPickerIntents", intents);
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("slot", slot);

        Controller.show(context, uid, AppPickerDialog.class, params);
    }

    public static void dismiss(Context context) {
        Controller.dismiss(context, null);
    }


    private final ActivityResultClient.Listener _activityResultClient_onListener = new ActivityResultClient.ResultListener() {
        @Override
        public void onConnected() {
            _activityResultClient.subOnActivityResult();
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(TAG, topicId);
            super.onEvent(topicId, payload);
        }

        @Override
        public ActivityResultClient getClient() {
            return _activityResultClient;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            _activityResultClient.clearOnActivityResult();
            Log.v(TAG, "onActivityResult");

            try {
                Log.v(TAG, "onActivityResult() resultCode= " + resultCode);
                Log.v(TAG, "onActivityResult() requestCode= " + requestCode);

                if ((requestCode == ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_DELIVERABLES
                        || requestCode == ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_DELIVERABLES)
                        && resultCode == Activity.RESULT_OK) {
//                    setLoading(true);

                    if (data == null) {
                        Log.e(TAG, "uploading an image using camera");
                        _tempUri = null;
                        if (_slot == null)
                            PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _task, _tempFile.getName(), _tempFile.toString());
                        else
                            PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _slot, _tempFile.getName(), _tempFile.toString());
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
                                    if (_slot == null)
                                        PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _task, FileUtils.getFileNameFromUri(App.get(), data.getData()), data.getData());
                                    else
                                        PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _slot, FileUtils.getFileNameFromUri(App.get(), data.getData()), data.getData());
                                } else {
                                    for (int i = 0; i < count; ++i) {
                                        uri = clipData.getItemAt(i).getUri();
                                        if (uri != null) {
                                            Attachment attachment = new Attachment();
                                            if (_slot == null) {
                                                attachment.folderId(_task.getAttachments().getId());
                                                AttachmentService.addAttachment(App.get(), _workOrderId, attachment, intent.setData(uri));
                                            } else {
                                                attachment.folderId(_slot.getId());
                                                AttachmentService.addAttachment(App.get(), _workOrderId, attachment, intent.setData(uri));
                                            }
                                        }
                                    }
                                }
                            } else {
                                Log.v(TAG, "Single local/ non-local file upload");
                                _tempUri = data.getData();
                                _tempFile = null;
                                if (_slot == null)
                                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _task, FileUtils.getFileNameFromUri(App.get(), data.getData()), _tempUri);
                                else
                                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _slot, FileUtils.getFileNameFromUri(App.get(), data.getData()), _tempUri);

                            }
                        } else {
                            Log.v(TAG, "Android version is pre-4.3");
                            _tempUri = data.getData();
                            _tempFile = null;
                            if (_slot == null)
                                PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _task, FileUtils.getFileNameFromUri(App.get(), data.getData()), _tempUri);
                            else
                                PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _slot, FileUtils.getFileNameFromUri(App.get(), data.getData()), _tempUri);
                        }
                    }

                    dismiss(true);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.logException(ex);
            }
        }
    };


    /*-*********************************/
    /*-         Ok Listener           -*/
    /*-*********************************/
    public interface OnOkListener {
        void onOk(Intent pack);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((Intent) parameters[0]);
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

}