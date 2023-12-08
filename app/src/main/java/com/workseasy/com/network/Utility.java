package com.workseasy.com.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static boolean internetChack(Context context) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (    networkInfo != null && networkInfo.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }

//    public static String getUrl(String url) {
//
//        if (url.contains("http") || url.contains("https")) {
//            return url;
//        } else {
//            return APIClient.baseUrl + "/" + url;
//        }
//    }

    public static void toastShort(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static Date getDate(String originalDate, String sourceFormat) throws ParseException {
        SimpleDateFormat sourceSimpleDateFormat = new SimpleDateFormat(sourceFormat, Locale.getDefault());
        return sourceSimpleDateFormat.parse(originalDate);
    }

    public static String getCurrentDate(String pattern) {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat(pattern/*,Locale.UK*/);
        return sdf.format(date);
    }

    public static String getFormattedDate(Date date, String targetFormat) {
        SimpleDateFormat requiredSimpleDateFormat = new SimpleDateFormat(targetFormat, Locale.getDefault());
        return requiredSimpleDateFormat.format(date);
    }
}