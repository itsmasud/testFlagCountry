package com.fieldnation.ui.market;

import com.fieldnation.Log;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.ui.workorder.WorkorderListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaib.ahmed on 6/23/2016.
 */
public class RoutedWorkorderListFragment extends WorkorderListFragment {
    private static final String TAG = "RoutedWorkorderListFragment";

    @Override
    public void addPage(int page, List<Workorder> list) {
        List<Workorder> routedList = new ArrayList<>();

        WorkorderStatus status;
        WorkorderSubstatus substatus;

        for (Workorder workorder : list) {
            status = workorder.getWorkorderStatus();
            substatus = workorder.getWorkorderSubstatus();

            if (status == WorkorderStatus.AVAILABLE && substatus == WorkorderSubstatus.ROUTED) {
                routedList.add(workorder);
            }
        }

        if (routedList != null)
            super.setPageAtAdapter(page, routedList);

    }
}
