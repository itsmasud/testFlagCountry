package com.fieldnation.v2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 2/13/17.
 */

public class GetFileIntent implements Parcelable {
    private static final String TAG = "GetFileIntent";

    public Bundle bundle;

    private GetFileIntent(Bundle bundle) {
        this.bundle = bundle;
    }

    public GetFileIntent(Intent intent, String postfix) {
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
    public static final Parcelable.Creator<GetFileIntent> CREATOR = new Parcelable.Creator<GetFileIntent>() {

        @Override
        public GetFileIntent createFromParcel(Parcel source) {
            try {
                return new GetFileIntent(source.readBundle());
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GetFileIntent[] newArray(int size) {
            return new GetFileIntent[size];
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
