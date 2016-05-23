package com.fieldnation.service.data.workorder;

import com.fieldnation.App;
import com.fieldnation.R;

/**
 * Created by Michael Carver on 11/25/2015.
 */
public enum ReportProblemType {
    // 1 I can't make it to my assignment
    CANNOT_MAKE_ASSIGNMENT("CANNOT_MAKE_ASSIGNMENT", R.string.i_cant_make_my_assignment),
    // 2 I'm going to be late
    WILL_BE_LATE("WILL_BE_LATE", R.string.im_going_to_be_late),

    // 4 A Shipment
    DO_NOT_HAVE_SHIPMENT("DO_NOT_HAVE_SHIPMENT", R.string.a_shipment),
    // 5 Information
    DO_NOT_HAVE_INFO("DO_NOT_HAVE_INFO", R.string.information),
    // 6 A response from someone
    DO_NOT_HAVE_RESPONSE("DO_NOT_HAVE_RESPONSE", R.string.response_from_someone),
    // 7 Other
    DO_NOT_HAVE_OTHER("DO_NOT_HAVE_OTHER", R.string.other),

    // 14 Buyer unresponsive
    BUYER_UNRESPONSIVE("BUYER_UNRESPONSIVE", R.string.buyer_unresponsive),
    // 15 Scope of Work
    SCOPE_OF_WORK("SCOPE_OF_WORK", R.string.scope_of_work),

    // 17 Site contact not available
    SITE_NOT_READY_CONTACT("SITE_NOT_READY_CONTACT", R.string.contact_not_available),
    // 18 There is other works that needs to be completed prior to my work
    SITE_NOT_READY_PRIOR_WORK("SITE_NOT_READY_PRIOR_WORK", R.string.prior_work_needs_to_be_completed),
    // 19 I do not have access to the area where work is to be performed
    SITE_NOT_READY_ACCESS("SITE_NOT_READY_ACCESS", R.string.i_dont_have_access),
    // 20 Other
    SITE_NOT_READY_OTHER("SITE_NOT_READY_OTHER", R.string.other),

    // 21 Other
    OTHER("OTHER", R.string.other),

    // 9 WO not approved yet
    APPROVAL_NOT_YET("APPROVAL_NOT_YET", R.string.havent_approved_yet),
    // 10 Disagreement on work completion
    APPROVAL_DISAGREEMENT("APPROVAL_DISAGREEMENT", R.string.disagree_with_approval),
    // 12 Have not received payment
    PAYMENT_NOT_RECEIVED("PAYMENT_NOT_RECEIVED", R.string.havent_received_payment),
    // 13 Payment amount is not accurate
    PAYMENT_NOT_ACCURATE("PAYMENT_NOT_ACCURATE", R.string.amount_not_accurate),

    // stuff for the pickers
    SITE_NOT_READY("", R.string.site_not_ready),
    MISSING("", R.string.i_dont_have_what_i_need),
    APPROVAL("", R.string.approval);

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
