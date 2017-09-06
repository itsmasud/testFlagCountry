package com.fieldnation.v2.ui.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.v2.data.model.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/30/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
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
            tuple.type = ChatViewHolder.TYPE_EMPTY;
            objects.add(tuple);
            return;
        }

        // group by days
        List<List<Message>> dayGroup = new LinkedList<>();
        {
            Calendar lastCal = null;
            List<Message> day = null;
            for (Message message : source) {
                try {
                    Calendar myCal = message.getCreated().getCalendar();
                    if (lastCal != null && DateUtils.isSameDay(myCal, lastCal)) {
                        day.add(message);
                    } else {
                        day = new LinkedList<>();
                        dayGroup.add(day);
                        day.add(message);
                        lastCal = myCal;
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        // group days by people
        int thisUserId = (int) App.getProfileId();
        {
            for (List<Message> day : dayGroup) {
                int groupId = 0;
                List<List<Message>> groups = new LinkedList<>();
                {
                    List<Message> group = null;
                    for (Message message : day) {
                        int userId = message.getFrom().getId();
                        if (groupId == userId && group != null) {
                            group.add(message);
                        } else {
                            group = new LinkedList<>();
                            groups.add(group);
                            group.add(message);
                            groupId = userId;
                        }
                    }
                }

                // Add the header
                try {
                    if (day.size() > 0) {
                        Tuple tuple = new Tuple();
                        tuple.type = ChatViewHolder.TYPE_HEADER_TIME;
                        tuple.object = day.get(0).getCreated().getCalendar().getTime();
                        objects.add(tuple);
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

                // process the groups
                for (List<Message> group : groups) {
                    if (group.size() == 1) {
                        Message message = group.get(0);
                        boolean isMine = thisUserId == message.getFrom().getId();
                        Tuple t = new Tuple();
                        t.type = isMine ? ChatViewHolder.TYPE_RIGHT_FULL : ChatViewHolder.TYPE_LEFT_FULL;
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
                                t.type = isMine ? ChatViewHolder.TYPE_RIGHT_TOP : ChatViewHolder.TYPE_LEFT_TOP;

                                // end
                            } else if (i == group.size() - 1) {
                                t.type = isMine ? ChatViewHolder.TYPE_RIGHT_BOTTOM : ChatViewHolder.TYPE_LEFT_BOTTOM;

                                // middle
                            } else {
                                t.type = isMine ? ChatViewHolder.TYPE_RIGHT_CENTER : ChatViewHolder.TYPE_LEFT_CENTER;
                            }
                            objects.add(t);
                        }
                    }
                }

            }

        }
    }

    private static class Tuple {
        public int type;
        public Object object;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ChatViewHolder.TYPE_EMPTY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_messages, parent, false);
                break;
            case ChatViewHolder.TYPE_RIGHT_TOP:
            case ChatViewHolder.TYPE_RIGHT_CENTER:
            case ChatViewHolder.TYPE_RIGHT_BOTTOM:
            case ChatViewHolder.TYPE_RIGHT_FULL:
                view = new ChatRightView(parent.getContext());
                break;
            case ChatViewHolder.TYPE_LEFT_TOP:
            case ChatViewHolder.TYPE_LEFT_CENTER:
            case ChatViewHolder.TYPE_LEFT_BOTTOM:
            case ChatViewHolder.TYPE_LEFT_FULL:
                view = new ChatLeftView(parent.getContext());
                break;
            case ChatViewHolder.TYPE_HEADER_TIME:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_time_header, parent, false);
                break;
        }

        return new ChatViewHolder(view, viewType);
    }

    private static final SimpleDateFormat HEADER_FORMAT = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        if (holder.type == ChatViewHolder.TYPE_HEADER_TIME) {
            TextView tv = (TextView) holder.itemView;
            try {
                tv.setText(HEADER_FORMAT.format((Date) objects.get(position).object));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else if (holder.type != ChatViewHolder.TYPE_EMPTY) {
            ChatRenderer cr = (ChatRenderer) holder.itemView;
            cr.setMessage((Message) objects.get(position).object);
            switch (holder.type) {
                case ChatViewHolder.TYPE_RIGHT_TOP:
                case ChatViewHolder.TYPE_LEFT_TOP:
                    cr.setPosition(Position.TOP);
                    break;
                case ChatViewHolder.TYPE_LEFT_CENTER:
                case ChatViewHolder.TYPE_RIGHT_CENTER:
                    cr.setPosition(Position.CENTER);
                    break;
                case ChatViewHolder.TYPE_RIGHT_BOTTOM:
                case ChatViewHolder.TYPE_LEFT_BOTTOM:
                    cr.setPosition(Position.BOTTOM);
                    break;
                case ChatViewHolder.TYPE_RIGHT_FULL:
                case ChatViewHolder.TYPE_LEFT_FULL:
                    cr.setPosition(Position.FULL);
                    break;
            }
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
