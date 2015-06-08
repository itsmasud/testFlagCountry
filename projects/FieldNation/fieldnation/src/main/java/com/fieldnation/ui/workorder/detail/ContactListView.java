package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.User;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 5/26/2015.
 */
public class ContactListView extends RelativeLayout {

    private LinearLayout _listLayout;

    private Workorder _workorder;

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

        if (_workorder.getWorkorderManagerInfo() != null) {
            User user = _workorder.getWorkorderManagerInfo();

            if (!misc.isEmptyOrNull(user.getFullName()) || !misc.isEmptyOrNull(user.getPhone())) {
                ContactTileView tileView = new ContactTileView(getContext());
                tileView.setData(user.getFullName(), user.getPhone(), "Work Order Manager");
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
                if (!misc.isEmptyOrNull(location.getContactPhoneExt())) {
                    phone += " x" + location.getContactPhoneExt();
                }
                tileView.setData(location.getContactName(),
                        phone, "Location Contact");
                _listLayout.addView(tileView);
            }
        }

        if (addedContact) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }

    }
}
