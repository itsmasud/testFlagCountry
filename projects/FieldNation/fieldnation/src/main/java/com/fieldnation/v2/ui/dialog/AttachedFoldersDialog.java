package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.AttachmentHelper;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.ui.AttachedFoldersAdapter;
import com.fieldnation.v2.ui.GetFileIntent;

import java.util.List;

/**
 * Created by mc on 8/24/17.
 */

public class AttachedFoldersDialog extends FullScreenDialog {
    private static final String TAG = "SlotDialog";

    // Dialog Tags
    private static final String DIALOG_GET_FILE = TAG + ".getFileDialog";
    private static final String DIALOG_PHOTO_UPLOAD = TAG + ".photoUploadDialog";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _list;

    // Data
    private AttachmentFolders _folders = null;
    private int _workOrderId;
    private AttachmentFolder _currentFolder;

    /*-*********----------**********-*/
    /*-         Life Cycle          -*/
    /*-*********----------**********-*/
    public AttachedFoldersDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_toolbar_recycle, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle("Attachment Folders");

        _list = v.findViewById(R.id.list);
        _list.setItemAnimator(new DefaultItemAnimator());
        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _list.setAdapter(_adapter);
        GetFileDialog.addOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        PhotoUploadDialog.addOnOkListener(DIALOG_PHOTO_UPLOAD, _photoUpload_onOk);
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "show");
        super.show(params, animate);
        _folders = params.getParcelable("folders");
        _workOrderId = params.getInt("workOrderId");

        _adapter.setAttachments(_folders);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey("currentFolder"))
            _currentFolder = savedState.getParcelable("currentFolder");
        super.onRestoreDialogState(savedState);
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_currentFolder != null)
            outState.putParcelable("currentFolder", _currentFolder);
        super.onSaveDialogState(outState);
    }

    @Override
    public void onStop() {
        GetFileDialog.removeOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        PhotoUploadDialog.removeOnOkListener(DIALOG_PHOTO_UPLOAD, _photoUpload_onOk);
        super.onStop();
    }

    private final AttachedFoldersAdapter _adapter = new AttachedFoldersAdapter() {
        @Override
        public void onItemClick(AttachmentFolder attachmentFolder) {
            _currentFolder = attachmentFolder;
            startAppPickerDialog();
        }
    };

    private void startAppPickerDialog() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        GetFileIntent intent1 = new GetFileIntent(intent, "Get Content");

        if (getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            GetFileIntent intent2 = new GetFileIntent(intent, "Take Picture");
            GetFileDialog.show(getContext(), DIALOG_GET_FILE, new GetFileIntent[]{intent1, intent2});
        } else {
            GetFileDialog.show(getContext(), DIALOG_GET_FILE, new GetFileIntent[]{intent1});
        }
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, int workOrderId, AttachmentFolders folders) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("folders", folders);

        Controller.show(context, uid, AttachedFoldersDialog.class, params);
    }

    private final GetFileDialog.OnFileListener _getFile_onFile = new GetFileDialog.OnFileListener() {
        @Override
        public void onFile(List<GetFileDialog.UriIntent> fileResult) {
            Log.v(TAG, "onFile");
            if (fileResult.size() == 0)
                return;

            if (fileResult.size() == 1) {
                GetFileDialog.UriIntent fui = fileResult.get(0);
                if (fui.uri != null) {
                    PhotoUploadDialog.show(App.get(), DIALOG_PHOTO_UPLOAD, _workOrderId, _currentFolder, FileUtils.getFileNameFromUri(App.get(), fui.uri), fui.uri);
                } else {
                    // TODO show a toast?
                }
                return;
            }

            for (GetFileDialog.UriIntent fui : fileResult) {
                try {
                    Attachment attachment = new Attachment();
                    attachment.folderId(_currentFolder.getId());
                    AttachmentHelper.addAttachment(App.get(), _workOrderId, attachment, fui.intent);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
            _currentFolder = null;
            dismiss(true);
            AttachedFilesDialog.show(App.get(), null, _workOrderId, _folders);
        }
    };

    private final PhotoUploadDialog.OnOkListener _photoUpload_onOk = new PhotoUploadDialog.OnOkListener() {
        @Override
        public void onOk() {
            dismiss(false);
            AttachedFilesDialog.show(App.get(), null, _workOrderId, _folders);
        }
    };
}
