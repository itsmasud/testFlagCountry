package com.fieldnation.shortstraw;

import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonObject;

/**
 * Created by michael.carver on 12/3/2014.
 */
public class Point {
    private static final String TAG = "Point";

    public float x;
    public float y;
    public long t;
    public Float stroke = null;

    public Point() {

    }

    public Point(float x, float y, long t) {
        this.x = x;
        this.y = y;
        this.t = t;

    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        try {
            o.put("x", x);
            o.put("y", y);
            o.put("t", t);
            o.put("stroke", stroke);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
        }

        return p;
    }
}
