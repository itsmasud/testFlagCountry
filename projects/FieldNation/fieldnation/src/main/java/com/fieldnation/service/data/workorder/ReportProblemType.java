package com.fieldnation.service.data.workorder;

import com.fieldnation.App;
import com.fieldnation.R;

/**
 * Created by Michael Carver on 11/25/2015.
 */
public enum ReportProblemType {
    CANNOT_MAKE_ASSIGNMENT("CANNOT_MAKE_ASSIGNMENT", R.string.i_cant_make_my_assignment),
    WILL_BE_LATE("WILL_BE_LATE", R.string.im_going_to_be_late),
    BUYER_UNRESPONSIVE("BUYER_UNRESPONSIVE", R.string.buyer_unresponsive),
    SCOPE_OF_WORK("SCOPE_OF_WORK", R.string.scope_of_work),
    OTHER("OTHER", R.string.other),
    DO_NOT_HAVE_SHIPMENT("DO_NOT_HAVE_SHIPMENT", R.string.a_shipment),
    DO_NOT_HAVE_INFO("DO_NOT_HAVE_INFO", R.string.information),
    DO_NOT_HAVE_RESPONSE("DO_NOT_HAVE_RESPONSE", R.string.response_from_someone),
    DO_NOT_HAVE_OTHER("DO_NOT_HAVE_OTHER", R.string.other),
    APPROVAL_DISAGREEMENT("APPROVAL_DISAGREEMENT", R.string.disagree_with_approval),
    APPROVAL_NOT_YET("APPROVAL_NOT_YET", R.string.havent_approved_yet),
    PAYMENT_NOT_ACCURATE("PAYMENT_NOT_ACCURATE", R.string.amount_not_accurate),
    PAYMENT_NOT_RECEIVED("PAYMENT_NOT_RECEIVED", R.string.havent_received_payment),
    SITE_NOT_READY_CONTACT("SITE_NOT_READY_CONTACT", R.string.contact_not_available),
    SITE_NOT_READY_PRIOR_WORK("SITE_NOT_READY_PRIOR_WORK", R.string.prior_work_needs_to_be_completed),
    SITE_NOT_READY_ACCESS("SITE_NOT_READY_ACCESS", R.string.i_dont_have_access),
    SITE_NOT_READY_OTHER("SITE_NOT_READY_OTHER", R.string.other),

    // stuff for the pickers
    SITE_NOT_READY("", R.string.site_not_ready),
    MISSING("", R.string.i_dont_have_what_i_need);

    public String value;
    public int stringResId;

    ReportProblemType(String value, int resId) {
        this.value = value;
        stringResId = resId;
    }

    @Override
    public String toString() {
        return App.get().getString(stringResId);
    }
}
