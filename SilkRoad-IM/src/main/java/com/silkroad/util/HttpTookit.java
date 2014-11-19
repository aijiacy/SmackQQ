package com.silkroad.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



/**
* HTTP工具箱
*
* @author leizhimin 2009-6-19 16:36:18
*/
public final class HttpTookit {
	
	  public static  String  doGet(String urlStr) throws Exception{
	       
	         System.out.println(urlStr);
	         URL url = new URL(urlStr);
	         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	         conn.setRequestMethod("GET");
	         conn.setRequestProperty("Content-Type","text/html; charset=UTF-8");
	        
	         conn.connect();
	         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         String line ;
	         String result ="";
	         while( (line =br.readLine()) != null ){
	             result += line;
	         }
	         
	         br.close();
			return result;
	     }
	  
		public static String postXml(String url,String xml) throws Exception{
			
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(url);
	        StringEntity myEntity = new StringEntity(xml, "GBK");
	        httppost.addHeader("Content-Type", "text/xml");
	        httppost.setEntity(myEntity);
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity resEntity = response.getEntity();
	        InputStreamReader reader = new InputStreamReader(resEntity.getContent(), "GBK");
	        
	        char[] buff = new char[1024];
	        int length = 0;
	        
	        String html="";
	        while ((length = reader.read(buff)) != -1) {
	        	html=html+ new String(buff, 0, length);
	        }
	        httpclient.getConnectionManager().shutdown();

			return html;
		}

		
		 public static String sendGet(String url,String data) throws ClientProtocolException, IOException  
		    {  
		        // 创建HttpClient实例     
		        HttpClient httpclient = new DefaultHttpClient();  
		        // 创建Get方法实例     
		        HttpGet httpgets = new HttpGet(url+data);    
		        HttpResponse response = httpclient.execute(httpgets);    
		        HttpEntity entity = response.getEntity();    
		        if (entity != null) {    
		            InputStream instreams = entity.getContent();    
		            String str = convertStreamToString(instreams);  
		            httpgets.abort();    
		            return str;  
		        }  
		        return null;  
		    }  
		      
		    public static String convertStreamToString(InputStream is) {      
		        StringBuilder sb1 = new StringBuilder();      
		        byte[] bytes = new byte[4096];    
		        int size = 0;    
		          
		        try {      
		            while ((size = is.read(bytes)) > 0) {    
		                String str = new String(bytes, 0, size, "UTF-8");    
		                sb1.append(str);    
		            }    
		        } catch (IOException e) {      
		            e.printStackTrace();      
		        } finally {      
		            try {      
		                is.close();      
		            } catch (IOException e) {      
		               e.printStackTrace();      
		            }      
		        }      
		        return sb1.toString();      
		    }  
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

	public static void main(String args[]) throws Exception{
		
		//HttpTookit.doGet("http://128.129.99.28:8080/uag/client/customer!getCustomerPro.action?card_number=4111801298&user_name=lan&code=123456cessstomeaa&interface=2&type=2");
	}
     
}