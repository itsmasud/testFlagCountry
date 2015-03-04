package com.fieldnation.service.transaction;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.service.objectstore.ObjectStoreClient;
import com.fieldnation.service.objectstore.StoredObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public class WorkorderListTransactionHandler extends TransactionHandler implements WebServiceConstants {
    private ObjectStoreClient _osc;
    private Context _context;
    private Listener _listener;
    private Transaction _transaction;
    private Bundle _resultData;

    private List<Workorder> _workorders = null;
    private int _index = 0;
    private boolean _updatedIndex = false;

    @Override
    public void handleResult(Context context, Listener listener, Transaction transaction, Bundle resultData) {
        _context = context;
        _listener = listener;
        _transaction = transaction;
        _resultData = resultData;

        // connect to the objectstore
        _osc = new ObjectStoreClient(_osc_listener);
        _osc.connect(context);

        // start parsing workorders
        new WorkorderParseAsync().executeEx(resultData);
    }

    private void saveWorkorders() {
        if (!_updatedIndex) {
            _osc.get("WorkorderList", new String(_transaction.getMeta()));
            _updatedIndex = true;
        }
        if (_index < _workorders.size()) {
            Workorder wo = _workorders.get(_index);
            _osc.get("Workorder", wo.getWorkorderId() + "");
        } else {
            _listener.onComplete();
        }
    }

    private class WorkorderParseAsync extends AsyncTaskEx<Bundle, Object, List<Workorder>> {
        @Override
        protected List<Workorder> doInBackground(Bundle... params) {
            Bundle resultData = params[0];

            String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));

            JsonArray objects = null;
            try {
                objects = new JsonArray(data);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            List<Workorder> list = new LinkedList<>();
            for (int i = 0; i < objects.size(); i++) {
                try {
                    list.add(Workorder.fromJson(objects.getJsonObject(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Workorder> workorders) {
            super.onPostExecute(workorders);
            _workorders = workorders;
            if (_osc.isConnected())
                saveWorkorders();
        }
    }

    private final ObjectStoreClient.Listener _osc_listener = new ObjectStoreClient.Listener() {
        @Override
        public void onConnected() {
            if (_workorders != null)
                saveWorkorders();
        }

        @Override
        public void onDisconnected() {
        }

        @Override
        public void onDelete(boolean success, String objectName, String objectKey) {
        }

        @Override
        public void onPut(StoredObject obj) {
            _index++;
            saveWorkorders();
        }

        @Override
        public void onGet(StoredObject obj) {
            if (obj.getObjName().equals("Workorder")) {
                Workorder wo = _workorders.get(_index);
                if (obj == null) {
                    _osc.put("Workorder", wo.getWorkorderId() + "", null, wo.toJson().toString());
                } else {
                    try {
                        JsonObject json = new JsonObject(new String(obj.getData()));
                        json.merge(wo.toJson(), true, true);
                        _osc.put("Workorder", wo.getWorkorderId() + "", null, json.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        _osc.put("Workorder", wo.getWorkorderId() + "", null, wo.toJson().toString());
                    }
                }
            } else if (obj.getObjName().equals("WorkorderList")) {
                // TODO merge id numbers with existing structure?
                obj.
            }
        }

        @Override
        public void onList(StoredObject[] objects) {
        }
    };
}
