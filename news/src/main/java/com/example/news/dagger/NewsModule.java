package com.example.news.dagger;



import com.example.commonlibrary.bean.chat.DaoSession;
import com.example.commonlibrary.dagger.scope.PerApplication;
import com.example.commonlibrary.repository.DefaultRepositoryManager;
import com.example.news.interceptor.CacheControlInterceptor;
import com.example.news.interceptor.NewsInterceptor;
import com.example.news.util.NewsUtil;

import java.io.File;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:14
 * QQ:             1981367757
 */
@Module
public class NewsModule {




    @Provides
    @PerApplication
    public DefaultRepositoryManager provideRepositoryManager(@Named("news") Retrofit retrofit, DaoSession daoSession) {
        return new DefaultRepositoryManager(retrofit, daoSession);
    }

    @Provides
    @Named("news")
    @PerApplication
    public Retrofit provideRetrofit(@Named("news") OkHttpClient okHttpClient,Retrofit.Builder builder){
        return builder.baseUrl(NewsUtil.BASE_URL).client(okHttpClient).build();
    }


    @Provides
    @Named("news")
    @PerApplication
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder,File cacheFile,@Named("news")NewsInterceptor interceptor){
        CacheControlInterceptor cacheControlInterceptor=new CacheControlInterceptor();
        builder.addInterceptor(interceptor)
        .addInterceptor(cacheControlInterceptor)
                .addNetworkInterceptor(cacheControlInterceptor)
        .cache(new Cache(new File(cacheFile.getAbsolutePath(),"news"),
                1024*1024*100));
        builder.followRedirects(true);
        return builder.build();
    }


    @Provides
    @Named("news")
    @PerApplication
    public NewsInterceptor provideNewsInterceptor(){
        return new NewsInterceptor();
    }
}
