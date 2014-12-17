package com.fieldnation.data.workorder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

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
    private static final String TAG = "data.workorder.ExpenseCategories";
    private static Hashtable<Context, ExpenseCategories> _instances = new Hashtable<Context, ExpenseCategories>();

    private Context _context;
    private WorkorderService _ws;
    private ExpenseCategory[] _categories;
    private Listener _listener = null;

    public static ExpenseCategories getInstance(Context context) {
        Context appcontext = context.getApplicationContext();

        if (!_instances.containsKey(appcontext)) {
            _instances.put(appcontext, new ExpenseCategories(appcontext));
        }

        return _instances.get(appcontext);
    }

    private ExpenseCategories(Context context) {
        _context = context.getApplicationContext();
        AuthTopicService.startService(context);
        AuthTopicService.subscribeAuthState(context, 0, TAG, _authReceiver);
    }

    @Override
    protected void finalize() throws Throwable {
        if (_context != null)
            TopicService.delete(_context, 1, TAG);
        super.finalize();
    }

    public ExpenseCategory[] getCategories() {
        return _categories;
    }

    public void setListener(Listener listener) {
        _listener = listener;

        if (_categories != null) {
            _listener.onHaveCategories(_categories);
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
                _context.startService(_ws.listExpenseCategories(0, true));
            }
        }

        @Override
        public void onAuthenticationFailed() {
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
            try {
                JsonArray ja = new JsonArray(new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));

                _categories = new ExpenseCategory[ja.size()];

                for (int i = 0; i < ja.size(); i++) {
                    JsonObject obj = ja.getJsonObject(i);
                    ExpenseCategory cat = ExpenseCategory.fromJson(obj);
                    _categories[i] = cat;
                }

                if (_listener != null) {
                    _listener.onHaveCategories(_categories);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            _ws = null;
            AuthTopicService.requestAuthInvalid(_context);
        }
    };

    public interface Listener {
        public void onHaveCategories(ExpenseCategory[] categories);
    }
}
