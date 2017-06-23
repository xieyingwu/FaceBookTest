package com.ggtf.xieyingwu.facebooktest.facebook;

import java.util.List;

/**
 * Created by xieyingwu on 2017/6/21.
 * FaceBook video信息
 */

public class FBVideo {
    public List<ID> data;
    public FBPaging paging;

    public static class ID {
        public String id;
    }

    public static class Video {
        public String id;
        public float length;/*视频时长duration*/
        public String picture;/*视频缩略图url*/
        public String source;/*视频地址url*/
        public Status status;/*视频状态*/

        public boolean isReady() {
            return status != null && "ready".equals(status.video_status);
        }
    }

    public static class Status {
        public String video_status;
    }
}
