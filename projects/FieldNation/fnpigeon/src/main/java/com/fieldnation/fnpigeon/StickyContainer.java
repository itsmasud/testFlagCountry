package com.fieldnation.fnpigeon;

/**
 * Created by mc on 8/25/17.
 */
class StickyContainer {
    public Object message;
    public long createdDate;
    public Sticky stickyType;

    public StickyContainer(Object message, Sticky stickyType) {
        createdDate = System.currentTimeMillis();
        this.stickyType = stickyType;
        this.message = message;
    }
}
