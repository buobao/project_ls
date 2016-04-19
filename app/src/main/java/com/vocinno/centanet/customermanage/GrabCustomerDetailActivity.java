package com.vocinno.centanet.customermanage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.SuperSlideMenuActivity;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.selfdefineview.ListViewNeedResetHeight;
import com.vocinno.centanet.customermanage.adapter.ContentAdapter;
import com.vocinno.centanet.customermanage.adapter.CustomerDetailAdapter;
import com.vocinno.centanet.model.CustomerDetail;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.Requets;
import com.vocinno.centanet.model.Track;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GrabCustomerDetailActivity extends SuperSlideMenuActivity {
	private String mCusterCode = null;
	private View mBackView, mImgViewAddTrack,mSubmit;
	private RelativeLayout mGrabCustomer;
	private TextView mTvCustomerCode, mTvCustomerName, mTvType, mTvAcreage,
			mTvPrice, mTvTenancyTime, mTvMoney, mTvPaymenttype;
	private ListViewNeedResetHeight mLvTracks;
//	private ImageView  mImgViewQQ, mImgWeixin;
	private CustomerDetail mDetail = null;
	private RelativeLayout mImgViewPhone;
	private Drawable drawable;
	private static final int RESET_LISTVIEW_TRACK = 1001;
	private boolean firstRefresh=true,robRefresh=true,returnRefresh=true,firstGetContent=true;//防止重复加载数据
	public GrabCustomerDetailActivity() {
	}
	@Override
	public Handler setHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				GrabCustomerDetailActivity.this.closeMenu(msg);
				switch (msg.what) {
					case RESET_LISTVIEW_TRACK:
						MethodsExtra.resetListHeightBasedOnChildren(mLvTracks);
						break;
					default:
						break;
				}
			}
		};
	}

	@Override
	public int setContentLayoutId() {
		return R.layout.activity_grab_customer_detail;
	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		MethodsExtra.findHeadTitle1(mContext, mRootView, R.string.customernews,
				null);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, mRootView, 0, 0);
		mGrabCustomer = (RelativeLayout) findViewById(R.id.rlyt_seize_customerDetailActivity);
		mTvCustomerCode = (TextView) findViewById(R.id.tv_customercode_customerDetailActivity);
		mTvCustomerName = (TextView) findViewById(R.id.tv_customername_customerDetailActivity);
		mTvType = (TextView) findViewById(R.id.tv_type_customerDetailActivity);
		mTvAcreage = (TextView) findViewById(R.id.tv_acreage_customerDetailActivity);
		mTvPrice = (TextView) findViewById(R.id.tv_price_customerDetailActivity);
		mTvTenancyTime = (TextView) findViewById(R.id.tv_tenancytime_customerDetailActivity);
		mTvMoney = (TextView) findViewById(R.id.tv_money_customerDetailActivity);
		mTvPaymenttype = (TextView) findViewById(R.id.tv_paymenttype_customerDetailActivity);
		mLvTracks = (ListViewNeedResetHeight) findViewById(R.id.lv_track_customerDetailActivity);
		mImgViewAddTrack = findViewById(R.id.imgView_addTrack_customerDetailActivity);
		mImgViewPhone = (RelativeLayout) findViewById(R.id.imgView_phone_customerDetailActivity);
//		mImgViewQQ = (ImageView) findViewById(R.id.imgView_qq_customerDetailActivity);
//		mImgWeixin = (ImageView) findViewById(R.id.imgView_wx_customerDetailActivity);
	}

