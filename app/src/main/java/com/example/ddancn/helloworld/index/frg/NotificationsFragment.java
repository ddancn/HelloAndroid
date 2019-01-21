package com.example.ddancn.helloworld.index.frg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.ui.dialog.comment.CommentDialogActivity;
import com.sunhapper.spedittool.view.SpEditText;

import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private Button btnChoose;
    private SpEditText spEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        btnChoose = view.findViewById(R.id.btn_choose_photo);
        spEditText = view.findViewById(R.id.edit_text);

        btnChoose.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), CommentDialogActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(0,0);

        });

        spEditText.setKeyReactListener(new SpEditText.KeyReactListener() {
            @Override
            public void onKeyReact(String key) {
                switch (key) {
                    case "@":
                        spEditText.insertSpecialStr(" @ddancn ", true, 0,
                                new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)));
                        break;
                    case "#":
                        spEditText.insertSpecialStr(" #tagtag# ", true, 1,
                                new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)));
                        break;
                    default:
                }
            }
        });

        return view;
    }



}
