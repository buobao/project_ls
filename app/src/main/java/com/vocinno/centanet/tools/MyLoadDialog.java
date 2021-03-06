package com.vocinno.centanet.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vocinno.centanet.R;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MyLoadDialog extends Dialog {
    private static Context context;
    private static MyLoadDialog mInstance;
    private TextView tvMsg;
    private static Dialog dialog;

    public static MyLoadDialog getInstance(Context mContext)
    {
        if (mInstance == null)
        {
            synchronized (MyLoadDialog.class)
            {
                if (mInstance == null)
                {
                    context=mContext;
                    getLoadDialog();
                    mInstance=new MyLoadDialog(context);
                }
            }
        }
        return mInstance;
    }

    private static void getLoadDialog() {
        dialog = new Dialog(context, R.style.Theme_dialog);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth() * 3 / 4;
        // params.height = default_height;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public MyLoadDialog(Context context) {
        super(context, R.style.Theme_dialog);
        setContentView(R.layout.loading);
        Window window = getWindow();
        this.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = window.getAttributes();
        // float density = getDensity(context);
        // params.width = (int) (default_width * density);
        // params.height = (int) (default_height * density);
        params.width = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth() * 3 / 4;
        ; // 默认宽度
        // params.height = default_height;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public void getLoading(){
        Dialog dialog=new Dialog(context);
        dialog.addContentView(LinearLayout.inflate(context, R.layout.loading, null), null);
    }
   /* private MyLoadDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        this.setContentView(R.layout.item_custom_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        tvMsg=(TextView)findViewById(R.id.tv_dialg_title);
    }*/
    public static void showDialog(){
        if(mInstance!=null&&!(mInstance.isShowing())){
            mInstance.show();
        }
    }
    public static void dismissDialog(){
        if(mInstance!=null&&mInstance.isShowing()){
            mInstance.dismiss();
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }
    }

}
