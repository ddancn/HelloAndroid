package com.example.ddancn.helloworld.utils.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ddancn.helloworld.R;

public class CustomDialog extends BaseDialog {
    private Button btnConfirm;
    private Button btnCancel;
    private TextView tvTitle;
    private TextView tvMsg;
    private View vLine;
    private String txtConfirm;
    private String txtCancel;
    private String title;
    private String msg;
    private OnConfirmClickListener confirmListener;
    private OnCancelClickListener cancelListener;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public CustomDialog(Context context, int layoutRes, int themeResId) {
        super(context, layoutRes, themeResId);
    }

    @Override
    protected void initView() {
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        tvTitle = findViewById(R.id.title);
        tvMsg = findViewById(R.id.msg);
        vLine = findViewById(R.id.line_vertical);
        btnConfirm.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        tvMsg.setVisibility(View.GONE);
        vLine.setVisibility(View.GONE);

        if(title != null) { tvTitle.setText(title); tvTitle.setVisibility(View.VISIBLE);}
        if(msg != null) { tvMsg.setText(msg); tvMsg.setVisibility(View.VISIBLE);}
        if(txtConfirm != null) { btnConfirm.setText(txtConfirm); btnConfirm.setVisibility(View.VISIBLE);}
        if(txtCancel != null) { btnCancel.setText(txtCancel); btnCancel.setVisibility(View.VISIBLE);}
        if(txtConfirm != null && txtCancel !=null) vLine.setVisibility(View.VISIBLE);

        btnConfirm.setOnClickListener(v->{
            if (confirmListener != null && confirmListener.onClick()) {
                dismiss();
            }
        });
        btnCancel.setOnClickListener(v -> {
            if (cancelListener != null && cancelListener.onClick()) {
                dismiss();
            }
        });
    }

    public CustomDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CustomDialog setMsg(String msg){
        this.msg = msg;
        return this;
    }

    public CustomDialog setOnConfirmClickListener(String txtConfirm, OnConfirmClickListener onConfirmClickListener){
        if(txtConfirm != null) this.txtConfirm = txtConfirm;
        this.confirmListener = onConfirmClickListener;
        return this;
    }

    public CustomDialog setOnCancelClickListener(String txtCancel, OnCancelClickListener onCancelClickListener){
        if(txtCancel != null) this.txtCancel = txtCancel;
        this.cancelListener = onCancelClickListener;
        return this;
    }

    public interface OnConfirmClickListener {
        boolean onClick();
    }

    public interface OnCancelClickListener {
        boolean onClick();
    }

}
