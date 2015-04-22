package com.fieldnation.service.data.workorder;

import android.content.Context;

import com.fieldnation.data.transfer.WorkorderTransfer;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.NullWebTransactionHandler;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.utils.misc;

import java.io.File;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class WorkorderTransactionBuilder implements WorkorderDataConstants {

    public static void getWorkorder(Context context, long workorderId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .key((isSync ? "Sync/" : "") + "Workorder/" + workorderId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/details"))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getWorkorderList(Context context, String selector, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderListTransactionHandler.class)
                    .handlerParams(WorkorderListTransactionHandler.generateParams(page, selector))
                    .key((isSync ? "Sync/" : "") + "WorkorderList/" + selector + "/" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + selector)
                            .urlParams("?page=" + page))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getSignature(Context context, long workorderId, long signatureId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pGetSignature(workorderId, signatureId))
                    .key((isSync ? "Sync/" : "") + "GetSignature/" + signatureId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getBundle(Context context, long bundleId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(BundleTransactionHandler.class)
                    .handlerParams(BundleTransactionHandler.generateGetParams(bundleId))
                    .key("GetBundle/" + bundleId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/bundle/" + bundleId))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postDeliverable(Context context, String filePath, String filename, long workorderId, long uploadSlotId) {
        StoredObject upFile = StoredObject.put(context, "TempFile", filePath, new File(filePath));

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables")
                    .multipartFile("file", filename, upFile)
                    .doNotRead();

            if (uploadSlotId != 0) {
                builder.path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + uploadSlotId);
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DeliverableDeleteTransactionHandler.class)
                    .handlerParams(DeliverableDeleteTransactionHandler.generateParams(workorderId))
                    .useAuth(true)
                    .request(builder)
                    .transform(Transform.makeTransformQuery(
                            "Workorder",
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeUploadDeliverable(uploadSlotId, filename).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postSignatureJson(Context context, long workorderId, String name, String json) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(NullWebTransactionHandler.class)
//                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature")
                            .body("signatureFormat=json"
                                    + "&printName=" + misc.escapeForURL(name)
                                    + "&signature=" + json))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeAddSignatureTransfer(name).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postSignatureJsonTask(Context context, long workorderId, long taskId, String name, String json) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth(true)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/tasks/complete/" + taskId)
                            .body("print_name=" + misc.escapeForURL(name)
                                    + "&signature_json=" + json))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCompletingTaskTransfer("signature", taskId, name).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
