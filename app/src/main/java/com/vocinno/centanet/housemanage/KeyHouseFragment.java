package com.vocinno.centanet.housemanage;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.squareup.okhttp.Request;
import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.HouseListBaseFragment;
import com.vocinno.centanet.housemanage.adapter.KeyHouseListAdapter;
import com.vocinno.centanet.model.HouseItem;
import com.vocinno.centanet.model.JSReturn;
import com.vocinno.centanet.model.KeyHouseList;
import com.vocinno.centanet.myinterface.HttpInterface;
import com.vocinno.centanet.tools.OkHttpClientManager;
import com.vocinno.centanet.tools.constant.NetWorkConstant;
import com.vocinno.centanet.tools.constant.NetWorkMethod;
import com.vocinno.utils.MethodsJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class KeyHouseFragment extends HouseListBaseFragment implements HttpInterface {
    private List<HouseItem> listHouses;
    private boolean firstLoading=true;
    @Override
    public int setContentLayoutId() {
        return R.layout.activity_near_sell;
    }

    public KeyHouseFragment( int position) {
        this.viewPosition=position;
    }
    public KeyHouseFragment() {
    }
    @Override
    public void initView() {
        if(HouseListBaseFragment.NEAR_SELL==viewPosition){
            initData();
        }
    }
    public void setFirstLoading(){
        firstLoading=true;
    }
    @Override
    public void addNotification() {
        TAG=this.getClass().getName();
    }
    @Override
    public void initData() {
        if(firstLoading){
            keyHouseListAdapter = new KeyHouseListAdapter(mContext, HouseType.YAO_SHI,true);
            keyHouseListAdapter.setDataList(null);
            XHouseListView.setAdapter(keyHouseListAdapter);
            type = HouseType.YAO_SHI;
            getData(1, false,true);
        }
    }
    public void searchForList(int tagIndex,String param){
        switch (tagIndex){
            case 0:
                price=param;
                break;
            case 1:
                square=param;
                break;
            case 2:
                frame=param;
                break;
            case 3:
                tag=param;
                break;
            case 4:
                usageType=param;
                break;
        }
        resetSearchOtherTag(tagIndex);
        getData(1, false,true);
    }
    public void getData(int page,boolean isXListViewLoad,final boolean isRefresh){
        if(!isXListViewLoad){
//            showDialog();
            pl_progress.showProgress();
        }
//        getDataInterface.getListData("" + type, price, square, frame, tag, usageType, page, pageSize, sidx, sord, searchId, searchType);
        URL= NetWorkConstant.PORT_URL+ NetWorkMethod.houList;
        Map<String,String> map=new HashMap<String,String>();
        map.put(NetWorkMethod.type,type+"");
        map.put("listType", "KEY_HOULIST");//listType = "KEY_HOULIST";
        map.put(NetWorkMethod.price,price);
        map.put(NetWorkMethod.square,square);
        map.put(NetWorkMethod.frame,frame);
        map.put(NetWorkMethod.tag,tag);
        map.put(NetWorkMethod.usageType,usageType);
        map.put(NetWorkMethod.page,page+"");
        map.put(NetWorkMethod.pageSize,pageSize+"");
        map.put(NetWorkMethod.sidx,sidx);
        map.put(NetWorkMethod.sord,sord);
        map.put(NetWorkMethod.searchId,searchId);
        map.put(NetWorkMethod.searchType,searchType);
        map.put(NetWorkMethod.buildingName,searchBuildingName);
        map.put(NetWorkMethod.roomNo,searchRoomNo);
        OkHttpClientManager.getAsyn(URL, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                XHouseListView.stopRefresh();
                XHouseListView.stopLoadMore();
                //                Loading.dismissLoading();
                pl_progress.showErrorText();
            }

            @Override
            public void onResponse(String response) {
                XHouseListView.stopRefresh();
                XHouseListView.stopLoadMore();
                pl_progress.showContent();
                JSReturn jsReturn = MethodsJson.jsonToJsReturn(response, KeyHouseList.class);
                if (jsReturn.isSuccess()) {
                    int dataType=0;
                    if(!isRefresh){
                        dataType=1;
                    }
                    setListData(dataType, jsReturn.getListDatas());
                }
            }
        });
       }
    @Override
    public Handler setHandler() {
        return null;
    }

    @Override
    public void setListData(int dataType,List list) {
        dismissDialog();
        firstLoading=false;
        switch (dataType){
            case LIST_REFRESH:
                keyHouseDataRefresh(list);
                break;
            case LIST_LOADMORE:
                keyHouseDtaLoadMore(list);
                break;
        }
    }

    //重置搜索条件
    public void resetSearch(){
        page = 1;
        pageSize = 20;
//        delType = "s";
        type = HouseType.YAO_SHI;
        price = "0-不限";
        square = "0-不限";
        frame = "不限-不限-不限-不限";
        tag = "";
        usageType = "";
        sidx = "";
        sord = "";
        searchId = "";
        searchType = "";
        searchBuildingName="";
        searchRoomNo="";
    };
    @Override
    public void onRefresh() {
        resetSearch();
        getData(1,true,true);
    }

    @Override
    public void onLoadMore() {
        getData(page+1,true,false);
    }

    @Override
    public void netWorkResult(String name, String className, Object data) {

    }

    @Override
    public void againLoading() {
        resetSearch();
        getData(1, false,true);
    }
}
