package com.fieldnation.ui.inbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.SwipeableRecyclerViewTouchListener;
import com.fieldnation.ui.TabActionBarFragmentActivity;
import com.fieldnation.ui.UnavailableCardView;
import com.fieldnation.ui.worecycler.PagingAdapter;
import com.fieldnation.v2.ui.worecycler.BaseHolder;

import java.util.List;

public class InboxNotificationListFragment extends Fragment implements TabActionBarFragmentActivity.TabFragment {
    private static final String TAG_BASE = "InboxNotificationListFragment";
    private String TAG = TAG_BASE;

    // State
    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // UI
    private OverScrollRecyclerView _listView;
    private RefreshView _loadingView;
    private UnavailableCardView _emptyView;

    // Data
    private ProfileClient _profileClient;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TAG)) {
                TAG = savedInstanceState.getString(STATE_TAG);
            } else {
                TAG = UniqueTag.makeTag(TAG_BASE);
            }
        }

        if (TAG_BASE.equals(TAG)) {
            TAG = UniqueTag.makeTag(TAG_BASE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");

        _loadingView = (RefreshView) view.findViewById(R.id.loading_view);
        _loadingView.setListener(_refreshViewListener);

        _listView = (OverScrollRecyclerView) view.findViewById(R.id.listview);
        _listView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        _listView.setAdapter(_adapter);
        _listView.setOnOverScrollListener(_loadingView);
        _listView.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(view.getContext(), _listView,_swipeListener));

        _emptyView = (UnavailableCardView) view.findViewById(R.id.empty_view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putString(STATE_TAG, TAG);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);

        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(App.get());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        if (_profileClient != null) _adapter.refreshAll();

        setLoading(true);
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach()");

        if (_profileClient != null) _profileClient.disconnect(App.get());

        super.onDetach();
    }

    @Override
    public void isShowing() {
        Log.v(TAG, "isShowing");
    }

    private void setLoading(boolean loading) {
        Log.v(TAG, "setLoading()");
        if (_loadingView != null) {
            if (loading) {
                _loadingView.startRefreshing();
            } else {
                _loadingView.refreshComplete();
            }
        }
    }

    public void update() {
        Log.v(TAG, "update()");
        _adapter.refreshAll();
    }

    private void requestList(int page, boolean allowCache) {
        Log.v(TAG, "requestList " + page);
        setLoading(true);

        ProfileClient.listNotifications(App.get(), page, false, allowCache);
    }

    public void addPage(int page, List<Notification> list) {
        Log.v(TAG, "addPage: page:" + page);
        if (page == 0 && (list == null || list.size() == 0)) {
            _emptyView.setNoNotifications();
            _emptyView.setVisibility(View.VISIBLE);
        } else {
            _emptyView.setVisibility(View.GONE);
        }

        _adapter.addObjects(page, list);
        setLoading(false);
    }

    /*-*************************************-*/
    /*-				Events UI				-*/
    /*-*************************************-*/

    private final RefreshView.Listener _refreshViewListener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            Log.v(TAG, "_refreshViewListener.onStartRefresh()");
            _adapter.refreshAll();
        }
    };

    /*
        private final PagingAdapter<Notification> _adapter = new PagingAdapter<Notification>() {
            @Override
            public View getView(Notification object, View convertView, ViewGroup parent) {
                NotificationTileView v = null;
                if (convertView == null) {
                    v = new NotificationTileView(parent.getContext());
                } else if (convertView instanceof NotificationTileView) {
                    v = (NotificationTileView) convertView;
                } else {
                    v = new NotificationTileView(parent.getContext());
                }
                v.setData(object);
                return v;
            }

            @Override
            public void requestPage(int page, boolean allowCache) {
                requestList(page, allowCache);
            }
        };
    */
    private final PagingAdapter<Notification> _adapter = new PagingAdapter<Notification>(Notification.class) {
        @Override
        public void requestPage(int page, boolean allowCache) {
            requestList(page, allowCache);
        }

        @Override
        public BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType) {
            NotificationTileView v = new NotificationTileView(parent.getContext());
            return new BaseHolder(v, viewType);
        }

        @Override
        public void onBindObjectViewHolder(BaseHolder holder, Notification object) {
            NotificationTileView v = (NotificationTileView) holder.itemView;
            v.setData(object);
        }

        @Override
        public boolean useHeader() {
            return false;
        }

        @Override
        public BaseHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return null;
        }

        @Override
        public void onBindHeaderViewHolder(BaseHolder holder) {
        }

        @Override
        public BaseHolder onCreateEmptyViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_work, parent, false);
            return new BaseHolder(v, BaseHolder.TYPE_EMPTY);
        }

        @Override
        public void onBindEmptyViewHolder(BaseHolder holder) {
        }
    };

    private final SwipeableRecyclerViewTouchListener.SwipeListener _swipeListener = new SwipeableRecyclerViewTouchListener.SwipeListener() {
        @Override
        public boolean canSwipe(int position) {
            return true;
        }

        @Override
        public void onDismissedBySwipe(RecyclerView recyclerView, int[] reverseSortedPositions) {

        }

/*
        @Override
        public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                //change some data if you swipe left
            }
            _adapter.notifyDataSetChanged();
        }

        @Override
        public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                //change some data if you swipe right
            }
            _adapter.notifyDataSetChanged();
        }
*/
    };

    private final RecyclerView.OnItemTouchListener _onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

/*
    private final PagingAdapter.OnLoadingCompleteListener _adapterListener = new PagingAdapter.OnLoadingCompleteListener() {
        @Override
        public void onLoadingComplete() {
//            Log.v(TAG, "_adapterListener.onLoadingComplete");
            setLoading(false);
        }
    };
*/

    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/
    private final ProfileClient.Listener _profileClient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subListNotifications();
            _adapter.refreshAll();
        }

        @Override
        public void onNotificationList(List<Notification> list, int page, boolean failed, boolean isCached) {
            addPage(page, list);
        }
    };
}

