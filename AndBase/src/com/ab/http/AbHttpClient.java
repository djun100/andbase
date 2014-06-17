/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.http;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppException;
import com.ab.global.AbConstant;
import com.ab.task.AbThreadFactory;
import com.ab.util.AbAppUtil;
import com.ab.util.AbFileUtil;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbHttpClient.java 
 * 描述：Http客户端
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-13 上午9:00:52
 */
public class AbHttpClient {
	
	/** 日志标记. */
    private static final String TAG = "AbHttpClient";
    
    /** 上下文. */
	private Context mContext;
	
	/** 线程执行器. */
	public static Executor mExecutorService = null;
	
	private static final String ENCODE = HTTP.UTF_8;
    private static final String USER_AGENT = "User-Agent";
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 BIDUBrowser/6.x Safari/537.31";
    
    /** 最大连接数. */
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    
    /** 超时时间. */
    public static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    
    /** 重试. */
    private static final int DEFAULT_MAX_RETRIES = 5;
    
    /** 缓冲大小. */
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    
    /** 缓冲大小. */
    private static final int BUFFER_SIZE = 4096;
    
    /** 成功. */
    protected static final int SUCCESS_MESSAGE = 0;
    
    /** 失败. */
    protected static final int FAILURE_MESSAGE = 1;
    
    /** 开始. */
    protected static final int START_MESSAGE = 2;
    
    /** 完成. */
    protected static final int FINISH_MESSAGE = 3;
    
    /** 进行中. */
    protected static final int PROGRESS_MESSAGE = 4;
    
    /** 重试. */
    protected static final int RETRY_MESSAGE = 5;
    
    /** 超时时间. */
	private int timeout = DEFAULT_SOCKET_TIMEOUT;
	
	/** debug true表示是内网. */
	private boolean debug = false;
	
	/** 通用证书. 如果要求HTTPS连接，则使用SSL打开连接*/
	private boolean isOpenEasySSL = true;
	
