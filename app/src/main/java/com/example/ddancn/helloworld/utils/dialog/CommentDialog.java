package com.example.ddancn.helloworld.utils.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.utils.ToastUtil;

public class CommentDialog extends BaseDialog{

    private EditText editText;
    private ImageButton btnChooseEmo;
    private ImageButton btnChoosePic;
    private ImageButton btnChooseFile;
    private Button btnSend;
    private String txtSend;
    private OnSendClickListener sendListener;

    public CommentDialog(Context context) {
        super(context);
    }

    public CommentDialog(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    public CommentDialog(Context context, int layoutRes, int themeResId) {
        super(context, layoutRes, themeResId);
    }

    public CommentDialog setOnSendClickListener(String txtSend, OnSendClickListener onSendClickListener){
        if(txtSend != null) this.txtSend = txtSend;
        this.sendListener = onSendClickListener;
        return this;
    }

    @Override
    protected void initView() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_comment, null);
        this.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);

        if(getWindow()!=null) {
            this.getWindow().setGravity(Gravity.BOTTOM);
            //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        InputMethodManager inputMethodManager=(InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText,0);

        editText = findViewById(R.id.edit_text);
        btnChooseEmo = findViewById(R.id.btn_choose_emo);
        btnChoosePic = findViewById(R.id.btn_choose_pic);
        btnChooseFile = findViewById(R.id.btn_choose_file);
        btnSend = findViewById(R.id.btn_send);
        btnChooseEmo.setOnClickListener(v->{
            ToastUtil.show("emo");
        });
        btnChoosePic.setOnClickListener(v->{
            ToastUtil.show("pic");
        });
        btnChooseFile.setOnClickListener(v->{
            ToastUtil.show("file");
        });
        btnSend.setOnClickListener(v->{
            if (sendListener != null && sendListener.onClick()) {
                dismiss();
            }
        });
    }

    public interface OnSendClickListener{
        boolean onClick();
    }
}
