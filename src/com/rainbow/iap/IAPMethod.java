package com.rainbow.iap;

import com.rainbow.iap.entity.IAPMethodType;

public class IAPMethod
{
	private IAPMethodType	type;
	private int logoId;
	private int nameId;
	private int descId;
	
	public IAPMethod()
	{
	}
	
	public IAPMethod(IAPMethodType type, int logoId, int nameId, int descId)
	{
		this();
		this.type = type;
		this.logoId = logoId;
		this.nameId = nameId;
		this.descId = descId;
	}
	
	public IAPMethodType getType()
	{
		return type;
	}
	public void setType(IAPMethodType type)
	{
		this.type = type;
	}
	public int getLogoId()
	{
		return logoId;
	}
	public void setLogoId(int logoId)
	{
		this.logoId = logoId;
	}
	public int getNameId()
	{
		return nameId;
	}
	public void setNameId(int nameId)
	{
		this.nameId = nameId;
	}
	public int getDescId()
	{
		return descId;
	}
	public void setDescId(int descId)
	{
		this.descId = descId;
	}
}
