package com.example.ex_seat;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpConnect{

	public String ceshi(StringBuffer result)
	{
		String cs = "class http";
		
		return cs;
	}
	public String getData(StringBuffer result)
	{
		String reqURL = result.toString();
		try {
    		//URL url = new URL(getResources().getString(R.string.Req_URL)+getString(R.string.Design_Total));
			URL url = new URL(reqURL);
    		HttpURLConnection urlConn=(HttpURLConnection)url.openConnection(); 
    		urlConn.setConnectTimeout(2000);
            //设置输入和输出流   
            urlConn.setDoOutput(true);  
            urlConn.setDoInput(true);  
            
            int nReturn = urlConn.getResponseCode();
            if ( nReturn != 200)    //从Internet获取网页,发送请求,将网页以流的形式读回来
            	throw new RuntimeException("请求url失败");
            
            InputStream is = urlConn.getInputStream();
            XML_SampleParser parser = new XML_SampleParser();  
            List<XML_Node> items = parser.parse(is);
            reqURL = Integer.toString(items.get(0).Total);
            //reqURL = MainActivity.Inputstr2Str_Reader(is, "GBK"); //文件流输入出文件用outStream.write
            //关闭连接  
            urlConn.disconnect();
            
            return reqURL;
            
    	} catch (Exception e) {
    		reqURL = "连接错误";  
    		e.printStackTrace();
    		return reqURL;
    	}
	}

}
