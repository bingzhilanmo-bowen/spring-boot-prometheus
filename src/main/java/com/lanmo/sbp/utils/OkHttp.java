package com.lanmo.sbp.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.lanmo.sbp.exception.GlobalCode;
import com.lanmo.sbp.exception.SbpException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class OkHttp {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS).build();

    public enum OkHttpMethod {
        POST, PUT, DELETE
    }

    public static <T> T get(String url, Map<String, String> params, Type clazz) {
        url = addQuery(url, params);
        return getMethod(url, clazz);

    }

    //################### GET METHOD ###########################

    private static <T> T getMethod(String url, Type clazz) {
        Request request = new Request.Builder().get().url(url).build();
        T value = null;
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                String message = String.format("query request %s failed: [code %d, body %s]",
                    response.request().toString(), response.code(),
                    response.body() != null ? response.body().string() : "");
                throw new SbpException(GlobalCode.RPC_ERROR, message);
            }
            value = JSONObject.parseObject(response.body().bytes(), clazz);
        } catch (IOException e) {
            throw new SbpException(GlobalCode.RPC_ERROR, e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return value;
    }

    public static String getMethod(String url) {
        Request request = new Request.Builder().get().url(url).build();
        String value = null;
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                String message = String.format("query request %s failed: [code %d, body %s]",
                    response.request().toString(), response.code(),
                    response.body() != null ? response.body().string() : "");
                throw new SbpException(GlobalCode.RPC_ERROR, message);
            }
            value = response.body().string();
        } catch (IOException e) {
            throw new SbpException(GlobalCode.RPC_ERROR, e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return value;
    }


    //################### PUT METHOD ###########################
    public static <T> T putMethod(String url, Map<String, Object> data, Type clazz) {
        return putMethod(url, JSONObject.toJSONString(data), clazz);
    }

    private static <T> T putMethod(String url, String jsonData, Type clazz) {
        Response response = null;
        T value = null;
        try {
            RequestBody requestBody = getRequestBody(jsonData);
            Request request = new Request.Builder().url(url).put(requestBody).build();
            response = httpClient.newCall(request).execute();
            value = getResp(response, clazz);
            log.debug("url {},put params {},result {}", url, jsonData,
                JSONObject.toJSONString(value));
            return value;
        } catch (IOException e) {
            log.error("url {},put params {},error {}", url, jsonData, e);
            throw new SbpException(GlobalCode.RPC_ERROR, e.getMessage());
        } finally {
            closeResponse(response);
        }
    }

    public static <T> T postMethod(String url, Map<String, Object> data, Type clazz) {
        return postMethod(url, JSONObject.toJSONString(data), clazz);
    }

    private static <T> T postMethod(String url, String jsonData, Type clazz) {
        Response response = null;
        T value = null;
        try {
            RequestBody requestBody = getRequestBody(jsonData);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            response = httpClient.newCall(request).execute();
            value = getResp(response, clazz);
            log.info("post url {},put params {},result {}", url, jsonData,
                JSONObject.toJSONString(value));
            return value;
        } catch (IOException e) {
            log.error("url {},put params {},error {}", url, jsonData, e);
            throw new SbpException(GlobalCode.RPC_ERROR, e.getMessage());
        } finally {
            closeResponse(response);
        }
    }


    public static <T> T dynamic(String url,
        Map<String, String> params,
        Map<String, Object> paramsBody,
        Map<String, String> headers,
        OkHttpMethod okHttpMethod,
        Type clazz) {
        url = addQuery(url, params);
        Request.Builder builder = new Request.Builder();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((k, v) -> builder.header(k, v));
        }

        if (null != paramsBody) {
            RequestBody body = RequestBody.create(JSON, JSONObject.toJSONString(paramsBody));
            switch (okHttpMethod) {
                case POST:
                    builder.post(body);
                    break;
                case PUT:
                    builder.put(body);
                    break;
                case DELETE:
                    builder.delete(body);
                    break;
                default:
                    builder.post(body);
                    break;

            }
        } else {
            switch (okHttpMethod) {
                case DELETE:
                default:
                    builder.delete();
                    break;

            }
        }

        T value = null;
        Request request = builder.url(url).build();
        try {
            Response response = httpClient.newCall(request).execute();
            value = getResp(response, clazz);
        } catch (IOException e) {
            log.error("rpc error:{}", e.getCause());
        }
        return value;
    }


    private static RequestBody getRequestBody(String jsonData) {
        return RequestBody.create(JSON, jsonData);
    }

    /**
     * 获取返回对象.
     */
    private static <T> T getResp(Response response, Type clazz) throws IOException {
        T value = null;
        if (!response.isSuccessful()) {
            String message = String
                .format("request %s failed: [code %d, body %s]", response.request().toString(),
                    response.code(), response.body() != null ? response.body().string() : "");

            throw new SbpException(GlobalCode.RPC_ERROR, message);
        }
        value = JSONObject.parseObject(response.body().string(), clazz);
        return value;
    }

    private static String addQuery(String url, Map<String, String> queries) {
        return url + "?" + Joiner.on("&").join(queries.entrySet().stream().map(
            (Function<Map.Entry<String, String>, Object>) e -> Joiner.on("=")
                .join(e.getKey(), e.getValue())).collect(Collectors.toList()));
    }

    private static void closeResponse(Response response) {

        if (response != null) {
            response.close();
        }
    }


}
