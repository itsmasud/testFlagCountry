package com.fieldnation.service.topics;

/**
 * Created by Michael Carver on 5/14/2015.
 */
public enum Sticky {
    /**
     * The event will be transmitted once and forgotten.
     */
    NONE,
    /**
     * The event will be transmitted once. When a new client subscribes to this topic they will
     * receive it. The event will only go away if the app is shut down and restarted.
     */
    FOREVER,
    /**
     * The event will be transmitted once. When a new client subscribes to this topic they will
     * receive it. The event will eventually be purged when the OS requests the app to free up
     * memory, and after a short time.
     */
    TEMP
}
