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
public class AvailableWorkorderListFragment extends WorkorderListFragment {
    private static final String TAG = "AvailableWorkorderListFragment";

    @Override
    public void addPage(int page, List<Workorder> list) {
        List<Workorder> availableWorkorderWithoutRoutedList = new ArrayList<>();

        WorkorderStatus status;
        WorkorderSubstatus substatus;

        for (Workorder workorder : list) {
            status = workorder.getWorkorderStatus();
            substatus = workorder.getWorkorderSubstatus();

            if (status == WorkorderStatus.AVAILABLE && substatus != WorkorderSubstatus.ROUTED) {
                availableWorkorderWithoutRoutedList.add(workorder);
            }
        }

        if (availableWorkorderWithoutRoutedList != null)
            super.setPageAtAdapter(page, availableWorkorderWithoutRoutedList);

    }

}
