package com.vocinno.centanet.customermanage;

import android.os.Handler;

import com.vocinno.centanet.R;
import com.vocinno.centanet.baseactivity.OtherBaseActivity;

/**
 * Created by hewei26 on 2016/6/15.
 */
public class SelectCompanyActivity extends OtherBaseActivity{
    @Override
    public int setContentLayoutId() {
        return R.layout.dialog_choose_search;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public Handler setHandler() {
        return null;
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
