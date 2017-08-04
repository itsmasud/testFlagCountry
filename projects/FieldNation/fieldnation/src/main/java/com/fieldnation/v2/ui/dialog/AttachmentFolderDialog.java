package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.ui.AttachmentFoldersAdapter;

/**
 * Created by mc on 3/9/17.
 */

public class AttachmentFolderDialog extends SimpleDialog {
    private static final String TAG = "AttachmentFolderDialog";

    // Ui
    private Toolbar _toolbar;
    private RecyclerView _list;

    // Data
    private AttachmentFolders folders = null;
    private AttachmentFoldersAdapter adapter = null;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public AttachmentFolderDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_attachment_folder, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle("Attachments");

        _list = v.findViewById(R.id.list);
        _list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        return v;
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        folders = payload.getParcelable("folders");
        adapter = new AttachmentFoldersAdapter();
        adapter.setAttachments(folders);
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _list.setAdapter(adapter);
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, AttachmentFolders folders) {
        Bundle params = new Bundle();
        params.putParcelable("folders", folders);

        Controller.show(context, uid, AttachmentFolderDialog.class, params);
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