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
	
	private static final int PROGRESSBAR_STEP_COMPLETED = 1;
	private static final int PROGRESSBAR_STEP_ACTIVE = 2;
	private static final int PROGRESSBAR_STEP_INACTIVE = 3;
	private static final int PROGRESSBAR_ON_HOLD_ACKNOWLEDGEMENT = 4;
	private static final int PROGRESSBAR_ON_HOLD_UNACKNOWLEDGEMENT = 5;

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
		buildProgressBar(substatus);
		/*_nodes[0].setActive(ord >= WorkorderSubstatus.AVAILABLE.ordinal());
		_nodes[1].setActive(ord >= WorkorderSubstatus.REQUESTED.ordinal());
		_nodes[2].setActive(ord >= WorkorderSubstatus.CONFIRMED.ordinal());
		_nodes[3].setActive(ord >= WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED.ordinal());*/

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
	
	private void buildProgressBar(WorkorderSubstatus substatus) {
		switch (substatus) {
			case AVAILABLE:
				_nodes[0].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[1].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				break;
			case ROUTED:
				_nodes[0].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[1].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);				
				hideLast();
				_nodes[0].setLabel("Routed");
				_nodes[1].setLabel("Accept");
				_nodes[2].setLabel("Assigned");
				break;
			case REQUESTED:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				break;
			case COUNTEROFFERED:
				_nodes[0].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[1].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Counter Offer");
				_nodes[1].setLabel("Assigned");
				_nodes[2].setLabel("In Progress");
				_nodes[3].setLabel("Completed");
				break;
			case UNCONFIRMED:
				_nodes[0].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[1].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Confirm");
				_nodes[1].setLabel("Assigned");
				_nodes[2].setLabel("In Progress");
				_nodes[3].setLabel("Completed");
				break;
			case CONFIRMED:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Confirm");
				_nodes[1].setLabel("Assigned");
				_nodes[2].setLabel("In Progress");
				_nodes[3].setLabel("Completed");
				break;
			case ONHOLD_UNACKNOWLEDGED:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_ON_HOLD_UNACKNOWLEDGEMENT);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Confirm");
				_nodes[1].setLabel("Assigned");
				_nodes[2].setLabel("In Progress");
				_nodes[3].setLabel("Completed");
				break;
			case ONHOLD_ACKNOWLEDGED:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_ON_HOLD_ACKNOWLEDGEMENT);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Confirm");
				_nodes[1].setLabel("Assigned");
				_nodes[2].setLabel("In Progress");
				_nodes[3].setLabel("Completed");
				break;
			case CHECKEDIN:
			case CHECKEDOUT:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[2].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Confirm");
				_nodes[1].setLabel("Assigned");
				_nodes[2].setLabel("In Progress");
				_nodes[3].setLabel("Completed");
				break;				
			case PENDINGREVIEWED:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Completed");
				_nodes[1].setLabel("In Review");
				_nodes[2].setLabel("Approved");
				_nodes[3].setLabel("Paid");
			case INREVIEW:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Completed");
				_nodes[1].setLabel("In Review");
				_nodes[2].setLabel("Approved");
				_nodes[3].setLabel("Paid");
				break;	
			case APPROVED_PROCESSINGPAYMENT:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[2].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Completed");
				_nodes[1].setLabel("In Review");
				_nodes[2].setLabel("Approved");
				_nodes[3].setLabel("Paid");
				break;	
			case PAID:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[2].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[3].setActive(PROGRESSBAR_STEP_COMPLETED);
				
				_nodes[0].setLabel("Completed");
				_nodes[1].setLabel("In Review");
				_nodes[2].setLabel("Approved");
				_nodes[3].setLabel("Paid");
				break;	
			case CANCELLED:
			case CANCELLED_LATEFEEPROCESSING:
			case CANCELLED_LATEFEEPAID:
				_nodes[0].setActive(PROGRESSBAR_STEP_COMPLETED);
				_nodes[1].setActive(PROGRESSBAR_STEP_ACTIVE);
				_nodes[2].setActive(PROGRESSBAR_STEP_INACTIVE);
				_nodes[3].setActive(PROGRESSBAR_STEP_INACTIVE);
				
				_nodes[0].setLabel("Confirm");
				_nodes[1].setLabel("Assigned");
				_nodes[2].setLabel("In Progress");
				_nodes[3].setLabel("Completed");
				break;	
			default:
				return;
		}
	}
}
