package com.fieldnation.v2.ui.chat;

import com.fieldnation.v2.data.model.Message;

/**
 * Created by mc on 8/30/17.
 */

public interface ChatRenderer {

    void setMessage(Message message, boolean offline);

    void setPosition(Position position);
}
