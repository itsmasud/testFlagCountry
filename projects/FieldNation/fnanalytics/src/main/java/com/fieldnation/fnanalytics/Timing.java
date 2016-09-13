package com.fieldnation.fnanalytics;

/**
 * Created by Michael on 9/13/2016.
 */
public class Timing {

    final public String tag;
    final public String category;
    final public String label;
    final public Integer timing;
    final public String variable;

    public Timing(Builder builder) {
        this.tag = builder.tag;
        this.category = builder.category;
        this.label = builder.label;
        this.timing = builder.timing;
        this.variable = builder.variable;
    }

    public static class Builder {
        public String tag;
        public String category;
        public String label;
        public Integer timing;
        public String variable;

        public Builder() {
        }

        public Timing build() {
            return new Timing(this);
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder timing(Integer timing) {
            this.timing = timing;
            return this;
        }

        public Builder variable(String variable) {
            this.variable = variable;
            return this;
        }
    }
}