/*	@Override
	protected void onRestart() {
		super.onRestart();
		// adapter.notifyDataSetChanged();
	}*/
	@Override
	public void setListener() {
		mBackView.setOnClickListener(this);
		mGrabCustomer.setOnClickListener(this);
		mImgViewAddTrack.setOnClickListener(this);
//		mImgViewQQ.setOnClickListener(this);
		mImgViewPhone.setOnClickListener(this);
//		mImgWeixin.setOnClickListener(this);
	}

	@Override
	public void initData() {
//		TAG = this.getClass().getName();
		// 添加通知
		MethodsJni.addNotificationObserver(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT, TAG);
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_CONTACT_RESULT, TAG);
//		mCusterCode = MethodsDeliverData.string;
		mCusterCode=getIntent().getStringExtra("custCode");
		showDialog();
		// 调用数据
		MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
				CST_JS.JS_Function_CustomerList_getCustomerInfo,
				CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
		if (MethodsDeliverData.flag1 == 1) {
//			MethodsDeliverData.flag1 = -1;
			mGrabCustomer.setVisibility(View.VISIBLE);
			mSubmit = MethodsExtra.findHeadRightView1(mContext, mRootView, 0,R.drawable.phone_img);
			mSubmit.setOnClickListener(this);
			mImgViewPhone.setVisibility(View.GONE);
		}else{
			mImgViewPhone.setVisibility(View.VISIBLE);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.img_right_mhead1:
				if(mDetail.getPhone()==null||mDetail.getPhone().length()<=0){
					MethodsExtra.toast(mContext,"暂无联系人号码");
				}else{
					List<CustomerDetail.Content>list=new ArrayList<CustomerDetail.Content>();
					CustomerDetail.Content content=new CustomerDetail().new Content();
					content.setName(mDetail.getName());
					content.setPhone(mDetail.getPhone());
					list.add(content);
					showCallCosturmerDialog(list);
				}
				break;
			case R.id.img_left_mhead1:
				onBack();
				break;
			case R.id.imgView_addTrack_customerDetailActivity:
				MethodsJni.removeNotificationObserver(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
				MethodsDeliverData.string = mCusterCode;
				// listTracks
				Intent intent = new Intent(mContext,
						AddFollowInCustomerActivity.class);
//			MethodsExtra.startActivityForResult(mContext, 10, intent);
				MethodsExtra.startActivityForResult(mContext,10,intent);
				// MethodsExtra.startActivity(mContext,
				// AddFollowInCustomerActivity.class);
				break;
			case R.id.imgView_phone_customerDetailActivity:
				firstGetContent=true;
				showDialog();
				// 调用联系人列表数据
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
						CST_JS.JS_Function_CustomerList_CustContactList,
						CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
				/*if (mDetail != null && mDetail.getPhone() != null) {
					MethodsExtra.tel(mContext, mDetail.getPhone());
				} else {
					MethodsExtra.toast(mContext, "该房源没有维护电话号码");
				}*/
				break;
			/*case R.id.imgView_qq_customerDetailActivity:
				// MethodsExtra.openQQChat(mContext, "504964825");
				if (mDetail != null && !TextUtils.isEmpty(mDetail.getQq())
						&& !mDetail.getQq().equals("null")) {
					MethodsExtra.openQQChat(mContext, mDetail.getQq());
				} else {
					MethodsExtra.toast(mContext, "该房源没有维护QQ号码");
				}
				break;
			case R.id.imgView_wx_customerDetailActivity:
				if (mDetail != null && !TextUtils.isEmpty(mDetail.getWechat())
						&& !mDetail.getWechat().equals("null")) {
					try {
						// 登录微信
						Intent intent1 = new Intent();
						ComponentName cmp = new ComponentName("com.tencent.mm",
								"com.tencent.mm.ui.LauncherUI");
						intent1.setAction(Intent.ACTION_MAIN);
						intent1.addCategory(Intent.CATEGORY_LAUNCHER);
						intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent1.setComponent(cmp);
						startActivity(intent1);
					} catch (Exception e) {
						MethodsExtra.toast(mContext, "没有安装微信");
					}
				} else {
					MethodsExtra.toast(mContext, "该房源没有维护微信号码");
				}
				break;*/
			case R.id.rlyt_seize_customerDetailActivity:
				mGrabCustomer.setClickable(false);
				showDialog();
				// 抢
				robRefresh=true;
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,CST_JS.JS_Function_CustomerList_claimCustomer,CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));
				mGrabCustomer.setClickable(true);
			default:
				break;
		}
	}

	@Override
	public void onBack() {
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
		MethodsJni.removeNotificationObserver(
				CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT, TAG);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*if (data == null) {
			return;
		}*/
		if (resultCode == ConstantResult.REFRESH) {
			/*MethodsJni.addNotificationObserver(CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT, TAG);
			showDialog();
			firstRefresh=true;
			// 调用数据
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,CST_JS.JS_Function_CustomerList_getCustomerInfo,CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));

			if (MethodsDeliverData.flag1 == 1) {
				MethodsDeliverData.flag1 = -1;
				mGrabCustomer.setVisibility(View.VISIBLE);
			}*/
			firstRefresh=true;
//			MethodsJni.removeNotificationObserver(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
//			MethodsJni.addNotificationObserver(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, TAG);
			showDialog();
			// 调用数据
			MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
					CST_JS.JS_Function_CustomerList_getCustomerInfo,
					CST_JS.getJsonStringForGetCustomerInfo(mCusterCode));

//			initData();

			/*Track track = new Track();
			track.setTracktime(data.getStringExtra("time"));
			track.setContent(data.getStringExtra("content"));
			listTracks.add(listTracks.size(), track);
			adapter = new CustomerDetailAdapter(mContext, listTracks);
			mLvTracks.setAdapter(adapter);
			mHander.sendEmptyMessageDelayed(RESET_LISTVIEW_TRACK, 50);
			adapter.notifyDataSetChanged();*/
		}
	}
	private Dialog mCallCustormerDialog;
	private void showCallCosturmerDialog(List<CustomerDetail.Content> list) {
		mCallCustormerDialog = new Dialog(mContext, R.style.Theme_dialog);
		mCallCustormerDialog
				.setContentView(R.layout.dialog_call_custormer_house_resouse_detial);
		Window win = mCallCustormerDialog.getWindow();
		win.setGravity(Gravity.BOTTOM);
		mCallCustormerDialog.setCanceledOnTouchOutside(true);

		win.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		ListView mListViewCustormer = (ListView) mCallCustormerDialog
				.findViewById(R.id.lv_custormerPhone_HouseDetailActivity);

		ContentAdapter phoneAdapter = new ContentAdapter(
				mContext, list);
		mListViewCustormer.setAdapter(phoneAdapter);
		mListViewCustormer
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
						TextView tvTel = (TextView) arg1
								.findViewById(R.id.tv_custNothing_CustormerPhoneAdapter);

						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + tvTel.getText().toString()));
						mContext.startActivity(intent);
						mCallCustormerDialog.dismiss();
					}
				});
		mCallCustormerDialog.show();
	}
	@Override
	public void notifCallBack(String name, String className, Object data) {
		dismissDialog();
		String strJson = (String) data;
		JSReturn jsReturn = MethodsJson.jsonToJsReturn(strJson,
				CustomerDetail.class);
		if(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_CONTACT_RESULT.equals(name)){
			if(firstGetContent){
				CustomerDetail contentList=getContent(strJson);
				if(contentList!=null){
					List<CustomerDetail.Content> list=contentList.getContent();
					showCallCosturmerDialog(list);
				}else{
					MethodsExtra.toast(mContext,jsReturn.getMsg());
				}
				firstGetContent=false;
			}
//			Log.i("jsReturn", "jsReturn"+jsReturn);
		}else if(name.equals(CST_JS.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT)){
			if(firstRefresh){
				mDetail = (CustomerDetail) jsReturn.getObject();
				mTvCustomerCode.setText("编号：" + mDetail.getCustCode());
				mTvCustomerName.setText("姓名：" + mDetail.getName());
				mTvPaymenttype.setText("付款方式：" + mDetail.getPaymentType());
				if (mDetail.isPay() == false) {
					mTvMoney.setText(R.string.money_false);
				} else {
					mTvMoney.setText(R.string.money_true);
				}
				// 填充跟踪信息列表
				listTracks = mDetail.getTracks();
				if (listTracks != null && listTracks.size() >= 1) {
					adapter = new CustomerDetailAdapter(mContext, listTracks);
					mLvTracks.setAdapter(adapter);
					mHander.sendEmptyMessageDelayed(RESET_LISTVIEW_TRACK, 50);
				}
				// 填充需求信息
				List<Requets> listReqs = mDetail.getRequets();
				if (listReqs != null && listReqs.size() >= 1) {
					Requets req = listReqs.get(0);
					mTvType.setText("类型：" + req.getReqType());// 类型
					mTvAcreage.setText("区域：" + req.getAcreage());
					mTvPrice.setText("租价：" + req.getPrice());// 价格
					mTvTenancyTime.setText("租期：" + req.getTenancyTime());
				}
				// 联系方式
				if (mDetail == null || TextUtils.isEmpty(mDetail.getPhone())||mDetail.getPhone().equals("null")) {
//					mImgViewPhone.setImageResource(R.drawable.c_manage_icon_contact01);
				}
				if (mDetail == null || TextUtils.isEmpty(mDetail.getQq())||mDetail.getQq().equals("null")) {
//					mImgViewQQ.setImageResource(R.drawable.c_manage_icon_qq01);
				}
				if (mDetail == null || TextUtils.isEmpty(mDetail.getWechat())||mDetail.getWechat().equals("null")) {
//					mImgWeixin.setImageResource(R.drawable.c_manage_icon_wechat01);
				}
				firstRefresh=false;
			}
		}else if (name.equals(CST_JS.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT)) {
			if (jsReturn.isSuccess()) {
				if(returnRefresh){
					MethodsExtra.toast(mContext, jsReturn.getMsg());
					setResult(ConstantResult.REFRESH);
					returnRefresh=false;
					onBack();
				}
			} else {
				if(robRefresh){
					if (!jsReturn.isSuccess() || jsReturn.getObject() == null) {
						MethodsExtra.toast(mContext,jsReturn.getMsg());
						/*MyDialog.Builder dialog=new MyDialog.Builder(this);
						dialog.setTitle("提示");
						dialog.setMessage(jsReturn.getMsg());
						dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						dialog.create().show();*/
					}
					robRefresh=false;
				}
//				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		}
	}

	private CustomerDetail getContent(String strJson) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(strJson);
			CustomerDetail customerDetail=(CustomerDetail)new Gson().fromJson(jsonObject.toString(),CustomerDetail.class);
//			customerDetail.getContent();
			return customerDetail;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		MethodsJni.removeAllNotifications(TAG);
	}

	private CustomerDetailAdapter adapter;
	private List<Track> listTracks;

	public TextView getmTvCustomerCode() {
		return mTvCustomerCode;
	}
}