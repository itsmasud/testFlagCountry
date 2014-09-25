package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.WorkorderSubstatus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WoProgressBar extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.WoProgressBar";

	// UI
	private LinearLayout _nodeLayout;
	private ProgressNode[] _nodes;
	private LinearLayout[] _nodeLayouts;

	public WoProgressBar(Context context) {
		super(context);
		init();
	}

	public WoProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WoProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_progress, this);

		if (isInEditMode())
			return;

		_nodes = new ProgressNode[4];

		_nodes[0] = (ProgressNode) findViewById(R.id.progressNode1);
		_nodes[1] = (ProgressNode) findViewById(R.id.progressNode2);
		_nodes[2] = (ProgressNode) findViewById(R.id.progressNode3);
		_nodes[3] = (ProgressNode) findViewById(R.id.progressNode4);

		_nodeLayouts = new LinearLayout[4];
		_nodeLayouts[0] = (LinearLayout) findViewById(R.id.progressnode_layout1);
		_nodeLayouts[1] = (LinearLayout) findViewById(R.id.progressnode_layout2);
		_nodeLayouts[2] = (LinearLayout) findViewById(R.id.progressnode_layout3);
		_nodeLayouts[3] = (LinearLayout) findViewById(R.id.progressnode_layout4);

		_nodeLayout = (LinearLayout) findViewById(R.id.node_layout);

	}

	public void setSubstatus(WorkorderSubstatus substatus) {
		int ord = substatus.ordinal();

		_nodes[0].setActive(ord >= WorkorderSubstatus.AVAILABLE.ordinal());
		_nodes[1].setActive(ord >= WorkorderSubstatus.REQUESTED.ordinal());
		_nodes[2].setActive(ord >= WorkorderSubstatus.CONFIRMED.ordinal());
		_nodes[3].setActive(ord >= WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED.ordinal());

		// hideLast();
	}

	public void hideLast() {
		_nodeLayouts[3].setVisibility(View.GONE);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _nodeLayouts[3].getLayoutParams();
		params.width = 0;
		_nodeLayouts[3].setLayoutParams(params);

		_nodeLayout.setWeightSum(3f);
		requestLayout();
	}

	public void showLast() {
		_nodeLayouts[3].setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _nodeLayouts[3].getLayoutParams();
		params.width = 1;
		_nodeLayouts[3].setLayoutParams(params);

		_nodeLayout.setWeightSum(4f);

		requestLayout();
	}

	public void setTitle(int index, int resId) {
		_nodes[index].setLabel(resId);
	}

	public void setIcon(int index, int position) {

	}
}
