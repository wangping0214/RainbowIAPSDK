package com.rainbow.iap.entity;

public enum IAPMethodType
{
	IAP_METHOD_TYPE_CHINA_UNICOM(0),
	IAP_METHOD_TYPE_ALIPAY(1),
	IAP_METHOD_TYPE_UNION_PAY(2),
	;
	
	private int _value;
	
	private IAPMethodType(int value)
	{
		_value = value;
	}
	
	public int getValue()
	{
		return _value;
	}
	
	public static IAPMethodType getByValue(int value)
	{
		for (IAPMethodType type : IAPMethodType.values())
		{
			if (type.getValue() == value)
			{
				return type;
			}
		}
		return null;
	}
}
