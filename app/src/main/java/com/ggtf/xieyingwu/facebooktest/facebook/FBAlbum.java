package com.ggtf.xieyingwu.facebooktest.facebook;

import java.util.List;

/**
 * Created by xieyingwu on 2017/6/21.
 * FaceBook album信息
 */

public class FBAlbum {
    public List<Album> data;
    public FBPaging paging;

    public static class Album {
        public String id;
        public String name;
    }

    public static class AlbumPhoto {
        public List<FBPhoto.Photo> data;
    }
}
