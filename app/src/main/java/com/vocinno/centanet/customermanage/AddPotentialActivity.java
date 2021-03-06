package com.vocinno.centanet.customermanage;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_JS;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.CustomUtils;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.MethodsJson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 添加客户
 *
 * @author Administrator
 *
 */
@SuppressLint("CutPasteId")
public class AddPotentialActivity extends OtherBaseActivity {
	private Map<String, String> mapPianQu = new HashMap<String, String>();
	private static enum ConnectionType {
		none, connTel, connQQ, connWeixin
	};
	private View mBackView;
	private TextView mSubmitView;		//标题栏右侧
	private EditText et_name_addqianke,et_tel_addqianke;
	private EditText /*mEtConnectionNumber, */mEtCustormerName;
	private LinearLayout ll_source_addpotential,ll_level_addpotential;
	private ConnectionType mCurrConnType = ConnectionType.none;
	private CheckBox cb_source_addpotential, cb_level_addpotential;

	private RelativeLayout rl_choose_source_addpotential,rl_choose_level_addpotential;
	private WheelView wv_source_addpotential, wv_level_addpotential;
	private Button bt_source_addpotential, bt_level_addpotential;
	private TextView tv_source_addpotential,tv_level_addpotential;

	private int isSameTel = 0;	//是否是同一个号码
	@Override
	public int setContentLayoutId() {
		return R.layout.activity_add_potential;
	}

