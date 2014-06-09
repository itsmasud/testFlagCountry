package com.fieldnation;

import com.fieldnation.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkorderAssignedFragment extends Fragment {
	// Data

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO kick off available devices query
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Method Stub: onCreateView()");
		return inflater
				.inflate(R.layout.fragment_wo_assigned, container, false);
	}

}
