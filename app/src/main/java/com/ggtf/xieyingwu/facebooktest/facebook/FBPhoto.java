package com.ggtf.xieyingwu.facebooktest.facebook;

import java.util.List;

/**
 * Created by xieyingwu on 2017/6/21.
 * FaceBook photo信息
 */

public class FBPhoto {
    public List<ID> data;
    public FBPaging paging;

    public static class ID {
        public String id;
    }

    public static class Photo {
        public String id;
        public int height;/*原生图片高度pix*/
        public int width;/*原始图片宽度pix*/
        public String picture;/*缩略图url*/
        public List<Size> images;
        public ID album;/*photo所属的album*/
    }

    public static class Size {
        public String source;/*图片url*/
        public int height;/*图片高度*/
        public int width;/*图片宽度*/
    }
}
