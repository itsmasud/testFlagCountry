package com.fieldnation.data.workorder;

import android.content.Context;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

import java.io.InputStream;

public class ExpenseCategories {
    private final String TAG = UniqueTag.makeTag("data.workorder.ExpenseCategories");

    private static ExpenseCategory[] _categories = null;

    private final Context _context;
    // private WorkorderService _ws;
    private Listener _listener = null;

    public ExpenseCategories(Context context) {
        _context = context.getApplicationContext();
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
            // TODO look at async task
            try {
                InputStream is = _context.getResources().openRawResource(R.raw.expensecategories);
                //noinspection ConstantConditions, can't be null beacuse resource always exists
                JsonArray ja = new JsonArray(
                        new String(misc.readAllFromStream(is, 1024, -1, 1000)));

                ExpenseCategory[] cats = new ExpenseCategory[ja.size()];

                for (int i = 0; i < cats.length; i++) {
                    JsonObject obj = ja.getJsonObject(i);
                    ExpenseCategory cat = ExpenseCategory.fromJson(obj);
                    cats[i] = cat;
                }

                _categories = cats;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            _listener.onHaveCategories(_categories);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
//    private final AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
//        @Override
//        public void onAuthentication(String username, String authToken, boolean isNew) {
//            if (_ws == null || isNew) {
//                _ws = new WorkorderService(_context, username, authToken, _resultReciever);
//            }
//            getCategories();
//        }
//
//        @Override
//        public void onAuthenticationFailed(boolean networkDown) {
//            _ws = null;
//        }
//
//        @Override
//        public void onAuthenticationInvalidated() {
//            _ws = null;
//        }
//
//        @Override
//        public void onRegister(int resultCode, String topicId) {
//            AuthTopicService.requestAuthentication(_context);
//        }
//    };

//    private final WebResultReceiver _resultReciever = new WebResultReceiver(new Handler()) {
//
//        @Override
//        public void onSuccess(int resultCode, Bundle resultData) {
//            new AsyncTaskEx<Bundle, Object, ExpenseCategory[]>() {
//
//                @Override
//                protected ExpenseCategory[] doInBackground(Bundle... params) {
//                    Bundle resultData = params[0];
//                    ExpenseCategory[] list = null;
//                    try {
//                        JsonArray ja = new JsonArray(new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));
//
//                        list = new ExpenseCategory[ja.size()];
//
//                        for (int i = 0; i < ja.size(); i++) {
//                            JsonObject obj = ja.getJsonObject(i);
//                            ExpenseCategory cat = ExpenseCategory.fromJson(obj);
//                            list[i] = cat;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return list;
//                }
//
//                @Override
//                protected void onPostExecute(ExpenseCategory[] expenseCategories) {
//                    super.onPostExecute(expenseCategories);
//                    _categories = expenseCategories;
//                    getCategories();
//                }
//            }.executeEx(resultData);
//        }
//
//        @Override
//        public Context getContext() {
//            return _context;
//        }
//
//        @Override
//        public void onError(int resultCode, Bundle resultData, String errorType) {
//            super.onError(resultCode, resultData, errorType);
//            _ws = null;
//            AuthTopicService.requestAuthentication(getContext());
//        }
//    };

    public interface Listener {
        public void onHaveCategories(ExpenseCategory[] categories);
    }
}
