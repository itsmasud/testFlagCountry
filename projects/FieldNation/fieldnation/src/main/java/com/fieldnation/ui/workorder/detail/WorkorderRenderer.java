package com.fieldnation.ui.workorder.detail;

import com.fieldnation.data.workorder.Workorder;

public interface WorkorderRenderer {
    public void setWorkorder(Workorder workorder, boolean isCached);
}
