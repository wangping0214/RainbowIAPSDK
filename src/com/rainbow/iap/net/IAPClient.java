package com.rainbow.iap.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import com.rainbow.iap.entity.IAPMethodType;
import com.rainbow.iap.entity.PurchaseOrder;
import com.rainbow.iap.entity.PurchaseOrderRequest;

import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class IAPClient
{
	private static final String TAG = "RainbowIAPSDK";
	private static final String	IAPServerURL = "http://182.92.65.140/IAPServer/PurchaseOrderService";
	private static final String AlipaySignURL = "http://www.chaimiyouxi.com/IAPServer/AlipaySignService";
	private static final IAPClient _instance;
	
	static
	{
		_instance = new IAPClient();
	}
	
	public static IAPClient getInstance()
	{
		return _instance;
	}
	
	private AndroidHttpClient _httpClient;
	
	public IAPClient()
	{
		_httpClient = AndroidHttpClient.newInstance("RainbowIAP");
	}
	
	public void getPurchaseOrder(IAPMethodType type, String productId, String customData, Handler handler)
	{
		final IAPMethodType finalType = type;
		final String finalProductId = productId;
		final String finalCustomData = customData;
		final Handler finalHandler = handler;
		new Thread() 
		{
			@Override
			public void run()
			{
				PurchaseOrderRequest request = new PurchaseOrderRequest(finalType, finalProductId, finalCustomData);
				JSONObject jsonObj = new JSONObject();
				try
				{
					request.marshal(jsonObj);
					HttpPost post = new HttpPost(IAPServerURL);
					StringEntity strEntity = new StringEntity(jsonObj.toString(),"UTF-8");
					Log.d(TAG, jsonObj.toString());
					post.setEntity(strEntity);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String orderStr = _httpClient.execute(post, responseHandler);
					Log.d(TAG, orderStr);
					PurchaseOrder purchaseOrder = new PurchaseOrder();
					purchaseOrder.unmarshal(new JSONObject(orderStr));
					Message msg = new Message();
					msg.obj = purchaseOrder;
					finalHandler.sendMessage(msg);
				} catch (JSONException e)
				{
					e.printStackTrace();
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void alipaySign(String content, Handler handler)
	{
		final String finalContent = content;
		final Handler finalHandler = handler;
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					HttpPost post = new HttpPost(AlipaySignURL);
					StringEntity strEntity = new StringEntity(finalContent,"UTF-8");
					post.setEntity(strEntity);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String signStr = _httpClient.execute(post, responseHandler);
					Log.d(TAG, "AlipaySignStr: " + signStr);
					Message msg = new Message();
					msg.obj = signStr;
					finalHandler.sendMessage(msg);
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
}
