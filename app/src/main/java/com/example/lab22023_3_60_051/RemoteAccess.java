package com.example.lab22023_3_60_051;


import java.io.*;
import java.net.*;

@SuppressWarnings("ALL")
public class RemoteAccess {

    private String TAG = "RemoteAccess";
    private static RemoteAccess instance = new RemoteAccess();

    private RemoteAccess() {
    }

    public static RemoteAccess getInstance() {
        return instance;
    }

    public String makeHttpRequest(String url, String method, String[] keys, String[] values) {
        HttpURLConnection http = null;
        InputStream is = null;
        try {
            StringBuilder postData = new StringBuilder();
            if (keys != null && values != null) {
                for (int i = 0; i < keys.length; i++) {
                    if (i > 0) postData.append("&");
                    postData.append(URLEncoder.encode(keys[i], "UTF-8"));
                    postData.append("=");
                    postData.append(URLEncoder.encode(values[i], "UTF-8"));
                }
            }

            if (method.equals("GET") && postData.length() > 0) {
                url += "?" + postData.toString();
            }

            System.out.println("@RemoteAccess-" + ": " + url);
            URL urlc = new URL(url);
            http = (HttpURLConnection) urlc.openConnection();
            http.setRequestMethod(method);
            http.setConnectTimeout(10000);
            http.setReadTimeout(10000);

            if (method.equals("POST")) {
                http.setDoOutput(true);
                OutputStream os = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData.toString());
                writer.flush();
                writer.close();
                os.close();
            }

            http.connect();
            int responseCode = http.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = http.getInputStream();
            } else {
                is = http.getErrorStream();
            }

            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        return null;
    }
}