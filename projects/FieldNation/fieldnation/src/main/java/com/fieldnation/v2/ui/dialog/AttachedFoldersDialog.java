package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.ui.AttachedFoldersAdapter;

/**
 * Created by mc on 8/24/17.
 */

public class AttachedFoldersDialog extends FullScreenDialog {
    private static final String TAG = "SlotDialog";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _list;

    // Data
    private AttachmentFolders _folders = null;
    private int _worKOrderId;

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
        _worKOrderId = params.getInt("workOrderId");

        _adapter.setAttachments(_folders);
    }

    private final AttachedFoldersAdapter _adapter = new AttachedFoldersAdapter() {
        @Override
        public void onItemClick(AttachmentFolder attachmentFolder) {
            _onFolderSelectedDispatcher.dispatch(getUid(), attachmentFolder);
            dismiss(true);
        }
    };

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

    /*-*********************************-*/
    /*-         FolderSelected          -*/
    /*-*********************************-*/
    public interface OnFolderSelectedListener {
        void onFolderSelected(AttachmentFolder folder);
    }

    private static KeyedDispatcher<OnFolderSelectedListener> _onFolderSelectedDispatcher = new KeyedDispatcher<OnFolderSelectedListener>() {
        @Override
        public void onDispatch(OnFolderSelectedListener listener, Object... parameters) {
            listener.onFolderSelected((AttachmentFolder) parameters[0]);
        }
    };

    public static void addOnFolderSelectedListener(String uid, OnFolderSelectedListener onFolderSelectedListener) {
        Log.v(TAG, "addOnFolderSelectedListener");
        _onFolderSelectedDispatcher.add(uid, onFolderSelectedListener);
    }

    public static void removeOnFolderSelectedListener(String uid, OnFolderSelectedListener onFolderSelectedListener) {
        Log.v(TAG, "removeOnFolderSelectedListener");
        _onFolderSelectedDispatcher.remove(uid, onFolderSelectedListener);
    }

    public static void removeAllOnFolderSelectedListener(String uid) {
        _onFolderSelectedDispatcher.removeAll(uid);
    }
}
