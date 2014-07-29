package com.rainbow.iap;

public class Purchase
{
	private String _productId;
	private String _orderId;
	private String _customData;
	private double _price;
	private long _purchaseTime;
	
	public Purchase()
	{
	}
	
	public Purchase(String productId, String orderId, String customData, double price, long purchaseTime)
	{
		setProductId(productId);
		setOrderId(orderId);
		setCustomData(customData);
		setPrice(price);
		setPurchaseTime(purchaseTime);
	}
	
	public void setProductId(String productId)
	{
		_productId = productId;
	}
	
	/** @return 本次支付的商品ID。*/
	public String getProductId()
	{
		return _productId;
	}
	
	public void setOrderId(String orderId)
	{
		_orderId = orderId;
	}
	
	/** @return 本次支付的订单ID。*/
	public String getOrderId()
	{
		return _orderId;
	}
	
	public void setCustomData(String customData)
	{
		_customData = customData;
	}
	
	public String getCustomData()
	{
		return _customData;
	}
	
	public double getPrice()
	{
		return _price;
	}

	public void setPrice(double price)
	{
		_price = price;
	}

	public void setPurchaseTime(long purchaseTime)
	{
		_purchaseTime = purchaseTime;
	}
	
	/** @return 本次支付的时间。*/
	public long getPurchaseTime()
	{
		return _purchaseTime;
	}
}
