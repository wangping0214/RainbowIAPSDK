package com.rainbow.iap.alipay;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

public class Result
{
	private static final String TAG = "RainbowIAP_ALIPAY";
	
	private static final Map<String, String> sResultStatus;

	private String mResult;

	private String _resultStatus = null;
	private String _message = null;
	//private String _memo = null;
	private boolean _isSignOk = false;

	public Result(String result)
	{
		this.mResult = result;
		parseResult();
	}

	static
	{
		sResultStatus = new HashMap<String, String>();
		sResultStatus.put("9000", "操作成功");
		sResultStatus.put("4000", "系统异常");
		sResultStatus.put("4001", "数据格式不正确");
		sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
		sResultStatus.put("4004", "该用户已解除绑定");
		sResultStatus.put("4005", "绑定失败或没有绑定");
		sResultStatus.put("4006", "订单支付失败");
		sResultStatus.put("4010", "重新绑定账户");
		sResultStatus.put("6000", "支付服务正在进行升级操作");
		sResultStatus.put("6001", "用户中途取消支付操作");
		sResultStatus.put("7001", "网页支付失败");
	}

	/*
	private String getResult()
	{
		String src = mResult.replace("{", "");
		src = src.replace("}", "");
		return getContent(src, "memo=", ";result");
	}
	*/
	
	public boolean isSuccess()
	{
		return "9000".equals(_resultStatus) && _isSignOk;
	}
	
	public String getMessage()
	{
		return _message;
	}
	
	private void parseResult()
	{
		try
		{
			String src = mResult.replace("{", "");
			src = src.replace("}", "");
			_resultStatus = getContent(src, "resultStatus=", ";memo");
			if (sResultStatus.containsKey(_resultStatus))
			{
				_message = sResultStatus.get(_resultStatus);
			} else
			{
				_message = "其他错误";
			}
			//_memo = getContent(src, "memo=", ";result");
			String result = getContent(src, "result=", null);
			_isSignOk = checkSign(result);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private boolean checkSign(String result)
	{
		boolean retVal = false;
		try
		{
			JSONObject json = string2JSON(result, "&");

			int signTypePos = result.indexOf("&sign_type=");
			int signPos = result.indexOf("&sign=");
			int pos = Math.min(signTypePos, signPos);
			String signContent = result.substring(0, pos);
			Log.d(TAG, signContent);
			
			String signType = json.getString("sign_type");
			signType = signType.replace("\"", "");

			String sign = json.getString("sign");
			sign = sign.replace("\"", "");

			if (signType.equalsIgnoreCase("RSA"))
			{
				retVal = Rsa.doCheck(signContent, sign, Keys.PUBLIC);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.i(TAG, "Exception =" + e);
		}
		Log.i(TAG, "checkSign =" + retVal);
		return retVal;
	}

	private JSONObject string2JSON(String src, String split)
	{
		JSONObject json = new JSONObject();

		try
		{
			String[] arr = src.split(split);
			for (int i = 0; i < arr.length; i++)
			{
				String[] arrKey = arr[i].split("=");
				json.put(arrKey[0], arr[i].substring(arrKey[0].length() + 1));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return json;
	}

	private String getContent(String src, String startTag, String endTag)
	{
		String content = src;
		int start = src.indexOf(startTag);
		start += startTag.length();

		try
		{
			if (endTag != null)
			{
				int end = src.indexOf(endTag);
				content = src.substring(start, end);
			} else
			{
				content = src.substring(start);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return content;
	}
}
