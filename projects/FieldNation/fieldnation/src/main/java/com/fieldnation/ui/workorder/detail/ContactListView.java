package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.User;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderContacts;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.misc;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 5/26/2015.
 */
public class ContactListView extends RelativeLayout {

    private LinearLayout _listLayout;

    private Workorder _workorder;
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

    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_listLayout == null)
            return;

        _listLayout.removeAllViews();

        boolean addedContact = false;
        final List<WorkorderContacts> contactList = new LinkedList<>();


        if (_workorder.getWorkorderManagerInfo() != null) {
            User user = _workorder.getWorkorderManagerInfo();

            if (!misc.isEmptyOrNull(user.getFullName()) || !misc.isEmptyOrNull(user.getPhone())) {
                ContactTileView tileView = new ContactTileView(getContext());
                tileView.setData(user.getFullName(), user.getPhone(), null, "Work Order Manager");
                addedContact = true;
                _listLayout.addView(tileView);
            }
        }

        if (_workorder.getLocation() != null) {
            Location location = _workorder.getLocation();
            String phone = location.getContactPhone();
            if (!misc.isEmptyOrNull(location.getContactName()) || !misc.isEmptyOrNull(phone)) {
                ContactTileView tileView = new ContactTileView(getContext());
                addedContact = true;
                tileView.setData(location.getContactName(), location.getContactPhone(), location.getContactPhoneExt(), "Location Contact");
                _listLayout.addView(tileView);
            }
        }

        if (_workorder.getWorkorderContacts() != null)
            Collections.addAll(contactList, _workorder.getWorkorderContacts());

        if (contactList.size() > 0) {
            if (_contactsRunnable != null)
                _contactsRunnable.cancel();

            if (_listLayout != null) {
                addedContact = true;
                _contactsRunnable = new ForLoopRunnable(contactList.size(), new Handler()) {
                    private final List<ContactTileView> _views = new LinkedList<>();
                    WorkorderContacts contact = null;

                    @Override
                    public void next(int i) throws Exception {
                        ContactTileView v = new ContactTileView(getContext());
                        if (contactList.get(i) instanceof WorkorderContacts) {
                            contact = contactList.get(i);
                            v.setData(contact.getName(), contact.getPhoneNumber(), contact.getRole());
                            v.setData(contact.getName(), contact.getPhoneNumber(), contact.getPhoneExt(), contact.getRole());
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
