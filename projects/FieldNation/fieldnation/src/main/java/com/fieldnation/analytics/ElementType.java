package com.fieldnation.analytics;

/**
 * Created by Michael on 9/14/2016.
 */
public enum ElementType {
    BUTTON("Button"),
    LIST_ITEM("List Item"),
    TAB("Tab"),
    BAR_BUTTON("Bar Button"),
    KEYBOARD_BUTTON("Keyboard Button");

    public final String elementType;

    ElementType(String elementType) {
        this.elementType = elementType;
    }
}
