package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.fieldnation.data.workorder.Workorder;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by Michael on 4/13/2016.
 */
public class ReportAProblemAdapterFactory {

    public static ReportProblemType[] getPrimaryList(Workorder workorder) {

        switch (workorder.getWorkorderStatus()) {
            case ASSIGNED:
            case INPROGRESS:
                return new ReportProblemType[]{
                        ReportProblemType.CANNOT_MAKE_ASSIGNMENT,
                        ReportProblemType.WILL_BE_LATE,
                        ReportProblemType.MISSING,
                        ReportProblemType.BUYER_UNRESPONSIVE,
                        ReportProblemType.SCOPE_OF_WORK,
                        ReportProblemType.SITE_NOT_READY,
                        ReportProblemType.OTHER,
                };
            case PAID:
            case APPROVED:
            case COMPLETED: {
                switch (workorder.getWorkorderSubstatus()) {
                    case PENDINGREVIEW:
                        return new ReportProblemType[]{
                                ReportProblemType.CANNOT_MAKE_ASSIGNMENT,
                                ReportProblemType.WILL_BE_LATE,
                                ReportProblemType.MISSING,
                                ReportProblemType.BUYER_UNRESPONSIVE,
                                ReportProblemType.SCOPE_OF_WORK,
                                ReportProblemType.SITE_NOT_READY,
                                ReportProblemType.OTHER,
                        };
                    case INREVIEW:
                        return new ReportProblemType[]{
                                ReportProblemType.PAYMENT_NOT_ACCURATE,
                                ReportProblemType.PAYMENT_NOT_RECEIVED,
                                ReportProblemType.OTHER
                        };
                    case APPROVED_PROCESSINGPAYMENT:
                    case PAID:
                        return new ReportProblemType[]{
                                ReportProblemType.PAYMENT_NOT_ACCURATE,
                                ReportProblemType.PAYMENT_NOT_RECEIVED,
                                ReportProblemType.OTHER
                        };
                    default:
                        break;
                }
            }
            case CANCELED:
                break;
            case NA:
                break;
        }
        return null;
    }

    public static ArrayAdapter getSecondaryList() {
        return null;
    }

}
