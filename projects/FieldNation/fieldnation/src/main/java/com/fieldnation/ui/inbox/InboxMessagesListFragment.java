package com.fieldnation.ui.inbox;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.TabActionBarFragmentActivity;
import com.fieldnation.ui.UnavailableCardView;
import com.fieldnation.ui.worecycler.PagingAdapter;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.ui.worecycler.BaseHolder;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.List;

public class InboxMessagesListFragment extends Fragment implements TabActionBarFragmentActivity.TabFragment {
    private static final String TAG_BASE = "InboxMessagesListFragment";
    private String TAG = TAG_BASE;

    // State
    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // UI
    private OverScrollRecyclerView _listView;
    private RefreshView _loadingView;
    private UnavailableCardView _emptyView;

    // Data
    private ProfileClient _profileClient;
    private PhotoClient _photoClient;
    private WorkordersWebApi _workOrderApi;
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

        _listView = (OverScrollRecyclerView) view.findViewById(R.id.listview);
        _listView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        _listView.setAdapter(_adapter);
        _listView.setOnOverScrollListener(_loadingView);
        //_listView.addOnItemTouchListener(_onItemTouchListener);

        _emptyView = (UnavailableCardView) view.findViewById(R.id.empty_view);
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

        if (_profileClient != null && _profileClient.isConnected())
            _adapter.refreshAll();

        setLoading(true);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);

        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(App.get());
        _photoClient = new PhotoClient(_photoClient_listener);
        _photoClient.connect(App.get());
        _workOrderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workOrderApi.connect(App.get());
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach()");

        if (_profileClient != null) _profileClient.disconnect(App.get());
        if (_photoClient != null) _photoClient.disconnect(App.get());
        _picCache.clear();

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
*/

    /*
        private final PagingAdapter.OnLoadingCompleteListener _adapterListener = new PagingAdapter.OnLoadingCompleteListener() {
            @Override
            public void onLoadingComplete() {
    //            Log.v(TAG, "_adapterListener.onLoadingComplete");
                setLoading(false);
            }
        };
    */
    private final PagingAdapter<Message> _adapter = new PagingAdapter<Message>(Message.class) {
        @Override
        public void requestPage(int page, boolean allowCache) {
            requestList(page, allowCache);
        }

        @Override
        public BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType) {
            MessageTileView v = new MessageTileView(parent.getContext());
            return new BaseHolder(v, viewType);
        }

        @Override
        public void onBindObjectViewHolder(BaseHolder holder, Message object) {
            MessageTileView v = (MessageTileView) holder.itemView;
            v.setData(object, _messageCard_listener);
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
    private final WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderApi.subWorkordersWebApi();
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("deleteMessage")) {
                _adapter.refreshAll();
                ProfileClient.get(App.get());
            }
        }
    };

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
            _adapter.refreshAll();
        }

        @Override
        public void onMessageList(List<Message> list, int page, boolean failed, boolean isCached) {
            addPage(page, list);
        }
    };
}

