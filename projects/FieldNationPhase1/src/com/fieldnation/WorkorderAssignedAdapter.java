package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.json.JsonArray;
import com.fieldnation.service.rpc.WorkorderRpc;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class WorkorderAssignedAdapter extends BaseAdapter {
	private static final int RPC_GET_ASSIGNED = 1;

	private GlobalState _gs;
	private Context _context;
	private JsonArray _workorders = null;

	public WorkorderAssignedAdapter(Context context) {
		_context = context;
		_gs = (GlobalState) context.getApplicationContext();
	}

	public void update() {
		_workorders = new JsonArray();
		WorkorderRpc.getAssigned(_context, _rpcReceiver, RPC_GET_ASSIGNED,
				_gs.accessToken, 1);
	}

	private ResultReceiver _rpcReceiver = new ResultReceiver(new Handler()) {
		protected void onReceiveResult(int resultCode, Bundle resultData) {

			if (resultCode == RPC_GET_ASSIGNED) {
				int page = resultData.getInt("PARAM_PAGE");
				String data = new String(resultData.getByteArray("PARAM_DATA"));
				System.out.println(data);

				JsonArray orders = null;
				try {
					orders = new JsonArray(data);
				} catch (Exception ex) {
					// TODO report problem?
					ex.printStackTrace();
				}
				if (orders == null)
					// TODO this is bad! fail!? retry!?
					return;

				_workorders.merge(orders);

				notifyDataSetChanged();

				if (orders.size() == 25) {
					WorkorderRpc.getAssigned(_context, _rpcReceiver,
							RPC_GET_ASSIGNED, _gs.accessToken, page + 1);
				}
			}

			System.out.println("WorkorderAssignedAdapter._rpcReceiver");
		};
	};

	@Override
	public int getCount() {
		if (_workorders == null)
			return 0;

		return _workorders.size();
	}

	@Override
	public Object getItem(int position) {
		return _workorders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WorkorderSummaryView wosum = null;

		if (convertView == null) {
			wosum = new WorkorderSummaryView(parent.getContext());
		} else if (convertView instanceof WorkorderSummaryView) {
			wosum = (WorkorderSummaryView) convertView;
		} else {
			wosum = new WorkorderSummaryView(parent.getContext());
		}

		wosum.setWorkorder(_workorders.getJsonObject(position));

		return wosum;
	}

}
