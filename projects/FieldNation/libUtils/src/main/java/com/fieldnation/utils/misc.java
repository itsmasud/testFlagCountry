package com.fieldnation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class misc {
    private static final String HEXES = "0123456789ABCDEF";
    private static NumberFormat _currencyFormat = NumberFormat.getCurrencyInstance();
    private static NumberFormat _maxTwoDecimal = new DecimalFormat("#.##");

    // private static NumberFormat _normalNumber =
    // NumberFormat.getIntegerInstance();

    public static Bitmap resizeBitmap(Bitmap source, int width, int height) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, source.getWidth(), source.getHeight()), new RectF(0, 0, width, height), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, true);
    }

    public static void printStackTrace(String message) {
        try {
            throw new Exception(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Location locationFromCoordinates(double lat, double lon) {
        Location loc = new Location("reverseGeocoded");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        return loc;
    }

    public static String cardinalDirectionBetween(Location start, Location end) {
        return null;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static File dumpLogcat(Context context, String version) {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");
        temppath.mkdirs();
        File tempfile = new File(temppath + "/logcat-" + (System.currentTimeMillis() / 1000) + ".log");
        try {
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(tempfile));

            fout.write("APP VERSION: " + version + "\n");
            fout.write("MANUFACTURER: " + Build.MANUFACTURER + "\n");
            fout.write("MODEL: " + Build.MODEL + "\n");
            fout.write("RELEASE: " + Build.VERSION.RELEASE + "\n");
            fout.write("SDK: " + Build.VERSION.SDK_INT + "\n");

            try {
                Process process = Runtime.getRuntime().exec("logcat -d");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    fout.write(line);
                    fout.write("\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            fout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tempfile;
    }

    public static void flushLogs(Context context, long deathAge) {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");

        String[] files = temppath.list();

        if (files == null)
            return;

        for (int i = 0; i < files.length; i++) {
            File file = new File(temppath.getAbsolutePath() + "/" + files[i]);

            Log.v("misc", "checking " + file.getAbsolutePath() + ":" + file.lastModified());

            if (file.lastModified() + deathAge < System.currentTimeMillis()) {
                Log.v("misc", "deleting " + file.getAbsolutePath());
                file.delete();
            }
        }

    }

/*
    public static String getLogcat() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append("\n");
            }
            return log.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
*/

    public static boolean isViewVisisble(ScrollView scrollView, View childView) {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        return childView.getLocalVisibleRect(scrollBounds);
    }

    public static Bitmap getViewBitmap(View view) {
        int width = view.getWidth();
        int height = view.getHeight();

        //Create a bitmap backed Canvas to draw the view into
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
        view.draw(c);

//        int h = b.getHeight();
//        int w = b.getWidth();
//        if (h > 2048) {
//            w = (w * 2048) / h;
//            h = 2048;
//            return Bitmap.createScaledBitmap(b, w, h, false);
//        } else if (w > 2048) {
//            w = 2048;
//            h = (h * 2048) / w;
//            return Bitmap.createScaledBitmap(b, w, h, false);
//        }
        return b;
    }

    public static String to2Decimal(double value) {
        return _maxTwoDecimal.format(value);
    }

    public static String to2Decimal(float value) {
        return _maxTwoDecimal.format(value);
    }

    public static String toCurrency(double money) {
        return _currencyFormat.format(money);
    }


    public static String toCurrencyTrim(double money) {
        String curr = _currencyFormat.format(money);

        if (curr.endsWith(".00"))
            return curr.substring(0, curr.length() - 3);
        return curr;
    }

    public static String toRoundDuration(long milliseconds) {

        if (milliseconds < 60000) {
            return milliseconds / 1000 + "s";
        }

        if (milliseconds < 3600000) {
            return milliseconds / 60000 + "m";
        }

        if (milliseconds < 86400000) {
            return milliseconds / 3600000 + "hr";
        }

        return milliseconds / 86400000 + "day";
    }

    public static Spannable linkifyHtml(String html, int linkifyMask) {
        Spanned text = Html.fromHtml(html);
        URLSpan[] currentSpans = text.getSpans(0, text.length(), URLSpan.class);

        SpannableString buffer = new SpannableString(text);
        Linkify.addLinks(buffer, linkifyMask);

        for (URLSpan span : currentSpans) {
            int end = text.getSpanEnd(span);
            int start = text.getSpanStart(span);
            buffer.setSpan(span, start, end, text.getSpanFlags(span));
        }
        return buffer;
    }

    public static Spannable htmlify(String html) {
        Spanned text = Html.fromHtml(html);
        URLSpan[] currentSpans = text.getSpans(0, text.length(), URLSpan.class);

        SpannableString buffer = new SpannableString(text);

        for (URLSpan span : currentSpans) {
            int end = text.getSpanEnd(span);
            int start = text.getSpanStart(span);
            buffer.setSpan(span, start, end, text.getSpanFlags(span));
        }
        return buffer;
    }

    /**
     * removes a circle from the source bitmap that is exactly centered
     *
     * @param source
     * @return
     */
    public static Bitmap extractCircle(Bitmap source) {
        int[] pixels = new int[source.getWidth() * source.getHeight()];

        source.getPixels(pixels, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());

        int size = Math.min(source.getWidth(), source.getHeight());
        int cx = source.getWidth() / 2;
        int cy = source.getHeight() / 2;
        int xoff = (source.getWidth() - size) / 2;
        int yoff = (source.getHeight() - size) / 2;

        int[] destpix = new int[size * size];

        int dist2 = Math.min(cx, cy);
        dist2 = dist2 * dist2;

        int l1 = (dist2 * 94) / 100;
        int l2 = (dist2 * 96) / 100;
        int l3 = (dist2 * 98) / 100;

        int dx = 0;
        int dy = 0;


        for (int x = xoff; x < source.getWidth() - xoff; x++) {
            for (int y = yoff; y < source.getHeight() - yoff; y++) {
                dx = cx - x;
                dy = cy - y;
                int dist = dx * dx + dy * dy;

                if (dist <= l1) {
                    destpix[(x - xoff) + (y - yoff) * size] = pixels[x + y * source.getWidth()];
                } else if (dist <= l2) {
                    int c = pixels[x + y * source.getWidth()];
                    int i = (x - xoff) + (y - yoff) * size;
                    if (i < destpix.length)
                        destpix[i] = (c & 0x00FFFFFF) + ((((c >> 56 & 0xFF) * 192) / 256) << 56 & 0xFF000000);
                } else if (dist <= l3) {
                    int c = pixels[x + y * source.getWidth()];
                    int i = (x - xoff) + (y - yoff) * size;
                    if (i < destpix.length)
                        destpix[i] = (c & 0x00FFFFFF) + ((((c >> 56 & 0xFF) * 128) / 256) << 56 & 0xFF000000);
                } else if (dist <= dist2) {
                    int c = pixels[x + y * source.getWidth()];
                    int i = (x - xoff) + (y - yoff) * size;
                    if (i < destpix.length)
                        destpix[i] = (c & 0x00FFFFFF) + ((((c >> 56 & 0xFF) * 64) / 256) << 56 & 0xFF000000);
                }
            }
        }

        return Bitmap.createBitmap(destpix, size, size, Config.ARGB_8888);
    }

    public static boolean isEmptyOrNull(String str) {
        if (str == null)
            return true;

        return str.trim().equals("");

    }

    public static String capitalize(String src) {
        return src.substring(0, 1).toUpperCase() + src.substring(1);
    }

    // public static JsonObject getNetworkInformation() throws ParseException {
    // int index = -1;
    // byte[] mac = new byte[1];
    // String ipaddr = "";
    //
    // try {
    // Enumeration<NetworkInterface> interfaces = NetworkInterface
    // .getNetworkInterfaces();
    //
    // while (interfaces.hasMoreElements()) {
    // NetworkInterface iface = interfaces.nextElement();
    //
    // if (iface.isLoopback())
    // continue;
    //
    // if (!iface.isUp())
    // continue;
    //
    // if (iface.isVirtual())
    // continue;
    //
    // Enumeration<InetAddress> addresses = iface.getInetAddresses();
    // while (addresses.hasMoreElements()) {
    // InetAddress addr = addresses.nextElement();
    //
    // if (addr instanceof Inet6Address)
    // continue;
    //
    // if (index == -1 || index > iface.getIndex()) {
    // mac = iface.getHardwareAddress();
    // ipaddr = addr.getHostAddress();
    // index = iface.getIndex();
    // }
    // }
    //
    // }
    //
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // }
    //
    // JsonObject obj = new JsonObject();
    //
    // obj.put("mac", misc.getHex(mac));
    // obj.put("ipaddr", ipaddr);
    //
    // return obj;
    // }

    public static String cleanRequestPath(String applicationPath, String requestPath) {
        if (requestPath.startsWith(applicationPath)) {
            requestPath = requestPath.replace(applicationPath, "");
        }

        return requestPath;
    }

    public static Calendar applyTimeZone(Calendar fromCal) {
        TimeZone fromTz = fromCal.getTimeZone();
        TimeZone toTz = TimeZone.getDefault();

        if (toTz.equals(fromTz))
            return fromCal;

        Calendar toCal = Calendar.getInstance(toTz);

        if (toTz.useDaylightTime()) {
            toCal.setTimeInMillis(fromCal.getTimeInMillis() + toTz.getRawOffset() - toTz.getDSTSavings());
        } else {
            toCal.setTimeInMillis(fromCal.getTimeInMillis() + toTz.getRawOffset());
        }

        return toCal;
    }

	/*
     * http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#dt
	 */

    /**
     * @param calendar
     * @return June 3, 2014
     */
    public static String formatDateLong(Calendar calendar) {
        calendar = applyTimeZone(calendar);
        return String.format(Locale.US, "%tB", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
    }

    /**
     * @param calendar
     * @return Wednesday, Jun 3, 2014
     */
    public static String formatDateReallyLong(Calendar calendar) {
        calendar = applyTimeZone(calendar);
        return String.format(Locale.US, "%tA", calendar) + ", " + String.format(Locale.US, "%tb", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
    }

    /**
     * @param calendar
     * @param seconds
     * @return MM/DD/YYYY HH:MM:SS am/pm
     */
    public static String formatDateTime(Calendar calendar, boolean seconds) {
        return formatDate(calendar) + " " + formatTime(calendar, seconds);
    }

    /**
     * @param calendar
     * @return June 3, 2014 @ HH:MM am/pm
     */
    public static String formatDateTimeLong(Calendar calendar) {
        return formatDateLong(calendar) + " @ " + formatTime(calendar, false);
    }

    /**
     * @param calendar
     * @return in the form MM/DD/YYYY
     */
    public static String formatDate(Calendar calendar) {
        calendar = applyTimeZone(calendar);
        int months = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        return months + "/" + day + "/" + year;
    }

    public static String formatMessageTime(Calendar calendar) {
        Calendar cal = applyTimeZone(calendar);

        // today
        if (calendar.getTimeInMillis() / 86400000 == Calendar.getInstance().getTimeInMillis() / 86400000) {
            return "Today";
        }

        // > 1 year old
        if (Calendar.getInstance().get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
            return String.format(Locale.US, "%tb", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
        }
        return String.format(Locale.US, "%tb", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH) + " " + formatTime(
                cal, false);
    }

    /**
     * @param calendar
     * @param seconds  if true, then seconds are displayed.
     * @return HH:MM:SSam/pm
     */
    public static String formatTime(Calendar calendar, boolean seconds) {
        calendar = applyTimeZone(calendar);

        String time = "";

        int hours = calendar.get(Calendar.HOUR);

        if (hours == 0) {
            time += "12:";
        } else {
            time += hours + ":";
        }

        int min = calendar.get(Calendar.MINUTE);

        if (min < 10) {
            time += "0" + min;
        } else {
            time += min + "";
        }

        if (seconds) {
            int sec = calendar.get(Calendar.SECOND);

            if (sec < 10) {
                time += ":0" + sec;
            } else {
                time += ":" + sec;
            }

        }

        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            time += "am";
        } else {
            time += "pm";
        }

        return time;
    }

    public static String formatTime2(Calendar calendar) {
        calendar = applyTimeZone(calendar);

        String time = "";

        int hours = calendar.get(Calendar.HOUR);

        if (hours == 0) {
            time += "12:";
        } else {
            time += hours + ":";
        }

        int min = calendar.get(Calendar.MINUTE);

        if (min < 10) {
            time += "0" + min;
        } else {
            time += min + "";
        }

        return time;
    }

    public static void copyDirectoryTree(File sourceDir, File destDir) throws IOException {

        if (!destDir.exists()) {
            destDir.mkdir();
        }

        File[] children = sourceDir.listFiles();

        for (File sourceChild : children) {

            String name = sourceChild.getName();

            File destChild = new File(destDir, name);

            if (sourceChild.isDirectory()) {
                copyDirectoryTree(sourceChild, destChild);
            } else {
                copyFile(sourceChild, destChild);
            }

        }

    }

    public static boolean copyFile(File src, File dest) throws IOException {
        OutputStream outFile = null;
        InputStream inFile = null;

        long size = src.length();
        long pos = 0;
        int read = 0;
        byte[] packet = new byte[1024]; // 10MB

        try {
            inFile = new BufferedInputStream(new FileInputStream(src));
            outFile = new BufferedOutputStream(new FileOutputStream(dest));
            while (pos < size) {
                read = inFile.read(packet);
                if (read > 0) {
                    outFile.write(packet, 0, read);
                    pos += read;
                } else if (read == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                } else if (read == -1) {
                    break;
                }
            }
        } finally {
            try {
                inFile.close();
            } catch (IOException e) {
            }
            try {
                outFile.close();
            } catch (IOException e) {
            }
        }
        return pos == size;
    }

    public static void deleteDirectoryTree(File file) throws IOException {
        if (file.isDirectory()) {
            // directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
            } else {
                // list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    // construct the file structure
                    File fileDelete = new File(file, temp);

                    // recursive delete
                    deleteDirectoryTree(fileDelete);
                }

                // check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                }
            }

        } else {
            // if file, then delete it
            file.delete();
        }
    }

    public static String escapeForHTML(String Value) {
        if (Value == null) {
            return "";
        }

        Value = Value.replace("&", "&amp;");
        Value = Value.replace("'", "&apos;");
        Value = Value.replace("<", "&lt;");
        Value = Value.replace(">", "&gt;");
        Value = Value.replace("\"", "&quot;");

        return Value;
    }

    public static String escapeForURL(String Data) {
        String[] srch = {"\\x25", "\\x2B", "\\x20", "\\x3C", "\\x3E", "\\x23", "\\x7B", "\\x7D", "\\x7C", "\\x5C",
                "\\x5E", "\\x7E", "\\x5B", "\\x5D", "\\x60", "\\x3B", "\\x2F", "\\x3F", "\\x3A", "\\x40", "\\x3D",
                "\\x26", "\\x24"};

        String[] replace = {"%25", "%2B", "%20", "%3C", "%3E", "%23", "%7B", "%7D", "%7C", "%5C", "%5E", "%7E", "%5B",
                "%5D", "%60", "%3B", "%2F", "%3F", "%3A", "%40", "%3D", "%26", "%24"};

        for (int i = 0; i < srch.length; i++) {
            Data = Data.replaceAll(srch[i], replace[i]);
        }

        // System.out.println(Data);

        return Data;
    }

    public static String getHex(byte[] raw) {
        return getHex(raw, raw.length);
    }

    public static String getHex(byte[] raw, int length) {
        return getHex(raw, 0, raw.length);
    }

    public static String getHex(byte[] raw, int start, int length) {
        if (raw == null) {
            return "";
        }
        final StringBuilder hex = new StringBuilder(2 * length);
        for (int i = 0; i < length; i++) {
            byte b = raw[i + start];
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static String getHexJava(byte[] raw) {
        return getHexJava(raw, raw.length);
    }

    public static String getHexJava(byte[] raw, int length) {
        if (raw == null) {
            return "";
        }
        final StringBuilder hex = new StringBuilder(2 * length);
        for (int i = 0; i < length; i++) {
            byte b = raw[i];
            hex.append("0x");
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
            hex.append(",");
        }
        return hex.toString();
    }

    public static byte[] hexToBytes(String hex) {
        hex = hex.toUpperCase();

        byte[] data = new byte[hex.length() / 2];

        int val = 0;

        for (int i = 0; i < hex.length(); i += 2) {
            val = HEXES.indexOf(hex.charAt(i)) * 16;
            val += HEXES.indexOf(hex.charAt(i + 1));

            data[i / 2] = (byte) val;
        }

        return data;
    }

    public static long getLongFromElementChild(Element el, String Field) {
        try {
            return Long.parseLong(getTextFromElementChild(el, Field));
        } catch (Exception ex) {
            return Long.MIN_VALUE;
        }
    }

    public static int getIntegerFromElementChild(Element el, String Field) {
        try {
            return Integer.parseInt(getTextFromElementChild(el, Field));
        } catch (Exception ex) {
            return Integer.MIN_VALUE;
        }
    }

    public static String getTextFromElementChild(Element el, String Field) {
        try {
            NodeList macs = el.getElementsByTagName(Field);
            if (macs != null && macs.getLength() != 0) {
                Element elMac = (Element) macs.item(0);
                return elMac.getChildNodes().item(0).getNodeValue();
            }
        } catch (Exception ex) {
            return null;
        }

        return null;
    }

    public static long hexToInt(String hex) {
        long retval = 0;

        for (int i = 0; i < hex.length(); i++) {
            int v = HEXES.indexOf(hex.charAt(i));
            retval += v << (4 * (hex.length() - 1 - i));
        }
        return retval;
    }

    public static String intToHex(int value, int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(HEXES.charAt(0x0F & (value >> (i * 4))));
        }
        sb = sb.reverse();

        return sb.toString();
    }

    public static String longToHex(long value, int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(HEXES.charAt((int) (0x0F & (value >> (i * 4)))));
        }
        sb = sb.reverse();

        return sb.toString();
    }

    public static String parseXML(String Value) {
        StringBuilder result = new StringBuilder();
        int tabs = 0;
        int length;

        do {
            length = Value.length();
            Value = Value.replaceAll(" <", "<");
            Value = Value.replaceAll("\r<", "<");
            Value = Value.replaceAll("\n<", "<");
            Value = Value.replaceAll("\t<", "<");

            Value = Value.replaceAll("> ", ">");
            Value = Value.replaceAll(">\r", ">");
            Value = Value.replaceAll(">\n", ">");
            Value = Value.replaceAll(">\t", ">");

        } while (length != Value.length());

        Value = Value.replaceAll("><", ">\n<");
        // try to do tabbing
        String[] cmd = Value.split("\\n");
        for (int i = 0; i < cmd.length; i++) {
            if (cmd[i].indexOf("</") == 0) {
                tabs--;
            }

            Value = cmd[i];
            result.append(repeat("   ", tabs) + Value + "\n");

            if (cmd[i].indexOf("/>") == -1 && cmd[i].indexOf("</") == -1) {
                tabs++;
            }
        }

        return result.toString();
    }

    public static String repeat(String value, int count) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < count; i++) {
            data.append(value);
        }
        return data.toString();
    }

    public static String unescapeForHTML(String Value) {
        if (Value == null) {
            return "";
        }

        Value = Value.replace("&amp;", "&");
        Value = Value.replace("&apos;", "'");
        Value = Value.replace("&lt;", "<");
        Value = Value.replace("&gt;", ">");
        Value = Value.replace("&quot;", "\"");
        Value = Value.replace("&nbsp;", " ");

        return Value;
    }

    public static String unescapeForURL(String Data) {
        String[] replace = {" ", "<", ">", "#", "%", "{", "}", "|", "\\", "^", "~", "[", "]", "`", ";", "/", "?", ":",
                "@", "=", "&", "$"};

        String[] srch = {"%20", "%3C", "%3E", "%23", "%25", "%7B", "%7D", "%7C", "%5C", "%5E", "%7E", "%5B", "%5D",
                "%60", "%3B", "%2F", "%3F", "%3A", "%40", "%3D", "%26", "%24"};

        for (int i = 0; i < srch.length; i++) {
            Data = Data.replaceAll(srch[i], replace[i]);
        }

        return Data;
    }

    public static byte[] readAllFromStreamUntil(InputStream in, int packetSize, int expectedSize, int maxSize, long timeoutInMilli) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int read = 0;
        int pos = 0;
        int size = expectedSize;
        long timeout = System.currentTimeMillis() + timeoutInMilli;

        if (packetSize > expectedSize && expectedSize != -1) {
            packetSize = expectedSize;
        }

        byte[] packet = new byte[packetSize];
        boolean error = false;
        boolean timedOut = false;
        boolean complete = false;

        try {
            while (!error && !timedOut && !complete && bout.size() < maxSize) {

				/*
                 * if (!waitForData(in, timeoutInMilli)) { timedOut = true;
				 * break; }
				 */

                read = in.read(packet, 0, packetSize);

                if (read > 0) {
                    pos += read;

                    if (size - pos < packetSize && size != -1) {
                        packetSize = size - pos;
                    }

                    bout.write(packet, 0, read);
                    timeout = System.currentTimeMillis() + timeoutInMilli;
                } else if (read == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                } else if (read == -1) {
                    // error, stop
                    error = true;
                }

                // finished, stop
                if (pos == size && size != -1) {
                    complete = true;
                }

                // read too much!
                if (pos > size && size != -1) {
                    error = true;
                }

                // timeout, stop
                if (timeout < System.currentTimeMillis()) {
                    timedOut = true;
                }

            }
        } catch (IOException e) {
            return bout.toByteArray();
        }

        if (timedOut && size != -1) {
            return bout.toByteArray();
        } else if (complete) {
            return bout.toByteArray();
        } else if (size == -1) {
            return bout.toByteArray();
        }

        return null;
    }

    public static byte[] readAllFromStream(InputStream in, int packetSize, int expectedSize, long timeoutInMilli) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int read = 0;
        int pos = 0;
        int size = expectedSize;
        long timeout = System.currentTimeMillis() + timeoutInMilli;

        if (packetSize > expectedSize && expectedSize != -1) {
            packetSize = expectedSize;
        }

        byte[] packet = new byte[packetSize];
        boolean error = false;
        boolean timedOut = false;
        boolean complete = false;

        try {
            while (!error && !timedOut && !complete) {

				/*
                 * if (!waitForData(in, timeoutInMilli)) { timedOut = true;
				 * break; }
				 */

                read = in.read(packet, 0, packetSize);

                if (read > 0) {
                    pos += read;

                    if (size - pos < packetSize && size != -1) {
                        packetSize = size - pos;
                    }

                    bout.write(packet, 0, read);
                    timeout = System.currentTimeMillis() + timeoutInMilli;
                } else if (read == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                } else if (read == -1) {
                    // error, stop
                    error = true;
                }

                // finished, stop
                if (pos == size && size != -1) {
                    complete = true;
                }

                // read too much!
                if (pos > size && size != -1) {
                    error = true;
                }

                // timeout, stop
                if (timeout < System.currentTimeMillis()) {
                    timedOut = true;
                }

            }
        } catch (IOException e) {
            return bout.toByteArray();
        }

        if (timedOut && size != -1) {
            return bout.toByteArray();
        } else if (complete) {
            return bout.toByteArray();
        } else if (size == -1) {
            return bout.toByteArray();
        }

        return null;
    }

    public interface PacketListener {
        void onPacket(byte[] packet, int length);
    }

    public static void readAllFromStream(InputStream in, int packetSize, int expectedSize, long timeoutInMilli,
                                         PacketListener listener) throws IOException {
        int read = 0;
        int pos = 0;
        int size = expectedSize;
        long timeout = System.currentTimeMillis() + timeoutInMilli;

        if (packetSize > expectedSize && expectedSize != -1) {
            packetSize = expectedSize;
        }

        byte[] packet = new byte[packetSize];
        boolean error = false;
        boolean timedOut = false;
        boolean complete = false;

        try {
            while (!error && !timedOut && !complete) {

				/*
                 * if (!waitForData(in, timeoutInMilli)) { timedOut = true;
				 * break; }
				 */

                read = in.read(packet, 0, packetSize);

                if (read > 0) {
                    pos += read;

                    if (size - pos < packetSize && size != -1) {
                        packetSize = size - pos;
                    }
                    listener.onPacket(packet, read);
                    timeout = System.currentTimeMillis() + timeoutInMilli;
                } else if (read == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                } else if (read == -1) {
                    // error, stop
                    error = true;
                }

                // finished, stop
                if (pos == size && size != -1) {
                    complete = true;
                }

                // read too much!
                if (pos > size && size != -1) {
                    error = true;
                }

                // timeout, stop
                if (timeout < System.currentTimeMillis()) {
                    timedOut = true;
                }

            }
        } catch (IOException e) {
        }
    }

    public static void copyStream(InputStream source, OutputStream dest, int packetSize, int expectedSize,
                                  long timeoutInMilli) throws IOException {
        int read = 0;
        int pos = 0;
        int size = expectedSize;
        long timeout = System.currentTimeMillis() + timeoutInMilli;

        if (packetSize > expectedSize && expectedSize != -1) {
            packetSize = expectedSize;
        }

        byte[] packet = new byte[packetSize];
        boolean error = false;
        boolean timedOut = false;
        boolean complete = false;

        try {
            // if (!waitForData(source, timeoutInMilli)) {
            // return;
            // }

            while (!error && !timedOut && !complete) {
                read = source.read(packet, 0, packetSize);
                if (read > 0) {
                    pos += read;

                    if (size - pos < packetSize && size != -1) {
                        packetSize = size - pos;
                    }

                    dest.write(packet, 0, read);
                    timeout = System.currentTimeMillis() + timeoutInMilli;
                } else if (read == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                } else if (read == -1) {
                    // error, stop
                    error = true;
                }

                // finished, stop
                if (pos == size && size != -1) {
                    complete = true;
                }

                // read too much!
                if (pos > size && size != -1) {
                    error = true;
                }

                // timeout, stop
                if (timeout < System.currentTimeMillis()) {
                    timedOut = true;
                }

            }
        } catch (IOException e) {
            return;
        }
        if (timedOut && size != -1) {
            return;
        } else if (complete) {
            return;
        } else if (size == -1) {
            return;
        }

        throw new IOException("Error, could not read entire stream!");
    }

    public static boolean waitForData(InputStream inputStream, long timeout) {
        long finish = System.currentTimeMillis() + timeout;

        try {
            while (finish > System.currentTimeMillis()) {
                if (inputStream.available() > 0) {
                    return true;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
        }

        return false;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String getStackTrace(Exception e) {
        StringBuilder trace = new StringBuilder();

        trace.append(e.getMessage() + "\n");
        for (int i = 0; i < e.getStackTrace().length; i++) {
            StackTraceElement elem = e.getStackTrace()[i];
            trace.append(elem.getClassName() + "." + elem.getMethodName() + "(" + elem.getFileName() + ":" + String.valueOf(elem.getLineNumber()) + ")\n");
        }

        return trace.toString();
    }

    // Return a substring between the bedingString and endString
    public static String sliceOut(String beginString, String endString, String sliceMe) throws Exception {
        int start = sliceMe.indexOf(beginString);
        int stop = sliceMe.indexOf(endString, (start + 1));

        if (-1 == start) {
            throw new Exception("Could not find beginString(" + beginString + ") in " + sliceMe);
        }
        if (-1 == stop) {
            throw new Exception("Could not find endString(" + endString + ") in " + sliceMe);
        }

        return sliceMe.substring((start + beginString.length()), stop);
    }

    public static String convertMSToDHMS(long milliseconds) {
        return convertSecondsToDHMS(milliseconds / 1000, false);
    }

    public static String convertMSToDHMS(long milliseconds, boolean only_available) {
        return convertSecondsToDHMS(milliseconds / 1000, only_available);
    }

    public static String convertSecondsToDHMS(long Seconds) {
        return convertSecondsToDHMS(Seconds, false);
    }

    public static String convertMsToHuman(long milliseconds) {
        String result = "";

        long Seconds = milliseconds / 1000;
        long days = Seconds / 86400;
        Seconds = Seconds % 86400;
        long hours = Seconds / 3600;
        Seconds = Seconds % 3600;
        long min = Seconds / 60;
        Seconds = Seconds % 60;
        long sec = Seconds;

        result = "";

        if (days > 0) {
            result += days + " days ";
        }
        if (hours > 0) {
            result += hours + " hours ";
        }
        if (min > 0) {
            result += min + " min ";
        }
        if (sec > 0) {
            result += sec + " sec";
        }

        return result.trim();
    }

    public static String convertSecondsToDHMS(long Seconds, boolean only_available) {
        String result = "";

        String days = String.valueOf(Seconds / 86400);
        Seconds = Seconds % 86400;
        String hours = String.valueOf(Seconds / 3600);
        Seconds = Seconds % 3600;
        String min = String.valueOf(Seconds / 60);
        Seconds = Seconds % 60;
        String sec = String.valueOf(Seconds);

        while (days.length() < 2) {
            days = "0" + days;
        }

        while (hours.length() < 2) {
            hours = "0" + hours;
        }

        while (min.length() < 2) {
            min = "0" + min;
        }

        while (sec.length() < 2) {
            sec = "0" + sec;
        }

        result = "";

        if (only_available) {
            if (!"00".equals(days)) {
                result = days + "d " + hours + "h " + min + "m " + sec + "s";
            } else if (!"00".equals(hours)) {
                result = hours + "h " + min + "m " + sec + "s";
            } else if (!"00".equals(min)) {
                result = min + "m " + sec + "s";
            } else {
                result = sec + "s";
            }
        } else {
            result = days + "d " + hours + "h " + min + "m " + sec + "s";
        }

        return result;
    }

    public static void hideKeyboard(View v) {
        if (v != null) {
            InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String formatDateForCF(final Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
    }

    public static String formatTimeForCF(final Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("h:mm a").format(date);
    }

    public static String formatDateTimeForCF(final Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("MM/dd/yyyy h:mm a").format(date);
    }

}
