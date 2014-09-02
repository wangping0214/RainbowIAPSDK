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
     * @return true���֧���ɹ�������false
     */
    public boolean isSuccess()
    {
    	return _responseCode == PURCHASE_RESPONSE_RESULT_OK;
    }
    
    /**
     * @return ֧��������
     */
    public int getResponseCode()
    {
    	return _responseCode;
    }
    
    /**
     * @return ֧�������Ϣ
     */
    public String getMessage()
    {
    	return _message;
    }
}
