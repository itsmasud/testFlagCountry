package com.fieldnation.ui.workorder;

import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderActivity.PageRequestListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class WorkorderFragment extends Fragment {
	protected PageRequestListener pageRequestListener;

	public abstract void update();

	public abstract void setWorkorder(Workorder workorder);

	public void setPageRequestListener(PageRequestListener listener) {
		pageRequestListener = listener;
	}

	public abstract void doAction(Bundle bundle);

}
