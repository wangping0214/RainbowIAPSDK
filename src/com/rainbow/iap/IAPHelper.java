package com.rainbow.iap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class IAPHelper
{
	public static interface OnPurchaseFinishedListener
	{
		public void onPurchaseFinished(IAPResult result, Purchase purchase);
	}
	
	private Activity _context;
	private int _requestCode;
	private OnPurchaseFinishedListener	_listener;
	
	public IAPHelper(Activity context)
	{
		_context = context;
	}
	
	public void purchase(String productId, String customData, int requestCode, OnPurchaseFinishedListener listener)
	{
		_requestCode = requestCode;
		_listener = listener;
		Intent intent = new Intent(_context, IAPActivity.class);
		Bundle extraData = new Bundle();
		extraData.putString("productId", productId);
		extraData.putString("customData", customData);
		intent.putExtras(extraData);
		_context.startActivityForResult(intent, requestCode);
	}
	
	public boolean handleActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == _requestCode)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Bundle extraData = data.getExtras();
				int responseCode = extraData.getInt("responseCode");
				String message = extraData.getString("message");
				IAPResult iapResult = new IAPResult(responseCode, message);
				String productId = extraData.getString("productId");
				String orderId = extraData.getString("orderId");
				String customData = extraData.getString("customData");
				double price = extraData.getDouble("price");
				long purchaseTime = extraData.getLong("purchaseTime");
				Purchase purchase = new Purchase(productId, orderId, customData, price, purchaseTime);
				if (_listener != null)
				{
					_listener.onPurchaseFinished(iapResult, purchase);
				}
			}
		}
		return false;
	}
}
