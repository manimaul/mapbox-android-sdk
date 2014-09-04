/**
 * @author Brad Leege <bleege@gmail.com>
 * Created on 2/15/14 at 3:26 PM
 */

package com.mapbox.mapboxsdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.SSLSocketFactory;

public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static HttpURLConnection getHttpURLConnection(final URL url) {
        return getHttpURLConnection(url, null, null);
    }

    public static HttpURLConnection getHttpURLConnection(final URL url, final Cache cache) {
        return getHttpURLConnection(url, cache, null);
    }

    public static HttpURLConnection getHttpURLConnection(final URL url, final Cache cache, final SSLSocketFactory sslSocketFactory) {
        OkHttpClient client = new OkHttpClient();
        if (cache != null) {
            client.setCache(cache);
        }
        if (sslSocketFactory != null) {
            client.setSslSocketFactory(sslSocketFactory);
        }
        OkUrlFactory factory = new OkUrlFactory(client);
        HttpURLConnection connection = factory.open(url);
        connection.setRequestProperty("User-Agent", MapboxConstants.USER_AGENT);
        return connection;
    }

    public static Cache getResponseCache(final File cacheDir, final int maxSize) throws IOException {
        return new Cache(cacheDir, maxSize);
    }
}
