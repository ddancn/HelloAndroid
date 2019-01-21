package com.example.ddancn.helloworld.ui.dialog.comment.selector.at;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
    private String avatar;
    private String username;
    private String title;

    public UserInfo() {
    }

    public UserInfo(String avatar, String username, String title) {
        this.avatar = avatar;
        this.username = username;
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar);
        dest.writeString(username);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            UserInfo user = new UserInfo();
            user.avatar = source.readString();
            user.username = source.readString();
            user.title = source.readString();
            return user;
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
