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
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.v2.ui.KeyValuePairView;

/**
 * Created by mc on 7/12/17.
 */

public class ExpireDialog extends FullScreenDialog {
    private static final String TAG = "ExpireDialog";

    // Ui
    private Toolbar _toolbar;
    private RecyclerView _listView;

    // Data
    private String[] _titles;
    private int[] _values;

    public ExpireDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_expire, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle("Select Expiration Time");

        _listView = v.findViewById(R.id.list);
        _listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        _titles = context.getResources().getStringArray(R.array.co_expire_duration_titles);
        _values = context.getResources().getIntArray(R.array.co_expire_duration_values);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private static class VH extends RecyclerView.ViewHolder {
        public VH(View itemView) {
            super(itemView);
        }

        public KeyValuePairView getView() {
            return (KeyValuePairView) itemView;
        }
    }

    private final RecyclerView.Adapter<VH> adapter = new RecyclerView.Adapter<VH>() {
        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            KeyValuePairView kvp = new KeyValuePairView(parent.getContext());
            kvp.setOnClickListener(_item_onClick);
            return new VH(kvp);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.getView().set(_titles[position], "");
            holder.getView().setTag(position);
        }

        @Override
        public int getItemCount() {
            return _titles.length;
        }
    };

    /*-*************************-*/
    /*-         Events          -*/
    /*-*************************-*/
    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    private final View.OnClickListener _item_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int index = (Integer) view.getTag();
            _onOkDispatcher.dispatch(getUid(), _titles[index], _values[index] * 1000);
            dismiss(true);
        }
    };


    public static void show(Context context, String uid) {
        Controller.show(context, uid, ExpireDialog.class, null);
    }

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(String label, int milliseconds);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((String) parameters[0], (Integer) parameters[1]);
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