	@Override
	public void initView() {
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		mBackView = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
		mSubmitView = (TextView)MethodsExtra.findHeadRightView1(mContext, baseView,R.string.save,0);

		MethodsExtra.findHeadTitle1(mContext, baseView, R.string.add_potential_customer,
				null);

		et_name_addqianke = (EditText) findViewById(R.id.et_name_addqianke);
		et_tel_addqianke = (EditText) findViewById(R.id.et_tel_addqianke);

		tv_source_addpotential= (TextView) findViewById(R.id.tv_source_addpotential);
		tv_level_addpotential= (TextView) findViewById(R.id.tv_level_addpotential);

		rl_choose_source_addpotential=(RelativeLayout)findViewById(R.id.rl_choose_source_addpotential);	//来源下拉
		rl_choose_source_addpotential.setVisibility(View.GONE);
		rl_choose_level_addpotential=(RelativeLayout)findViewById(R.id.rl_choose_level_addpotential);	//等级下拉
		rl_choose_level_addpotential.setVisibility(View.GONE);

		wv_source_addpotential = (WheelView) rl_choose_source_addpotential.findViewById(R.id.wheelview_modelOneWheelView);
		wv_level_addpotential = (WheelView) rl_choose_level_addpotential.findViewById(R.id.wheelview_modelOneWheelView);

		ll_source_addpotential=(LinearLayout)findViewById(R.id.ll_source_addpotential);
		ll_source_addpotential.setOnClickListener(this);

		ll_level_addpotential=(LinearLayout)findViewById(R.id.ll_level_addpotential);
		ll_level_addpotential.setOnClickListener(this);

		cb_source_addpotential = (CheckBox) findViewById(R.id.cb_source_addpotential);

		cb_level_addpotential = (CheckBox) findViewById(R.id.cb_level_addpotential);

		bt_source_addpotential = (Button) rl_choose_source_addpotential.findViewById(R.id.btn_submit_modelOneWheelView);
		bt_source_addpotential.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String source = wv_source_addpotential.getSelectedText();
				tv_source_addpotential.setText(source);
				rl_choose_source_addpotential.setVisibility(View.GONE);
				cb_source_addpotential.setChecked(!cb_source_addpotential.isChecked());
				checkIsFinish();
			}
		});
		bt_level_addpotential = (Button) rl_choose_level_addpotential.findViewById(R.id.btn_submit_modelOneWheelView);
		bt_level_addpotential.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String level = wv_level_addpotential.getSelectedText();
				tv_level_addpotential.setText(level);
				rl_choose_level_addpotential.setVisibility(View.GONE);
				cb_level_addpotential.setChecked(!cb_level_addpotential.isChecked());
				checkIsFinish();
			}
		});

		setListener();
		setData();
	}

	private void setData() {
		wv_source_addpotential.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.source), CustomUtils.getWindowWidth(this));
		wv_source_addpotential.setEnabled(true);
		wv_source_addpotential.setSelectItem(0);

		wv_level_addpotential.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.level), CustomUtils.getWindowWidth(this));
		wv_level_addpotential.setEnabled(true);
		wv_level_addpotential.setSelectItem(0);
	}

	public void setListener() {
		mBackView.setOnClickListener(this);
		mSubmitView.setOnClickListener(this);
		mSubmitView.setClickable(false);
		mSubmitView.setEnabled(false);
		et_name_addqianke.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				checkIsFinish();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});
		et_tel_addqianke.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				checkIsFinish();
				//检测电话号码是否重复
				String tel = et_tel_addqianke.getText().toString().trim();
				if (tel.length() >= 11) {
					checkPhoneNum(tel);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});

	}
	@Override
	public void initData() {
		// 添加通知
		MethodsJni.addNotificationObserver(
				CST_JS.NOTIFY_NATIVE_ADD_CUSTOMER_RESULT, TAG);
	}
	@Override
	public Handler setHandler() {
		return null;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.img_left_mhead1:		//标题栏左侧图片
				finish();
				break;
			case R.id.tv_right_mhead1:		//标题栏右侧文字
				if(!isMobileNO(et_tel_addqianke.getText().toString().trim())){
					MethodsExtra.toast(mContext,"手机号码格式不正确");
					return;
				}

				if (isSameTel == 0) {
					MethodsExtra.toast(mContext, "手机号码请查重");
					return;
				} else if (isSameTel == 2) {
					MethodsExtra.toast(mContext, "手机号码重复");
					return;
				}
				// 上传数据
				Loading.show(this);
				URL= NetWorkConstant.PORT_URL+ NetWorkMethod.addPotential;
				Map<String,String>map=new HashMap<String,String>();
				String name=et_name_addqianke.getText().toString();
				String phone=et_tel_addqianke.getText().toString();
				String source=tv_source_addpotential.getText().toString();
				String origin = CST_Wheel_Data.sourceMap.get(source);
				String level = tv_level_addpotential.getText().toString();
				map.put(NetWorkMethod.name,name);
				map.put(NetWorkMethod.phone,phone);
				map.put(NetWorkMethod.origin,origin);
				map.put(NetWorkMethod.rank, level);
				OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
					@Override
					public void onError(Request request, Exception e) {
						Loading.dismissLoading();
					}
					@Override
					public void onResponse(String response) {
						Loading.dismissLoading();
						JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, Object.class);
						if (jsReturn.isSuccess()) {
							MethodsExtra.toast(mContext, jsReturn.getMsg());
							setResult(MyConstant.REFRESH);
							finish();
						} else {
							MethodsExtra.toast(mContext, jsReturn.getMsg());
						}
					}
				});

				/*String strJson = CST_JS.getJsonStringForAddCustomer(et_name_addqianke.getText().toString().toString(),
						et_tel_addqianke.getText().toString());
				MethodsJni.callProxyFun(hif,CST_JS.JS_ProxyName_CustomerList,
						CST_JS.JS_Function_CustomerList_addCustomer, strJson);*/
				break;
			case R.id.ll_source_addpotential:
				cb_source_addpotential.setChecked(!cb_source_addpotential.isChecked());
				if(cb_source_addpotential.isChecked()){
					rl_choose_source_addpotential.setVisibility(View.VISIBLE);
					checkOpenOrClose(rl_choose_source_addpotential.getId());
				}else{
					rl_choose_source_addpotential.setVisibility(View.GONE);
				}
				break;
			case R.id.ll_level_addpotential:
				cb_level_addpotential.setChecked(!cb_level_addpotential.isChecked());
				if(cb_level_addpotential.isChecked()){
					rl_choose_level_addpotential.setVisibility(View.VISIBLE);
					checkOpenOrClose(rl_choose_level_addpotential.getId());
				}else{
					rl_choose_level_addpotential.setVisibility(View.GONE);
				}
				break;
			default:
				break;
		}
	}
	private void checkOpenOrClose(int intId) {
		if (intId != rl_choose_source_addpotential.getId()) {
			closeLaiYuanContainer();
		}
		if (intId != rl_choose_level_addpotential.getId()) {
			closeDengJiContainer();
		}
	}
	private void closeLaiYuanContainer() {
		rl_choose_source_addpotential.setVisibility(View.GONE);
		cb_source_addpotential.setChecked(false);
	}
	private void closeDengJiContainer() {
		rl_choose_level_addpotential.setVisibility(View.GONE);
		cb_level_addpotential.setChecked(false);
	}
	@Override
	public void netWorkResult(String name, String className, Object data) {
		JSReturn jsReturn;
		if(name.equals(CST_JS.NOTIFY_NATIVE_CHECK_PNONENO)){
			jsReturn = MethodsJson.jsonToJsReturn((String) data, JSONObject.class);
			if(jsReturn.isSuccess()){
			}else{
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		} else {
			modelDialog.dismiss();
			jsReturn = MethodsJson.jsonToJsReturn((String) data, Object.class);
			if (jsReturn.isSuccess()) {
				MethodsExtra.toast(mContext, jsReturn.getMsg());
				finish();
			} else {
				MethodsExtra.toast(mContext, jsReturn.getMsg());
			}
		}
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
	/**************************检测是否信息都录入完整****************************/
	private void checkIsFinish() {
		boolean isFinish = true;
		String name = et_name_addqianke.getText().toString().trim();
		String tel = et_tel_addqianke.getText().toString().trim();
		if (name.length() <= 0 || tel.length() <= 0) {
			isFinish=false;
		} else if(tel.toString().trim().length() != 11||!isMobileNO(tel)) {
			isFinish=false;
		}else if(tv_source_addpotential==null||tv_source_addpotential.getText().toString().trim().length()<=0){
			isFinish=false;
		}else if(tv_level_addpotential==null||tv_level_addpotential.getText().toString().trim().length()<=0){
			isFinish=false;
		}
		if (isFinish) {
			mSubmitView.setClickable(true);
			mSubmitView.setEnabled(true);
		} else {
			mSubmitView.setClickable(false);
			mSubmitView.setEnabled(false);
		}
	}

	public static boolean isMobileNO(String mobiles) {
		if(mobiles!=null){
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0,7]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			return m.matches();
		}
		return false;
	}

	public void notifCallBack(String name, String className, Object data) {

	}
	public void setLoseFocus(){
		if(isMobileNO("") && !TextUtils.isEmpty("")){
			if(et_tel_addqianke.isFocusable()){
				et_tel_addqianke.setFocusable(false);
			}
		}else{
			MethodsExtra.toast(mContext, "手机号码有误，请重新输入！");
			et_tel_addqianke.setFocusable(true);
			et_tel_addqianke.setFocusableInTouchMode(true);
		}
	}

	/**************************判断号码是否重复  隐藏dialog框****************************/
	private void checkPhoneNum(String tel) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(NetWorkMethod.phone, tel);
		URL = NetWorkConstant.PORT_URL + NetWorkMethod.checkMpNo;
		//显示dialog
		showDialog();
		OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				dismissDialog();
			}

			@Override
			public void onResponse(String response) {
				dismissDialog();
				JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, JSONObject.class);
				if (jsReturn.isSuccess()) {
					isSameTel = 1;
				} else {
					isSameTel = 2;
					MethodsExtra.toast(mContext, jsReturn.getMsg());
				}
			}
		});
	}

}
