package com.vocinno.centanet.baseactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.vocinno.centanet.R;
import com.vocinno.centanet.apputils.MyUtils;
import com.vocinno.centanet.apputils.dialog.ModelDialog;
import com.vocinno.centanet.apputils.dialog.MyDialog;
import com.vocinno.centanet.customermanage.CustomerManageActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity;
import com.vocinno.centanet.housemanage.HouseManageActivity2;
import com.vocinno.centanet.housemanage.HouseType;
import com.vocinno.centanet.keymanage.KeyGetInActivity;
import com.vocinno.centanet.keymanage.KeyManageActivity;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.remind.MessageListActivity;
import com.vocinno.utils.MethodsDeliverData;
import com.vocinno.utils.MethodsExtra;
import com.vocinno.utils.MethodsJni;
import com.vocinno.utils.view.refreshablelistview.XListView;
import com.zbar.lib.CaptureActivity;

/**
 * Created by Administrator on 2016/4/20.
 */
public abstract class OtherBaseActivity extends Activity implements HttpInterface,XListView.IXListViewListener,View.OnClickListener{
    public Intent intent;
    public DrawerLayout drawer_layout;
    public View leftMenuView;
    public XListView mListView;
    public View mBackView;
    public MethodsJni methodsJni;
    public Activity mContext = null;
    public Handler mHander = null;
    public ModelDialog modelDialog;//loading
    public MyDialog.Builder myDialog;//自定义窗口
    /*******************抽象方法***************************/
    public abstract int setContentLayoutId();
    public abstract void initView();
    public abstract void initData();
    public  String TAG = null;
    public abstract Handler setHandler();
    public View baseView=null;
    public LinearLayout ll_left_menu;
    private RelativeLayout fuJinChuShou,fuJinChuZu, yueKanFangYuan,
            woDeChuShou,woDeChuZu,yaoShiGuanLi, woDeKeYuan, qiangGongShou,
            qiangGongZu, qiangGongKe, shuPINMa, saoYiSao,woDeTiXing;
    /********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        MyUtils.addActivityToAllList(this);
        TAG=this.getClass().getName();
        int layoutId=setContentLayoutId();
        View view=getLayoutInflater().inflate(layoutId, null);
        baseView=getLayoutInflater().inflate(R.layout.base_left_menu_layout,null);
        ll_left_menu=(LinearLayout)baseView.findViewById(R.id.ll_left_menu);
        ll_left_menu.addView(view);
        setContentView(baseView);
        initView();
        mHander = setHandler();
        setClickListener();
        initData();
    }
    private void setClickListener() {
        drawer_layout=(DrawerLayout)findViewById(R.id.drawer_layout);
        leftMenuView=findViewById(R.id.left_base_menu);

        fuJinChuShou=(RelativeLayout)findViewById(R.id.rlyt_sell_house_main_page_slid_menus);
        fuJinChuShou.setOnClickListener(this);

        fuJinChuZu = (RelativeLayout) findViewById(R.id.rlyt_rent_house_main_page_slid_menus);
        fuJinChuZu.setOnClickListener(this);

        yueKanFangYuan = (RelativeLayout) findViewById(R.id.rlyt_see_house_main_page_slid_menus);
        yueKanFangYuan.setOnClickListener(this);

        woDeChuShou = (RelativeLayout) findViewById(R.id.rlyt_my_house_main_page_slid_menus);
        woDeChuShou.setOnClickListener(this);

        woDeChuZu = (RelativeLayout) findViewById(R.id.rlyt_my_house_main_page_slid_menus2);
        woDeChuZu.setOnClickListener(this);

        yaoShiGuanLi = (RelativeLayout) findViewById(R.id.rlyt_key_house_main_page_slid_menus);
        yaoShiGuanLi.setOnClickListener(this);


        woDeKeYuan = (RelativeLayout) findViewById(R.id.rlyt_my_customer_main_page_slid_menus);
        woDeKeYuan.setOnClickListener(this);

        qiangGongShou = (RelativeLayout) findViewById(R.id.rlyt_grab_house_main_page_slid_menus);
        qiangGongShou.setOnClickListener(this);

        qiangGongZu = (RelativeLayout) findViewById(R.id.rlyt_grab_house_main_page_slid_menus2);
        qiangGongZu.setOnClickListener(this);

        qiangGongKe = (RelativeLayout) findViewById(R.id.rlyt_grab_customer_main_page_slid_menus);
        qiangGongKe.setOnClickListener(this);

        shuPINMa = (RelativeLayout) findViewById(R.id.rlyt_password_main_page_slid_menus);
        shuPINMa.setOnClickListener(this);

        saoYiSao = (RelativeLayout) findViewById(R.id.rlyt_sacn_customer_main_page_slid_menus);
        saoYiSao.setOnClickListener(this);

        woDeTiXing = (RelativeLayout)findViewById(R.id.rlyt_remind_customer_main_page_slid_menus);
        woDeTiXing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //附近出售
            case R.id.rlyt_sell_house_main_page_slid_menus:
                startIntentToHouseManager(0);
                break;
            //附近出租
            case R.id.rlyt_rent_house_main_page_slid_menus:
                startIntentToHouseManager(1);
                break;
            //约看房源
            case R.id.rlyt_see_house_main_page_slid_menus:
                startIntentToHouseManager(2);
                break;
            //我的出售
            case R.id.rlyt_my_house_main_page_slid_menus:
                startIntentToHouseManager(3);
                break;
            //我的出租
            case R.id.rlyt_my_house_main_page_slid_menus2:
                startIntentToHouseManager(4);
                break;
            //钥匙管理
            case R.id.rlyt_key_house_main_page_slid_menus:
                MyUtils.removeActivityFromAllList();
                MethodsExtra.startActivity(mContext, KeyManageActivity.class);
                break;
            //我的客源
            case R.id.rlyt_my_customer_main_page_slid_menus:
                MyUtils.removeActivityFromAllList();
                MethodsDeliverData.keYuanOrGongKe=1;
                MethodsDeliverData.isMyCustomer = true;
                MethodsExtra.startActivity(mContext,
                        CustomerManageActivity.class);
                break;
            //抢公售
            case R.id.rlyt_grab_house_main_page_slid_menus:
                MyUtils.removeActivityFromAllList();
                MethodsDeliverData.flag = 1;
                MethodsDeliverData.mIntHouseType = HouseType.GONG_FANG;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //抢公租
            case R.id.rlyt_grab_house_main_page_slid_menus2:
                MyUtils.removeActivityFromAllList();
                MethodsDeliverData.flag = 1;
                MethodsDeliverData.mIntHouseType = HouseType.GONG_FANGZU;
                MethodsExtra.startActivity(mContext, HouseManageActivity.class);
                break;
            //抢公客
            case R.id.rlyt_grab_customer_main_page_slid_menus:
                MyUtils.removeActivityFromAllList();
                MethodsDeliverData.keYuanOrGongKe=0;
                MethodsDeliverData.flag = 1;
                MethodsDeliverData.isMyCustomer = false;
                MethodsExtra.startActivity(mContext,
                        CustomerManageActivity.class);
                break;
            //pin码
            case R.id.rlyt_password_main_page_slid_menus:
                MyUtils.removeActivityFromAllList();
                MethodsExtra.startActivity(mContext, KeyGetInActivity.class);
                break;
            //扫一扫
            case R.id.rlyt_sacn_customer_main_page_slid_menus:
                MyUtils.removeActivityFromAllList();
                MethodsExtra.startActivity(mContext, CaptureActivity.class);
                break;
            //我的提醒
            case R.id.rlyt_remind_customer_main_page_slid_menus:
                MyUtils.removeActivityFromAllList();
                MethodsDeliverData.flag = -1;
                MethodsExtra.startActivity(mContext, MessageListActivity.class);
                break;
        }
    }

    public void showDialog(){
        if(this.modelDialog==null){
            this.modelDialog=ModelDialog.getModelDialog(this);
        }
        this.modelDialog.show();
    }
    public void dismissDialog(){
        if(this.modelDialog!=null&&this.modelDialog.isShowing()){
            this.modelDialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(drawer_layout.isDrawerOpen(leftMenuView)){
            drawer_layout.closeDrawer(leftMenuView);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startIntentToHouseManager(int index){
        MyUtils.removeActivityFromAllList();
        if(intent==null){
            intent=new Intent();
        }
        intent.setClass(mContext, HouseManageActivity2.class);
        intent.putExtra("viewPageIndex", index);
        startActivity(intent);
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyUtils.allActList.remove(this);
    }
}