package com.fieldnation.data.workorder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.UniqueTag;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicService;

import java.util.Hashtable;

public class ExpenseCategories {
    private final String TAG = UniqueTag.makeTag("data.workorder.ExpenseCategories");

    private Context _context;
    private WorkorderService _ws;
    private ExpenseCategory[] _categories = null;
    private Listener _listener = null;

    public ExpenseCategories(Context context) {
        _context = context.getApplicationContext();
        AuthTopicService.subscribeAuthState(context, 0, TAG, _authReceiver);
        AuthTopicService.requestAuthentication(context);
    }

    @Override
    protected void finalize() throws Throwable {
        if (_context != null)
            TopicService.delete(_context, TAG);
        super.finalize();
    }

    public void setListener(Listener listener) {
        _listener = listener;

        if (_categories != null) {
            _listener.onHaveCategories(_categories);
        } else {
            getCategories();
        }
    }

    private void getCategories() {
        if (_categories != null) {
            if (_listener != null)
                _listener.onHaveCategories(_categories);
        } else {
            if (_ws != null) {
                _context.startService(_ws.listExpenseCategories(0, true));
            } else {
                AuthTopicService.requestAuthentication(_context);
            }
        }

    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_ws == null || isNew) {
                _ws = new WorkorderService(_context, username, authToken, _resultReciever);
            }
            getCategories();
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _ws = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _ws = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(_context);
        }
    };

    private WebResultReceiver _resultReciever = new WebResultReceiver(new Handler()) {

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            new AsyncTaskEx<Bundle, Object, ExpenseCategory[]>() {

                @Override
                protected ExpenseCategory[] doInBackground(Bundle... params) {
                    Bundle resultData = params[0];
                    ExpenseCategory[] list = null;
                    try {
                        JsonArray ja = new JsonArray(new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));

                        list = new ExpenseCategory[ja.size()];

                        for (int i = 0; i < ja.size(); i++) {
                            JsonObject obj = ja.getJsonObject(i);
                            ExpenseCategory cat = ExpenseCategory.fromJson(obj);
                            list[i] = cat;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return list;
                }

                @Override
                protected void onPostExecute(ExpenseCategory[] expenseCategories) {
                    super.onPostExecute(expenseCategories);
                    _categories = expenseCategories;
                    getCategories();
                }
            }.executeEx(resultData);
        }

        @Override
        public Context getContext() {
            return _context;
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            _ws = null;
            AuthTopicService.requestAuthentication(getContext());
        }
    };

    public interface Listener {
        public void onHaveCategories(ExpenseCategory[] categories);
    }
}
