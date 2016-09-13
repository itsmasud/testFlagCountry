package com.fieldnation.fnanalytics;

/**
 * Created by Michael on 9/13/2016.
 */
public class Screen {

    final public String tag;
    final public String name;

    public Screen(Screen.Builder builder) {
        this.tag = builder.tag;
        this.name = builder.name;
    }

    public static class Builder {
        public String tag;
        public String name;

        public Builder() {
        }

        public Screen build() {
            return new Screen(this);
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }
    }
}
