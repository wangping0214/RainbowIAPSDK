package com.rainbow.iap.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements JSONSerializable
{
	private String 	id;
	private String 	name;
	
	private double	price;
	
	private String 	description;
	
	public Product()
	{
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	@Override
	public JSONObject marshal(JSONObject jsonObj) throws JSONException
	{
		jsonObj.put("id", id);
		jsonObj.put("name", name);
		jsonObj.put("price", price);
		jsonObj.put("description", description);
		return jsonObj;
	}

	@Override
	public JSONObject unmarshal(JSONObject jsonObj) throws JSONException
	{
		id = jsonObj.getString("id");
		name = jsonObj.getString("name");
		if (jsonObj.has("price"))
		{
			price = jsonObj.getDouble("price");
		}
		description = jsonObj.getString("description");
		return jsonObj;
	}
}
