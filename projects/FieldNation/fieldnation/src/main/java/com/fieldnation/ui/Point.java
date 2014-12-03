package com.fieldnation.ui;

import com.fieldnation.json.JsonObject;

/**
 * Created by michael.carver on 12/3/2014.
 */
class Point {
    public float x;
    public float y;
    public long t;
    public Float stroke = null;

    public static float MIN_X = Float.MAX_VALUE;
    public static float MIN_Y = Float.MAX_VALUE;
    public static float MAX_X = Float.MIN_VALUE;
    public static float MAX_Y = Float.MIN_VALUE;

    public Point(float x, float y, long t) {
        this.x = x;
        this.y = y;
        this.t = t;

        if (x < MIN_X)
            MIN_X = x;
        if (y < MIN_Y)
            MIN_Y = y;
        if (x > MAX_X)
            MAX_X = x;
        if (y > MAX_Y)
            MAX_Y = y;
    }

    public static void resetBounds() {
        MIN_X = Float.MAX_VALUE;
        MIN_Y = Float.MAX_VALUE;
        MAX_X = Float.MIN_VALUE;
        MAX_Y = Float.MIN_VALUE;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        try {
            o.put("x", x);
            o.put("y", y);
            o.put("t", t);
            o.put("stroke", stroke);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return o;
    }

    public static Point fromJson(JsonObject o) {
        Point p = null;

        try {
            p = new Point(o.getFloat("x"), o.getFloat("y"), o.getLong("t"));

            if (o.has("stroke") && o.get("stroke") != null) {
                p.stroke = o.getFloat("stroke");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return p;
    }
}
