package com.vocinno.centanet.tools;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.utils.MethodsJson;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.FileNameMap;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zr on 2016/5/9.
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;
    public static String TAG;
    private Context context;
    private OkHttpClientManager()
    {
        TAG=this.getClass().getSimpleName();
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(18, TimeUnit.SECONDS);
        //cookie enabled
//        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }
    public static OkHttpClientManager getInstance()
    {
        if (mInstance == null)
        {
            synchronized (OkHttpClientManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }
    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Param... params)
    {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }
    private void _postJsonAsyn(String url, final ResultCallback callback,String json)
    {
        MyUtils.LogI(url);
        Request request =jsonPostRequest(url, json);
        deliveryResult(callback, request);
    }
    private void requestForGet(String url, final ResultCallback callback)
    {
        Request request = buildGetRequest(url);
        deliveryResult(callback, request);
    }
    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params)
    {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private Response _post(String url, Param... params) throws IOException
    {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    private String _postAsString(String url, Param... params) throws IOException
    {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException
    {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey, Param... params) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException
    {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }
    private void _uploadImg(String url, File[] files, ResultCallback callback) throws IOException
    {
        Request request = buildMultipartFormRequest(url, files);
        deliveryResult(callback, request);
    }
    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String destFileDir, final ResultCallback callback)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(false, request, e, callback);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(true, response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }
    private void _downloadAsyn(final String url, final String destFileDir, final Callback callback)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    private String getFileName(String path)
    {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    public static void getAsyn(String url, ResultCallback callback)
    {
        getInstance()._getAsyn(url, callback);
    }

    public static Response post(String url, Param... params) throws IOException
    {
        return getInstance()._post(url, params);
    }

    public static String postAsString(String url, Param... params) throws IOException
    {
        return getInstance()._postAsString(url, params);
    }

    public static void postAsyn(String url, final ResultCallback callback, Param... params)
    {
        getInstance()._postAsyn(url, callback, params);
    }
    public static void postJsonAsyn(String url,Object object, final ResultCallback callback)
    {
        String result = new Gson().toJson(object);
        MyUtils.LogI(result);
        getInstance()._postJsonAsyn(url+NetWorkConstant.getStrToken(), callback,result);
    }

    public static void postAsyn(String url, Map<String, String> params, final ResultCallback callback)
    {
//        MyLoadDialog.showDialog();
        Map<String, String> tokenMap=NetWorkConstant.getToken();
        if(tokenMap!=null&&tokenMap.size()>0){
            params.putAll(tokenMap);
        }
        getInstance()._postAsyn(url, callback, params);
    }
    public static void getAsyn(String url, Map<String, String> params, final ResultCallback callback)
    {
//        MyLoadDialog.showDialog();
        Map<String, String> tokenMap=NetWorkConstant.getToken();
        if(tokenMap!=null&&tokenMap.size()>0){
            params.putAll(tokenMap);
        }
        List<BasicNameValuePair> NVPList=new ArrayList<BasicNameValuePair>();
        BasicNameValuePair pair=null;
        for (Map.Entry<String, String> temp : params.entrySet()) {
            pair=new BasicNameValuePair(temp.getKey(), temp.getValue());
            NVPList.add(pair);
            MyUtils.LogI(temp.getKey(),temp.getValue());
        }
        url=attachHttpGetParams(url,NVPList);
        getInstance().requestForGet(url, callback);
    }

    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException
    {
        return getInstance()._post(url, files, fileKeys, params);
    }

    public static Response post(String url, File file, String fileKey) throws IOException
    {
        return getInstance()._post(url, file, fileKey);
    }

    public static Response post(String url, File file, String fileKey, Param... params) throws IOException
    {
        return getInstance()._post(url, file, fileKey, params);
    }

    public static void postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException
    {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }
    public static void uploadImg(String url, File[] files,ResultCallback callback) throws IOException
    {
        getInstance()._uploadImg(url,files,callback);
    }

    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException
    {
        getInstance()._postAsyn(url, callback, file, fileKey);
    }


    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException
    {
        getInstance()._postAsyn(url, callback, file, fileKey, params);
    }



    public static void downloadApp(String url, String destDir, ResultCallback callback)
    {
        getInstance()._downloadAsyn(url, destDir, callback);
    }
    public static void downloadApp(String url, String destDir,Callback callback)
    {
        getInstance()._downloadAsyn(url, destDir, callback);
    }
    //****************************


    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params)
    {
        params = validateParam(params);

        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);

        for (Param param : params)
        {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null)
        {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
    private Request buildMultipartFormRequest(String url, File[] files)
    {

        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);

        if (files != null)
        {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"file1\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params)
    {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params)
    {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries)
        {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();
    private void deliveryResult(final Callback callback, Request request)
    {
        mOkHttpClient.newCall(request).enqueue(callback);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }
    private void deliveryResult(final ResultCallback callback, Request request)
    {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(false, request, e, callback);
               /* e.getMessage();
                e.getCause();
                e.getLocalizedMessage();
                Log.i("e.getMessage()", "e.getMessage()==" + e.getMessage()+
                        "e.getStackTrace()==" + e.getStackTrace()+"e.getLocalizedMessage()==" + e.getLocalizedMessage()+"e.getCause()==" + e.getCause().getMessage());
           */
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    Log.i("response", "return==" + string);
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(true, response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailedStringCallback(true, response.request(), e, callback);
                }

            }
        });
    }

    private void sendFailedStringCallback(final boolean isException ,final Request request, final Exception e, final ResultCallback callback)
    {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    if (isException) {
                        MyToast.showToast("net work error");
                    } else {
                        if ("timeout".equalsIgnoreCase(e.getMessage())) {
                            MyToast.showToast("请求超时,请稍后再试");
                        } else if (e instanceof ConnectException) {
                            MyToast.showToast("请检查网络之后再试");
                        } else if (e instanceof SocketTimeoutException) {
                            if (e.getMessage() != null && e.getMessage().indexOf("after") >= 0) {
                                MyToast.showToast("请检查网络之后再试");
                            } else {
                                MyToast.showToast("请求超时,请稍后再试");
                            }
                        } else {
                            MyToast.showToast("请检查网络之后再试");
                        }
                        /*if(e.getMessage().indexOf("unreachable")>=0){

                        }else{
                            MyToast.showToast("请求超时,请稍后再试");
                        }*/
                    }
                    callback.onError(request, e);
                }
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback)
    {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    try{
                        JSReturn jsReturn = MethodsJson.jsonToJsReturn(object.toString(),Object.class);
                        if(jsReturn.isSuccess()){
                            callback.onResponse(object);
                        }else{
                            callback.onError(null,null);
                            MyToast.showToast(jsReturn.getMsg());
                        }
                    }catch (Exception e){
                        callback.onError(null,e);
                    }
                }
            }
        });
    }

    public static String formatParams(List<BasicNameValuePair> params){
        return URLEncodedUtils.format(params, "UTF-8");
    }
    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params){
        return url + "?" + formatParams(params);
    }
    private Request buildGetRequest(String url)
    {
        return new Request.Builder()
                .url(url)
                .build();
    }
    private Request buildPostRequest(String url, Param[] params)
    {
        if (params == null)
        {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params)
        {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    private Request jsonPostRequest(String url,String json)
    {
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();


    }

    public static abstract class ResultCallback<T>
    {
        Type mType;

        public ResultCallback()
        {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass)
        {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class)
            {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    public static class Param
    {
        public Param()
        {
        }

        public Param(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }
}
