package com.fieldnation.v2.ui.workorder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.data.model.Message;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/30/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private static final String TAG = "MessageAdapter";

    private List<Message> source = null;
    private List<Tuple> objects = new LinkedList<>();

    public void setMessages(List<Message> messages) {
        source = messages;
        rebuild();
        notifyDataSetChanged();
    }

    private void rebuild() {
        objects.clear();

        if (source == null || source.size() == 0) {
            Tuple tuple = new Tuple();
            tuple.type = MessageViewHolder.TYPE_EMPTY;
            objects.add(tuple);
            return;
        }

        List<List<Message>> groups = new LinkedList<>();

        {
            // First, group the messages by user
            int groupId = 0;
            List<Message> group = new LinkedList<>();
            groups.add(group);

            for (Message message : source) {
                int userId = message.getFrom().getId();
                if (groupId == userId) {
                    group.add(message);
                } else {
                    group = new LinkedList<>();
                    groups.add(group);
                    group.add(message);
                    groupId = userId;
                }
            }
        }

        int thisUserId = (int) App.getProfileId();

        // process the groups
        for (List<Message> group : groups) {
            if (group.size() == 1) {
                Message message = group.get(0);
                boolean isMine = thisUserId == message.getFrom().getId();
                Tuple t = new Tuple();
                t.type = isMine ? MessageViewHolder.TYPE_RIGHT_FULL : MessageViewHolder.TYPE_LEFT_FULL;
                t.object = message;
                objects.add(t);
            } else {
                for (int i = 0; i < group.size(); i++) {
                    Tuple t = new Tuple();
                    Message message = group.get(i);
                    boolean isMine = thisUserId == message.getFrom().getId();

                    t.object = message;
                    // first
                    if (i == 0) {
                        t.type = isMine ? MessageViewHolder.TYPE_RIGHT_TOP : MessageViewHolder.TYPE_LEFT_TOP;

                        // end
                    } else if (i == group.size() - 1) {
                        t.type = isMine ? MessageViewHolder.TYPE_RIGHT_BOTTOM : MessageViewHolder.TYPE_LEFT_BOTTOM;

                        // middle
                    } else {
                        t.type = isMine ? MessageViewHolder.TYPE_RIGHT_CENTER : MessageViewHolder.TYPE_LEFT_CENTER;
                    }
                    objects.add(t);
                }
            }
        }
    }

    private static class Tuple {
        public int type;
        public Object object;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case MessageViewHolder.TYPE_EMPTY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_messages, parent, false);
                break;
            case MessageViewHolder.TYPE_RIGHT_TOP:
                view = new ChatRightTopView(parent.getContext());
                break;
            case MessageViewHolder.TYPE_RIGHT_CENTER:
                view = new ChatRightCenterView(parent.getContext());
                break;
            case MessageViewHolder.TYPE_RIGHT_BOTTOM:
                view = new ChatRightBottomView(parent.getContext());
                break;
            case MessageViewHolder.TYPE_RIGHT_FULL:
                view = new ChatRightFullView(parent.getContext());
                break;
            case MessageViewHolder.TYPE_LEFT_TOP:
                view = new ChatLeftTopView(parent.getContext());
                break;
            case MessageViewHolder.TYPE_LEFT_CENTER:
                view = new ChatLeftCenterView(parent.getContext());
                break;
            case MessageViewHolder.TYPE_LEFT_BOTTOM:
                view = new ChatLeftBottomView(parent.getContext());
                break;
            case MessageViewHolder.TYPE_LEFT_FULL:
                view = new ChatLeftFullView(parent.getContext());
                break;
        }

        return new MessageViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if (holder.type != MessageViewHolder.TYPE_EMPTY) {
            ChatRenderer cr = (ChatRenderer) holder.itemView;
            cr.setMessage((Message) objects.get(position).object);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).type;
    }
}
