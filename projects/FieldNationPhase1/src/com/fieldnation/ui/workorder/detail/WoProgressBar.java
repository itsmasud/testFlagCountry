package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.WorkorderSubstatus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WoProgressBar extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.WoProgressBar";

	// UI
	private ProgressNode[] _nodes;

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
	}

	public void setSubstatus(WorkorderSubstatus substatus) {
		int ord = substatus.ordinal();

		_nodes[0].setActive(ord >= WorkorderSubstatus.AVAILABLE.ordinal());
		_nodes[1].setActive(ord >= WorkorderSubstatus.REQUESTED.ordinal());
		_nodes[2].setActive(ord >= WorkorderSubstatus.CONFIRMED.ordinal());
		_nodes[3].setActive(ord >= WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED.ordinal());
	}
}
