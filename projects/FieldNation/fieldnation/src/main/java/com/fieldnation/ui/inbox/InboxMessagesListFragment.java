package com.fieldnation.ui.inbox;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.data.profile.Message;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.OverScrollListView;
import com.fieldnation.ui.PagingAdapter;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.TabActionBarFragmentActivity;
import com.fieldnation.ui.UnavailableCardView;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.List;

public class InboxMessagesListFragment extends Fragment implements TabActionBarFragmentActivity.TabFragment {
    private static final String TAG_BASE = "InboxMessagesListFragment";
    private String TAG = TAG_BASE;

    // State
    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // UI
    private OverScrollListView _listView;
    private RefreshView _loadingView;
    private UnavailableCardView _emptyView;

    // Data
    private ProfileClient _profileClient;
    private PhotoClient _photoClient;
    private static final Hashtable<String, WeakReference<Drawable>> _picCache = new Hashtable<>();

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

        _adapter.setOnLoadingCompleteListener(_adapterListener);

        _listView = (OverScrollListView) view.findViewById(R.id.listview);
        _listView.setDivider(null);
        _listView.setOnOverScrollListener(_loadingView);
        _listView.setAdapter(_adapter);

        _emptyView = (UnavailableCardView) view.findViewById(R.id.empty_view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putString(STATE_TAG, TAG);
        super.onSaveInstanceState(outState);
    }


    public String getGaLabel() {
        return TAG_BASE;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        if (_profileClient != null && _profileClient.isConnected())
            _adapter.refreshPages();

        setLoading(true);

        GoogleAnalyticsTopicClient.dispatchScreenView(App.get(), getGaLabel());
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);

        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(App.get());
        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(App.get());
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach()");

        if (_profileClient != null && _profileClient.isConnected())
            _profileClient.disconnect(App.get());
        if (_photoClient != null && _photoClient.isConnected())
            _photoClient.disconnect(App.get());
        _picCache.clear();

        super.onDetach();
    }

    @Override
    public void isShowing() {
        Log.v(TAG, "isShowing");
        GoogleAnalyticsTopicClient.dispatchScreenView(App.get(), getGaLabel());
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

        ProfileClient.listMessages(App.get(), page, false, allowCache);
    }

    public void addPage(int page, List<Message> list) {
        Log.v(TAG, "addPage: page:" + page);
        if (page == 0 && (list == null || list.size() == 0)) {
            _emptyView.setNoMessages();
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

    private final PagingAdapter<Message> _adapter = new PagingAdapter<Message>() {
        @Override
        public View getView(Message object, View convertView, ViewGroup parent) {
            MessageTileView v = null;
            if (convertView == null) {
                v = new MessageTileView(parent.getContext());
            } else if (convertView instanceof MessageTileView) {
                v = (MessageTileView) convertView;
            } else {
                v = new MessageTileView(parent.getContext());
            }

            v.setData(object, _messageCard_listener);

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

    private final MessageTileView.Listener _messageCard_listener = new MessageTileView.Listener() {
        @Override
        public Drawable getPhoto(MessageTileView view, String url, boolean circle) {
            if (_picCache.containsKey(url) && _picCache.get(url).get() != null) {
                return _picCache.get(url).get();
            } else {
                _photoClient.subGet(url, circle, false);
                PhotoClient.get(App.get(), url, circle, false);
            }
            return null;
        }
    };

    /*-*****************************-*/
    /*-             WEB             -*/
    /*-*****************************-*/
    private final PhotoClient.Listener _photoClient_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(String url, BitmapDrawable bitmapDrawable, boolean isCircle, boolean failed) {
            if (bitmapDrawable == null || url == null)
                return;

            Drawable pic = bitmapDrawable;
            _picCache.put(url, new WeakReference<>(pic));
        }
    };

    private final ProfileClient.Listener _profileClient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subListMessages();
            _adapter.refreshPages();
        }

        @Override
        public void onMessageList(List<Message> list, int page, boolean failed, boolean isCached) {
            addPage(page, list);
        }
    };
}

