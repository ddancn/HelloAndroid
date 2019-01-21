package com.example.ddancn.helloworld.ui.dialog.comment.selector.file;

import android.os.Parcel;
import android.os.Parcelable;

public class FileInfo implements Parcelable {

    private String name;
    private String path;
    private Long size;
    private Long time;
    private int imageId;

    public FileInfo() {
    }

    public FileInfo(String name, String path, Long size, Long time, int imageId) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.time = time;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeLong(size);
        dest.writeLong(time);
        dest.writeInt(imageId);
    }

    public static final Parcelable.Creator<FileInfo> CREATOR = new Parcelable.Creator<FileInfo>() {
        @Override
        public FileInfo createFromParcel(Parcel source) {
            FileInfo file = new FileInfo();
            file.name = source.readString();
            file.path = source.readString();
            file.size = source.readLong();
            file.time = source.readLong();
            file.imageId = source.readInt();
            return file;
        }

        @Override
        public FileInfo[] newArray(int size) {
            return new FileInfo[size];
        }
    };
}
