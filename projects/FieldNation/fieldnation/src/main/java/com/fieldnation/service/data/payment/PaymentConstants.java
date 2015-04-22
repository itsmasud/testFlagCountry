package com.fieldnation.service.data.payment;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public interface PaymentConstants {
    public static final long CALL_BOUNCE_TIMER = 30000;

    public static final String TOPIC_ID_GET_ALL = "PaymentConstants:TOPIC_ID_GET_ALL";
    public static final String TOPIC_ID_PAYMENT = "PaymentConstants:TOPIC_ID_PAYMENT";

    public static final String PARAM_ACTION = "PARAM_ACTION";

    public static final String PARAM_ACTION_GET_ALL = "ACTION_GET_ALL";
    public static final String PARAM_ACTION_PAYMENT = "PARAM_ACTION_PAYMENT";
    public static final String PARAM_IS_SYNC = "PARAM_IS_SYNC";

    public static final String PARAM_PAGE = "PAGE";
    public static final String PARAM_ID = "ID";
    public static final String PARAM_DATA_PARCELABLE = "PARAM_DATA_PARCELABLE";

    public static final String PSO_PAYMENT_GET_ALL = "PaymentGetAll";
    public static final String PSO_PAYMENT_GET = "PaymentGet";
}
