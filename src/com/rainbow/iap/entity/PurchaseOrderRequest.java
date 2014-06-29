package com.rainbow.iap.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseOrderRequest implements JSONSerializable
{
	private IAPMethodType	_iapMethodType;
	private String			_productId;

	public PurchaseOrderRequest()
	{
	}
	
	public PurchaseOrderRequest(IAPMethodType iapMethodType, String productId)
	{
		_iapMethodType = iapMethodType;
		_productId = productId;
	}
	
	public IAPMethodType getIAPMethodType()
	{
		return _iapMethodType;
	}
	
	public String getProductId()
	{
		return _productId;
	}
	
	@Override
	public JSONObject marshal(JSONObject jsonObj) throws JSONException
	{
		jsonObj.put("iapMethodType", _iapMethodType.getValue());
		jsonObj.put("productId", _productId);
		return jsonObj;
	}

	@Override
	public JSONObject unmarshal(JSONObject jsonObj) throws JSONException
	{
		_iapMethodType = IAPMethodType.getByValue(jsonObj.getInt("iapMethodType"));
		_productId = jsonObj.getString("productId");
		return jsonObj;
	}

}
