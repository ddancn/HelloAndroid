package com.example.ddancn.helloworld.utils.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.index.MainActivity;
import com.example.ddancn.helloworld.utils.ToastUtil;
import com.example.ddancn.helloworld.utils.selector.emo.EmojiVpAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;


public class CommentDialog extends BaseDialog implements MainActivity.OnPictureChosen {

    public static final int CHOOSE_PIC_FROM_ALBUM = 1;

    private EditText editText;
    private ImageButton btnChooseEmo;
    private ImageButton btnChoosePic;
    private ImageButton btnChooseFile;
    private Button btnSend;
    private LinearLayout emoBoard;
    private ViewPager emoViewPager;
    private LinearLayout emoPointer;
    private String txtSend;
    private FrameLayout imageBoard;
    private ImageView imageView;

    private OnSendClickListener sendListener;

    private SharedPreferences pref;

    public CommentDialog(Context context) {
        super(context, R.layout.dialog_comment);
        pref=PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public CommentDialog(Context context, int themeResId) {
        super(context, R.layout.dialog_comment, themeResId);
        pref=PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public CommentDialog setOnSendClickListener(String txtSend, OnSendClickListener onSendClickListener) {
        if (txtSend != null) this.txtSend = txtSend;
        this.sendListener = onSendClickListener;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void initView() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_comment, null);
        this.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);

        if (this.getWindow() != null) {
            this.getWindow().setGravity(Gravity.BOTTOM);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        editText = findViewById(R.id.edit_text);
        btnChooseEmo = findViewById(R.id.btn_choose_emo);
        btnChoosePic = findViewById(R.id.btn_choose_pic);
        btnChooseFile = findViewById(R.id.btn_choose_file);
        btnSend = findViewById(R.id.btn_send);
        emoBoard = findViewById(R.id.emo_board);
        emoViewPager = findViewById(R.id.emo_viewpager);
        emoPointer = findViewById(R.id.emo_pointer);
        imageBoard = findViewById(R.id.image_board);
        imageView = findViewById(R.id.iv_pic);

        editText.setOnClickListener(v -> {
            emoBoard.setVisibility(View.GONE);
        });
        btnSend.setText(txtSend);
        btnChooseEmo.setOnClickListener(v -> {
            ToastUtil.show("emo");
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (emoBoard.isShown()) {
                emoBoard.setVisibility(View.GONE);
                if(imm != null) imm.showSoftInput(editText, 0);
            }
            else {
                if(imm != null) imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
                emoBoard.setVisibility(View.VISIBLE);
            }
        });
        btnChoosePic.setOnClickListener(v -> {
            final RxPermissions rxPermissions = new RxPermissions((FragmentActivity)mContext);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            openAlbum();
                        } else {
                            ToastUtil.show("request permission denied");
                        }
                    });
        });
        btnChooseFile.setOnClickListener(v -> {
            ToastUtil.show("file");
        });
        btnSend.setOnClickListener(v -> {
            if (sendListener != null && sendListener.onClick(editText.getText().toString())) {
                dismiss();
            }
        });

        //设置表情面板的高度
        ViewGroup.LayoutParams params = emoBoard.getLayoutParams();
        params.height = getSoftInputHeightFromPref();
        if(params.height == 0)
            params.height = calculateSoftInputHeight();

        //设置表情监听、表情指示器等
        EmojiVpAdapter adapter = new EmojiVpAdapter(mContext);
        adapter.setOnEmojiClickListener((emoji)->{
            if ("del".equals(emoji)) {
                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
                editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
            } else {
                editText.append(emoji);
            }
        });
        adapter.setupWithPagerPoint(emoViewPager, emoPointer);
        emoViewPager.setAdapter(adapter);

        //图片监听
        imageBoard.setOnClickListener(v->{
            imageBoard.setVisibility(View.GONE);
        });
    }

    public interface OnSendClickListener {
        boolean onClick(String content);
    }

    private  int getSoftInputHeightFromPref(){
         return pref.getInt("softInputHeight",0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int calculateSoftInputHeight() {
        //通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
        Rect rect = new Rect();
        ((Activity)mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //获取屏幕的高度
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //计算软键盘的高度
        int softInputHeight = metrics.heightPixels - rect.bottom;
        if(softInputHeight <= 0)
            softInputHeight = 787;
        SharedPreferences.Editor editor=pref.edit();
        editor.putInt("softInputHeight", softInputHeight);
        editor.apply();
        return softInputHeight;
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        ((Activity)mContext).startActivityForResult(intent, CHOOSE_PIC_FROM_ALBUM); // 打开相册
    }

    @Override
    public void onChosen(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageBoard.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        } else {
            ToastUtil.show("failed to get image");
        }
    }

}
