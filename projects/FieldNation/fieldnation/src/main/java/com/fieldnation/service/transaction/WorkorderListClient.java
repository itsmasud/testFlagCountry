package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderListClient {

    public void requestList(Context context, WorkorderDataSelector selector, int page) throws ParseException {
        new WebTransactionBuilder(context)
                .method("GET")
                .path("/api/rest/v1/workorder/" + selector.getCall())
                .urlParams("?page=" + page)
                .key("Workorder" + selector.getCall() + "ListPage" + page)
                .extra("page", page)
                .extra("listName", selector.getCall())
                .handler(WorkorderListTransactionHandler.class)
                .send();
    }

    public void details(Context context, long workorderId) throws ParseException {
        new WebTransactionBuilder(context)
                .method("GET")
                .path("/api/rest/v1/workorder/" + workorderId + "/details")
                .key("Workorder:" + workorderId)
                .handler(WorkorderDetailsTransactionHandler.class)
                .send();
    }
}
