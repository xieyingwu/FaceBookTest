package com.ggtf.xieyingwu.facebooktest.facebook;

/**
 * Created by xieyingwu on 2017/6/21.
 * FaceBook user信息
 */

public class FBUser {
    public String name;
    public String id;

    public String getPhotoGraphUrl(Type type) {
        return "/" + id + "/photos?type=" + type.name();
    }

    public String getVideoGraphUrl(Type type) {
        return "/" + id + "/videos?type=" + type.name();
    }

    public String getPhotoGraphNextUrl(Type type, String nextCursor) {
        return getPhotoGraphUrl(type) + "&after=" + nextCursor;
    }

    public String getVideoGraphNextUrl(Type type, String nextCursor) {
        return getVideoGraphUrl(type) + "&after=" + nextCursor;
    }

    public String getAlbumGraphUrl() {
        return "/" + id + "/albums";
    }

    public enum Type {
        tagged,
        uploaded
    }

    public enum Order {
        chronological,
        reverse_chronological
    }
}
