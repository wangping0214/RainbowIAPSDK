package com.rainbow.iap;

public class IAPResult
{
	public static final int PURCHASE_RESPONSE_RESULT_OK = 0;
    public static final int PURCHASE_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final int PURCHASE_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int PURCHASE_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int PURCHASE_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int PURCHASE_RESPONSE_RESULT_ERROR = 6;
    //public static final int PURCHASE_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    //public static final int PURCHASE_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
    
    private int 	_responseCode;
    private String	_message;
    
    public IAPResult(int responseCode, String message)
    {
    	_responseCode = responseCode;
    	_message = message;
    }
    
    /**
     * @return true如果支付成功，否则false
     */
    public boolean isSuccess()
    {
    	return _responseCode == PURCHASE_RESPONSE_RESULT_OK;
    }
    
    /**
     * @return 支付返回码
     */
    public int getResponseCode()
    {
    	return _responseCode;
    }
    
    /**
     * @return 支付结果信息
     */
    public String getMessage()
    {
    	return _message;
    }
}
