package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Contact;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 5/26/2015.
 */
public class ContactListView extends RelativeLayout {

    private LinearLayout _listLayout;

    private WorkOrder _workOrder;
    private ForLoopRunnable _contactsRunnable = null;


    public ContactListView(Context context) {
        super(context);
        init();
    }

    public ContactListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_list, this);

        if (isInEditMode())
            return;

        _listLayout = (LinearLayout) findViewById(R.id.contactList_layout);

        setVisibility(GONE);
    }

    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_listLayout == null)
            return;

        _listLayout.removeAllViews();

        boolean addedContact = false;
        final List<Contact> contactList = new LinkedList<>();

        if (_workOrder.getLocation() != null
                && _workOrder.getLocation().getSavedLocation() != null
                && _workOrder.getLocation().getSavedLocation().getContact() != null) {

            Contact contact = _workOrder.getLocation().getSavedLocation().getContact();
            if (misc.isEmptyOrNull(contact.getName()) && misc.isEmptyOrNull(contact.getPhone())) {
                ContactTileView tileView = new ContactTileView(getContext());
                tileView.setData(contact.getName(), contact.getPhone(), contact.getExt(), contact.getRole());
                addedContact = true;
                _listLayout.addView(tileView);
            }
        }

        if (_workOrder.getContacts() != null && _workOrder.getContacts().getResults() != null)
            Collections.addAll(contactList, _workOrder.getContacts().getResults());

        if (contactList.size() > 0) {
            if (_contactsRunnable != null)
                _contactsRunnable.cancel();

            if (_listLayout != null) {
                addedContact = true;
                _contactsRunnable = new ForLoopRunnable(contactList.size(), new Handler()) {
                    private final List<ContactTileView> _views = new LinkedList<>();
                    Contact contact = null;

                    @Override
                    public void next(int i) throws Exception {
                        ContactTileView v = new ContactTileView(getContext());
                        if (contactList.get(i) instanceof Contact) {
                            contact = contactList.get(i);
                            v.setData(contact.getName(), contact.getPhone(), contact.getExt(), contact.getRole());
                        }
                        _views.add(v);
                    }

                    @Override
                    public void finish(int count) throws Exception {
                        for (ContactTileView v : _views) {
                            _listLayout.addView(v);
                        }
                    }
                };
                post(_contactsRunnable);
            }
        }
        if (addedContact) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }
}
