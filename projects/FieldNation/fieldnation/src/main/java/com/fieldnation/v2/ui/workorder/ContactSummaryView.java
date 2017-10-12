package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Contact;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.ContactListDialog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 10/12/17.
 */

public class ContactSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "ContactSummaryView";

    // Ui
    private TextView _titleTextView;
    private TextView _countTextView;

    // Data
    private WorkOrder _workOrder;

    public ContactSummaryView(Context context) {
        super(context);
        init();
    }

    public ContactSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_task_summary_row, this, true);

        if (isInEditMode()) return;

        _titleTextView = findViewById(R.id.title_textview);
        _countTextView = findViewById(R.id.count_textview);
        _countTextView.setBackgroundResource(R.drawable.round_rect_gray);

        setVisibility(GONE);
        populateUi();

        setOnClickListener(_this_onClick);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        setVisibility(GONE);
        if (_countTextView == null)
            return;

        if (_workOrder == null)
            return;

        _titleTextView.setText("Contacts");

        final List<Contact> contactList = new LinkedList<>();

        if (_workOrder.getContacts().getResults().length > 0) {
            for (Contact contact : _workOrder.getContacts().getResults()) {
                if (!misc.isEmptyOrNull(contact.getName()) && !misc.isEmptyOrNull(contact.getPhone())) {
                    contactList.add(contact);
                }
            }
        }
        _countTextView.setText(contactList.size() + "");

        if (contactList.size() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            ContactListDialog.show(App.get(), null, _workOrder.getContacts());
        }
    };
}
