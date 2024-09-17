package com.doku.sdk.dokujavalibrary.common;

import com.doku.sdk.dokujavalibrary.exception.GeneralException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ConverterUtils {

    public static String toFormData(Map<String, String> request) {
        StringBuilder v1FormData = new StringBuilder();
        for (Map.Entry<String, String> entry : request.entrySet()) {
            if (v1FormData.length() > 0) {
                v1FormData.append("&");
            }

            try {
                v1FormData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            } catch (Exception e) {
                throw new GeneralException("", "Failed converting to form data");
            }
        }
        return v1FormData.toString();
    }
}
