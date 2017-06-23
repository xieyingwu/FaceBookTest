package com.ggtf.xieyingwu.facebooktest.facebook;

import android.text.TextUtils;

/**
 * Created by xieyingwu on 2017/6/21.
 * FaceBook page信息
 */

public class FBPaging {
    public Cursors cursors;
    public String previous;
    public String next;

    public static class Cursors {
        public String before;
        public String after;
    }

    public boolean isHadNext() {
        return !TextUtils.isEmpty(next);
    }
}
