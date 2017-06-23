package com.ggtf.xieyingwu.facebooktest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ggtf.xieyingwu.facebooktest.facebook.FBAccessTokenTracker;
import com.ggtf.xieyingwu.facebooktest.facebook.FBAlbum;
import com.ggtf.xieyingwu.facebooktest.facebook.FBPhoto;
import com.ggtf.xieyingwu.facebooktest.facebook.FBUser;
import com.ggtf.xieyingwu.facebooktest.facebook.FBVideo;
import com.ggtf.xieyingwu.facebooktest.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final int WHAT_LOGIN_SUC = 100;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_LOGIN_SUC:
                    loginSuc();
                    break;
            }
        }
    };
    private ImageView icon;
    private FBAccessTokenTracker tokenTracker;

    private void loginSuc() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.w(TAG, "object = " + object);
                        JSONObject jsonObject = response.getJSONObject();
                        JSONArray jsonArray = response.getJSONArray();
                        Log.w(TAG, "jsonObject = " + jsonObject);
                        Log.w(TAG, "jsonArray = " + jsonArray);
                        if (object != null) {
                            FBUser user = GsonUtil.jsonToObject(object, FBUser.class);
                            Log.w(TAG, "FBUser.id = " + user.id);
//                            toGetPhotoIdList(user);
//                            toGetVideoIdList(user);
                            toGetAlbumsIdList(user);
                        }
                    }
                });
        request.executeAsync();
    }

    private void toGetAlbumsIdList(FBUser user) {
        String graphAlbumUrl = user.getAlbumGraphUrl();
        GraphRequest albumRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphAlbumUrl, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                Log.w(TAG, "album thread? = " + Thread.currentThread());
                Log.w(TAG, "album onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "album jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "album jsonArray = " + jsonArray);
                if (jsonObject != null) {
                    FBAlbum fbAlbum = GsonUtil.jsonToObject(jsonObject, FBAlbum.class);
                    List<FBAlbum.Album> data = fbAlbum.data;
                    if (data != null && !data.isEmpty()) {
                        Log.w(TAG, "Album data.size= " + data.size());
                        for (FBAlbum.Album album : data) {
                            getAlbumDetails(album.id);
                            break;
                        }
                    }
                }
            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "id,name");
        params.putInt("limit", 25);
        albumRequest.setParameters(params);
        albumRequest.executeAsync();
    }

    private void getAlbumDetails(String albumId) {
        String graphAlbumDetailUrl = "/" + albumId + "/photos";
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphAlbumDetailUrl, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    Log.w(TAG, "AlbumDetail errorCode = " + error.getErrorCode() + " ;ErrorMessage = " + error.getErrorMessage() + "  ;ErrorType = " + error.getErrorType());
                }
                Log.w(TAG, "AlbumDetail thread? = " + Thread.currentThread());
                Log.w(TAG, "AlbumDetail onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "AlbumDetail jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "AlbumDetail jsonArray = " + jsonArray);
                if (jsonObject != null) {
                    FBAlbum.AlbumPhoto albumPhoto = GsonUtil.jsonToObject(jsonObject, FBAlbum.AlbumPhoto.class);
                    List<FBPhoto.Photo> data = albumPhoto.data;
                    Log.w(TAG, "AlbumDetil data.size = " + data.size());
                    for (FBPhoto.Photo photo : data) {
                        setImage(photo);
                        break;
                    }
                }
            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "id,height,width,picture,images");
        graphRequest.setParameters(params);
        graphRequest.executeAsync();
    }

    private void toGetVideoIdList(FBUser FBUser) {
        String graphVideoUrl = "/" + FBUser.id + "/videos?type=uploaded";
        GraphRequest videoRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphVideoUrl, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    Log.w(TAG, "video; errorCode = " + error.getErrorCode() + " ;ErrorMessage = " + error.getErrorMessage() + "  ;ErrorType = " + error.getErrorType());
                }
                Log.w(TAG, "video thread? = " + Thread.currentThread());
                Log.w(TAG, "video onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "video jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "video jsonArray = " + jsonArray);
                if (jsonObject != null) {
                    FBVideo fbVideo = GsonUtil.jsonToObject(jsonObject, FBVideo.class);
                    List<FBVideo.ID> data = fbVideo.data;
                    if (data != null && !data.isEmpty()) {
                        Log.w(TAG, "video data.size = " + data.size());
                        for (FBVideo.ID video : data) {
                            getVideoDetails(video.id);
                        }
                    }
                }
            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "id");
        videoRequest.setParameters(params);
        videoRequest.executeAsync();
    }

    private void getVideoDetails(String videoId) {
        String graphVideoDetailUrl = "/" + videoId;
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphVideoDetailUrl, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    Log.w(TAG, "videoDetail errorCode = " + error.getErrorCode() + " ;ErrorMessage = " + error.getErrorMessage() + "  ;ErrorType = " + error.getErrorType());
                }
                Log.w(TAG, "videoDetail thread? = " + Thread.currentThread());
                Log.w(TAG, "videoDetail onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "videoDetail jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "videoDetail jsonArray = " + jsonArray);
                if (jsonObject != null) {
                    FBVideo.Video video = GsonUtil.jsonToObject(jsonObject, FBVideo.Video.class);
                }
            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "id,length,picture,source,status");
        graphRequest.setParameters(params);
        graphRequest.executeAsync();
    }

    private void toGetPhotoIdList(FBUser FBUser) {
        String graphVideoUrl = "/" + FBUser.id + "/videos?type=uploaded";
        String graphPhotoUrl = "/" + FBUser.id + "/photos?type=uploaded";
        GraphRequest photoRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPhotoUrl, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                Log.w(TAG, "photo thread? = " + Thread.currentThread());
                Log.w(TAG, "photo onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "photo jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "photo jsonArray = " + jsonArray);
                if (jsonObject != null) {
                    FBPhoto fbPhoto = GsonUtil.jsonToObject(jsonObject, FBPhoto.class);
                    List<FBPhoto.ID> data = fbPhoto.data;
                    if (data != null && !data.isEmpty()) {
                        Log.w(TAG, "photo data.size = " + data.size());
                        for (FBPhoto.ID photo : data) {
                            getPhotoDetails(photo.id);
                            break;
                        }
                    }
                    boolean hadNext = fbPhoto.paging.isHadNext();
                    Log.w(TAG, "photo hadNext = " + hadNext);
                    if (hadNext) {
                        String graphUrl = response.getRequest().getGraphPath() + "&after=" + fbPhoto.paging.cursors.after;
                        Log.w(TAG, "photo next graphUrl = " + graphUrl);
                        getNextPhotoPage(graphUrl);
                    }
                }
            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "id");
        params.putInt("limit", 25);
        photoRequest.setParameters(params);
        photoRequest.executeAsync();

        if (true) return;
        GraphRequestBatch batch = new GraphRequestBatch();
        batch.add(GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), graphPhotoUrl, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                Log.w(TAG, "photo onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "photo jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "photo jsonArray = " + jsonArray);
            }
        }));
        batch.add(GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), graphVideoUrl, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                Log.w(TAG, "video onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "video jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "video jsonArray = " + jsonArray);
            }
        }));
        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch batch) {
                Log.w(TAG, "onBatchCompleted()");
            }
        });
        GraphRequestAsyncTask graphRequestAsyncTask = batch.executeAsync();
    }

    private void getNextPhotoPage(String graphUrl) {
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphUrl, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                Log.w(TAG, "photo next onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "photo next jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "photo next jsonArray = " + jsonArray);
            }
        });
        graphRequest.executeAsync();
    }

    private void getPhotoDetails(String photoId) {
        String graphPhotoDetailUrl = "/" + photoId;
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPhotoDetailUrl, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                String graphPath = response.getRequest().getGraphPath();
                Log.w(TAG, "graphPath = " + graphPath);
                Log.w(TAG, "photoDetail thread? = " + Thread.currentThread());
                Log.w(TAG, "photoDetail onCompleted()");
                JSONObject jsonObject = response.getJSONObject();
                Log.w(TAG, "photoDetail jsonObject = " + jsonObject);
                JSONArray jsonArray = response.getJSONArray();
                Log.w(TAG, "photoDetail jsonArray = " + jsonArray);
                if (jsonObject != null) {
                    FBPhoto.Photo photo = GsonUtil.jsonToObject(jsonObject, FBPhoto.Photo.class);
                    setImage(photo);
                }
            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "id,height,width,picture,images,album{id}");
        graphRequest.setParameters(params);
        graphRequest.executeAsync();
    }

    private void setImage(FBPhoto.Photo photo) {
        String imgUrl = photo.images.get(0).source;
        Glide.with(this)
                .load(imgUrl)
                .error(R.mipmap.ic_launcher_round)
                .into(icon);
    }

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tokenTracker = new FBAccessTokenTracker();
        tokenTracker.startTracking();
        icon = (ImageView) findViewById(R.id.icon);
        callbackManager = CallbackManager.Factory.create();
        AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
        if (currentAccessToken != null) {
            loginSuc();
            return;
        }
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_photos", "user_videos");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.w(TAG, "thread = " + Thread.currentThread());
                Log.w(TAG, "onSuccess()");
                AccessToken accessToken = loginResult.getAccessToken();
                Log.w(TAG, "accessToken = " + accessToken.getToken());
                Set<String> recentlyGrantedPermissions = loginResult.getRecentlyGrantedPermissions();
                for (String recentlyGrantedPermission : recentlyGrantedPermissions) {
                    Log.w(TAG, "recentlyGrantedPermission = " + recentlyGrantedPermission);
                }

                Set<String> recentlyDeniedPermissions = loginResult.getRecentlyDeniedPermissions();
                for (String recentlyDeniedPermission : recentlyDeniedPermissions) {
                    Log.w(TAG, "recentlyDeniedPermission = " + recentlyDeniedPermission);
                }
//                登录成功；获取用户信息
                handler.sendEmptyMessage(WHAT_LOGIN_SUC);
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "onCancel()");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG, "onError()");
                error.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tokenTracker.stopTracking();
    }
}
