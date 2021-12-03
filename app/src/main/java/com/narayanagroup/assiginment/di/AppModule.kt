package com.narayanagroup.assiginment.di

import android.content.Context
import android.webkit.WebView
import com.narayanagroup.assiginment.api.githubApi
import com.narayanagroup.assiginment.data.dao.PlayersDao
import com.narayanagroup.assiginment.data.dao.RemoteKeysDao
import com.narayanagroup.assiginment.data.db.AppDataBase
import com.narayanagroup.assiginment.utils.BASE_URL
import com.narayanagroup.assiginment.utils.value
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("authority", "api.github.com")
                    .addHeader(  "scheme", "https")
                    .addHeader("cache-control"," max-age=0")
                    .addHeader("language", Locale.getDefault().language)
                    .addHeader("os", android.os.Build.VERSION.RELEASE)
                    .addHeader("Authorization", "MyauthHeaderContent")
                    .addHeader("user-agent","Narayana Assignment").build()

                chain.proceed(request)
            })
            .build()

    @Provides
    @Singleton
    fun providesDB(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.invoke(context)
    }

    @Singleton
    @Provides
    fun providesKeysDao(appDataBase: AppDataBase): RemoteKeysDao = appDataBase.remoteKeysDao

    @Singleton
    @Provides
    fun providesDao(appDataBase: AppDataBase): PlayersDao = appDataBase.playersDao


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    @Singleton
    fun providePlayersApi(retrofit: Retrofit): githubApi = retrofit.create(githubApi::class.java)

}


