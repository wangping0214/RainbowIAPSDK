package com.rainbow.iap.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseOrder implements JSONSerializable
{
	public static final int RESPONSE_SUCCEED 	= 0;
	public static final int RESPONSE_FAILED		= 1;
	
	private	int		responseCode;
	private String 	productId;
	private String 	name;
	
	//for china unicom
	private String 	chinaUnicomProductId;
	private String 	chinaUnicomConsumeCode;
	//for alipay and union pay
	private double	price;
	
	private String 	description;
	private String	orderId;
	private long	purchaseTime;
	
	public PurchaseOrder()
	{
	}
	
	public int getResponseCode()
	{
		return responseCode;
	}

	public void setResponseCode(int responseCode)
	{
		this.responseCode = responseCode;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getChinaUnicomProductId()
	{
		return chinaUnicomProductId;
	}

	public void setChinaUnicomProductId(String chinaUnicomProductId)
	{
		this.chinaUnicomProductId = chinaUnicomProductId;
	}

	public String getChinaUnicomConsumeCode()
	{
		return chinaUnicomConsumeCode;
	}

	public void setChinaUnicomConsumeCode(String chinaUnicomConsumeCode)
	{
		this.chinaUnicomConsumeCode = chinaUnicomConsumeCode;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
	
	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	
	public long getPurchaseTime()
	{
		return purchaseTime;
	}

	public void setPurchaseTime(long purchaseTime)
	{
		this.purchaseTime = purchaseTime;
	}

	@Override
	public JSONObject marshal(JSONObject jsonObj) throws JSONException
	{
		jsonObj.put("responseCode", responseCode);
		jsonObj.put("productId", productId);
		jsonObj.put("name", name);
		jsonObj.put("chinaUnicomProductId", chinaUnicomProductId);
		jsonObj.put("chinaUnicomConsumeCode", chinaUnicomConsumeCode);
		jsonObj.put("price", price);
		jsonObj.put("description", description);
		jsonObj.put("orderId", orderId);
		jsonObj.put("purchaseTime", purchaseTime);
		return jsonObj;
	}

	@Override
	public JSONObject unmarshal(JSONObject jsonObj) throws JSONException
	{
		responseCode = jsonObj.getInt("responseCode");
		productId = jsonObj.getString("productId");
		if (jsonObj.has("name"))
		{
			name = jsonObj.getString("name");
		}
		if (jsonObj.has("chinaUnicomProductId"))
		{
			chinaUnicomProductId = jsonObj.getString("chinaUnicomProductId");
		}
		if (jsonObj.has("chinaUnicomConsumeCode"))
		{
			chinaUnicomConsumeCode = jsonObj.getString("chinaUnicomConsumeCode");
		}
		if (jsonObj.has("price"))
		{
			price = jsonObj.getDouble("price");
		}
		if (jsonObj.has("description"))
		{
			description = jsonObj.getString("description");
		}
		if (jsonObj.has("orderId"))
		{
			orderId = jsonObj.getString("orderId");
		}
		if (jsonObj.has("purchaseTime"))
		{
			purchaseTime = jsonObj.getLong("purchaseTime");
		}
		return jsonObj;
	}
}
