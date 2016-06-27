package com.fieldnation.ui.workorder;

import com.fieldnation.Log;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaib.ahmed on 6/23/2016.
 */
public class AvailableWorkorderListFragment extends WorkorderListFragment {
    private static final String TAG = "AvailableWorkorderListFragment";


    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        super.setListener(_listener);
    }


    private WorkorderDataSelectorListener _listener = new WorkorderDataSelectorListener() {
        @Override
        public void onRouted(WorkorderListFragment fragment, int page, List<Workorder> list) {
        }

        @Override
        public void onAvailable(WorkorderListFragment fragment, int page, List<Workorder> list) {

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
                fragment.setPageAtAdapter(page, availableWorkorderWithoutRoutedList);

        }
    };


}
