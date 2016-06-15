package com.vocinno.centanet.customermanage;

import android.app.Dialog;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.customermanage.adapter.ImportCustormerAdapter;
import com.vocinno.centanet.housemanage.adapter.SearchAdapter;
import com.vocinno.centanet.model.CustomerList;
import com.vocinno.centanet.model.EstateSearchItem;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.myinterface.NoDoubleClickListener;
import com.vocinno.centanet.tools.Loading;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.MyUtils;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.MyConstant;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客源管理
 *
 * @author Administrator
 */
public class ChoosePeopleActivity extends OtherBaseActivity  {
    private Dialog mMenuDialog, mSearchDialog;
    private View mBack;
    private ImportCustormerAdapter mListAdapter;
    private List<EstateSearchItem> mSearchListData;
    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_choose_people;
    }
    @Override
    public void initView() {
        intent = getIntent();
        MethodsExtra.findHeadTitle1(mContext, baseView,
                R.string.choose_people, null);
        mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
//        getCustomerData();
    }

    private void getCustomerData(String customCode, int pageNo, final boolean isRefresh) {
        if (isReFreshOrLoadMore) {
            isReFreshOrLoadMore = false;
        } else {
            Loading.showForExit(this, true);
        }
        URL = NetWorkConstant.PORT_URL + NetWorkMethod.custlist;
        Map<String, String> map = new HashMap<String, String>();
        if (null != customCode) {
            map.put(NetWorkMethod.custCode, customCode);
        }
        map.put(NetWorkMethod.page, pageNo + "");
        map.put(NetWorkMethod.pageSize, MyConstant.pageSize + "");
        String type = NetWorkMethod.my;//搜索还有
        map.put(NetWorkMethod.type, type);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Loading.dismissLoading();
            }

            @Override
            public void onResponse(String response) {
                Loading.dismissLoading();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, CustomerList.class);
                if (jsReturn.isSuccess()) {
                    if (jsReturn.getListDatas() != null && jsReturn.getListDatas().size() < MyConstant.pageSize) {
                    } else {
                    }
                    if (isRefresh) {
                        mListAdapter.setListData(jsReturn.getListDatas());
                    } else {
                        page++;
                        mListAdapter.addListData(jsReturn.getListDatas());
                    }
                } else {
                    MyToast.showToast(jsReturn.getMsg());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_search_customer:    //标题右侧Toast 关键词搜索
                showSearchDialog();
                mMenuDialog.dismiss();
                break;
            case R.id.img_left_mhead1:        //标题右侧按钮
                finish();
                break;
            case R.id.btn_close_dialogSearchHouseManage:
                mEtSearch.setText("");
                mSearch.setList(null);
                mSearch.notifyDataSetChanged();
                MyUtils.setListHeight(mSearch, searchListView);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {
    }


    private void showMenuDialog() {
        mMenuDialog = new Dialog(mContext, R.style.Theme_dialog);
        mMenuDialog.setContentView(R.layout.dialog_menu_customer_manage);
        Window win = mMenuDialog.getWindow();
        win.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.RIGHT | Gravity.TOP);
        mMenuDialog.setCanceledOnTouchOutside(true);
        mMenuDialog.show();
        LinearLayout ll_search_customer = (LinearLayout) mMenuDialog
                .findViewById(R.id.ll_search_customer);
        ll_search_customer.setOnClickListener(this);
        LinearLayout ll_add_customer = (LinearLayout) mMenuDialog
                .findViewById(R.id.ll_add_customer);
        ll_add_customer.setOnClickListener(this);
        ll_add_customer.setVisibility(View.GONE);
    }

    private ListView searchListView;
    public static EditText mEtSearch;
    private SearchAdapter mSearch;

    private void showSearchDialog() {
        mSearchDialog = new Dialog(mContext, R.style.Theme_dialog);
        mSearchDialog.setContentView(R.layout.dialog_search_house_manage);
        Window win = mSearchDialog.getWindow();
        win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.TOP);
        mSearchDialog.setCanceledOnTouchOutside(true);
        searchListView = (ListView) mSearchDialog.findViewById(R.id.lv_historySearch_dialogSearchHouseManage);
        mSearch = new SearchAdapter(mContext,
                new ArrayList<EstateSearchItem>());
        searchListView.setAdapter(mSearch);

        mEtSearch = (EditText) mSearchDialog
                .findViewById(R.id.et_search_dialogSearchHouseManage);
        mEtSearch.setHint(getText(R.string.search_cust_hit));
        Button mBtnSearch = (Button) mSearchDialog
                .findViewById(R.id.btn_search_dialogSearchHouseManage);
        Button mBtnClean = (Button) mSearchDialog
                .findViewById(R.id.btn_close_dialogSearchHouseManage);
        mBtnSearch.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                searchKeYuan(mEtSearch.getText().toString().trim());
            }
        });
        mBtnClean.setOnClickListener(this);
        // 根据mEtSearch得到的字符串去请求

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//				searchKeYuan(arg0.toString().trim());
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String custCode = mSearchListData.get(arg2).getCustCode();
                mSearchDialog.dismiss();
                getCustomerData(custCode, 1, true);
                /*String reqparm = CST_JS.getJsonStringForCustomerList((isMyCustomerType ? CST_JS.JS_CustomerList_Type_My
						: CST_JS.JS_CustomerList_Type_Public), custCode, 1, 20);
				MethodsJni.callProxyFun(CST_JS.JS_ProxyName_CustomerList,
						CST_JS.JS_Function_CustomerList_getList, reqparm);
				showDialog();*/
            }
        });
        mSearchDialog.show();
    }

    private void searchKeYuan(String editString) {
        mSearch.setList(null);
        searchListView.setAdapter(mSearch);
        MyUtils.setListHeight(mSearch, searchListView);
        if (editString == null || editString.length() <= 0) {
            mSearch.setList(null);
            searchListView.setAdapter(mSearch);
        } else {
            String paramType = NetWorkMethod.text;
            if (MethodsExtra.isNumeric(editString.toString().trim())) {
                if (!MethodsExtra.isMobileNO(editString.toString().trim())) {
                    MethodsExtra.toast(mContext, "请输入正确的手机号码");
                    return;
                }
                paramType = NetWorkMethod.character;
            } else {
                mSearch.setColorText(editString.toString().trim());
                paramType = NetWorkMethod.text;
            }

            Loading.show(this);
            URL = NetWorkConstant.PORT_URL + NetWorkMethod.custListMobileSerarch;
            Map<String, String> map = new HashMap<String, String>();
            map.put(NetWorkMethod.page, 1 + "");
            map.put(NetWorkMethod.pageSize, MyConstant.pageSize + "");
            String type = NetWorkMethod.my;
            map.put(NetWorkMethod.type, type);
            map.put(NetWorkMethod.name, editString);
            map.put(NetWorkMethod.paramType, paramType);
            OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    stopRefreshOrLoadMore();
                }

                @Override
                public void onResponse(String response) {
                    stopRefreshOrLoadMore();
                    JSReturn jReturn = MethodsJson.jsonToJsReturn(response, EstateSearchItem.class);
                    if (jReturn.isSuccess()) {
                        if (jReturn.getListDatas() != null && jReturn.getListDatas().size() > 0) {
                            mSearchListData = jReturn.getListDatas();
                            mSearch.setList(mSearchListData);
                            mSearch.notifyDataSetChanged();
                            MyUtils.setListHeight(mSearch, searchListView);
                            searchListView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        MethodsExtra.toast(mContext, jReturn.getMsg());
                    }
                }
            });

			/*// 在打字期间添加搜索栏数据
			String reqparm = CST_JS.getJsonStringForKeYuanGuanJianZi((isMyCustomerType ? CST_JS.JS_CustomerList_Type_My:CST_JS.JS_CustomerList_Type_Public),editString,paramType, 1, 20);
			MethodsJni.callProxyFun(hif, CST_JS.JS_ProxyName_CustomerList,
					CST_JS.JS_Function_CustListMobile_Serarch, reqparm);*/
        }
    }
}