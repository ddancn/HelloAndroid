package com.example.ddancn.helloworld.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.ddancn.helloworld.ui.dialog.comment.selector.file.FileInfo;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static FileInfo getFileInfoFromFile(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(file.getName());
        fileInfo.setPath(file.getPath());
        fileInfo.setSize(file.length());
        fileInfo.setTime(file.lastModified());
        return fileInfo;
    }

    public static String getFileSize(Long length) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (length == 0) {
            return wrongSize;
        } else if (length < 1024) {
            fileSizeString = df.format((double) length) + "B";
        } else if (length < 1048576) {
            fileSizeString = df.format((double) length / 1024) + "KB";
        } else if (length < 1073741824) {
            fileSizeString = df.format((double) length / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) length / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static String getStrTime(Long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
        return sdf.format(new Date(timeStamp));
    }
}
