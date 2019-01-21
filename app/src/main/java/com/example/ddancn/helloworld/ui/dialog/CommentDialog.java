package com.example.ddancn.helloworld.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.index.MainActivity;
import com.example.ddancn.helloworld.utils.ToastUtil;
import com.example.ddancn.helloworld.ui.dialog.comment.selector.emo.EmojiVpAdapter;
import com.example.ddancn.helloworld.ui.dialog.comment.selector.file.FileInfo;
import com.example.ddancn.helloworld.ui.dialog.comment.selector.file.FileSelectorActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;


public class CommentDialog extends BaseDialog implements MainActivity.OnChosen {

    public static final int CHOOSE_PIC = 1;
    public static final int CHOOSE_FILE = 2;
    public static final String SOFT_INPUT_HEIGHT = "softInputHeight";
    public static final int SOFT_INPUT_DEFAULT_HEIGHT = 787;

    private EditText editText;
    private ImageButton btnChooseEmo;
    private ImageButton btnChoosePic;
    private ImageButton btnChooseFile;
    private Button btnSend;

    private LinearLayout emoBoard;
    private ViewPager emoViewPager;
    private LinearLayout emoPointer;

    private FrameLayout imageBoard;
    private ImageView ivImage;

    private RelativeLayout fileBoard;
    private ImageView ivFile;
    private TextView tvFile;
    private ImageButton btnDelete;

    private String txtSend;
    private OnSendClickListener sendListener;
    private boolean hasText = false;
    private boolean hasPic = false;
    private boolean hasFile = false;

    private SharedPreferences pref;

    public CommentDialog(Context context) {
        super(context, R.layout.dialog_comment);
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public CommentDialog(Context context, int themeResId) {
        super(context, R.layout.dialog_comment, themeResId);
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public CommentDialog setOnSendClickListener(String txtSend, OnSendClickListener onSendClickListener) {
        if (txtSend != null) this.txtSend = txtSend;
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
        final RxPermissions rxPermissions = new RxPermissions((FragmentActivity) mContext);

        //设置对话框在底部、设置软键盘模式
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
        ivImage = findViewById(R.id.iv_pic);
        fileBoard = findViewById(R.id.file_board);
        ivFile = findViewById(R.id.iv_file);
        tvFile = findViewById(R.id.tv_file);
        btnDelete = findViewById(R.id.btn_delete);

        editText.setOnClickListener(v -> {
            emoBoard.setVisibility(View.GONE);
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(editText.getText())){
                    hasText = false;
                } else {
                    hasText = true;
                }
                changeBtnState();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnSend.setText(txtSend);
        btnChooseEmo.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            //切换表情面板和软键盘
            setEmoBoardHeight();
            if (emoBoard.isShown()) {
                emoBoard.setVisibility(View.GONE);
                imm.showSoftInput(editText, 0);
            } else {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                emoBoard.setVisibility(View.VISIBLE);
            }
        });
        btnChoosePic.setOnClickListener(v -> {
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            openAlbum();
                        } else {
                            ToastUtil.show("请在设置中打开存储权限");
                        }
                    });
        });
        btnChooseFile.setOnClickListener(v -> {
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            openFileChooser();
                        } else {
                            ToastUtil.show("请在设置中打开存储权限");
                        }
                    });
        });
        btnSend.setOnClickListener(v -> {
            if (sendListener != null && (hasText||hasPic||hasFile)) {
                sendListener.onClick(editText.getText().toString());
                dismiss();
            }
        });

        //设置表情监听、表情指示器等
        EmojiVpAdapter adapter = new EmojiVpAdapter(mContext);
        adapter.setOnEmojiClickListener((emoji) -> {
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
        imageBoard.setOnClickListener(v -> {
            imageBoard.setVisibility(View.GONE);
            hasPic = false;
            changeBtnState();
        });

        //文件监听
        btnDelete.setOnClickListener(v -> {
            fileBoard.setVisibility(View.GONE);
            hasFile = false;
            changeBtnState();
        });
    }

    public interface OnSendClickListener {
        boolean onClick(String content);
    }

    /**
     * 设置表情面板的高度=SP/计算
     */
    private void setEmoBoardHeight() {
        ViewGroup.LayoutParams params = emoBoard.getLayoutParams();
        //已计算过正确的值
        if(params.height != -2 && params.height != SOFT_INPUT_DEFAULT_HEIGHT)
            return ;
        int height = getSoftInputHeightFromPref();
        if(height == SOFT_INPUT_DEFAULT_HEIGHT)
            height = calculateSoftInputHeight();
        params.height = height;

    }

    /**
     * 从SP文件中获取软键盘的高度
     * @return 软键盘高度
     */
    private int getSoftInputHeightFromPref() {
        return pref.getInt(SOFT_INPUT_HEIGHT,  SOFT_INPUT_DEFAULT_HEIGHT);
    }

    /**
     * 计算软键盘高度
     * @return 软键盘高度
     */
    private int calculateSoftInputHeight() {
        //通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
        Rect rect = new Rect();
        ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //获取屏幕的高度
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //计算软键盘的高度
        int softInputHeight = metrics.heightPixels - rect.bottom;
        if (softInputHeight <= 0)
            softInputHeight = SOFT_INPUT_DEFAULT_HEIGHT;
        //存到SP
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(SOFT_INPUT_HEIGHT, softInputHeight);
        editor.apply();
        return softInputHeight;
    }

    /**
     * 打开系统相册
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        ((Activity) mContext).startActivityForResult(intent, CHOOSE_PIC);
    }

    /**
     * 打开文件选择
     */
    private void openFileChooser() {
        Intent intent = new Intent(mContext,FileSelectorActivity.class);
        ((Activity) mContext).startActivityForResult(intent, CHOOSE_FILE);
    }

    /**
     * 改变发送按钮的状态
     */
    private void changeBtnState(){
        if(hasText || hasPic || hasFile)
            btnSend.setEnabled(true);
        else
            btnSend.setEnabled(false);
    }

    @Override
    public void onPictureChosen(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageBoard.setVisibility(View.VISIBLE);
            ivImage.setImageBitmap(bitmap);
            hasPic = true;
            changeBtnState();
        } else {
            ToastUtil.show("failed to get image");
        }
    }

    @Override
    public void onFileChosen(FileInfo file) {
        if (file != null) {
            fileBoard.setVisibility(View.VISIBLE);
            ivFile.setImageResource(file.getImageId());
            tvFile.setText(file.getName());
            hasFile = true;
            changeBtnState();
        } else {
            ToastUtil.show("failed to get file");
        }
    }


}
