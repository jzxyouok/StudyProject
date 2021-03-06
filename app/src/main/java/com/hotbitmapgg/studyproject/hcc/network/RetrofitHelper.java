package com.hotbitmapgg.studyproject.hcc.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.hotbitmapgg.studyproject.hcc.StudyApp;
import com.hotbitmapgg.studyproject.hcc.model.GankPostBoby;
import com.hotbitmapgg.studyproject.hcc.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitHelper
{

    public static final String BASE_ZHUANGBI_URL = "http://zhuangbi.info/";

    public static final String BASE_GANK_URL = "http://gank.io/api/";

    public static final String BASE_POST_GANK_URL = "https://gank.io/api/add2gank";

    public static final String BASE_HUABAN_URL = "http://route.showapi.com/";

    public static final String BASE_DOUBAN_URL = "http://www.dbmeinv.com/dbgroup/";

    public static final String BASE_GITHUB_URL = "https://api.github.com/";

    public static final String BASE_GITHUB_LOGIN_URL = "https://github.com/login/oauth/";

    private static OkHttpClient mOkHttpClient;

    static
    {
        initOkHttpClient();
    }

    /**
     * Gank干货Api
     *
     * @return
     */
    public static GankApi getGankApi()
    {

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_GANK_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GankApi gankApi = mRetrofit.create(GankApi.class);

        return gankApi;
    }

    /**
     * 提交干货Api
     *
     * @return
     */

    public static String getPostGankResult(GankPostBoby boby)
    {

        RequestBody requestBody = new FormBody.Builder()
                .add("url", boby.getUrl())
                .add("desc", boby.getTitle())
                .add("who", boby.getName())
                .add("type", boby.getType())
                .add("debug", boby.getIsdebug())
                .build();
        Request request = new Request.Builder()
                .url(BASE_POST_GANK_URL)
                .post(requestBody)
                .build();
        try
        {
            Response response = mOkHttpClient.newCall(request).execute();

            return response.body().string();
        } catch (IOException e1)
        {
            e1.printStackTrace();

            return null;
        }
    }


    /**
     * Gank妹子Api
     *
     * @return
     */
    public static GankMeiziApi getGankMeiziApi()
    {

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_GANK_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GankMeiziApi gankMeiziApi = mRetrofit.create(GankMeiziApi.class);

        return gankMeiziApi;
    }


    /**
     * 表情包搜索Api
     *
     * @return
     */
    public static ExpressionPackageApi getExpressionPackageApi()
    {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_ZHUANGBI_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ExpressionPackageApi expressionPackageApi = retrofit.create(ExpressionPackageApi.class);

        return expressionPackageApi;
    }


    /**
     * 花瓣Api
     *
     * @return
     */
    public static HuaBanMeiziApi getHuaBanMeiziApi()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_HUABAN_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        HuaBanMeiziApi huaBanMeiziApi = retrofit.create(HuaBanMeiziApi.class);

        return huaBanMeiziApi;
    }

    /**
     * 豆瓣Api
     *
     * @return
     */
    public static DoubanMeizhiApi getDoubanMeiziApi()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_DOUBAN_URL)
                .client(new OkHttpClient())
                .build();

        return retrofit.create(DoubanMeizhiApi.class);
    }


    /**
     * GithubApi
     *
     * @return
     */
    public static GithubApi getGithubApi()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_GITHUB_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubApi githubApi = retrofit.create(GithubApi.class);

        return githubApi;
    }

    /**
     * Github登录Api
     *
     * @return
     */
    public static GithubApi getGithubLoginApi()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_GITHUB_LOGIN_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubApi githubApi = retrofit.create(GithubApi.class);

        return githubApi;
    }


    /**
     * 初始化OKHttpClient
     */
    private static void initOkHttpClient()
    {

        LogUtil.all("初始化OkHttpClient");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null)
        {
            synchronized (RetrofitHelper.class)
            {
                if (mOkHttpClient == null)
                {
                    //设置Http缓存
                    Cache cache = new Cache(new File(StudyApp.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(new StethoInterceptor())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }
}
