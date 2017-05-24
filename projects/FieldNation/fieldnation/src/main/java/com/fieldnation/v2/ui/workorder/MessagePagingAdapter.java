package com.fieldnation.v2.ui.workorder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.workorder.detail.MessageRcvdView;
import com.fieldnation.ui.workorder.detail.MessageSentView;
import com.fieldnation.v2.data.model.Message;
import com.fieldnation.v2.ui.PagingAdapter;

/**
 * Created by mc on 3/7/17.
 */

public abstract class MessagePagingAdapter extends PagingAdapter<Message, MessagePagingAdapter.BaseHolder> {
    private static final String TAG = "MessagePagingAdapter";

    private static final Object EMPTY = new Object();

    public MessagePagingAdapter() {
        super(Message.class);
    }

    @Override
    public boolean shouldInjectPlaceHolder(int position) {
        return false;
    }

    @Override
    public Object getInjectedPlaceHolder(int position) {
        return null;
    }

    @Override
    public Object getEmptyPlaceHolder() {
        return EMPTY;
    }

    @Override
    public int getItemViewType(int position) {
            Object obj = getObject(position);
            if (obj == EMPTY) {
                return BaseHolder.TYPE_EMPTY;
            } else if (obj instanceof Message) {
                if (((Message) obj).getFrom().getId().equals((int) App.getProfileId())) {
                    return BaseHolder.TYPE_SEND;
                } else {
                    return BaseHolder.TYPE_RCVD;
                }
            }
        return -1;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BaseHolder.TYPE_EMPTY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_messages, parent, false);
                return new BaseHolder(view, BaseHolder.TYPE_EMPTY);
            }
            case BaseHolder.TYPE_RCVD: {
                MessageRcvdView view = new MessageRcvdView(parent.getContext());
                return new BaseHolder(view, BaseHolder.TYPE_RCVD);
            }
            case BaseHolder.TYPE_SEND: {
                MessageSentView view = new MessageSentView(parent.getContext());
                return new BaseHolder(view, BaseHolder.TYPE_SEND);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {

            switch (holder.type) {
                case BaseHolder.TYPE_EMPTY:
                    // do nothing. no data to bind
                    break;
                case BaseHolder.TYPE_RCVD: {
                    MessageRcvdView view = (MessageRcvdView) holder.itemView;
                    view.setMessage((Message) getObject(position));
                    break;
                }
                case BaseHolder.TYPE_SEND: {
                    MessageSentView view = (MessageSentView) holder.itemView;
                    view.setMessage((Message) getObject(position));
                    break;
                }
            }
        super.onBindViewHolder(holder, position);
    }

    public static class BaseHolder extends RecyclerView.ViewHolder {
        public static final int TYPE_RCVD = 0;
        public static final int TYPE_SEND = 1;
        public static final int TYPE_EMPTY = 2;

        public int type;

        public BaseHolder(View itemView, int id) {
            super(itemView);
            type = id;
        }
    }

}
