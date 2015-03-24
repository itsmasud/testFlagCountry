package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class WorkorderDataClient extends TopicClient implements WorkorderDataConstants {
    public final String TAG = UniqueTag.makeTag("WorkorderDataClient");

    public WorkorderDataClient(Listener listener) {
        super(listener);
    }

    public static void listWorkorders(Context context, WorkorderDataSelector selector, int page) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST);
        intent.putExtra(PARAM_LIST_SELECTOR, selector.getCall());
        intent.putExtra(PARAM_PAGE, page);
        context.startService(intent);
    }

    public boolean registerWorkorderList(WorkorderDataSelector selector) {
        if (!isConnected())
            return false;

        return register(PARAM_ACTION_LIST + "/" + selector.getCall(), TAG);
    }


    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(PARAM_ACTION_LIST)) {
                preOnWorkorderList((Bundle) payload);
            }
        }

        protected void preOnWorkorderList(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, List<Workorder>>() {
                private WorkorderDataSelector selector;
                private int page;

                @Override
                protected List<Workorder> doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        selector = WorkorderDataSelector.fromName(bundle.getString(PARAM_LIST_SELECTOR));
                        page = bundle.getInt(PARAM_PAGE);
                        List<Workorder> list = new LinkedList<Workorder>();
                        JsonArray ja = new JsonArray(bundle.getByteArray(PARAM_DATA));
                        for (int i = 0; i < ja.size(); i++) {
                            list.add(Workorder.fromJson(ja.getJsonObject(i)));
                        }
                        return list;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<Workorder> workorders) {
                    onWorkorderList(workorders, selector, page);
                }
            }.executeEx(payload);
        }

        public void onWorkorderList(List<Workorder> list, WorkorderDataSelector selector, int page) {
        }
    }
}
