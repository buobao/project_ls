package com.vocinno.centanet.customermanage;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.cst.CST_Wheel_Data;
import com.vocinno.centanet.apputils.selfdefineview.WheelView;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;
import com.vocinno.centanet.tools.MyToast;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.CustomUtils;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.view.refreshablelistview.XListView;

import java.util.HashMap;
import java.util.Map;

public class AddDemandActivity extends OtherBaseActivity {

    private XListView mLvCustormers;
    private View mBack;
    private ImageView mSubmitView,iv_zu_demand,iv_gou_demand;
    private Intent intent;

    private RelativeLayout rl_type_demand,rl_fangxing_demand,rl_place_demand,rl_pianqu_demand,rl_area_demand,rl_price_demand;
    private RelativeLayout il_fangxing_demand,il_place_demand,il_pianqu_demand,il_area_demand,il_price_demand;
    private WheelView wv_start_fangxing_demand,wv_end_fangxing_demand,wv_place_demand,wv_pianqu_demand,wv_start_area_demand,wv_end_area_demand,wv_start_price_demand,wv_end_price_demand;
    private LinearLayout ll_type_demand;
    private RelativeLayout rl_zu_demand,rl_gou_demand;
    private CheckBox cb_type_demand,cb_fangxing_demand,cb_place_demand,cb_pianqu_demand,cb_area_demand,cb_price_demand;
    private TextView tv_type_demand,tv_fangxing_demand,tv_changePlace_demand,tv_changePianqu_demand,tv_changeArea_demand,tv_changePrice_demand;
    private Button bt_fangxing_submit,bt_place_submit,bt_pianqu_submit,bt_area_submit,bt_price_submit;
    private String custCode;
    private String reqType;
    private TextView tv_min_fangxing,tv_max_fangxing,tv_min_area,tv_max_area;
    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_add_demand;
    }

    @Override
    public void initView() {
        custCode = getIntent().getStringExtra("custCode");
        MethodsExtra.findHeadTitle1(mContext, baseView,
                R.string.my_potential_customer, null);
        mBack = MethodsExtra.findHeadLeftView1(mContext, baseView, 0, 0);
        mBack.setOnClickListener(this);
        mSubmitView = (ImageView)MethodsExtra.findHeadRightView1(mContext, baseView, 0,
                R.drawable.universal_button_undone);
        mSubmitView.setClickable(false);
        mSubmitView.setEnabled(false);
        mSubmitView.setOnClickListener(this);

        iv_zu_demand= (ImageView) findViewById(R.id.iv_zu_demand);
        iv_gou_demand= (ImageView) findViewById(R.id.iv_gou_demand);

        rl_type_demand= (RelativeLayout) findViewById(R.id.rl_type_demand);
        rl_type_demand.setOnClickListener(this);

        rl_fangxing_demand= (RelativeLayout) findViewById(R.id.rl_fangxing_demand);
        rl_fangxing_demand.setOnClickListener(this);

        rl_place_demand= (RelativeLayout) findViewById(R.id.rl_place_demand);
        rl_place_demand.setOnClickListener(this);

        rl_pianqu_demand= (RelativeLayout) findViewById(R.id.rl_pianqu_demand);
        rl_pianqu_demand.setOnClickListener(this);

        rl_area_demand= (RelativeLayout) findViewById(R.id.rl_area_demand);
        rl_area_demand.setOnClickListener(this);

        rl_price_demand= (RelativeLayout) findViewById(R.id.rl_price_demand);
        rl_price_demand.setOnClickListener(this);


        cb_type_demand= (CheckBox) findViewById(R.id.cb_type_demand);
        cb_type_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_fangxing_demand= (CheckBox) findViewById(R.id.cb_fangxing_demand);
        cb_fangxing_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_place_demand= (CheckBox) findViewById(R.id.cb_place_demand);
        cb_place_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_pianqu_demand= (CheckBox) findViewById(R.id.cb_pianqu_demand);
        cb_pianqu_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_area_demand= (CheckBox) findViewById(R.id.cb_area_demand);
        cb_area_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());

        cb_price_demand= (CheckBox) findViewById(R.id.cb_price_demand);
        cb_price_demand.setOnCheckedChangeListener(getCheckBoxChangeListener());


        rl_zu_demand= (RelativeLayout) findViewById(R.id.rl_zu_demand);
        rl_zu_demand.setOnClickListener(this);
        rl_gou_demand= (RelativeLayout) findViewById(R.id.rl_gou_demand);
        rl_gou_demand.setOnClickListener(this);

        ll_type_demand= (LinearLayout) findViewById(R.id.ll_type_demand);
        il_fangxing_demand= (RelativeLayout) findViewById(R.id.il_fangxing_demand);
        il_fangxing_demand.setVisibility(View.GONE);

        il_place_demand= (RelativeLayout) findViewById(R.id.il_place_demand);
        il_place_demand.setVisibility(View.GONE);

        il_pianqu_demand= (RelativeLayout) findViewById(R.id.il_pianqu_demand);
        il_pianqu_demand.setVisibility(View.GONE);

        il_area_demand= (RelativeLayout) findViewById(R.id.il_area_demand);
        il_area_demand.setVisibility(View.GONE);

        il_price_demand= (RelativeLayout) findViewById(R.id.il_price_demand);
        il_price_demand.setVisibility(View.GONE);


        wv_start_fangxing_demand= (WheelView) il_fangxing_demand.findViewById(R.id.wheelview_start_modelTwoWheelView);
        wv_end_fangxing_demand= (WheelView) il_fangxing_demand.findViewById(R.id.wheelview_end_modelTwoWheelView);
        wv_place_demand= (WheelView) il_place_demand.findViewById(R.id.wheelview_modelOneWheelView);
        wv_pianqu_demand= (WheelView) il_pianqu_demand.findViewById(R.id.wheelview_modelOneWheelView);
        wv_start_area_demand= (WheelView) il_area_demand.findViewById(R.id.wheelview_start_modelTwoWheelView);
        wv_end_area_demand= (WheelView) il_area_demand.findViewById(R.id.wheelview_end_modelTwoWheelView);
        wv_start_price_demand= (WheelView) il_price_demand.findViewById(R.id.wheelview_start_modelTwoWheelView);
        wv_end_price_demand= (WheelView) il_price_demand.findViewById(R.id.wheelview_end_modelTwoWheelView);

        tv_min_fangxing= (TextView) il_fangxing_demand.findViewById(R.id.tv_startTitle_modelTwoWheelView);
        tv_max_fangxing= (TextView) il_fangxing_demand.findViewById(R.id.tv_endTitle_modelTwoWheelView);
        tv_min_area= (TextView) il_area_demand.findViewById(R.id.tv_startTitle_modelTwoWheelView);
        tv_max_area= (TextView) il_area_demand.findViewById(R.id.tv_endTitle_modelTwoWheelView);
        tv_min_fangxing.setText("最小房型");
        tv_max_fangxing.setText("最大房型");
        tv_min_area.setText("最小面积");
        tv_max_area.setText("最大面积");

        bt_fangxing_submit= (Button) il_fangxing_demand.findViewById(R.id.btn_submit_modelTwoWheelView);
        bt_fangxing_submit.setOnClickListener(submitSelect(0));
        bt_place_submit= (Button) il_place_demand.findViewById(R.id.btn_submit_modelOneWheelView);
        bt_place_submit.setOnClickListener(submitSelect(1));
        bt_pianqu_submit= (Button) il_pianqu_demand.findViewById(R.id.btn_submit_modelOneWheelView);
        bt_pianqu_submit.setOnClickListener(submitSelect(2));
        bt_area_submit = (Button) il_area_demand.findViewById(R.id.btn_submit_modelTwoWheelView);
        bt_area_submit.setOnClickListener(submitSelect(3));
        bt_price_submit= (Button) il_price_demand.findViewById(R.id.btn_submit_modelTwoWheelView);
        bt_price_submit.setOnClickListener(submitSelect(4));


        tv_type_demand= (TextView) findViewById(R.id.tv_type_demand);
        tv_fangxing_demand= (TextView) findViewById(R.id.tv_fangxing_demand);
        tv_changePlace_demand= (TextView) findViewById(R.id.tv_changePlace_demand);
        tv_changePianqu_demand= (TextView) findViewById(R.id.tv_changePianqu_demand);
        tv_changeArea_demand= (TextView) findViewById(R.id.tv_changeArea_demand);
        tv_changePrice_demand= (TextView) findViewById(R.id.tv_changePrice_demand);
        initWheelViewData();
    }

    @NonNull
    private View.OnClickListener submitSelect(final int type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case 0:
                        String fangxing = wv_start_fangxing_demand.getSelectedText();
                        String maxFangxing = wv_end_fangxing_demand.getSelectedText();
                        if(Integer.parseInt(fangxing.replace("室",""))>=Integer.parseInt(maxFangxing.replace("室", ""))){
                            MyToast.showToast("最大房型必须大于最小房型");
                            return;
                        }else{
                            tv_fangxing_demand.setText(wv_start_fangxing_demand.getSelectedText()+"-"+wv_end_fangxing_demand.getSelectedText());
                            cb_fangxing_demand.setChecked(false);
                            il_fangxing_demand.setVisibility( View.GONE);
                        }
                    break;
                    case 1:
                        tv_changePlace_demand.setText(wv_place_demand.getSelectedText());
                        cb_place_demand.setChecked(false);
                        il_place_demand.setVisibility( View.GONE);
                    break;
                    case 2:
                        tv_changePianqu_demand.setText(wv_pianqu_demand.getSelectedText());
                        cb_pianqu_demand.setChecked(false);
                        il_pianqu_demand.setVisibility(View.GONE);
                        String url= NetWorkConstant.PORT_URL+ NetWorkMethod.addCustomerdelMobile;
                        Map<String,String>map=new HashMap<String,String>();
                        map.put(NetWorkConstant.token,myApp.getToken());
                        /////////////////////////////////////
                        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback() {
                            @Override
                            public void onError(Request request, Exception e) {

                            }

                            @Override
                            public void onResponse(Object response) {

                            }
                        },map);
                    break;
                    case 3:
                        String area = wv_start_area_demand.getSelectedText();
                        String maxarea = wv_end_area_demand.getSelectedText();
                        if(Integer.parseInt(area.replace("平米",""))>=Integer.parseInt(maxarea.replace("平米", ""))){
                            MyToast.showToast("最大面积应大于最小面积");
                            return;
                        }else{
                            tv_changeArea_demand.setText(wv_start_area_demand.getSelectedText()+"-"+wv_end_area_demand.getSelectedText());
                            cb_area_demand.setChecked(false);
                            il_area_demand.setVisibility(View.GONE);
                        }
                    break;
                    case 4:
                        String price = wv_start_price_demand.getSelectedText();
                        String maxprice = wv_end_price_demand.getSelectedText();
                        if("rent".equals(reqType)){
                            if(Integer.parseInt(price.replace("元",""))>=Integer.parseInt(maxprice.replace("元", ""))){
                                MyToast.showToast("最高价格应大于最低价格");
                                return;
                            }else{
                                tv_changePrice_demand.setText(wv_start_price_demand.getSelectedText()+"-"+wv_end_price_demand.getSelectedText());
                                cb_price_demand.setChecked(false);
                                il_price_demand.setVisibility(View.GONE);
                            }
                        }else{
                            if(Integer.parseInt(price.replace("万",""))>=Integer.parseInt(maxprice.replace("万", ""))){
                                MyToast.showToast("最高价格应大于最低价格");
                                return;
                            }else{
                                tv_changePrice_demand.setText(wv_start_price_demand.getSelectedText()+"-"+wv_end_price_demand.getSelectedText());
                                cb_price_demand.setChecked(false);
                                il_price_demand.setVisibility( View.GONE);
                            }
                        }

                    break;
                }
                checkIsFinish();
            }
        };
    }

    private void initWheelViewData() {
        wv_start_fangxing_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.fangxing), CustomUtils.getWindowWidth(this)/2);
        wv_start_fangxing_demand.setEnabled(true);
        wv_start_fangxing_demand.setSelectItem(0);

        wv_end_fangxing_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.maxfangxing), CustomUtils.getWindowWidth(this)/2);
        wv_end_fangxing_demand.setEnabled(true);
        wv_end_fangxing_demand.setSelectItem(0);

        wv_place_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.area), CustomUtils.getWindowWidth(this));
        wv_place_demand.setEnabled(true);
        wv_place_demand.setSelectItem(0);

        wv_start_area_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.squareStart), CustomUtils.getWindowWidth(this)/2);
        wv_start_area_demand.setEnabled(true);
        wv_start_area_demand.setSelectItem(0);

        wv_end_area_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.squareEnd), CustomUtils.getWindowWidth(this)/2);
        wv_end_area_demand.setEnabled(true);
        wv_end_area_demand.setSelectItem(0);

        wv_start_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChushouStart), CustomUtils.getWindowWidth(this)/2);
        wv_start_price_demand.setEnabled(true);
        wv_start_price_demand.setSelectItem(0);

        wv_end_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChushouEnd), CustomUtils.getWindowWidth(this)/2);
        wv_end_price_demand.setEnabled(true);
        wv_end_price_demand.setSelectItem(0);

    }

    private CompoundButton.OnCheckedChangeListener getCheckBoxChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        };
    }
    private void checkOpenOrClose(boolean flag,int intId) {
        if(flag){
            if (intId != ll_type_demand.getId()) {
                closeTypeContainer();
            }
            if (intId != il_fangxing_demand.getId()) {
                closeFangxingContainer();
            }
            if (intId != il_place_demand.getId()) {
                closePlaceContainer();
            }
            if (intId != il_pianqu_demand.getId()) {
                closePianquContainer();
            }
            if (intId != il_area_demand.getId()) {
                closeAreaContainer();
            }
            if (intId != il_price_demand.getId()) {
                closePriceContainer();
            }
        }

    }

    private void closeTypeContainer() {
        ll_type_demand.setVisibility(View.GONE);
        cb_type_demand.setChecked(false);
    }
    private void closeFangxingContainer() {
        il_fangxing_demand.setVisibility(View.GONE);
        cb_fangxing_demand.setChecked(false);
    }
    private void closePlaceContainer() {
        il_place_demand.setVisibility(View.GONE);
        cb_place_demand.setChecked(false);
    }
    private void closePianquContainer() {
        il_pianqu_demand.setVisibility(View.GONE);
        cb_pianqu_demand.setChecked(false);
    }
    private void closeAreaContainer() {
        il_area_demand.setVisibility(View.GONE);
        cb_area_demand.setChecked(false);
    }
    private void closePriceContainer() {
        il_price_demand.setVisibility(View.GONE);
        cb_price_demand.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        boolean cb_flag;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_left_mhead1:
                finish();
                break;
            case R.id.img_right_mhead1:

                break;
            case R.id.rl_type_demand:
                cb_flag=!cb_type_demand.isChecked();
                cb_type_demand.setChecked(cb_flag);
                ll_type_demand.setVisibility(cb_flag ? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,ll_type_demand.getId());
                break;
            case R.id.rl_fangxing_demand:
                cb_flag=!cb_fangxing_demand.isChecked();
                cb_fangxing_demand.setChecked(cb_flag);
                il_fangxing_demand.setVisibility(cb_flag? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_fangxing_demand.getId());
                break;
            case R.id.rl_place_demand:
                cb_flag=!cb_place_demand.isChecked();
                cb_place_demand.setChecked(cb_flag);
                il_place_demand.setVisibility(cb_flag? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_place_demand.getId());
                break;
            case R.id.rl_pianqu_demand:
                cb_flag=!cb_pianqu_demand.isChecked();
                cb_pianqu_demand.setChecked(cb_flag);
                il_pianqu_demand.setVisibility(cb_flag? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_pianqu_demand.getId());
                break;
            case R.id.rl_area_demand:
                cb_flag=!cb_area_demand.isChecked();
                cb_area_demand.setChecked(cb_flag);
                il_area_demand.setVisibility(cb_flag? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_area_demand.getId());
                break;
            case R.id.rl_price_demand:
                cb_flag=!cb_price_demand.isChecked();
                cb_price_demand.setChecked(cb_flag);
                il_price_demand.setVisibility(cb_flag ? View.VISIBLE : View.GONE);
                checkOpenOrClose(cb_flag,il_price_demand.getId());
                break;
            case R.id.rl_zu_demand:
                iv_zu_demand.setImageResource(R.drawable.c_manage_button_choose);
                iv_gou_demand.setImageResource(R.drawable.c_manage_button_unselected);
                cb_type_demand.setChecked(false);
                ll_type_demand.setVisibility(View.GONE);
                // 求租
                reqType = "rent";
                tv_type_demand.setText("求租");
                wv_start_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChuzuStart), CustomUtils.getWindowWidth(this)/2);
                wv_start_price_demand.setEnabled(true);
                wv_start_price_demand.setSelectItem(0);

                wv_end_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChuzuEnd), CustomUtils.getWindowWidth(this)/2);
                wv_end_price_demand.setEnabled(true);
                wv_end_price_demand.setSelectItem(0);
                break;
            case R.id.rl_gou_demand:
                iv_gou_demand.setImageResource(R.drawable.c_manage_button_choose);
                iv_zu_demand.setImageResource(R.drawable.c_manage_button_unselected);
                cb_type_demand.setChecked(false);
                ll_type_demand.setVisibility(View.GONE);
                // 求购
                reqType = "buy";
                tv_type_demand.setText("求购");
                wv_start_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChushouStart), CustomUtils.getWindowWidth(this)/2);
                wv_start_price_demand.setEnabled(true);
                wv_start_price_demand.setSelectItem(0);

                wv_end_price_demand.setData(CST_Wheel_Data.getListDatas(CST_Wheel_Data.WheelType.priceChushouEnd), CustomUtils.getWindowWidth(this)/2);
                wv_end_price_demand.setEnabled(true);
                wv_end_price_demand.setSelectItem(0);
                break;
            default:
                break;
        }
    }
    @Override
    public void initData() {
        intent=getIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ConstantResult.REFRESH:
                break;
        }
    }
    private void checkIsFinish() {
        boolean isFinish = true;
        if (tv_type_demand.getText() == null || tv_type_demand.getText().toString().length() == 0) {
            isFinish = false;
        } else if(tv_fangxing_demand==null|| tv_fangxing_demand.getText().toString().length() == 0){
            isFinish = false;
        }else if(tv_changePlace_demand==null|| tv_changePlace_demand.getText().toString().length() == 0){
            isFinish = false;
        }else if(tv_changePianqu_demand==null|| tv_changePianqu_demand.getText().toString().length() == 0){
            isFinish = false;
        }else if (tv_changeArea_demand.getText() == null || tv_changeArea_demand.getText().toString().length() == 0) {
            isFinish = false;
        } else if (tv_changePrice_demand.getText() == null || tv_changePrice_demand.getText().toString().length() == 0) {
            isFinish = false;
        }
        if (isFinish) {
            mSubmitView = (ImageView)MethodsExtra.findHeadRightView1(mContext, baseView, 0,
                    R.drawable.universal_button_done);
            mSubmitView.setClickable(true);
            mSubmitView.setEnabled(true);
        } else {
            mSubmitView = (ImageView)MethodsExtra.findHeadRightView1(mContext, baseView, 0,
                    R.drawable.universal_button_undone);
            mSubmitView.setClickable(false);
            mSubmitView.setEnabled(false);
        }
    }
    @Override
    public void netWorkResult(String name, String className, Object data) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}