package com.fieldnation.fntools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.location.Location;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.fieldnation.fnlog.Log;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public final class misc {
    private static final String HEXES = "0123456789ABCDEF";
    private static final NumberFormat _currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private static final NumberFormat _maxTwoDecimal = new DecimalFormat("#.##");
    private static final NumberFormat _maxOneDecimal = new DecimalFormat("#.#");

    public static String humanReadableBytes(long bytes) {

        // 1kb = 1024
        // 1mb = 1048576
        // 1gb = 1073741824
        if (bytes > 1073741824) {
            return _maxTwoDecimal.format(bytes / 1073741824.0) + "GB";
        }
        if (bytes > 1048576) {
            return _maxTwoDecimal.format(bytes / 1048576.0) + "MB";
        }

        if (bytes > 1024) {
            return _maxTwoDecimal.format(bytes / 1024.0) + "KB";
        }
        return bytes + "B";
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

    public static boolean isViewVisible(ScrollView scrollView, View childView) {
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

    public static String to1Decimal(double value) {
        return _maxOneDecimal.format(value);
    }

    public static String to1Decimal(float value) {
        return _maxOneDecimal.format(value);
    }

    public static String to2Decimal(double value) {
        return _maxTwoDecimal.format(value);
    }

    public static String to2Decimal(float value) {
        return _maxTwoDecimal.format(value);
    }

    public static String toCurrency(double money) {
        return _currencyFormat.format(Math.round(money * 100.0) / 100.0);
    }

    public static String toShortCurrency(double money) {
        StringBuilder sb = new StringBuilder(_currencyFormat.getCurrency().getSymbol());

        // 1 digit- 1.00
        if (money < 10) {
            sb.append(_maxTwoDecimal.format(money));

            // 2-3 digits 10, 123
        } else if (money < 1000) {
            sb.append((int) money);

            // 4 digits 9.0K
        } else if (money < 10000) {
            sb.append(_maxOneDecimal.format(money / 1000)).append("K");

            // 5-6 digits 100K, 10K
        } else if (money < 1000000) {
            sb.append((int) (money / 1000)).append("K");
        }

        return sb.toString();
    }

    public static String toCurrencyTrim(double money) {
        String curr = _currencyFormat.format(money);

        if (curr.endsWith(".00"))
            return curr.substring(0, curr.length() - 3);
        return curr;
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


    public static boolean isEmptyOrNull(String str) {
        if (str == null)
            return true;

        return str.trim().equals("");

    }

    public static String capitalizeWords(String src) {
        StringBuilder builder = new StringBuilder();


        String[] strArr = src.split(" ");
        for (String str : strArr) {
            builder.append(capitalize(str.trim()));
            builder.append(" ");
        }

        return builder.toString().trim();
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

    public static String extractNumbers(String blob) {
        return blob.replaceAll("[^0-9]", "");
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

    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
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
        return convertMSToDHMS(milliseconds, true);
    }

    public static String convertMsToHuman(long milliseconds, boolean includeSeconds) {
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

        if (days == 1) {
            result += days + " day ";
        } else if (days > 1) {
            result += days + " days ";
        }

        if (hours == 1) {
            result += hours + " hour ";
        } else if (hours > 1) {
            result += hours + " hours ";
        }

        if (min == 1) {
            result += min + " min ";
        } else if (min > 1) {
            result += min + " mins ";
        }

        if (includeSeconds && sec > 0) {
            result += sec + " sec";
        }

        return result.trim();
    }

    private static class Tupple {
        String time;
        String type;

        public Tupple(String time, String type) {
            this.time = time;
            this.type = type;
        }
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

        if (only_available) {
            List<Tupple> list = new LinkedList<>();
            list.add(new Tupple(days, "d"));
            list.add(new Tupple(hours, "h"));
            list.add(new Tupple(min, "m"));
            list.add(new Tupple(sec, "s"));

            // remove from the front
            while (list.size() > 0) {
                if (list.get(0).time.equals("00")) {
                    list.remove(0);
                } else {
                    break;
                }
            }

            while (list.size() > 0) {
                if (list.get(list.size() - 1).time.equals("00")) {
                    list.remove(list.size() - 1);
                } else {
                    break;
                }
            }

            for (Tupple tupple : list) {
                result += tupple.time + tupple.type + " ";
            }

            result = result.trim();

            if (misc.isEmptyOrNull(result)) {
                result = "00s";
            }
        } else {
            result = days + "d " + hours + "h " + min + "m " + sec + "s";
        }

        return result.trim();
    }

    public static void hideKeyboard(View v) {
        if (v != null) {
            InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
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


    /**
     * @param trackingId
     * @return 0 = Fedex, 1 = UPS, 2 = USPS, 3 = Other/unknown
     */
    public static int getCarrierId(String trackingId) {
        trackingId = trackingId.toUpperCase();
        if (Pattern.compile("^E\\D{1}\\d{9}\\D{2}$|^9\\d{15,21}$").matcher(trackingId).matches()) {
            Log.e("ShipmentAddDialog", "tracking id: " + trackingId);
            return 2;
        } else if (Pattern.compile("(\\b96\\d{20}\\b)|(\\b\\d{15}\\b)|(\\b\\d{12}\\b)").matcher(trackingId).matches()
                || Pattern.compile("\\b((98\\d\\d\\d\\d\\d?\\d\\d\\d\\d|98\\d\\d)\\s*?\\d\\d\\d\\d\\s*?\\d\\d\\d\\d(\\s*?\\d\\d\\d)?)\\b").matcher(trackingId).matches()
                || Pattern.compile("^[0-9]{15}$").matcher(trackingId).matches()) {
            return 0; // test with any 12 digit
        } else if (Pattern.compile("/\\b(1Z ?[0-9A-Z]{3} ?[0-9A-Z]{3} ?[0-9A-Z]{2} ?[0-9A-Z]{4} ?[0-9A-Z]{3} ?[0-9A-Z]|[\\dT]\\d\\d\\d ?\\d\\d\\d\\d ?\\d\\d\\d|\\d{22})\\b/i").matcher(trackingId).matches()
                || Pattern.compile("1Z\\s*?[0-9A-Z]{3}\\s*?[0-9A-Z]{3}\\s*?[0-9A-Z]{2}\\s*?[0-9A-Z]{4}\\s*?[0-9A-Z]{3}\\s*?[0-9A-Z]").matcher(trackingId).matches()
                || Pattern.compile("[\\dT]\\d\\d\\d\\s*?\\d\\d\\d\\d\\s*?\\d\\d\\d").matcher(trackingId).matches()
                || Pattern.compile("\\d{22}").matcher(trackingId).matches()) {
            return 1; // test with any 11 digit
        }

        return 3;
    }

    public static String md5(String message) {
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(message.getBytes(), 0, message.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }
}
