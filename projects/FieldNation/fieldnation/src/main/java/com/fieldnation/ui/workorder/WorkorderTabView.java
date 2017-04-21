package com.fieldnation.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

public class WorkorderTabView extends RelativeLayout {
    private static final String TAG = "WorkorderTabView";

    // UI
    private RelativeLayout _detailLayout;
    //    private RelativeLayout _tasksLayout;
    private TextView _messagesTextView;
    private RelativeLayout _messagesLayout;
    private RelativeLayout _attachmentsLayout;

    // Data
    private Listener _listener;
    private View[] _buttons;
    private RelativeLayout[] _layouts;
    private int _currentTab = 0;

    /*-*************************************-*/
    /*-				Lifecycle				-*/
    /*-*************************************-*/
    public WorkorderTabView(Context context) {
        super(context);
        init();
    }

    public WorkorderTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkorderTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_tabview, this);

        if (isInEditMode())
            return;

        _detailLayout = (RelativeLayout) findViewById(R.id.detail_layout);
        _detailLayout.setOnClickListener(_detailLayout_onClick);

/*
        _tasksLayout = (RelativeLayout) findViewById(R.id.tasks_layout);
        _tasksLayout.setOnClickListener(_tasksLayout_onClick);
*/

        _messagesTextView = (TextView) findViewById(R.id.messages_textview);
        _messagesLayout = (RelativeLayout) findViewById(R.id.messages_layout);
        _messagesLayout.setOnClickListener(_messagesLayout_onClick);

        _attachmentsLayout = (RelativeLayout) findViewById(R.id.attachments_layout);
        _attachmentsLayout.setOnClickListener(_attachmentsLayout_onClick);

        _buttons = new View[3];
        _buttons[0] = _detailLayout;
//        _buttons[1] = _tasksLayout;
        _buttons[1] = _messagesLayout;
        _buttons[2] = _attachmentsLayout;

        _layouts = new RelativeLayout[5];
        _layouts[0] = _detailLayout;
//        _layouts[1] = _tasksLayout;
        _layouts[1] = _messagesLayout;
        _layouts[2] = _attachmentsLayout;

        setSelected(0);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _detailLayout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelected(0);
            if (_listener != null)
                _listener.onChange(0);
        }
    };

    private final View.OnClickListener _messagesLayout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelected(1);
            if (_listener != null)
                _listener.onChange(1);
        }
    };

    private final View.OnClickListener _attachmentsLayout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelected(2);
            if (_listener != null)
                _listener.onChange(2);
        }
    };

    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    public void setSelected(int index) {
        _currentTab = index;
        for (int i = 0; i < _buttons.length; i++) {
            _buttons[i].setSelected(i == index);
        }
    }

    public void setMessagesCount(int count) {
        if (count == 0) {
            _messagesTextView.setVisibility(GONE);
        } else {
            _messagesTextView.setVisibility(VISIBLE);
            _messagesTextView.setText(count + "");
        }
    }


//    public void hideTab(int index) {
//        _layouts[index].setVisibility(View.GONE);
//    }
//
//    public void showTab(int index) {
//        _layouts[index].setVisibility(View.VISIBLE);
//    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*********************************-*/
    /*-				Private				-*/
    /*-*********************************-*/
    public interface Listener {
        void onChange(int index);
    }
}
