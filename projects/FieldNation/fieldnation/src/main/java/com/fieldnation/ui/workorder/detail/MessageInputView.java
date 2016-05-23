package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fieldnation.R;

public class MessageInputView extends RelativeLayout {
    // UI
    private EditText _messageEditText;
    private Button _sendButton;

    // Data

    /*-*************************************-*/
    /*-				LifeCycle				-*/
	/*-*************************************-*/
    public MessageInputView(Context context) {
        super(context);
        init();
    }

    public MessageInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_message_input, this);

        if (isInEditMode())
            return;

        _messageEditText = (EditText) findViewById(R.id.message_edittext);
        _sendButton = (Button) findViewById(R.id.send_button);
    }

    public void setOnSendButtonClick(View.OnClickListener listener) {
        _sendButton.setOnClickListener(listener);
    }

    public String getInputText() {
        return _messageEditText.getText().toString();
    }

    public void clearText() {
        _messageEditText.setText("");
    }

    public void setHint(int resId) {
        _messageEditText.setHint(resId);
    }

    public void setButtonEnabled(boolean enabled) {
        _sendButton.setEnabled(enabled);
    }


	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
