package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.TileTextView;
import com.fieldnation.v2.data.model.AttachmentFolder;

/**
 * Created by mc on 3/9/17.
 */

public class AttachmentFolderDialog extends SimpleDialog {
    private static final String TAG = "AttachmentFolderDialog";

    // Ui
    private TextView _titleTextView;
    private ListView _items;

    // Data
    private AttachmentFolder[] folders = null;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public AttachmentFolderDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_item_list, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _items = (ListView) v.findViewById(R.id.apps_listview);

        return v;
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        Parcelable[] parcels = payload.getParcelableArray("folders");
        folders = new AttachmentFolder[parcels.length];

        for (int i = 0; i < parcels.length; i++) {
            folders[i] = (AttachmentFolder) parcels[i];
        }

        _titleTextView.setText("Select Upload Slot");

        _items.setAdapter(_adapter);
    }

    private final BaseAdapter _adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if (folders == null)
                return 0;

            return folders.length;
        }

        @Override
        public Object getItem(int position) {
            return folders[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TileTextView v = new TileTextView(parent.getContext());

            v.setText(folders[position].getName());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _onFolderSelectedDispatcher.dispatch(getUid(), folders[position]);
                    dismiss(true);
                }
            });
            return v;
        }
    };

    public static void show(Context context, String uid, AttachmentFolder[] folders) {
        Bundle params = new Bundle();
        params.putParcelableArray("folders", folders);

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
        _onFolderSelectedDispatcher.add(uid, onFolderSelectedListener);
    }

    public static void removeOnFolderSelectedListener(String uid, OnFolderSelectedListener onFolderSelectedListener) {
        _onFolderSelectedDispatcher.remove(uid, onFolderSelectedListener);
    }

    public static void removeAllOnFolderSelectedListener(String uid) {
        _onFolderSelectedDispatcher.removeAll(uid);
    }
}