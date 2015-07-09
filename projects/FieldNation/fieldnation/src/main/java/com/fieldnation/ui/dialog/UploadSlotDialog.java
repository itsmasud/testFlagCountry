package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.ui.TileTextView;

/**
 * Created by Michael Carver on 7/9/2015.
 */
public class UploadSlotDialog extends ListDialog {
    private static String TAG = "UploadSlotDialog";

    // Data
    private UploadSlot[] _uploadSlots;
    private Listener _listener;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static UploadSlotDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, UploadSlotDialog.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(_uploadSlot_adapter);
        return v;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setUploadSlots(UploadSlot[] uploadSlots) {
        _uploadSlots = uploadSlots;
        _uploadSlot_adapter.notifyDataSetChanged();
    }

    private final BaseAdapter _uploadSlot_adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if (_uploadSlots == null)
                return 0;

            return _uploadSlots.length;
        }

        @Override
        public Object getItem(int position) {
            return _uploadSlots[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TileTextView v = null;

            if (convertView != null && convertView instanceof TileTextView) {
                v = (TileTextView) convertView;
            } else {
                v = new TileTextView(parent.getContext());
            }
            v.setText(_uploadSlots[position].getSlotName());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_listener != null) {
                        _listener.onItemClick(position);
                    }
                }
            });
            return v;
        }
    };

    @Override
    public void show() {
        setTitle("Select Upload Slot");
        super.show();
    }

    public interface Listener {
        void onItemClick(int position);
    }
}
