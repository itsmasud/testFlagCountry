package com.fieldnation.service.data.payment;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public interface PaymentConstants {
    long CALL_BOUNCE_TIMER = 30000;

    String TOPIC_ID_LIST = "PaymentConstants:TOPIC_ID_LIST";
    String TOPIC_ID_GET = "PaymentConstants:TOPIC_ID_GET";

    String PARAM_ACTION = "PARAM_ACTION";
    String PARAM_ACTION_LIST = "PARAM_ACTION_LIST";
    String PARAM_ACTION_GET = "PARAM_ACTION_GET";
    String PARAM_IS_SYNC = "PARAM_IS_SYNC";
    String PARAM_ERROR = "PARAM_ERROR";
    String PARAM_ALLOW_CACHE = "PARAM_ALLOW_CACHE";
    String PARAM_IS_CACHED = "PARAM_IS_CACHED";

    String PARAM_PAGE = "PARAM_PAGE";
    String PARAM_PAYMENT_ID = "PARAM_PAYMENT_ID";
    String PARAM_DATA_PARCELABLE = "PARAM_DATA_PARCELABLE";

    String PSO_PAYMENT_LIST = "PaymentListPage";
    String PSO_PAYMENT = "Payment";
}
