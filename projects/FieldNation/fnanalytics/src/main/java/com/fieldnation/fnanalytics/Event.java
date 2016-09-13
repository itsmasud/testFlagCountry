package com.fieldnation.fnanalytics;

/**
 * Created by Michael on 9/13/2016.
 */
public class Event {

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
}
