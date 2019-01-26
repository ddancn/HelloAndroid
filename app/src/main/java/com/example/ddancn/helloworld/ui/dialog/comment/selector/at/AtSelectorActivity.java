package com.example.ddancn.helloworld.ui.dialog.comment.selector.at;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.utils.DimenUtil;
import com.example.ddancn.helloworld.utils.ToastUtil;

import java.util.ArrayList;

public class AtSelectorActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    public static String USERS_CHOSEN = "usersChosen";
    public ArrayList<UserInfo> checkedList = new ArrayList<>();

    ImageButton btnBack;
    Button btnConfirm;
    EditText editText;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_selector);

        btnBack = findViewById(R.id.btn_back);
        btnConfirm = findViewById(R.id.btn_confirm);
        editText = findViewById(R.id.edit_text);
        recyclerView = findViewById(R.id.rv_at_user);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_search);
        drawable.setBounds(0,0,DimenUtil.dp2px(20),DimenUtil.dp2px(20));
        editText.setCompoundDrawables(drawable,null,null,null);

        ArrayList<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo(null, "用户1", "圈主"));
        list.add(new UserInfo(null, "用户2", "嘉宾"));
        list.add(new UserInfo(null, "用户3", "管理员"));
        list.add(new UserInfo(null, "用户4", null));
        AtUserRvAdapter adapter = new AtUserRvAdapter(R.layout.item_at_user, list);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        btnBack.setOnClickListener(v-> this.finish());
        btnConfirm.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(USERS_CHOSEN, checkedList);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        UserInfo user = (UserInfo) adapter.getData().get(position);

        if(checkBox.isChecked()){
            checkBox.setChecked(false);
            checkedList.remove(user);
        } else {
            checkBox.setChecked(true);
            checkedList.add(user);
        }
        changeBtnState();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        CheckBox checkBox = (CheckBox) view;
        UserInfo user = (UserInfo) adapter.getData().get(position);

        if(checkBox.isChecked()){
            checkedList.add(user);
        } else {
            checkedList.remove(user);
        }
        changeBtnState();
    }

    private void changeBtnState(){
        int size = checkedList.size();
        if(size == 0){
            btnConfirm.setText("确定");
            btnConfirm.setTextColor(getResources().getColor(R.color.colorGray));
            btnConfirm.setEnabled(false);
        } else{
            btnConfirm.setText("确定"+checkedList.size());
            btnConfirm.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnConfirm.setEnabled(true);
        }
    }
}
