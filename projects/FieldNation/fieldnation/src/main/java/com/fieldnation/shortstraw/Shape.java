package com.fieldnation.shortstraw;



import com.fieldnation.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 12/10/2014.
 */
public class Shape {
    private static final String TAG = "Shape";

    private static final float THRESHOLD = 0.95F;
    private static final int WINDOW = 3;
    private static final float RESAMPLE_CONST = 40F;

    private List<Point> _points;

    private float _minX = Float.MAX_VALUE;
    private float _minY = Float.MAX_VALUE;
    private float _maxX = Float.MIN_VALUE;
    private float _maxY = Float.MIN_VALUE;


    public Shape() {
        _points = new LinkedList<Point>();
    }

    public void add(Point p) {
        if (p.x < _minX)
            _minX = p.x;
        if (p.y < _minY)
            _minY = p.y;
        if (p.x > _maxX)
            _maxX = p.x;
        if (p.y > _maxY)
            _maxY = p.y;

        _points.add(p);
    }

    public Point get(int index) {
        return _points.get(index);
    }

    public int size() {
        return _points.size();
    }

    public float getMaxX() {
        return _maxX;
    }

    public float getMaxY() {
        return _maxY;
    }

    public float getMinY() {
        return _minY;
    }

    public float getMinX() {
        return _minX;
    }

    public void simplify() {
        Log.v(TAG, "Before Simplify: " + _points.size());
        if (resample()) {
            Log.v(TAG, "After resample: " + _points.size());
            getCorners();
        }
        Log.v(TAG, "After Simplify: " + _points.size());
    }

    private boolean resample() {
        if (_points.size() < 1) {
            Log.v(TAG, "resample false");
            return false;
        }

        List<Point> resampled = new LinkedList<Point>();

        float dist = 0;
        float sd;
        Point p;
        Point lp = _points.get(0);
        resampled.add(lp);
        int i = 1;
        float spacing = resampleSpacing();
        while (i < _points.size()) {
            lp = _points.get(i - 1);
            p = _points.get(i);
            sd = distance(lp, p);

            if (dist + sd > spacing) {
                Point q = new Point();
                float scale = ((spacing - dist) / sd);
                q.x = lp.x + scale * (p.x - lp.x);
                q.y = lp.y + scale * (p.y - lp.y);

                resampled.add(q);
                _points.add(i, q);
                dist = 0;
            } else {
                dist += sd;
            }
            i++;
        }

        _points = resampled;
        Log.v(TAG, "resample true");
        return true;
    }

    private void getCorners() {
        Log.v(TAG, "getCorners");
        float[] straws = new float[_points.size()];
        List<Integer> corners = new LinkedList<Integer>();

        corners.add(0);

        float strawsum = 0;
        int count = 0;
        float strawAvg = 0; // alrogithm calls for median, but this is easier.

        Log.v(TAG, "getCorners, first loop");
        for (int i = WINDOW; i < _points.size() - WINDOW; i++) {
            Point lp = _points.get(i - WINDOW);
            Point p = _points.get(i + WINDOW);
            straws[i] = distance(lp, p);
            strawsum += straws[i];
            count++;
        }
        strawAvg = (strawsum / count) * THRESHOLD;

        Log.v(TAG, "getCorners, second loop");
        for (int i = WINDOW; i < _points.size() - WINDOW; i++) {
            if (straws[i] < strawAvg) {
                float localMin = Float.MAX_VALUE;
                int localMinIndex = i;
                while (i < straws.length && straws[i] < strawAvg) {
                    if (straws[i] < localMin) {
                        localMin = straws[i];
                        localMinIndex = i;
                    }
                    i++;
                }
                corners.add(localMinIndex);
            }
        }
        // add last point as a corner
        corners.add(_points.size() - 1);

        Log.v(TAG, "Corners before: " + corners.size());

        postProcessCorners(corners, straws);

        Log.v(TAG, "Corners after: " + corners.size());

        List<Point> nps = new LinkedList<>();

        for (int i = 0; i < corners.size(); i++) {
            nps.add(_points.get(corners.get(i)));
        }

        _points = nps;
        Log.v(TAG, "getCorners/");
    }

    private void postProcessCorners(List<Integer> corners, float[] straws) {
        Log.v(TAG, "postProcessCorners");
        boolean done;
        while (true) {
            done = true;
            for (int i = 2; i < corners.size(); i++) {
                int c1 = corners.get(i - 2);
                int c2 = corners.get(i);
                if (!isLine(c1, c2)) {
                    int newCorner = getHalfwayCorner(straws, c1, c2);
                    if (newCorner != -1) {
                        corners.add(i, newCorner);
                        done = false;
                    }
                }
            }
            if (done)
                break;
        }

        Log.v(TAG, "postProcessCorners second Loop");
        for (int i = 1; i < corners.size() - 1; i++) {
            int c1 = corners.get(i - 1);
            int c2 = corners.get(i + 1);
            if (isLine(c1, c2)) {
                corners.remove(i);
                i--;
            }
        }
        Log.v(TAG, "postProcessCorners/");
    }

    private boolean isLine(int c1, int c2) {
        Log.v(TAG, "isLine(" + c1 + ", " + c2 + ")");
        float dist = distance(_points.get(c1), _points.get(c2));
        float pathDist = getPathDistance(c1, c2);
        return (dist / pathDist) >= THRESHOLD;
    }

    private float resampleSpacing() {
        return distance(_maxX, _maxY, _minX, _minY) / RESAMPLE_CONST;
    }

    private float getPathDistance(int start, int end) {
        float d = 0;
        Point p1;
        Point p2;
        for (int i = start; i < end; i++) {
            p1 = _points.get(i);
            p2 = _points.get(i + 1);
            d += distance(p1, p2);
        }
        return d;
    }

    private int getHalfwayCorner(float[] straws, int a, int b) {
        Log.v(TAG, "getHalfwayCorner " + a + " " + b);
        int quarter = (b - a) / 2;
        float minValue = Float.MAX_VALUE;
        int minIndex = -1;
        for (int i = a + quarter; i < b - quarter; i++) {
            if (straws[i] < minValue) {
                minValue = straws[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    private static float distance(Point p1, Point p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    private static float distance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

}
