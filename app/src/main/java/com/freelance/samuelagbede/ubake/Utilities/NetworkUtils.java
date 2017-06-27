package com.freelance.samuelagbede.ubake.Utilities;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Agbede Samuel D on 6/9/2017.
 */

public class NetworkUtils {
    public static String urlSring = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static String MakeNetworkCall() throws IOException {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(NetworkUtils.urlSring).build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        return responseString;
    }

}
