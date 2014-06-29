package com.rainbow.iap;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alipay.android.app.sdk.AliPay;
import com.rainbow.iap.alipay.Keys;
import com.rainbow.iap.alipay.Result;
import com.rainbow.iap.alipay.Rsa;
import com.rainbow.iap.entity.IAPMethodType;
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
	
	private static final int UNION_PAY_REQUEST_CODE = 1;
	private static final int ALIPAY_RESPONSE_CODE	= 2;
	
	private String _productId;
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
		setContentView(R.layout.activity_iap);
		
		Bundle extraData = getIntent().getExtras();
		_productId = extraData.getString("productId");
		_purchaseOrder = null;
		
		_alipayHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg)
			{
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
		IAPMethodListAdapter listAdapter = new IAPMethodListAdapter(this);
		ListView iapMethodListView = (ListView) findViewById(R.id.iap_method_list_view);
		iapMethodListView.setAdapter(listAdapter);
		iapMethodListView.setOnItemClickListener(_iapMethodListViewListener);
	}
	
	@SuppressLint("HandlerLeak")
	private void purchase(IAPMethodType type)
	{
		final IAPMethodType finalType = type;
		IAPClient.getInstance().getPurchaseOrder(finalType, _productId, new Handler() {
			@Override
			public void handleMessage(android.os.Message msg)
			{
				_purchaseOrder = (PurchaseOrder) msg.obj;
				if (_purchaseOrder != null)
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
		payBundle.putString("productid", _purchaseOrder.getChinaUnicomConsumeCode());
		//��ͨ�����������
		payBundle.putString("consumeCode", _purchaseOrder.getChinaUnicomProductId());
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
        return tm.getSubscriberId();
    }
	
	private void alipayPurchase()
	{
		try
		{
			String orderInfo = getAlipayOrderInfo(_purchaseOrder);
			//TODO ��Ҫ��ǩ�����̷ŵ�������
			String sign = Rsa.sign(orderInfo, Keys.PRIVATE);
			sign = URLEncoder.encode(sign, "UTF-8");
			orderInfo += "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
			final String finalOrderInfo = orderInfo;
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
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private String getAlipayOrderInfo(PurchaseOrder purchaseOrder) throws UnsupportedEncodingException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");					//����������ID�����ɿ�
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
		sb.append(URLEncoder.encode("http://http://182.92.65.140/IAPServer/UnionPayIAPService", "UTF-8"));
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
		payInfo.merchantNum = "22294531";
		// �̻���ע
		payInfo.merchantRemark = "�˴���д�̻���ע��С��200λ���Ǳ���";
		// �̻�ҵ������
		payInfo.bizType = 0;
		// �����156 #�̶�Ϊ156
		payInfo.currency = 156;
		// �ⲿ֪ͨURL #
		payInfo.notifyUrl = "www.jd.com";
		// �û���
		payInfo.userName = "username";
		// �ֻ���
		payInfo.mobile = "18688888888";
		// ����
		payInfo.email = "im_lilei@163.com";
		// �û�ʵ��
		payInfo.realName = "��Ƚ";
		// ֤������
		payInfo.idNum = "230103198801111111";
		// ��ʼ�����п���Ϣ
		List<BankCard> bankList = new ArrayList<BankCard>();
		// ���Ӵ��
		BankCard card1 = new BankCard();
		card1.cardNum = "3563900000050850";	//#
		card1.cardHolderName = "����";
		card1.cardHolderMobile = "13112345678";
		card1.cardHolderIDNum = "440304198302060077";
		bankList.add(card1);
		payInfo.bankcards = bankList;
		
		return payInfo;
	}
	
	private void onPurchaseResponsed(int responseCode, String message)
	{
		Intent resultIntent = new Intent();
		Bundle extraData = new Bundle();
		extraData.putInt("responseCode", responseCode);
		extraData.putString("message", message);
		extraData.putString("productId", _productId);
		extraData.putString("orderId", _purchaseOrder.getOrderId());
		extraData.putLong("purchaseTime", _purchaseOrder.getPurchaseTime());
		resultIntent.putExtras(extraData);
		//������ɣ�����
		setResult(RESULT_OK, resultIntent);
		finish();
	}
}