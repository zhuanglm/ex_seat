package com.tweebaa.ex_seat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpConnect{

	public String ceshi(StringBuffer result)
	{
		String cs = "class http";
		
		return cs;
	}

	private InputStream HttpRequest(String reqURL,StringBuffer params){
		InputStream is = null;
		PrintWriter printWriter = null;

		try {
			//URL url = new URL(getResources().getString(R.string.Req_URL)+getString(R.string.Design_Total));
			URL url = new URL(reqURL);
			HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
			urlConn.setConnectTimeout(2000);
			//设置输入和输出流
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);

			// 获取URLConnection对象对应的输出流
			if(params!=null) {
				printWriter = new PrintWriter(urlConn.getOutputStream());
				printWriter.write(params.toString());
				printWriter.flush();
			}

			int nReturn = urlConn.getResponseCode();
			if ( nReturn != 200)    //从Internet获取网页,发送请求,将网页以流的形式读回来
				throw new RuntimeException("请求url失败");

			is = urlConn.getInputStream();
			urlConn.disconnect();

			return is;

		} catch (Exception e) {
			//reqURL = "连接错误";
			e.printStackTrace();
			return null;
		}

	}

	public String getXMLData(StringBuffer reqURL)
	{
		String result = reqURL.toString();

		InputStream is = HttpRequest(result,null);
		if(is!=null) {
			XML_SampleParser parser = new XML_SampleParser();
			try {
				List<XML_Node> items = parser.parse(is,"Published","Total");
				result = Integer.toString(items.get(0).Total);
				//reqURL = MainActivity.Inputstr2Str_Reader(is, "GBK"); //文件流输入出文件用outStream.write
				//关闭连接
			} catch (Exception e) {
				result = "parsing error";
				e.printStackTrace();
			}
		}else{
			result = "connecting error";
		}

		return result;
	}

	public String postXMLData(String reqURL,String params) {
		InputStream is = HttpRequest(reqURL,new StringBuffer(params));
		String result = params.toString();

		if (is != null) {
			XML_SampleParser parser = new XML_SampleParser();
			try {
				List<XML_Node> items = parser.parse(is,"mobileApp","error");
				result = Integer.toString(items.get(0).Total);
				//reqURL = MainActivity.Inputstr2Str_Reader(is, "GBK"); //文件流输入出文件用outStream.write
				//关闭连接
			} catch (Exception e) {
				result = "parsing error";
				e.printStackTrace();
			}
		} else {
			result = "connecting error";

		}
		return result;
	}


	/**
	 * 获取URL图片资源
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url){
		URL myFileURL;
		Bitmap bitmap=null;
		try{
			myFileURL = new URL(url);
			//获得连接
			HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
			//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			//连接设置获得数据流
			conn.setDoInput(true);
			//不使用缓存
			conn.setUseCaches(false);
			//这句可有可无，没有影响
			//conn.connect();
			//得到数据流
			InputStream is = conn.getInputStream();
			//解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			//关闭数据流
			is.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		return bitmap;

	}

}
