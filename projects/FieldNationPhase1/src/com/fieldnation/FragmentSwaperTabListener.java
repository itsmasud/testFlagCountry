package com.fieldnation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;

public class FragmentSwaperTabListener implements TabListener {
	private Fragment _fragment;
	private int _containerId;

	public FragmentSwaperTabListener(Fragment fragment, int containerId) {
		_fragment = fragment;
		_containerId = containerId;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		System.out.println("Method Stub: onTabReselected()");
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		System.out.println("Method Stub: onTabSelected()");
		ft.replace(_containerId, _fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		System.out.println("Method Stub: onTabUnselected()");
		ft.remove(_fragment);
	}
}
