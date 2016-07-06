package com.fieldnation.ui.market;

import com.fieldnation.App;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.workorder.WorkorderListFragment;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shoaib.ahmed on 6/23/2016.
 */
public class RoutedWorkorderListFragment extends WorkorderListFragment {
    private static final String TAG = "RoutedWorkorderListFragment";

    @Override
    public void addPage(int page, List<Workorder> list) {
        List<Workorder> routedList = new LinkedList<>();

        if (list != null && list.size() > 0) {
            WorkorderStatus status;
            WorkorderSubstatus substatus;

            for (Workorder workorder : list) {
                status = workorder.getWorkorderStatus();
                substatus = workorder.getWorkorderSubstatus();

                if (status == WorkorderStatus.AVAILABLE && substatus == WorkorderSubstatus.ROUTED) {
                    routedList.add(workorder);
                }
            }
        }
        if (list != null && list.size() > 0 && routedList.size() < list.size()) {
            WorkorderClient.list(App.get(), getDisplayType(), page + 1, false, false);
        }

        super.setPageAtAdapter(page, routedList);
    }
}
