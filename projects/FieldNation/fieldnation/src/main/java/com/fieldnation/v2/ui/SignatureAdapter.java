package com.fieldnation.v2.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.v2.data.model.Signature;

/**
 * Created by Shoaib on 10/11/17.
 */

public class SignatureAdapter extends RecyclerView.Adapter<SignatureViewHolder> {
    private static final String TAG = "SignatureAdapter";

    private Signature[] signatures;
    private SignatureAdapter.Listener _listener;


    public void setListener(SignatureAdapter.Listener listener) {
        _listener = listener;
    }

    public void setSignatures(Signature[] signatures) {
        this.signatures = signatures;
        rebuild();
    }

    private void rebuild() {
        notifyDataSetChanged();
    }

    @Override
    public SignatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemTwoVertView v = new ListItemTwoVertView(parent.getContext());
        return new SignatureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SignatureViewHolder holder, int position) {
        ListItemTwoVertView v = (ListItemTwoVertView) holder.itemView;
        v.setTag(signatures[position]);
        v.setIcon(App.get().getString(R.string.icon_circle_signature), ContextCompat.getColor(App.get(), R.color.fn_accent_color));
        v.setOnLongClickListener(_signature_onLongClick);
        try {
            v.set(signatures[position].getName(), "Signed by " + signatures[position].getName()
                    + " on " + DateUtils.formatDateLong(signatures[position].getCreated().getCalendar()));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private final View.OnLongClickListener _signature_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Signature signature = (Signature) v.getTag();
            if (signature.getActionsSet().contains(Signature.ActionsEnum.DELETE)) {
                _listener.onLongClick(v, signature);
                return true;
            }
            return false;
        }
    };

    @Override
    public int getItemCount() {
        if (signatures == null)
            return 0;
        return signatures.length;
    }

    public interface Listener {
        void onLongClick(View v, Signature signature);
    }
}
