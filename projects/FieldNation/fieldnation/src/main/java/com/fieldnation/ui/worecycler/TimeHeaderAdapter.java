package com.fieldnation.ui.worecycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.RateMeView;
import com.fieldnation.ui.workorder.WorkorderCardView;
import com.fieldnation.utils.ISO8601;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 3/11/2016.
 */
public abstract class TimeHeaderAdapter extends RecyclerView.Adapter<BaseHolder> {
    private static final String TAG = "TimeHeaderAdapter";

    private static final Object RATEME = new Object();

    private RateMeView _rateMeView = null;
    private List<Workorder> _workorders = new LinkedList<>();
    private List<Object> _displayList = new LinkedList<>();
    private WorkorderCardView.Listener _listener;
    private int _rateMePosition = 5;
    private boolean _showRateMe = false;
    private int _lastPage = 0;

    public TimeHeaderAdapter(WorkorderCardView.Listener listener) {
        super();

        requestPage(0, false);
        _listener = listener;
    }

    public void refreshPages() {
        _workorders.clear();
        _displayList.clear();
        notifyDataSetChanged();
        Log.v(TAG, "refreshPages");
        _lastPage = 0;
        requestPage(0, false);
    }

    public void setRateMeAllowed(boolean allowed) {
//        if (allowed) {
//            _showRateMe = App.get().showRateMe();
//        } else {
//            _showRateMe = false;
//        }
        _showRateMe = true;
    }

    public void addWorkorders(List<Workorder> list) {
        Set<Long> ids = new HashSet<>();

        if (list == null || list.size() == 0) {
            return;
        }

        List<Workorder> newList = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            ids.add(list.get(i).getWorkorderId());
            newList.add(list.get(i));
        }

        for (int i = 0; i < _workorders.size(); i++) {
            Workorder wo = _workorders.get(i);
            if (!ids.contains(wo.getWorkorderId())) {
                ids.add(wo.getWorkorderId());
                newList.add(wo);
            }
        }
        _workorders = newList;

        // sort list
        Collections.sort(_workorders, _comparator);

        rebuildList();
    }

    private void rebuildList() {
        // Build the real list
        _displayList.clear();
        try {
            Calendar startTime = ISO8601.toCalendar(_workorders.get(0).getSchedule().getStartTime());
            _displayList.add(startTime);
            _displayList.add(_workorders.get(0));
            for (int i = 1; i < _workorders.size(); i++) {
                Workorder wo = _workorders.get(i);

                Calendar newTime = ISO8601.toCalendar(wo.getSchedule().getStartTime());

                if (newTime.get(Calendar.DAY_OF_YEAR) != startTime.get(Calendar.DAY_OF_YEAR)) {
                    _displayList.add(newTime);
                }
                _displayList.add(wo);

                startTime = newTime;

                if (i == _rateMePosition && _showRateMe) {
                    _displayList.add(RATEME);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        notifyDataSetChanged();
    }

    private final Comparator<Workorder> _comparator = new Comparator<Workorder>() {
        @Override
        public int compare(Workorder lhs, Workorder rhs) {
            try {
                long l = ISO8601.toUtc(lhs.getSchedule().getStartTime());
                long r = ISO8601.toUtc(rhs.getSchedule().getStartTime());

                if (l < r)
                    return 1;
                else if (l > r)
                    return -1;
                else
                    return 0;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return 0;
        }
    };

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BaseHolder.TYPE_WORKORDER:
                return new WorkorderHolder(new WorkorderCardView(parent.getContext()));
            case BaseHolder.TYPE_DATE:
                return new DateHolder(new ListTimeHeader(parent.getContext()));
            case BaseHolder.TYPE_RATE_ME:
                if (_rateMeView == null) {
                    _rateMeView = new RateMeView(parent.getContext());
                    _rateMeView.setListener(_rateMe_listener);
                }
                return new RateMeHolder(_rateMeView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        switch (holder.type) {
            case BaseHolder.TYPE_WORKORDER: {
                WorkorderHolder h = (WorkorderHolder) holder;
                WorkorderCardView v = h.getView();
                v.setWorkorder((Workorder) _displayList.get(position));
                v.setWorkorderSummaryListener(_listener);
                break;
            }
            case BaseHolder.TYPE_DATE: {
                DateHolder h = (DateHolder) holder;
                ListTimeHeader v = h.getView();
                v.setDate((Calendar) _displayList.get(position));
                break;
            }
            case BaseHolder.TYPE_RATE_ME: {
                // RateMeHolder h = (RateMeHolder) holder;
                // RateMeView v = h.getView();
                // Nothing to do.. no data needed
                break;
            }
        }

        if (position == (getItemCount() * 9) / 10) {
            requestPage(_lastPage + 1, false);
            _lastPage++;
        }
    }

    @Override
    public int getItemCount() {
        return _displayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = _displayList.get(position);
        if (obj instanceof Workorder) {
            return BaseHolder.TYPE_WORKORDER;
        } else if (obj instanceof Calendar) {
            return BaseHolder.TYPE_DATE;
        } else {
            return BaseHolder.TYPE_RATE_ME;
        }
    }

    private final RateMeView.Listener _rateMe_listener = new RateMeView.Listener() {
        @Override
        public void onHide() {
            _showRateMe = false;
            rebuildList();
        }
    };

    public abstract void requestPage(int page, boolean allowCache);
}