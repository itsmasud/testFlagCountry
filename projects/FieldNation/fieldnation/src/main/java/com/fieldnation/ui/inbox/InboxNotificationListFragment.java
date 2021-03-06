package com.fieldnation.ui.inbox;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.EmptyCardView;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.PagingAdapter;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.TabActionBarFragmentActivity;

import java.util.List;

public class InboxNotificationListFragment extends Fragment implements TabActionBarFragmentActivity.TabFragment {
    private static final String TAG_BASE = "InboxNotificationListFragment";
    private String TAG = TAG_BASE;

    // State
    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // UI
    private OverScrollListView _listView;
    private RefreshView _loadingView;
    private EmptyCardView _emptyView;

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

        _loadingView = view.findViewById(R.id.loading_view);
        _loadingView.setListener(_refreshViewListener);

        _adapter.setOnLoadingCompleteListener(_adapterListener);

        _listView = view.findViewById(R.id.listview);
        _listView.setDivider(null);
        _listView.setOnOverScrollListener(_loadingView);
        _listView.setAdapter(_adapter);

        _emptyView = view.findViewById(R.id.empty_view);
        _emptyView.setData(EmptyCardView.PARAM_VIEW_TYPE_NOTIFICATION);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putString(STATE_TAG, TAG);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        if (_profileClient != null) _adapter.refreshPages();

        setLoading(true);
    }

    @Override
    public void onAttach(Context context) {
        Log.v(TAG, "onAttach");
        super.onAttach(context);
        _profileClient.subListNotifications();
        _adapter.refreshPages();
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach()");

        _profileClient.unsubListNotifications();

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
        _adapter.refreshPages();
    }

    private void requestList(int page, boolean allowCache) {
        Log.v(TAG, "requestList " + page);
        if (page == 0)
            setLoading(true);

        ProfileClient.listNotifications(App.get(), page, false, allowCache);
    }

    public void addPage(int page, List<Notification> list) {
        Log.v(TAG, "addPage: page:" + page);
        if (page == 0 && (list == null || list.size() == 0)) {
            _emptyView.setVisibility(View.VISIBLE);
        } else {
            _emptyView.setVisibility(View.GONE);
        }

        if (list != null && list.size() == 0) {
            _adapter.setNoMorePages();
        }
        _adapter.setPage(page, list);
    }

    /*-*************************************-*/
    /*-				Events UI				-*/
    /*-*************************************-*/

    private final RefreshView.Listener _refreshViewListener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            Log.v(TAG, "_refreshViewListener.onStartRefresh()");
            _adapter.refreshPages();
        }
    };

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

    private final PagingAdapter.OnLoadingCompleteListener _adapterListener = new PagingAdapter.OnLoadingCompleteListener() {
        @Override
        public void onLoadingComplete() {
//            Log.v(TAG, "_adapterListener.onLoadingComplete");
            setLoading(false);
        }
    };

    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/
    private final ProfileClient _profileClient = new ProfileClient() {
        @Override
        public void onNotificationList(List<Notification> list, int page, boolean failed, boolean isCached) {
            addPage(page, list);
        }
    };
}

