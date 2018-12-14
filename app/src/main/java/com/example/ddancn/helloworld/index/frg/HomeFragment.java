package com.example.ddancn.helloworld.index.frg;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.utils.ToastUtil;
import com.example.ddancn.helloworld.utils.dialog.CommentDialog;
import com.example.ddancn.helloworld.utils.dialog.CustomDialog;
import com.example.ddancn.helloworld.utils.dialog.LoadingDialog;

public class HomeFragment extends Fragment {

    private Button btnShow;
    private Button btnShowFromThread;
    private Button btnCancel;
    private Button btnLoadingDialog;
    private Button btnDialog;
    private Button btnDialog2;
    private Button btnCommentDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnShow = getActivity().findViewById(R.id.btn_show);
        btnShowFromThread = getActivity().findViewById(R.id.btn_show_thread);
        btnCancel = getActivity().findViewById(R.id.btn_cancel);
        btnLoadingDialog = getActivity().findViewById(R.id.btn_loading_dialog);
        btnDialog = getActivity().findViewById(R.id.btn_dialog);
        btnDialog2 = getActivity().findViewById(R.id.btn_dialog2);
        btnCommentDialog = getActivity().findViewById(R.id.btn_comment_dialog);

        btnShow.setOnClickListener(v -> ToastUtil.show("show toast"));
        btnShowFromThread.setOnClickListener(v -> new Thread(() -> ToastUtil.showOnUIThread("show toast from thread")).start());
        btnCancel.setOnClickListener(v -> ToastUtil.cancel());
        btnLoadingDialog.setOnClickListener(v -> {
            LoadingDialog dialog = new LoadingDialog(getActivity());
            dialog.setMsg("Loading...");
            dialog.show();
        });
        btnDialog.setOnClickListener(v -> {
            CustomDialog dialog = new CustomDialog(getActivity(), R.style.CustomDialog);
            dialog.setTitle("标题")
                    .setMsg("内容内容内容内容内容内容内容内容内容内容")
                    .setOnConfirmClickListener("确认", () -> {
                        ToastUtil.show("确认");
                        return true;
                    });
            dialog.show();
        });
        btnDialog2.setOnClickListener(v -> {
            CustomDialog dialog = new CustomDialog(getActivity(), R.style.CustomDialog);
            dialog.setTitle("TITLE")
                    .setMsg("msgmsgmsgmsgmsgmsgmsgmsgmsg")
                    .setOnConfirmClickListener("confirm", () -> {
                        ToastUtil.show("confirm");
                        return true;
                    })
                    .setOnCancelClickListener("cancel", ()->{
                        ToastUtil.show("cancel");
                        return true;

                    });
            dialog.show();
        });
        btnCommentDialog.setOnClickListener(v -> {
            CommentDialog dialog = new CommentDialog(getActivity(), R.style.BottomDialog);
            dialog.setOnSendClickListener("Send", (s)->{
                ToastUtil.show(s);
                return true;
            });
            dialog.show();
        });
    }


}
