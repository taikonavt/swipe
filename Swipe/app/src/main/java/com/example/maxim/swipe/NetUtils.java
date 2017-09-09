package com.example.maxim.swipe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by maxim on 08.09.17.
 */

public class NetUtils {

    String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {

                String string = scanner.next();

                return string;
            } else {

                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
