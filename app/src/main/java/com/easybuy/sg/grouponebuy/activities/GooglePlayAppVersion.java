package com.easybuy.sg.grouponebuy.activities;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GooglePlayAppVersion extends AsyncTask<String, Void, String> {

    private final String packageName;
    private final Listener listener;
    public interface Listener {
        void result(String version);
    }

    public GooglePlayAppVersion(String packageName, Listener listener) {
        this.packageName = packageName;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        return getPlayStoreAppVersion(String.format("https://play.google.com/store/apps/details?id=%s", packageName));
    }

    @Override
    protected void onPostExecute(String version) {
        listener.result(version);
    }

    @Nullable
    private static String getPlayStoreAppVersion(String appUrlString) {
        String
                currentVersion_PatternSeq = "<div[^>]*?>Current\\sVersion</div><span[^>]*?>(.*?)><div[^>]*?>(.*?)><span[^>]*?>(.*?)</span>",
                appVersion_PatternSeq = "htlgb\">([^<]*)</s";
        try {
            URLConnection connection = new URL(appUrlString).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder sourceCode = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sourceCode.append(line);

                // Get the current version pattern sequence
                String versionString = getAppVersion(currentVersion_PatternSeq, sourceCode.toString());
                if (versionString == null) return null;

                // get version from "htlgb">X.X.X</span>
                return getAppVersion(appVersion_PatternSeq, versionString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private static String getAppVersion(String patternString, String input) {
        try {
            Pattern pattern = Pattern.compile(patternString);
            if (pattern == null) return null;
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) return matcher.group(1);
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}