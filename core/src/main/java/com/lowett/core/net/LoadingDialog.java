//package com.lowett.core.net;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//
//import com.lowett.core.R;
//
//
///**
// * Created by Hyu on 2016/11/28.
// * Email: fvaryu@qq.com
// */
//
//public class LoadingDialog extends Dialog {
//
//
//    public LoadingDialog(Context context) {
//        super(context, R.style.Dialog_Loading);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.dialog_loading);
//        Window window = getWindow();
//        if (window != null) {
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//            params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
//            window.setAttributes(params);
//            window.setGravity(Gravity.CENTER);
//
//        }
//
//        setCanceledOnTouchOutside(false);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//}
