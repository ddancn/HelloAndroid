package com.example.ddancn.helloworld.index;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.utils.selector.emo.EmojiVpAdapter;

public class CommentActivity extends AppCompatActivity {

    public static final String SOFT_INPUT_HEIGHT = "softInputHeight";
    public static final int SOFT_INPUT_DEFAULT_HEIGHT = 787;

    private View contentView;
    private EditText editText;
    private ImageButton btnChooseEmo;
    private Button btnSend;

    private LinearLayout emoBoard;
    private ViewPager emoViewPager;
    private LinearLayout emoPointer;

    private SharedPreferences pref;

    private static final Handler HANDLER = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acctivity_comment);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        contentView = findViewById(R.id.content);
        editText = findViewById(R.id.edit_text);
        btnChooseEmo = findViewById(R.id.btn_choose_emo);
        btnSend = findViewById(R.id.btn_send);
        emoBoard = findViewById(R.id.emo_board);
        emoViewPager = findViewById(R.id.emo_viewpager);
        emoPointer = findViewById(R.id.emo_pointer);

        contentView.setOnClickListener(v -> finish());
        editText.setOnClickListener(v -> emoBoard.setVisibility(View.GONE));
        btnSend.setText("send");
        btnChooseEmo.setOnClickListener(v -> {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            setEmoBoardHeight();
            if (emoBoard.isShown()) {
                lockContentHeight();
                emoBoard.setVisibility(View.GONE);
                if (imm != null) {
                    imm.showSoftInput(editText, 0);
                }
                unlockContentHeightDelayed();
            } else {
                if (calculateSoftInputHeight() != 0) {
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
        });

        //设置表情监听、表情指示器等
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        params.height = contentView.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        HANDLER.postDelayed(() -> ((LinearLayout.LayoutParams) contentView.getLayoutParams()).weight = 1.0F, 200L);
    }

    /**
     * 设置表情面板的高度=SP/计算
     */
    private void setEmoBoardHeight() {
        ViewGroup.LayoutParams params = emoBoard.getLayoutParams();
        //已计算过正确的值
        if (params.height != -2 && params.height != SOFT_INPUT_DEFAULT_HEIGHT)
            return;
        int height = getSoftInputHeightFromPref();
        if (height == SOFT_INPUT_DEFAULT_HEIGHT)
            height = calculateSoftInputHeight();
        params.height = height;

    }

    /**
     * 从SP文件中获取软键盘的高度
     *
     * @return 软键盘高度
     */
    private int getSoftInputHeightFromPref() {
        return pref.getInt(SOFT_INPUT_HEIGHT, SOFT_INPUT_DEFAULT_HEIGHT);
    }

    /**
     * 计算软键盘高度
     *
     * @return 软键盘高度
     */
    private int calculateSoftInputHeight() {
        //通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //获取屏幕的高度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
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

}
