package com.fieldnation.v2.ui.workorder;

import com.fieldnation.v2.data.model.Message;

/**
 * Created by mc on 8/30/17.
 */

public interface ChatRenderer {

    void setMessage(Message message);

    void setPosition(Position position);
}