	/** HTTP 上下文*/
	private HttpContext mHttpContext = null;
	
    
    /**
     * 初始化.
     *
     * @param context the context
     */
	public AbHttpClient(Context context) {
	    mContext = context;
		mExecutorService =  AbThreadFactory.getExecutorService();
		mHttpContext = new BasicHttpContext();
	}
	
	
	/**
	 * 描述：无参数的get请求.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void get(String url, AbHttpResponseListener responseListener) {
		get(url,null,responseListener);          
	}

	/**
	 * 描述：带参数的get请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void get(final String url,final AbRequestParams params,final AbHttpResponseListener responseListener) {
		
		responseListener.setHandler(new ResponderHandler(responseListener));
		mExecutorService.execute(new Runnable() { 
    		public void run() {
    			try {
    				doGet(url,params,responseListener);
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}
    		}                 
    	});                
	}
	
	/**
	 * 描述：无参数的post请求.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void post(String url, AbHttpResponseListener responseListener) {
		post(url,null,responseListener);          
	}
	
	/**
	 * 描述：带参数的post请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void post(final String url,final AbRequestParams params,
			final AbHttpResponseListener responseListener) {
		responseListener.setHandler(new ResponderHandler(responseListener));
		mExecutorService.execute(new Runnable() { 
    		public void run() {
    			try {
    				doPost(url,params,responseListener);
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}
    		}                 
    	});      
	}
	
	
	/**
	 * 描述：执行get请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	private void doGet(String url,AbRequestParams params,AbHttpResponseListener responseListener){
		  try {
			  
			  responseListener.sendStartMessage();
			  
			  if(!debug && !AbAppUtil.isNetworkAvailable(mContext)){
					responseListener.sendFailureMessage(AbConstant.CONNECT_FAILURE_CODE,AbConstant.CONNECTEXCEPTION, new AbAppException(AbConstant.CONNECTEXCEPTION));
			        return;
			  }
			  
			  //HttpGet连接对象  
			  if(params!=null){
				  url += params.getParamString();
			  }
			  HttpGet httpRequest = new HttpGet(url);  
			  
              BasicHttpParams httpParams = getHttpParams();
		      
		      // 设置请求参数
			  httpRequest.setParams(httpParams);
			  
			  //默认参数
              HttpClientParams.setRedirecting(httpParams, false);
              HttpClientParams.setCookiePolicy(httpParams,
                      CookiePolicy.BROWSER_COMPATIBILITY);
              httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, null);

              //取得默认的HttpClient
      	      HttpClient httpClient = getHttpClient(httpParams); 
      	      
			  //请求HttpClient，取得HttpResponse  
			  HttpResponse httpResponse = httpClient.execute(httpRequest);  
			  //请求成功  
			  int statusCode = httpResponse.getStatusLine().getStatusCode();
			  
			  //取得返回的字符串  
			  HttpEntity  mHttpEntity = httpResponse.getEntity();
			  if (statusCode == HttpStatus.SC_OK){  
				  if(responseListener instanceof AbStringHttpResponseListener){
					  String content = EntityUtils.toString(mHttpEntity);
					  ((AbStringHttpResponseListener)responseListener).sendSuccessMessage(statusCode, content);
				  }else if(responseListener instanceof AbBinaryHttpResponseListener){
					  readResponseData(mHttpEntity,((AbBinaryHttpResponseListener)responseListener));
				  }else if(responseListener instanceof AbFileHttpResponseListener){
					  //获取文件名
					  String fileName = AbFileUtil.getCacheFileNameFromUrl(url, httpResponse);
					  writeResponseData(mHttpEntity,fileName,((AbFileHttpResponseListener)responseListener));
				  }
			  }else{
				  String content = EntityUtils.toString(mHttpEntity);
				  responseListener.sendFailureMessage(statusCode, content, new AbAppException(AbConstant.UNKNOWNHOSTEXCEPTION));
			  }
		} catch (Exception e) {
			e.printStackTrace();
			//发送失败消息
			responseListener.sendFailureMessage(AbConstant.UNTREATED_CODE,e.getMessage(),new AbAppException(e));
		}finally{
			responseListener.sendFinishMessage();
		}
	}
	
	/**
	 * 描述：执行post请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	private void doPost(String url,AbRequestParams params,AbHttpResponseListener responseListener){
		  try {
			  responseListener.sendStartMessage();
			  
			  if(!debug && !AbAppUtil.isNetworkAvailable(mContext)){
					responseListener.sendFailureMessage(AbConstant.CONNECT_FAILURE_CODE,AbConstant.CONNECTEXCEPTION, new AbAppException(AbConstant.CONNECTEXCEPTION));
			        return;
			  }
			  
			  //HttpPost连接对象  
		      HttpPost httpRequest = new HttpPost(url);  
		      if(params != null){
		    	  //使用NameValuePair来保存要传递的Post参数设置字符集 
			      HttpEntity httpentity = params.getEntity(responseListener);
			      //请求httpRequest  
			      httpRequest.setEntity(httpentity); 
			  }
		      
		      BasicHttpParams httpParams = getHttpParams();
		      
		      httpRequest.setParams(httpParams);
              
      	      //默认参数
              HttpClientParams.setRedirecting(httpParams, false);
              HttpClientParams.setCookiePolicy(httpParams,
                      CookiePolicy.BROWSER_COMPATIBILITY);
              httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, null);

              //取得默认的HttpClient
      	      HttpClient httpClient = getHttpClient(httpParams);  
		      
		      //取得HttpResponse
		      HttpResponse httpResponse = httpClient.execute(httpRequest);  
			  //请求成功  
			  int statusCode = httpResponse.getStatusLine().getStatusCode();
			  //取得返回的字符串  
			  HttpEntity  mHttpEntity = httpResponse.getEntity();
			  if (statusCode == HttpStatus.SC_OK){  
				  if(responseListener instanceof AbStringHttpResponseListener){
					  String content = EntityUtils.toString(mHttpEntity);
					  ((AbStringHttpResponseListener)responseListener).sendSuccessMessage(statusCode, content);
				  }else if(responseListener instanceof AbBinaryHttpResponseListener){
					  readResponseData(mHttpEntity,((AbBinaryHttpResponseListener)responseListener));
				  }else if(responseListener instanceof AbFileHttpResponseListener){
					  //获取文件名
					  String fileName = AbFileUtil.getCacheFileNameFromUrl(url, httpResponse);
					  writeResponseData(mHttpEntity,fileName,((AbFileHttpResponseListener)responseListener));
				  }
			  }else{
				  String content = EntityUtils.toString(mHttpEntity);
				  responseListener.sendFailureMessage(statusCode, content, new AbAppException(AbConstant.UNKNOWNHOSTEXCEPTION));
			  }
			  
		} catch (Exception e) {
			e.printStackTrace();
			//发送失败消息
			responseListener.sendFailureMessage(AbConstant.UNTREATED_CODE,e.getMessage(),new AbAppException(e));
		}finally{
			responseListener.sendFinishMessage();
		}
	}
	
	
	/**
	 * 描述：写入文件并回调进度.
	 *
	 * @param entity the entity
	 * @param name the name
	 * @param responseListener the response listener
	 */
    public void writeResponseData(HttpEntity entity,String name,AbFileHttpResponseListener responseListener){
        
    	if(entity == null){
        	return;
        }
    	
    	if(responseListener.getFile() == null){
    		//创建缓存文件
    		responseListener.setFile(name);
        }
    	
    	InputStream inStream = null;
    	FileOutputStream outStream = null;
        try {
	        inStream = entity.getContent();
	        long contentLength = entity.getContentLength();
	        outStream = new FileOutputStream(responseListener.getFile());
	        if (inStream != null) {
	          
	              byte[] tmp = new byte[BUFFER_SIZE];
	              int l, count = 0;
	              while ((l = inStream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
	                  count += l;
	                  outStream.write(tmp, 0, l);
	                  responseListener.sendProgressMessage(count, (int) contentLength);
	              }
	        }
	        responseListener.sendSuccessMessage(200);
	    }catch(Exception e){
	        e.printStackTrace();
	        //发送失败消息
			responseListener.sendFailureMessage(AbConstant.RESPONSE_TIMEOUT_CODE,AbConstant.SOCKETTIMEOUTEXCEPTION,e);
	    } finally {
        	try {
        		if(inStream!=null){
        			inStream.close();
        		}
        		if(outStream!=null){
        			outStream.flush();
    				outStream.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        
    }
    
    /**
     * 描述：转换为二进制并回调进度.
     *
     * @param entity the entity
     * @param responseListener the response listener
     */
    public void readResponseData(HttpEntity entity,AbBinaryHttpResponseListener responseListener){
        
    	if(entity == null){
        	return;
        }
    	
        InputStream inStream = null;
        ByteArrayOutputStream outSteam = null; 
       
    	try {
    		inStream = entity.getContent();
			outSteam = new ByteArrayOutputStream(); 
			long contentLength = entity.getContentLength();
			if (inStream != null) {
				  int l, count = 0;
				  byte[] tmp = new byte[BUFFER_SIZE];
				  while((l = inStream.read(tmp))!=-1){ 
					  count += l;
			          outSteam.write(tmp,0,l); 
			          responseListener.sendProgressMessage(count, (int) contentLength);

			     } 
			  }
			 responseListener.sendSuccessMessage(200,outSteam.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			//发送失败消息
			responseListener.sendFailureMessage(AbConstant.RESPONSE_TIMEOUT_CODE,AbConstant.SOCKETTIMEOUTEXCEPTION,e);
		}finally{
			try {
				if(inStream!=null){
					inStream.close();
				}
				if(outSteam!=null){
					outSteam.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
        
    }
    
    /**
     * 描述：设置连接超时时间.
     *
     * @param timeout 毫秒
     */
    public void setTimeout(int timeout) {
    	this.timeout = timeout;
	}
    
    
    /**
     * 描述：调试模式.
     *
     * @return true, if is debug
     */
    public boolean isDebug() {
		return debug;
	}

    /**
     * 描述：是否为调试模式.
     *
     * @param debug the new debug
     */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * © 2012 amsoft.cn
	 * 名称：ResponderHandler.java 
	 * 描述：请求返回
	 *
	 * @author 还如一梦中
	 * @version v1.0
	 * @date：2013-11-13 下午3:22:30
	 */
    private static class ResponderHandler extends Handler {
		
		/** The response. */
		private Object[] response;
		
		/** The response listener. */
		private AbHttpResponseListener responseListener;
		

		/**
		 * Instantiates a new responder handler.
		 *
		 * @param responseListener the response listener
		 */
		public ResponderHandler(AbHttpResponseListener responseListener) {
			this.responseListener = responseListener;
		}

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
			case SUCCESS_MESSAGE:
				response = (Object[]) msg.obj;
				
				if (response != null){
					if(responseListener instanceof AbStringHttpResponseListener){
						if(response.length >= 2){
							((AbStringHttpResponseListener)responseListener).onSuccess((Integer) response[0],(String)response[1]);
						}else{
							Log.e(TAG, "SUCCESS_MESSAGE "+AbConstant.MISSINGPARAMETERS);
						}
						
					}else if(responseListener instanceof AbBinaryHttpResponseListener){
						if(response.length >= 2){ 
						    ((AbBinaryHttpResponseListener)responseListener).onSuccess((Integer) response[0],(byte[])response[1]);
						}else{
							Log.e(TAG, "SUCCESS_MESSAGE "+AbConstant.MISSINGPARAMETERS);
						}
					}else if(responseListener instanceof AbFileHttpResponseListener){
						
						if(response.length >= 1){ 
							AbFileHttpResponseListener mAbFileHttpResponseListener = ((AbFileHttpResponseListener)responseListener);
							mAbFileHttpResponseListener.onSuccess((Integer) response[0],mAbFileHttpResponseListener.getFile());
						}else{
							Log.e(TAG, "SUCCESS_MESSAGE "+AbConstant.MISSINGPARAMETERS);
						}
						
					}
				}
                break;
			case FAILURE_MESSAGE:
				 response = (Object[]) msg.obj;
                 if (response != null && response.length >= 3){
                	 //异常转换为可提示的
                	 AbAppException exception = new AbAppException((Exception) response[2]);
					 responseListener.onFailure((Integer) response[0], (String) response[1], exception);
                 }else{
                    Log.e(TAG, "FAILURE_MESSAGE "+AbConstant.MISSINGPARAMETERS);
                 }
	             break;
			case START_MESSAGE:
				responseListener.onStart();
				break;
			case FINISH_MESSAGE:
				responseListener.onFinish();
				break;
			case PROGRESS_MESSAGE:
				 response = (Object[]) msg.obj;
	             if (response != null && response.length >= 2){
	            	 responseListener.onProgress((Integer) response[0], (Integer) response[1]);
			     }else{
	                 Log.e(TAG, "PROGRESS_MESSAGE "+AbConstant.MISSINGPARAMETERS);
			     }
	             break;
			case RETRY_MESSAGE:
				responseListener.onRetry();
				break;
			default:
				break;
		   }
		}
		
	}
    
    /**
     * HTTP参数配置
     * @return
     */
    public BasicHttpParams getHttpParams(){
    	
    	BasicHttpParams httpParams = new BasicHttpParams();
        
        // 设置每个路由最大连接数
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(30);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		// 从连接池中取连接的超时时间，设置为1秒
	    ConnManagerParams.setTimeout(httpParams, timeout);
	    ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
	    ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
	    // 读响应数据的超时时间
	    HttpConnectionParams.setSoTimeout(httpParams, timeout);
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    HttpConnectionParams.setTcpNoDelay(httpParams, true);
	    HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

	    HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setUserAgent(httpParams, String.format("andbase-http/%s (http://www.amsoft.cn/)", 1.0));
	    // 设置请求参数
		return httpParams;
	      
    }
    
    /**
     * 获取HttpClient，自签名的证书，如果想做签名参考AuthSSLProtocolSocketFactory类
     * @param httpParams
     * @return
     */
    public HttpClient getHttpClient(BasicHttpParams httpParams){
    	HttpClient httpClient = null;
    	if(isOpenEasySSL){
    		 // 支持https的   SSL自签名的实现类
    	     EasySSLProtocolSocketFactory easySSL = new EasySSLProtocolSocketFactory();
    	      
    	     // Sets up the http part of the service.
             SchemeRegistry supportedSchemes = new SchemeRegistry();

             // Register the "http" protocol scheme, it is required
             // by the default operator to look up socket factories.
             SocketFactory sf = PlainSocketFactory.getSocketFactory();
             supportedSchemes.register(new Scheme("http", sf, 80));
             supportedSchemes.register(new Scheme("https",easySSL, 443));

             ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(
                    httpParams, supportedSchemes);
            
             //取得HttpClient
             httpClient = new DefaultHttpClient(connectionManager, httpParams);
    	}else{
    		 httpClient = new DefaultHttpClient();  
    	}
    	 
 	    return httpClient;
    }

    /**
     * 是否打开ssl 自签名
     */
    public boolean isOpenEasySSL(){
        return isOpenEasySSL;
    }


    /**
     * 打开ssl 自签名
     * @param isOpenEasySSL
     */
    public void setOpenEasySSL(boolean isOpenEasySSL){
        this.isOpenEasySSL = isOpenEasySSL;
    }
    
    /**
     * 使用ResponseHandler接口处理响应,支持重定向
     */
    private class RedirectionResponseHandler implements ResponseHandler<String>{
        @Override
        public String handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException{
            HttpUriRequest request = (HttpUriRequest) mHttpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            //200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    //如果压缩要解压
                    Header header = entity.getContentEncoding();
                    if (header != null){
                        String contentEncoding = header.getValue();
                        if (contentEncoding != null){
                            if (contentEncoding.contains("gzip")){
                                entity = new AbGzipDecompressingEntity(entity);
                            }
                        }
                    }
                    String charset = EntityUtils.getContentCharSet(entity) == null ? ENCODE : EntityUtils.getContentCharSet(entity);
                    
                    String responseBody = new String(EntityUtils.toByteArray(entity), charset);
                    
                    return responseBody;
                }
                
            }
            //301 302
            else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY
                    || response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY){
                // 从头中取出转向的地址
                Header locationHeader = response.getLastHeader("location");
                String location = locationHeader.getValue();
                if (request.getMethod().equalsIgnoreCase(HTTP_POST)){
                    //return post(location, null, "");
                }
                else if (request.getMethod().equalsIgnoreCase(HTTP_GET)){
                    //return getWithoutCache(location, null, "");
                }
            }
            return null;
        }
    }
    
    /**
     * 自动重试处理
     */
    private HttpRequestRetryHandler mRequestRetryHandler = new HttpRequestRetryHandler(){
        // 自定义的恢复策略
        public boolean retryRequest(IOException exception, int executionCount,
                HttpContext context){
            // 设置恢复策略，在发生异常时候将自动重试3次
            if (executionCount >= 3){
                // Do not retry if over max retry count
                return false;
            }
            if (exception instanceof NoHttpResponseException){
                // Retry if the server dropped connection on us
                return true;
            }
            if (exception instanceof SSLHandshakeException){
                // Do not retry on SSL handshake exception
                return false;
            }
            HttpRequest request = (HttpRequest) context
                    .getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent){
                // Retry if the request is considered idempotent
                return true;
            }
            if (exception != null){
                return true;
            }
            return false;
        }
    };
    
	
}
