package com.example.ddancn.helloworld.ui.dialog.comment.selector.file;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.utils.FileUtil;

import java.util.ArrayList;

public class FileRvAdapter extends RecyclerView.Adapter<FileRvAdapter.ViewHolder> {
    private ArrayList<FileInfo> mFileList;
    private Activity activity;

    class ViewHolder extends RecyclerView.ViewHolder {
        View fileView;
        ImageView fileImage;
        TextView fileName;
        TextView fileSize;
        TextView fileTime;

        public ViewHolder(View view) {
            super(view);
            fileView = view;
            fileImage = view.findViewById(R.id.iv_file);
            fileName = view.findViewById(R.id.tv_name);
            fileSize = view.findViewById(R.id.tv_size);
            fileTime = view.findViewById(R.id.tv_time);
        }
    }

    public FileRvAdapter(Activity activity, ArrayList<FileInfo> fileList) {
        this.activity = activity;
        mFileList = fileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.fileView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Intent intent = new Intent();
            intent.putExtra(FileSelectorActivity.FILE_CHOSEN, mFileList.get(position));
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileInfo file = mFileList.get(position);
        holder.fileName.setText(file.getName());
        holder.fileSize.setText(FileUtil.getFileSize(file.getSize()));
        holder.fileTime.setText(FileUtil.getStrTime(file.getTime()));
        holder.fileImage.setImageResource(file.getImageId());
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }
}