package com.rainbow.iap;

public class Purchase
{
	private String _productId;
	private String _orderId;
	private long _purchaseTime;
	
	public Purchase()
	{
	}
	
	public Purchase(String productId, String orderId, long purchaseTime)
	{
		setProductId(productId);
		setOrderId(orderId);
		setPurchaseTime(purchaseTime);
	}
	
	public void setProductId(String productId)
	{
		_productId = productId;
	}
	
	public String getProductId()
	{
		return _productId;
	}
	
	public void setOrderId(String orderId)
	{
		_orderId = orderId;
	}
	
	public String getOrderId()
	{
		return _orderId;
	}
	
	public void setPurchaseTime(long purchaseTime)
	{
		_purchaseTime = purchaseTime;
	}
	
	public long getPurchaseTime()
	{
		return _purchaseTime;
	}
}
