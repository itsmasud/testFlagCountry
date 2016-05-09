package com.fieldnation.service.data.workorder;

import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.ISO8601;

import java.util.Calendar;

/**
 * Created by Michael on 4/13/2016.
 * <p/>
 * This stuff was calculated from the following data points in the monolith application.
 * <p/>
 * --Database--
 * report_problem table in database
 * <p/>
 * --Code--
 * Workorder.Service.Status.ProviderStatus.php
 * Workorder.Serivce.Provider.GetReportProblems
 * Workorder.Api.v1.ReportProblemController
 * Workorder.Service.Provider.GetWorkorderDetail
 */
public class ReportProblemListFactory {

    /**
     * These are created at the top to prevent them from being constantly re-created during runtime.
     */
    private static final ReportProblemType[] RPT_ASSIGNED = new ReportProblemType[]{
            ReportProblemType.CANNOT_MAKE_ASSIGNMENT,
            ReportProblemType.WILL_BE_LATE,
            ReportProblemType.MISSING, // + DO_NOT_HAVE_SHIPMENT + DO_NOT_HAVE_INFO + DO_NOT_HAVE_RESPONSE + OTHER
            ReportProblemType.BUYER_UNRESPONSIVE,
            ReportProblemType.SCOPE_OF_WORK,
            ReportProblemType.SITE_NOT_READY, // + SITE_NOT_READY_CONTACT + SITE_NOT_READY_PRIOR_WORK + SITE_NOT_READY_ACCESS + OTHER
            ReportProblemType.OTHER,
    };

    private static final ReportProblemType[] RPT_ASSIGNED_MISSING = new ReportProblemType[]{
            ReportProblemType.DO_NOT_HAVE_SHIPMENT,
            ReportProblemType.DO_NOT_HAVE_INFO,
            ReportProblemType.DO_NOT_HAVE_RESPONSE,
            ReportProblemType.DO_NOT_HAVE_OTHER,
    };

    private static final ReportProblemType[] RPT_ASSIGNED_SITE_NOT_READY = new ReportProblemType[]{
            ReportProblemType.SITE_NOT_READY_CONTACT,
            ReportProblemType.SITE_NOT_READY_PRIOR_WORK,
            ReportProblemType.SITE_NOT_READY_ACCESS,
            ReportProblemType.SITE_NOT_READY_OTHER,
    };

    private static final ReportProblemType[] RPT_COMPLETED_PRE_REVIEW = new ReportProblemType[]{
            ReportProblemType.APPROVAL_DISAGREEMENT,
            ReportProblemType.BUYER_UNRESPONSIVE,
            ReportProblemType.OTHER,
    };

    private static final ReportProblemType[] RPT_COMPLETED = new ReportProblemType[]{
            ReportProblemType.APPROVAL_NOT_YET,
            ReportProblemType.APPROVAL_DISAGREEMENT,
            ReportProblemType.BUYER_UNRESPONSIVE,
            ReportProblemType.OTHER,
    };

    private static final ReportProblemType[] RPT_APPROVED = new ReportProblemType[]{
            ReportProblemType.PAYMENT_NOT_RECEIVED,
            ReportProblemType.PAYMENT_NOT_ACCURATE,
            ReportProblemType.OTHER,
    };

    private static final ReportProblemType[] RPT_PAID = new ReportProblemType[]{
            ReportProblemType.PAYMENT_NOT_RECEIVED,
            ReportProblemType.PAYMENT_NOT_ACCURATE,
            ReportProblemType.OTHER,
    };

    public static ReportProblemType[] getPrimaryList(Workorder workorder) {
        switch (workorder.getWorkorderStatus()) {
            case INPROGRESS:
            case ASSIGNED:
                return RPT_ASSIGNED;
            case COMPLETED:
                try {
                    if (workorder.getKeyEvents() != null && workorder.getKeyEvents().getWorkDoneTimeISO() != null
                            && workorder.getApprovalGrade() != null && workorder.getApprovalGrade().getReviewPeriodDays() > 0) {
                        Calendar doneTime = ISO8601.toCalendar(workorder.getKeyEvents().getWorkDoneTimeISO());
                        doneTime.add(Calendar.DAY_OF_YEAR, workorder.getApprovalGrade().getReviewPeriodDays());

                        if (System.currentTimeMillis() < doneTime.getTimeInMillis()) {
                            return RPT_COMPLETED_PRE_REVIEW;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return RPT_COMPLETED;
            case APPROVED:
                return RPT_APPROVED;
            case PAID:
                return RPT_PAID;
            default:
                break;
        }
        return null;
    }

    public static ReportProblemType[] getSecondaryList(Workorder workorder, ReportProblemType primaryReason) {
        switch (workorder.getWorkorderStatus()) {
            case INPROGRESS:
            case ASSIGNED: {
                switch (primaryReason) {
                    case MISSING:
                        return RPT_ASSIGNED_MISSING;
                    case SITE_NOT_READY:
                        return RPT_ASSIGNED_SITE_NOT_READY;
                    default:
                        break;
                }
            }
            default:
                break;
        }
        return null;
    }
}
