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
	
	/** @return ����֧������ƷID��*/
	public String getProductId()
	{
		return _productId;
	}
	
	public void setOrderId(String orderId)
	{
		_orderId = orderId;
	}
	
	/** @return ����֧���Ķ���ID��*/
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
	
	/** @return ����֧����ʱ�䡣*/
	public long getPurchaseTime()
	{
		return _purchaseTime;
	}
}
