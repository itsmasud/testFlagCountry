package com.fieldnation.v2.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionUtils;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Signature;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 10/11/17.
 */

public class SignatureAdapter extends RecyclerView.Adapter<SignatureViewHolder> {
    private static final String TAG = "SignatureAdapter";

    private Signature[] signatures;
    private SignatureAdapter.Listener _listener;
    private List<Object> objects = new LinkedList<>();
    private List<SignatureAdapter.Tuple> addedSignatures = new LinkedList<>();
    private Hashtable<Integer, WebTransaction> deletedSignatures = new Hashtable<>();
    private int _workOrderId;
    private int _running = 0;
    private boolean _runAgain = false;

    private static class Tuple {
        public WebTransaction webTransaction;
        public Signature signature;

        public Tuple(WebTransaction webTransaction, Signature signature) {
            this.webTransaction = webTransaction;
            this.signature = signature;
        }
    }

    public void setListener(SignatureAdapter.Listener listener) {
        _listener = listener;
    }

    public void setSignatures(int workOrderId, Signature[] signatures) {
        this.signatures = signatures;
        this._workOrderId = workOrderId;
        if (_running == 0) {
            _running = 2;
            addedSignatures.clear();
            deletedSignatures.clear();
            WebTransactionUtils.setData(_addSignature, WebTransactionUtils.KeyType.ADD_SIGNATURE, workOrderId);
            WebTransactionUtils.setData(_deleteSignature, WebTransactionUtils.KeyType.DELETE_SIGNATURE, workOrderId);
        } else {
            _runAgain = true;
        }
    }

    private void rebuild() {
        if (_runAgain) {
            _runAgain = false;
            setSignatures(_workOrderId, signatures);
            return;
        }

        objects.clear();

        for (SignatureAdapter.Tuple signature : addedSignatures) {
            objects.add(signature);
        }

        for (Signature signature : signatures) {
            if (deletedSignatures.containsKey(signature.getId()))
                continue;

            objects.add(signature);
        }

        notifyDataSetChanged();
    }

    @Override
    public SignatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemTwoVertView v = new ListItemTwoVertView(parent.getContext());
        v.setOffline(false);
        return new SignatureViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SignatureViewHolder holder, int position) {
        ListItemTwoVertView v = (ListItemTwoVertView) holder.itemView;
        v.setTag(objects.get(position));
        v.setOnLongClickListener(_signature_onLongClick);
        v.setIcon(App.get().getString(R.string.icon_circle_signature), ContextCompat.getColor(App.get(), R.color.fn_accent_color));
        v.setOnClickListener(_signature_onClick);
        Object object = objects.get(position);

        if (object instanceof Tuple) {
            Signature signature = ((Tuple) object).signature;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Tuple) object).webTransaction.getCreatedTime());
            v.setOffline(true);
            try {
                v.set(signature.getName(), "Signed by " + signature.getName() + " on " + DateUtils.formatDateLong(calendar));

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else if (object instanceof Signature) {
            Signature signature = (Signature) object;
            v.setOffline(false);
            try {
                v.set(signature.getName(), "Signed by " + signature.getName()
                        + " on " + DateUtils.formatDateLong(signature.getCreated().getCalendar()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }

    private final View.OnLongClickListener _signature_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            Object object = v.getTag();
            WebTransaction webTransaction = null;
            Signature signature = null;
            if (object instanceof SignatureAdapter.Tuple) {
                signature = ((Tuple) object).signature;
                webTransaction = ((SignatureAdapter.Tuple) object).webTransaction;
            } else if (object instanceof Signature) {
                signature = (Signature) object;
            }
            if (signature.getActionsSet().contains(Signature.ActionsEnum.DELETE) || webTransaction != null) {
                _listener.onLongClick(v, signature, webTransaction);
                return true;

            }
            return false;
        }
    };

    private final WebTransactionUtils.Listener _addSignature = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction, TransactionParams transactionParams, JsonObject methodParams) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                Signature signature = Signature.fromJson(new JsonObject(tp.methodParams).getJsonObject("signature"));
                addedSignatures.add(new SignatureAdapter.Tuple(webTransaction, signature));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running--;
            if (_running == 0) rebuild();
        }
    };

    private final WebTransactionUtils.Listener _deleteSignature = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction, TransactionParams transactionParams, JsonObject methodParams) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                int id = new JsonObject(tp.methodParams).getInt("signatureId");
                deletedSignatures.put(id, webTransaction);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running--;
            if (_running == 0) rebuild();
        }
    };

    private final View.OnClickListener _signature_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Signature signature = null;

            if (v.getTag() instanceof Tuple)
                signature = ((Tuple) v.getTag()).signature;
            else if (v.getTag() instanceof Signature)
                signature = (Signature) v.getTag();

            if (_listener != null)
                _listener.signatureOnClick(v, signature);
        }
    };

    @Override
    public int getItemCount() {
        if (objects == null)
            return 0;
        return objects.size();
    }

    public interface Listener {
        void onLongClick(View v, Signature signature, WebTransaction webTransaction);

        void signatureOnClick(View v, Signature signature);
    }
}
