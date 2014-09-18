package com.rainbow.iap;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import com.alipay.android.app.sdk.AliPay;
import com.rainbow.iap.alipay.Keys;
import com.rainbow.iap.alipay.Result;
import com.rainbow.iap.entity.IAPMethodType;
import com.rainbow.iap.entity.IMSIType;
import com.rainbow.iap.entity.PurchaseOrder;
import com.rainbow.iap.net.IAPClient;
import com.unicom.wounipaysms.WoUniPay;
import com.unicom.wounipaysms.delegate.RequestDelegate;
import com.wangyin.payments.BankCard;
import com.wangyin.payments.PayInfo;
import com.wangyin.payments.WYPay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class IAPActivity extends Activity
{
	private static final String TAG = "RainbowIAP";

	/*
	private static final String CHINA_MOBILE_IMSI_PREFIX_1 	= "46000";
	private static final String CHINA_MOBILE_IMSI_PREFIX_2 	= "46002";
	private static final String CHINA_UNICOM_IMSI_PREFIX 	= "46001";
	private static final String CHINA_TELECOM_IMSI_PREFIX	= "46003";
	*/
	
	private static final int UNION_PAY_REQUEST_CODE = 1;
	private static final int ALIPAY_RESPONSE_CODE	= 2;
	
	private String _productId;
	private String _customData;
	private PurchaseOrder	_purchaseOrder;
	private AdapterView.OnItemClickListener	_iapMethodListViewListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			ListView iapMethodListView = (ListView) parent;
			IAPMethodListAdapter listAdapter = (IAPMethodListAdapter) iapMethodListView.getAdapter();
			IAPMethod method = (IAPMethod) listAdapter.getItem(position);
			purchase(method.getType());
		}
		
	};
	
	private Handler	_alipayHandler = null;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_iap);
		setContentView(getResources().getIdentifier("activity_iap", "layout", getPackageName()));
		
		Bundle extraData = getIntent().getExtras();
		_productId = extraData.getString("productId");
		_customData = extraData.getString("customData");
		_purchaseOrder = null;
		
		_alipayHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg)
			{
				Log.d(TAG, (String)msg.obj);
				Result result = new Result((String) msg.obj);
				if (result.isSuccess())
				{
					onPurchaseResponsed(IAPResult.PURCHASE_RESPONSE_RESULT_OK, result.getMessage());
				}
				else
				{
					onPurchaseResponsed(IAPResult.PURCHASE_RESPONSE_RESULT_ERROR, result.getMessage());
				}
				Toast.makeText(IAPActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
			};
		};
		
		initIAPMethodListView();
	}
	
	@Override
	public void onBackPressed()
	{
		onPurchaseResponsed(IAPResult.PURCHASE_RESPONSE_RESULT_USER_CANCELED, "�û�ȡ������");
		super.onBackPressed();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == UNION_PAY_REQUEST_CODE && data != null)
		{
			Bundle responseResult = data.getExtras();

			int responseCode = responseResult.getInt(WYPay.RESPONSE_CODE, -1);
			String responseMessage = responseResult.getString(WYPay.RESPONSE_MESSAGE);
			String msg = null;
			if (responseCode == WYPay.RESPONSE_SUCCESS)
			{
				msg = "֧���ɹ�";
				onPurchaseResponsed(IAPResult.PURCHASE_RESPONSE_RESULT_OK, msg);
			}
			else
			{
				msg = "֧��ʧ��";
				onPurchaseResponsed(IAPResult.PURCHASE_RESPONSE_RESULT_ERROR, msg);
			}
			msg += "�������Ϣ��" + responseMessage;
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initIAPMethodListView()
	{
		IAPMethodListAdapter listAdapter = new IAPMethodListAdapter(this, getImsiType());
		ListView iapMethodListView = (ListView) findViewById(this.getResources().getIdentifier("iap_method_list_view", "id", this.getPackageName()));
		iapMethodListView.setAdapter(listAdapter);
		iapMethodListView.setOnItemClickListener(_iapMethodListViewListener);
	}
	
	@SuppressLint("HandlerLeak")
	private void purchase(IAPMethodType type)
	{
		final IAPMethodType finalType = type;
		IAPClient.getInstance().getPurchaseOrder(finalType, _productId, _customData, new Handler() {
			@Override
			public void handleMessage(android.os.Message msg)
			{
				_purchaseOrder = (PurchaseOrder) msg.obj;
				if (_purchaseOrder != null && _purchaseOrder.getResponseCode() == PurchaseOrder.RESPONSE_SUCCEED)
				{
					switch (finalType)
					{
					case IAP_METHOD_TYPE_CHINA_UNICOM:
						chinaUnicomPurchase();
						break;
					case IAP_METHOD_TYPE_ALIPAY:
						alipayPurchase();
						break;
					case IAP_METHOD_TYPE_UNION_PAY:
						unionPayPurchase();
						break;
					}
				}
				else
				{
					Toast.makeText(IAPActivity.this, "��Ʒ: " + _productId + " ������", Toast.LENGTH_SHORT).show();
					Log.w(TAG, "Failed to get product info of " + _productId);
				}
			};
		});
	}
	
	private void chinaUnicomPurchase()
	{
		WoUniPay woUniPay = WoUniPay.getInstance(this);
		woUniPay.setServicesAddress("119.39.227.243", 9098);
		Bundle payBundle = new Bundle();
		//�̻��ţ���ͨ����
		payBundle.putString("cpId", "14011006");
		payBundle.putString("imsi", getImsi());
		//�����ţ��̻�����
		payBundle.putString("cpOrderId", _purchaseOrder.getOrderId());
		//��ͨ�������ƷID
		payBundle.putString("productid", _purchaseOrder.getChinaUnicomProductId());
		//��ͨ�����������
		payBundle.putString("consumeCode", _purchaseOrder.getChinaUnicomConsumeCode());
		//��Ʒ����
		payBundle.putString("subject", _purchaseOrder.getDescription());
		woUniPay.payAsDianBoVAC(payBundle, new RequestDelegate() {

			@Override
			public void requestFailed(int retCode, String message)
			{
				onPurchaseResponsed(retCode, message);
			}

			@Override
			public void requestSuccessed(Object response)
			{
				onPurchaseResponsed(IAPResult.PURCHASE_RESPONSE_RESULT_OK, response.toString());
			}
			
		});
	}
	
	private String getImsi()
    {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(TAG, "imsi: " + tm.getSubscriberId());
        Log.d(TAG, tm.getSimOperator());
        Log.d(TAG, tm.getSimOperatorName());
        return tm.getSubscriberId();
    }
	
	private IMSIType getImsiType()
	{
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String simOperator = tm.getSimOperator();
		if (null == simOperator)
		{
			return IMSIType.IMSI_INVALID;
		}
		/*
		else if (imsiStr.equals(CHINA_MOBILE_IMSI_PREFIX_1) || imsiStr.equals(CHINA_MOBILE_IMSI_PREFIX_2))
		{
			return IMSIType.IMSI_CHINA_MOBILE;
		}
		else if (imsiStr.equals(CHINA_UNICOM_IMSI_PREFIX))
		{
			return IMSIType.IMSI_CHINA_UNICOM;
		}
		else if (imsiStr.equals(CHINA_TELECOM_IMSI_PREFIX))
		{
			return IMSIType.IMSI_CHINA_TELECOM;
		}
		else
		{
			return IMSIType.IMSI_INVALID;
		}
		*/
		return IMSIType.getBySimOperator(simOperator);
	}
	
	private void alipayPurchase()
	{
		try
		{
			final String orderInfo = getAlipayOrderInfo(_purchaseOrder);
			IAPClient.getInstance().alipaySign(orderInfo, new Handler() {
				@Override
				public void handleMessage(android.os.Message msg)
				{
					String signStr = (String) msg.obj;
					try
					{
						signStr = URLEncoder.encode(signStr, "UTF-8");
					}
					catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
					final String finalOrderInfo = orderInfo + "&sign=\"" + signStr + "\"&" + "sign_type=\"RSA\"";
					new Thread() {
						
						@Override
						public void run()
						{
							AliPay aliPay = new AliPay(IAPActivity.this, _alipayHandler);
							String result = aliPay.pay(finalOrderInfo);
							Log.i(TAG, "alipay result=" + result);
							Message msg = new Message();
							msg.what = ALIPAY_RESPONSE_CODE;
							msg.obj = result;
							_alipayHandler.sendMessage(msg);
						}
						
					}.start();
				}
			});
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	private String getAlipayOrderInfo(PurchaseOrder purchaseOrder) throws UnsupportedEncodingException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");					//���������ID�����ɿ�
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");			//����ID��Ψһ���̻��ṩ
		sb.append(purchaseOrder.getOrderId());			
		sb.append("\"&subject=\"");					//��Ʒ����
		sb.append(purchaseOrder.getName());		
		sb.append("\"&body=\"");					//��Ʒ����
		sb.append(purchaseOrder.getDescription());		
		sb.append("\"&total_fee=\"");				//��Ʒ�۸�
		sb.append(purchaseOrder.getPrice());		
		sb.append("\"&notify_url=\"");				//�������첽֪ͨҳ��

		// ��ַ��Ҫ��URL����
		sb.append(URLEncoder.encode("http://www.chaimiyouxi.com/IAPServer/notify_url.jsp", "UTF-8"));
		sb.append("\"&service=\"mobile.securitypay.pay");	//�ӿ����ƣ��̶�ֵ
		sb.append("\"&_input_charset=\"UTF-8");		//�̻���վʹ�õı����ʽ���̶�ֵ
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com", "UTF-8"));
		sb.append("\"&payment_type=\"1");			//֧������
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);				//����֧�����˺�

		// ���show_urlֵΪ�գ��ɲ���
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");				//δ����׳�ʱʱ��
		sb.append("\"");

		return new String(sb);
	}
	
	private void unionPayPurchase()
	{
		PayInfo _payInfo = getUnionPayInfo();
		//���й����ⲿ������Ʒ����Ϣ
		// �ⲿ������,�����ظ� #
		_payInfo.tradeNum = _purchaseOrder.getOrderId();
		// �ⲿ�������� #
		_payInfo.tradeName = _purchaseOrder.getName();
		// �ⲿ��������
		_payInfo.tradeDescription = _purchaseOrder.getDescription();
		// �ⲿ����ʱ�� #
		_payInfo.tradeTime = new Date();
		// �ⲿ�������
		_payInfo.tradeAmount = new BigDecimal("" + _purchaseOrder.getPrice());
		//
		String message = WYPay.pay(_payInfo, this, UNION_PAY_REQUEST_CODE);
		if (message != null) 
		{
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		}
	}
	
	private PayInfo getUnionPayInfo()
	{
		PayInfo payInfo = new PayInfo();
		// �̻��� #
		payInfo.merchantNum = "22968429";
		// �̻���ע
		payInfo.merchantRemark = "�������ʱ�տƼ����޹�˾ ";
		// �̻�ҵ������
		payInfo.bizType = 0;
		// �����156 #�̶�Ϊ156
		payInfo.currency = 156;
		// �ⲿ֪ͨURL #
		payInfo.notifyUrl = "http://www.chaimiyouxi.com/IAPServer/UnionPayIAPService";
		// �û���
		payInfo.userName = "�û���";
		// �ֻ���
		payInfo.mobile = "�ֿ�����ϵ�绰";
		// ����
		payInfo.email = "����";
		// �û�ʵ��
		payInfo.realName = "�ֿ�������";
		// ֤������
		payInfo.idNum = "�ֿ������֤��";
		// ��ʼ�����п���Ϣ
		payInfo.bankcards = new ArrayList<BankCard>();
		
		return payInfo;
	}
	
	private void onPurchaseResponsed(int responseCode, String message)
	{
		Intent resultIntent = new Intent();
		Bundle extraData = new Bundle();
		extraData.putInt("responseCode", responseCode);
		extraData.putString("message", message);
		extraData.putString("productId", _productId);
		extraData.putString("customData", _customData);
		if (_purchaseOrder != null)
		{
			extraData.putString("orderId", _purchaseOrder.getOrderId());
			extraData.putDouble("price", _purchaseOrder.getPrice());
			extraData.putLong("purchaseTime", _purchaseOrder.getPurchaseTime());
		}
		resultIntent.putExtras(extraData);
		//������ɣ�����
		setResult(RESULT_OK, resultIntent);
		finish();
	}
}
