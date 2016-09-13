package com.fieldnation.fnanalytics;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 9/13/2016.
 */
public class Event implements Parcelable {

    final public String tag;
    final public String category;
    final public String action;
    final public String label;
    final public String property;
    final public Number value;

    public Event(Builder builder) {
        this.tag = builder.tag;
        this.category = builder.category;
        this.action = builder.action;
        this.label = builder.label;
        this.property = builder.property;
        this.value = builder.value;
    }

    public static class Builder {

        public String tag;
        public String category;
        public String action;
        public String label;
        public String property;
        public Number value;

        public Builder() {
        }

        public Event build() {
            return new Event(this);
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder property(String property) {
            this.property = property;
            return this;
        }

        public Builder value(Number value) {
            this.value = value;
            return this;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle();
            Builder builder = new Builder();
            
            if (bundle.containsKey("tag"))
                builder.tag(bundle.getString("tag"));
            if (bundle.containsKey("category"))
                builder.category(bundle.getString("category"));
            if (bundle.containsKey("action"))
                builder.action(bundle.getString("action"));
            if (bundle.containsKey("label"))
                builder.label(bundle.getString("label"));
            if (bundle.containsKey("property"))
                builder.property(bundle.getString("property"));
            if (bundle.containsKey("value"))
                builder.value(bundle.getDouble("value"));

            return builder.build();
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        if (this.tag != null)
            bundle.putString("tag", this.tag);
        if (this.category != null)
            bundle.putString("category", this.category);
        if (this.action != null)
            bundle.putString("action", this.action);
        if (this.label != null)
            bundle.putString("label", this.label);
        if (this.property != null)
            bundle.putString("property", this.property);
        if (this.value != null)
            bundle.putDouble("value", this.value.doubleValue());
        dest.writeBundle(bundle);
    }
}
