package ru.ifmo.ctddev.skripnikov.androidhw3;

import android.graphics.drawable.Drawable;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;

public class ImagesGetter {
    private static final String SIZE = "Medium";
    private static HttpClient httpClient = new DefaultHttpClient();
    private static Header h = new BasicHeader("Authorization", "Basic OmJTREY2WldkbTZDZG9pSG9vZDcyejBGNDN1VXhONUVhRHRmOHVweHVZT2M=");

    public static Drawable downloadImage(String url) {
        Drawable d = null;
        try {
            d = Drawable.createFromStream(
                    (InputStream) new URL(url).getContent(), "src");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String[] getImagesUrl(String name) {
        String[] urls = null;
        try {
            String jString = getJSON(name);
            JSONObject jObject = new JSONObject(jString);
            jObject = jObject.getJSONObject("d");
            JSONArray jArray = jObject.getJSONArray("results");
            urls = new String[jArray.length()];
            for (int i = 0; i < jArray.length(); i++)
                urls[i] = jArray.getJSONObject(i).getString("MediaUrl");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return urls;
    }

    private static String getJSON(String name) throws IOException {
        String URL = "https://api.datamarket.azure.com/Bing/Search/v1/Image?Query=%27" + name + "%27&$top=20&$format=JSON&ImageFilters=%27Size%3A" + SIZE + "%27";
        HttpPost postRequest = new HttpPost(URL);
        postRequest.setHeader(h);
        HttpResponse response = httpClient.execute(postRequest);

        return inputSreamToString(response.getEntity().getContent());
    }

    private static String inputSreamToString(InputStream input) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(input));

        String line;
        while ((line = rd.readLine()) != null)
            builder.append(line);

        return builder.toString();
    }
}
