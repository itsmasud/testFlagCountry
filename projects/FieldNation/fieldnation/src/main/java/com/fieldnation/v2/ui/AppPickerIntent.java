package com.fieldnation.v2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 2/13/17.
 */

public class AppPickerIntent implements Parcelable {
    private static final String TAG = "AppPickerIntent";

    public Bundle bundle;

    private AppPickerIntent(Bundle bundle) {
        this.bundle = bundle;
    }

    public AppPickerIntent(Intent intent, String postfix) {
        bundle = new Bundle();
        bundle.putParcelable("intent", intent);
        bundle.putString("postfix", postfix);
    }

    public Intent getIntent() {
        return bundle.getParcelable("intent");
    }

    public String getPostfix() {
        return bundle.getString("postfix");
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<AppPickerIntent> CREATOR = new Parcelable.Creator<AppPickerIntent>() {

        @Override
        public AppPickerIntent createFromParcel(Parcel source) {
            try {
                return new AppPickerIntent(source.readBundle());
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public AppPickerIntent[] newArray(int size) {
            return new AppPickerIntent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(bundle);
    }
}
