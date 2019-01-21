package com.example.ddancn.helloworld.ui.dialog;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ddancn.helloworld.R;

public class LoadingDialog extends BaseDialog {

    private ProgressBar progressBar;
    private TextView tvMsg;
    private String msg;

    public LoadingDialog(Context context) {
        super(context, R.layout.dialog_loading);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, R.layout.dialog_loading, themeResId);
    }


    public LoadingDialog setMsg(String msg){
        this.msg = msg;
        return this;
    }

    @Override
    protected void initView() {
        progressBar = findViewById(R.id.progress_circular);
        tvMsg = findViewById(R.id.msg);
        if(msg != null) tvMsg.setText(msg);
    }
}
