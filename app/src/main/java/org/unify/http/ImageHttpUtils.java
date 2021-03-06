package org.unify.http;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.http.Header;
import org.json.*;
import org.unify.helper.AppHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lvshun on 15/9/23.
 */
public class ImageHttpUtils {
    private static String LOG_TAG = "http_file_upload";
    
    public static void uploadFiles(String url, String json, final CEImageResponseHandler responseHandler) {
    	System.out.println(url);
    	System.out.println(json);
    	try {
			JSONObject imagesjson = new JSONObject(json);
			JSONArray images = imagesjson.getJSONArray("images");
			JSONObject params = imagesjson.getJSONObject("params");
			
			Map<String, String> fileMap = new HashMap<String, String>();
			for (int i = 0 ; i < images.length(); i ++) {
				String filePath = (String) (images.getJSONObject(i).get("path"));
				fileMap.put(filePath, filePath);
			}
			
			Map<String, String> paramMap = new HashMap<String, String>();
			Iterator<String> keys = params.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				paramMap.put(key, params.getString(key));
			}
			
			postFiles(url, fileMap, paramMap, responseHandler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 以post上传文件
     * @param url 上传url
     * @param files  键为上传表单对应字段的name，值为文件路径
     */
    public static void postFiles(String url, Map<String,String> files, Map<String,String> params, final CEImageResponseHandler responseHandler){
        AsyncHttpClient client = new AsyncHttpClient();
        
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
        	
            @Override
			public void onSuccess(int statusCode,
					cz.msebera.android.httpclient.Header[] headers,
					String responseString) {
            	responseHandler.onSuccess(statusCode, responseString);
			}

			@Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
				responseHandler.onSuccess(statusCode, response.toString());
            }

			@Override
			public void onSuccess(int statusCode,
					cz.msebera.android.httpclient.Header[] headers,
					JSONArray response) {
				responseHandler.onSuccess(statusCode, response.toString());
			}
			
			
			@Override
			public void onFailure(int statusCode,
					cz.msebera.android.httpclient.Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				responseHandler.onSuccess(statusCode, errorResponse.toString());
			}

			@Override
			public void onFailure(int statusCode,
					cz.msebera.android.httpclient.Header[] headers,
					String responseString, Throwable throwable) {
				responseHandler.onSuccess(statusCode, responseString);
			}

			@Override
			public void onFailure(int statusCode,
					cz.msebera.android.httpclient.Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
			}
            
        };
        RequestParams fileParams = getFileParams(files);
        setParams(fileParams, params);
        
        executeUpload(client,url,fileParams,jsonHttpResponseHandler);
    }

    public static void httpPost(String url,Map<String,String> params){
        AsyncHttpClient client = new AsyncHttpClient();
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d(LOG_TAG,"Post请求成功"+response);
            }
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray timeline) {
                try{
                    JSONObject firstEvent = timeline.getJSONObject(0);
                    String tweetText = firstEvent.getString("text");
                    System.out.println(tweetText);
                }catch (JSONException e){

                }
            }
        };

        RequestParams postParams = getParams(params);
        setParams(postParams, params);
        
        executeUpload(client,url,postParams,jsonHttpResponseHandler);
    }

    /**
     * 为上传文件准备参数
     * @param files 键为上传表单对应字段的name，值为文件路径
     * @return
     */
    private static RequestParams getFileParams(Map<String,String > files){
        RequestParams params = new RequestParams();
        final String contentType = RequestParams.APPLICATION_OCTET_STREAM;
        Iterator<Map.Entry<String,String >> entries = files.entrySet().iterator();
        try{
            while (entries.hasNext()) {
                Map.Entry<String,String> entry = entries.next();
                params.put(entry.getKey(), new File(entry.getValue()), contentType, entry.getKey());
            }
        }catch (FileNotFoundException e){
            Log.e(LOG_TAG,"file not found "+e.getMessage());
        }
        params.setHttpEntityIsRepeatable(true);
        params.setUseJsonStreamer(false);
        return params;
    }
    
    /**
     * 准备参数
     * @param params 键为上传表单对应字段的name，值为value
     * @return
     */
    private static RequestParams getParams(Map<String,String > params){
        RequestParams postParams = new RequestParams();
        Iterator<Map.Entry<String,String >> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String,String> entry = entries.next();
            postParams.put(entry.getKey(), entry.getValue());
        }
        return postParams;
    }
    
    private static RequestParams setParams(RequestParams postParams, Map<String,String > params) {
    	Iterator<Map.Entry<String,String >> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String,String> entry = entries.next();
            params.put(entry.getKey(), entry.getValue());
        }
        return postParams;
    }

    /**
     * 执行上传操作
     * @param client
     * @param params
     * @param URL
     * @param responseHandler
     * @return
     */
    private static RequestHandle executeUpload(AsyncHttpClient client,String URL,RequestParams params, ResponseHandlerInterface responseHandler) {
        return client.post(null, URL, params, responseHandler);
    }
    
    public static void downloadImage(String url, final IImageDownloader iImage) {
    	Application app = AppHelper.getAppliction();
		if (app != null)
			initImageLoader(app, true);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage(url, new SimpleImageLoadingListener() {
			@Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				iImage.getBitmap(200, loadedImage, "");
            }
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				iImage.getBitmap(500, null, failReason.toString());
			}
			
		});
    }
    
    /**
     * 下载图片
     * @param url
     * @param handler
     */
	public static void downloadImage(String url, Boolean isCached, final CEImageResponseHandler handler) {
		
		Application app = AppHelper.getAppliction();
		if (app != null)
			initImageLoader(app, isCached);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage(url, new SimpleImageLoadingListener() {
        	@Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//        		String base64 = AppHelper.bitmapToBase64(loadedImage);
        		JSONObject json = new JSONObject();
        		try {
					json.put("url", imageUri);
//					json.put("imageData", base64);
					handler.onSuccess(200, "");
				} catch (JSONException e) {
					e.printStackTrace();
					handler.onFailure(500, e.getMessage());
				}
            }

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				handler.onFailure(500, failReason.toString());
			}
        	
        });
	}
	
	private static void initImageLoader(Context context, boolean cached) {
    	
    	DisplayImageOptions.Builder optionBuild = new DisplayImageOptions.Builder();
    	optionBuild.cacheOnDisk(cached);
    	DisplayImageOptions option = optionBuild.build();
    	
    	ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
    	File cacheDir = StorageUtils.getCacheDirectory(context);
    	System.out.println(cacheDir.getAbsolutePath());
		config.diskCache(new UnlimitedDiskCache(cacheDir));
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		
		config
        .threadPoolSize(3) // default  
        .threadPriority(Thread.NORM_PRIORITY - 1) // default  
        .tasksProcessingOrder(QueueProcessingType.FIFO) // default  
        .denyCacheImageMultipleSizesInMemory()  
        .memoryCache(new LruMemoryCache(5 * 1024 * 1024))  
        .memoryCacheSizePercentage(13) // default  
        .diskCacheSize(100 * 1024 * 1024)  
        .imageDownloader(new BaseImageDownloader(context)) // default  
        .defaultDisplayImageOptions(option) // default  
        .writeDebugLogs();
		
		ImageLoader.getInstance().init(config.build());
	}
}
