package com.fieldnation;

import com.fieldnation.R;
import com.fieldnation.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkorderAssignedFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Method Stub: onCreateView()");
		return inflater.inflate(R.layout.fragment_wo_assigned, container,
				false);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Method Stub: onViewStateRestored()
		System.out.println("Method Stub: onViewStateRestored()");
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Method Stub: onResume()
		System.out.println("Method Stub: WorkorderAssignedFragment.onResume()");
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		// TODO Method Stub: onDestroyView()
		System.out.println("Method Stub: onDestroyView()");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Method Stub: onDetach()
		System.out.println("Method Stub: onDetach()");
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Method Stub: onPause()
		System.out.println("Method Stub: onPause()");
		super.onPause();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Method Stub: onViewCreated()
		System.out.println("Method Stub: onViewCreated()");
		super.onViewCreated(view, savedInstanceState);
	}
}
