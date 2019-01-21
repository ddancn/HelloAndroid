package com.example.ddancn.helloworld.ui.dialog.comment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.ui.dialog.comment.selector.at.AtSelectorActivity;
import com.example.ddancn.helloworld.ui.dialog.comment.selector.at.UserInfo;
import com.example.ddancn.helloworld.utils.DimenUtil;
import com.example.ddancn.helloworld.utils.ImageUtil;
import com.example.ddancn.helloworld.utils.ToastUtil;
import com.example.ddancn.helloworld.ui.dialog.comment.selector.emo.EmojiVpAdapter;
import com.example.ddancn.helloworld.ui.dialog.comment.selector.file.FileInfo;
import com.example.ddancn.helloworld.ui.dialog.comment.selector.file.FileSelectorActivity;
import com.orhanobut.logger.Logger;
import com.sunhapper.spedittool.view.SpEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

public class CommentDialogActivity extends AppCompatActivity {

    public static final int CHOOSE_PIC = 1;
    public static final int CHOOSE_FILE = 2;
    public static final int CHOOSE_AT = 3;

    private View contentView;
    private SpEditText editText;
    private ImageButton btnChooseEmo;
    private ImageButton btnChoosePic;
    private ImageButton btnChooseFile;
    private ImageButton btnChooseAt;
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

    private boolean hasText = false;
    private boolean hasPic = false;
    private boolean hasFile = false;
    private boolean rollback = false;

    private static final Handler HANDLER = new Handler();
    private InputMethodManager imm;
    private final RxPermissions rxPermissions = new RxPermissions(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        contentView = findViewById(R.id.content);
        editText = findViewById(R.id.edit_text);
        btnChooseEmo = findViewById(R.id.btn_choose_emo);
        btnChoosePic = findViewById(R.id.btn_choose_pic);
        btnChooseFile = findViewById(R.id.btn_choose_file);
        btnChooseAt = findViewById(R.id.btn_choose_at);
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

        //点击空白处结束
        contentView.setOnClickListener(v -> finish());
        //设置输入框的监听
        setEditTextListener();

        btnChooseEmo.setOnClickListener(v -> switchBoard());
        btnChoosePic.setOnClickListener(v -> checkAndOpenAlbum());
        btnChooseFile.setOnClickListener(v -> checkAndOpenFileChooser());
        btnChooseAt.setOnClickListener(v-> {
            rollback = false;
            openAtChooser();
        });
        btnSend.setOnClickListener(v -> {
            //返回结果
            ToastUtil.show(editText.getText().toString());
        });

        //设置表情监听、表情指示器等
        setEmoBoardAdapter();

        //图片删除监听
        imageBoard.setOnClickListener(v -> {
            imageBoard.setVisibility(View.GONE);
            hasPic = false;
            changeBtnState();
        });

        //文件删除监听
        btnDelete.setOnClickListener(v -> {
            fileBoard.setVisibility(View.GONE);
            hasFile = false;
            changeBtnState();
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    /**
     * 设置输入框的监听
     */
    private void setEditTextListener(){
        editText.setOnClickListener(v -> emoBoard.setVisibility(View.GONE));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasText = !TextUtils.isEmpty(editText.getText());
                changeBtnState();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText.setKeyReactListener(key -> {
            switch (key) {
                case "@":
                    rollback = true;
                    openAtChooser();
                    break;
                case "#":
                    editText.insertSpecialStr(" #tagtag# ", true, 1,
                            new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)));
                    break;
                default:
            }
        });
    }

    /**
     * 切换表情面板和软键盘
     */
    private void switchBoard() {
        //隐藏模式防止切回页面时表情面板和软键盘冲突
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setEmoBoardHeight();
        if (emoBoard.isShown()) {
            lockContentHeight();
            emoBoard.setVisibility(View.GONE);
            if (imm != null) {
                imm.showSoftInput(editText, 0);
            }
            unlockContentHeightDelayed();
        } else {
            if (DimenUtil.calculateSoftInputHeight(this) != 0) {
                lockContentHeight();
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
                emoBoard.setVisibility(View.VISIBLE);
                unlockContentHeightDelayed();
            } else {
                emoBoard.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 打开相册
     */
    @SuppressLint("CheckResult")
    private void checkAndOpenAlbum() {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, CHOOSE_PIC);
                    } else {
                        ToastUtil.show("请在设置中打开存储权限");
                    }
                });
    }

    /**
     * 打开文件选择器
     */
    @SuppressLint("CheckResult")
    private void checkAndOpenFileChooser() {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(this, FileSelectorActivity.class);
                        startActivityForResult(intent, CHOOSE_FILE);
                    } else {
                        ToastUtil.show("请在设置中打开存储权限");
                    }
                });
    }

    /**
     * 打开@选择
     */
    private void openAtChooser(){
        Intent intent = new Intent(this, AtSelectorActivity.class);
        startActivityForResult(intent, CHOOSE_AT);
    }

    /**
     * 设置表情监听、表情指示器等
     */
    private void setEmoBoardAdapter() {
        EmojiVpAdapter adapter = new EmojiVpAdapter(this);
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
    }

    /**
     * 锁定内容高度
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        params.height = contentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 解锁内容高度
     */
    private void unlockContentHeightDelayed() {
        HANDLER.postDelayed(() -> ((LinearLayout.LayoutParams) contentView.getLayoutParams()).weight = 1.0F, 200L);
    }

    /**
     * 设置表情面板的高度=SP/计算
     */
    private void setEmoBoardHeight() {
        ViewGroup.LayoutParams params = emoBoard.getLayoutParams();
        //已计算过正确的值
        if (params.height != -2 && params.height != DimenUtil.SOFT_INPUT_DEFAULT_HEIGHT)
            return;
        int height = DimenUtil.getSoftInputHeightFromPref();
        if (height == DimenUtil.SOFT_INPUT_DEFAULT_HEIGHT)
            height = DimenUtil.calculateSoftInputHeight(this);
        params.height = height;
    }

    /**
     * 改变发送按钮的状态
     */
    private void changeBtnState() {
        if (hasText || hasPic || hasFile)
            btnSend.setEnabled(true);
        else
            btnSend.setEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PIC:
                    onPictureChosen(ImageUtil.getPath(this, data.getData()));
                    break;
                case CHOOSE_FILE:
                    onFileChosen(data.getParcelableExtra(FileSelectorActivity.FILE_CHOSEN));
                    break;
                case CHOOSE_AT:
                    onAtUserChosen(data.getParcelableArrayListExtra(AtSelectorActivity.USERS_CHOSEN));
                default:
            }
        }
    }


    /**
     * 处理图片的选择
     *
     * @param imagePath 图片路径
     */
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

    /**
     * 处理文件的选择
     *
     * @param file 自定义文件对象
     */
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

    /**
     * 处理@用户的选择
     * @param users 被选中的用户列表
     */
    private void onAtUserChosen(ArrayList<UserInfo> users) {
        for(UserInfo user : users){
            editText.insertSpecialStr("@"+user.getUsername()+" ", rollback, user,
                    new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)));
        }
    }
}
