package com.fieldnation.ui.workorder;

import com.fieldnation.data.workorder.Workorder;

import java.util.List;

/**
 * Created by shoaib.ahmed on 6/27/2016.
 */
public interface WorkorderDataSelectorListener {
    void onRouted(WorkorderListFragment fragment, int page, List<Workorder> list);
    void onAvailable(WorkorderListFragment fragment,int page, List<Workorder> list);
}
