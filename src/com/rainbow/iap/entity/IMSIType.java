package com.rainbow.iap.entity;

public enum IMSIType
{
	IMSI_INVALID(new String[]{""}),
	IMSI_CHINA_MOBILE(new String[]{"46000", "46002", "46007"}),
	IMSI_CHINA_UNICOM(new String[]{"46001", "46010"}),
	IMSI_CHINA_TELECOM(new String[]{"46007"});
	
	private String[] _simOperators;
	
	private IMSIType(String[] simOperators)
	{
		_simOperators = simOperators;
	}
	
	public static IMSIType getBySimOperator(String simOperator)
	{
		for (IMSIType imsiType : IMSIType.values())
		{
			for (String op : imsiType._simOperators)
			{
				if (op.equals(simOperator))
				{
					return imsiType;
				}
			}
		}
		return IMSI_INVALID;
	}
}
