package com.vocinno.centanet.customermanage.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vocinno.centanet.R;
import com.vocinno.centanet.customermanage.CustomerDetailActivity;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.model.CustomerItem;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;

public class CustormerListAdapter extends BaseAdapter {

	private CustomerManageActivity mContext;
	private LayoutInflater mInflater;
	private List<CustomerItem> mListCustomers;
	private boolean selectSOrZ=false;
	public void setListDatas(List<CustomerItem> listCustomers) {
		mListCustomers = listCustomers;
		notifyDataSetChanged();
	}

	public CustormerListAdapter(CustomerManageActivity mContext,
			List<CustomerItem> listCustomers) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mListCustomers = listCustomers;
	}
	public CustormerListAdapter(CustomerManageActivity mContext,
								List<CustomerItem> listCustomers,boolean selectSOrZ) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mListCustomers = listCustomers;
		this.selectSOrZ=selectSOrZ;
	}
	@Override
	public int getCount() {
		if (mListCustomers == null) {
			return 0;
		}
		return mListCustomers.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public CustomerItem getCustomerItem(int position) {
		return mListCustomers.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_custormer_manage_listview, null);
			holder.mTvCustormerId = (TextView) convertView
					.findViewById(R.id.tv_custormerId_customerManageListItem);
			holder.mTvCustormerName = (TextView) convertView
					.findViewById(R.id.tv_custormerName_customerManageListItem);
			holder.mTvDemandType = (TextView) convertView
					.findViewById(R.id.tv_demandType_customerManageListItem);
			holder.mTvDemandDetail = (TextView) convertView
					.findViewById(R.id.tv_houseDetail_customerManageListItem);
			holder.mTvDemandPrice = (TextView) convertView
					.findViewById(R.id.tv_demandPrice_customerManageListItem);
			holder.mTvDescription = (TextView) convertView
					.findViewById(R.id.tv_description_customerManageListItem);
			holder.mTvTime = (TextView) convertView
					.findViewById(R.id.tv_time_customerManageListItem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CustomerItem item = mListCustomers.get(position);
		// 客源编号
		holder.mTvCustormerId.setText(item.getCustCode());
		// 姓名
		holder.mTvCustormerName.setText(item.getName());
		// 需求类型
		holder.mTvDemandType.setText(item.getReqType());
		// 区域+户型+面积
		holder.mTvDemandDetail.setText(item.getArea() + " " + item.getFrame()
				+ " " + item.getAcreage());
		// 价格
		holder.mTvDemandPrice.setText(item.getPrice());
		// 说明
		holder.mTvDescription.setText(item.getOther());
		// 相对日期
		holder.mTvTime.setText(item.getRelativeDate());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectSOrZ){
					Intent intent=new Intent();
					intent.putExtra("custCode",item.getCustCode());
					mContext.setResult(101,intent);
					mContext.finish();
				}else{
					MethodsDeliverData.string = item.getCustCode();
					MethodsExtra.startActivity(mContext,
							CustomerDetailActivity.class);
					if (mContext.isMyCustomerType) {
						MethodsDeliverData.flag1 = -1;
					} else {
						MethodsDeliverData.flag1 = 1;
					}
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView mTvCustormerId;
		TextView mTvCustormerName;
		TextView mTvDemandType;
		TextView mTvDemandDetail;
		TextView mTvDemandPrice;
		TextView mTvDescription;
		TextView mTvTime;
	}
}
