package com.fieldnation;

import com.fieldnation.data.workorder.Workorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.ScrollingTabContainerView.TabView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public abstract class WorkorderFragment extends Fragment {

	public abstract void update();

	public abstract void setWorkorder(Workorder workorder);

}
