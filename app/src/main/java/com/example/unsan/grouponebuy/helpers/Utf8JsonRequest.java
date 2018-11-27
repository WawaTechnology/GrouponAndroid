package com.example.unsan.grouponebuy.helpers;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

public class Utf8JsonRequest extends StringRequest {
    private Priority priority;
    private Response.Listener<String> listener;

    public Utf8JsonRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.listener = listener;

    }

    public Utf8JsonRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String utf8String = new String(response.data, "UTF-8");

            return Response.success(new String(utf8String), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            // log error
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(String response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}
