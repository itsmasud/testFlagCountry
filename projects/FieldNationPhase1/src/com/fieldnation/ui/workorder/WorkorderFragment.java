package com.fieldnation.ui.workorder;

import com.fieldnation.data.workorder.Workorder;

import android.support.v4.app.Fragment;

public abstract class WorkorderFragment extends Fragment {

	public abstract void update();

	public abstract void setWorkorder(Workorder workorder);

}
