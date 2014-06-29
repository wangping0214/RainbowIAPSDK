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
	
	public void getPurchaseOrder(IAPMethodType type, String productId, Handler handler)
	{
		final IAPMethodType finalType = type;
		final String finalProductId = productId;
		final Handler finalHandler = handler;
		new Thread() 
		{
			@Override
			public void run()
			{
				PurchaseOrderRequest request = new PurchaseOrderRequest(finalType, finalProductId);
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
	
	public PurchaseOrder getPurchaseOrder(IAPMethodType type, String productId)
	{
		/*
		PurchaseOrder order = new PurchaseOrder();
		order.setProductId(productId);
		switch (type)
		{
		case IAP_METHOD_TYPE_CHINA_UNICOM:
			order.setName("短代测试物品");
			order.setChinaUnicomProductId("0000000000001001");
			order.setChinaUnicomConsumeCode("000000010029");
			order.setDescription("短代测试物品详细信息");
			order.setOrderId("00000001" + "0001" + "02" + String.format("%010d", System.currentTimeMillis()/1000));
			break;
		case IAP_METHOD_TYPE_ALIPAY:
			order.setName("支付宝测试物品");
			order.setPrice(0.01);
			order.setDescription("支付宝测试物品详细信息");
			order.setOrderId("00000001" + "0001" + "02" + String.format("%050d", System.currentTimeMillis()));
			break;
		case IAP_METHOD_TYPE_UNION_PAY:
			order.setName("银联测试物品");
			order.setPrice(0.01);
			order.setDescription("银联测试物品信息");
			order.setOrderId("00000001" + "0001" + "02" + String.format("%036d", System.currentTimeMillis()));
			break;
		}
		order.setPurchaseTime(System.currentTimeMillis());
		return order;
		*/
		PurchaseOrderRequest request = new PurchaseOrderRequest(type, productId);
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
			PurchaseOrder purchaseOrder = new PurchaseOrder();
			purchaseOrder.unmarshal(new JSONObject(orderStr));
			return purchaseOrder;
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
		return null;
	}
}
