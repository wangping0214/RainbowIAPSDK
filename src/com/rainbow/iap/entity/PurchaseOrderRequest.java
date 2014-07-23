package com.rainbow.iap.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseOrderRequest implements JSONSerializable
{
	private IAPMethodType	_iapMethodType;
	private String			_productId;
	private String 			_customData;

	public PurchaseOrderRequest()
	{
	}
	
	public PurchaseOrderRequest(IAPMethodType iapMethodType, String productId, String customData)
	{
		_iapMethodType = iapMethodType;
		_productId = productId;
		_customData = customData;
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
		jsonObj.put("customData", _customData);
		return jsonObj;
	}

	@Override
	public JSONObject unmarshal(JSONObject jsonObj) throws JSONException
	{
		_iapMethodType = IAPMethodType.getByValue(jsonObj.getInt("iapMethodType"));
		_productId = jsonObj.getString("productId");
		_customData = jsonObj.getString("customData");
		return jsonObj;
	}

}
