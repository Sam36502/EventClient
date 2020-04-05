package ch.pearcenet.eventclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Handler Class
 * @Author Samuel Pearce
 *
 * Handles all HTTP requests
 */
public class RequestHandler {

    /**
     * Sends an HTTP request
     *
     * @param method The HTTP method to use (POST, GET, PUT, DELETE supported)
     * @param urlStr The URL to connect to
     * @param params An map of parameters to send
     * @return The response text
     */
    public static String sendRequest(String method, String urlStr, HashMap<String, String> params) {
        String response = "";
        urlStr += "?" + formatParams(params);

        // Validate Method
        method = method.toUpperCase();
        if (!"POST".equals(method) &&
                !"GET".equals(method) &&
                !"PUT".equals(method) &&
                !"DELETE".equals(method)
        ) {
            Main.log("WARNING", "Invalid HTTP method '" + method + "'.");
            return null;
        }

        // Send Request and retrieve response
        try {
            // Open Connection
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(Integer.parseInt(Main.settingsMap.get("api.timeout")));
            conn.setReadTimeout(Integer.parseInt(Main.settingsMap.get("api.timeout")));

            // Read Response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line = in.readLine();
            if (line != null) response += line;
            while((line = in.readLine()) != null) {
                response += System.lineSeparator() + line;
            }
            in.close();

            // Close connection
            conn.disconnect();

        } catch (IOException e) {
            Main.log("WARN", "Failed to connect to EventAPI. Please try again later.");
            return null;
        }

        return response;
    }

    /**
     * Takes a map of parameters and formats them for an HTTP request
     *
     * @param params Map of parameters
     * @return HTTP url parameter string
     */
    private static String formatParams(HashMap<String, String> params) {
        if (params == null) return "";

        String pStr = "";
        for (Map.Entry<String, String> entry: params.entrySet()) {
            pStr += entry.getKey() + "=" + entry.getValue() + "&";
        }
        if (pStr.length() > 0) pStr = pStr.substring(0, pStr.length() - 1);

        return pStr;
    }

}
