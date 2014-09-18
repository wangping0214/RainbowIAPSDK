package com.rainbow.iap;

import com.rainbow.iap.entity.IAPMethodType;
import com.rainbow.iap.entity.IMSIType;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IAPMethodListAdapter extends BaseAdapter
{
	private Context					_context;
	private LayoutInflater			_layoutInflater;
	private SparseArray<IAPMethod>	_iapMethodList;
	
	public IAPMethodListAdapter(Context context, IMSIType imsiType)
	{
		_context = context;
		_layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_iapMethodList = new SparseArray<IAPMethod>();
		
		int index = 0;
		if (imsiType == IMSIType.IMSI_CHINA_UNICOM)
		{
			IAPMethod chinaUnicomMethod = new IAPMethod();
			chinaUnicomMethod.setType(IAPMethodType.IAP_METHOD_TYPE_CHINA_UNICOM);
			chinaUnicomMethod.setLogoId(_context.getResources().getIdentifier("china_unicom_logo", "drawable", _context.getPackageName()));
			chinaUnicomMethod.setNameId(_context.getResources().getIdentifier("iap_method_china_unicom_name", "string", _context.getPackageName()));
			chinaUnicomMethod.setDescId(_context.getResources().getIdentifier("iap_method_china_unicom_desc", "string", _context.getPackageName()));
			//chinaUnicomMethod.setLogoId(R.drawable.china_unicom_logo);
			//chinaUnicomMethod.setNameId(R.string.iap_method_china_unicom_name);
			//chinaUnicomMethod.setDescId(R.string.iap_method_china_unicom_desc);
			_iapMethodList.append(/*chinaUnicomMethod.getType().getValue()*/ index ++, chinaUnicomMethod);
		}
		
		IAPMethod alipayMethod = new IAPMethod();
		alipayMethod.setType(IAPMethodType.IAP_METHOD_TYPE_ALIPAY);
		alipayMethod.setLogoId(_context.getResources().getIdentifier("alipay_logo", "drawable", _context.getPackageName()));
		alipayMethod.setNameId(_context.getResources().getIdentifier("iap_method_alipay_name", "string", _context.getPackageName()));
		alipayMethod.setDescId(_context.getResources().getIdentifier("iap_method_alipay_desc", "string", _context.getPackageName()));
		//alipayMethod.setLogoId(R.drawable.alipay_logo);
		//alipayMethod.setNameId(R.string.iap_method_alipay_name);
		//alipayMethod.setDescId(R.string.iap_method_alipay_desc);
		_iapMethodList.append(/*alipayMethod.getType().getValue()*/ index ++, alipayMethod);
		
		IAPMethod unionPayMethod = new IAPMethod();
		unionPayMethod.setType(IAPMethodType.IAP_METHOD_TYPE_UNION_PAY);
		unionPayMethod.setLogoId(_context.getResources().getIdentifier("deposit_logo", "drawable", _context.getPackageName()));
		unionPayMethod.setNameId(_context.getResources().getIdentifier("iap_method_deposit_name", "string", _context.getPackageName()));
		unionPayMethod.setDescId(_context.getResources().getIdentifier("iap_method_deposit_desc", "string", _context.getPackageName()));
		//unionPayMethod.setLogoId(R.drawable.deposit_logo);
		//unionPayMethod.setNameId(R.string.iap_method_deposit_name);
		//unionPayMethod.setDescId(R.string.iap_method_deposit_desc);
		_iapMethodList.append(/*unionPayMethod.getType().getValue()*/ index ++, unionPayMethod);
	}
	
	@Override
	public int getCount()
	{
		return _iapMethodList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return _iapMethodList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			//convertView = _layoutInflater.inflate(R.layout.iap_method_item, null);
			convertView = _layoutInflater.inflate(_context.getResources().getIdentifier("iap_method_item", "layout", _context.getPackageName()), null);
		}
		IAPMethod method = _iapMethodList.get(position);
		ImageView logoImageView = (ImageView) convertView.findViewById(_context.getResources().getIdentifier("iap_method_item_logo", "id", _context.getPackageName()));
		//ImageView logoImageView = (ImageView) convertView.findViewById(R.id.iap_method_item_logo);
		logoImageView.setImageDrawable(_context.getResources().getDrawable(method.getLogoId()));
		//TextView nameLabel = (TextView) convertView.findViewById(R.id.iap_method_item_name);
		TextView nameLabel = (TextView) convertView.findViewById(_context.getResources().getIdentifier("iap_method_item_name", "id", _context.getPackageName()));
		nameLabel.setText(method.getNameId());
		//TextView descLabel = (TextView) convertView.findViewById(R.id.iap_method_item_desc);
		TextView descLabel = (TextView) convertView.findViewById(_context.getResources().getIdentifier("iap_method_item_desc", "id", _context.getPackageName()));
		descLabel.setText(method.getDescId());
		return convertView;
	}

}
