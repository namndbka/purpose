package vn.zuni.findpurpose.di.modules

import android.app.Application
import com.franmontiel.persistentcookiejar.BuildConfig
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.zuni.findpurpose.api.ISurveyApi
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *
 * Created by namnd on 1/11/18.
 */

@Module
class NetModule(private val application: Application) {
    companion object {
        private val API_URL = "http://esurvey.zuni.vn/api/v1/"
        private val CACHE_SIZE = (20 * 1024 * 1024).toLong() //20 MB
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        else loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideHttpCache(): Cache = Cache(application.cacheDir, CACHE_SIZE)

    @Singleton
    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        gsonBuilder.setPrettyPrinting()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        gsonBuilder.registerTypeAdapter(Date::class.java,
                JsonDeserializer<Date> { json, _, _ -> Date(json.asJsonPrimitive.asLong * 1000) })
        return gsonBuilder.create()
    }


    @Singleton
    @Provides
    fun provideCookieJar(): CookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(application.baseContext))

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache, cookieJar: CookieJar): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .cache(cache)
                .cookieJar(cookieJar)
                .addInterceptor(loggingInterceptor)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                            .addHeader("From", "androi_survey")
                            .addHeader("Accept", "application/json")
                            .build()
                    chain.proceed(request)
                }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(API_URL)
            .client(okHttpClient)
            .build()


    @Singleton
    @Provides
    fun provideSurveyAPI(retrofit: Retrofit): ISurveyApi = retrofit.create(ISurveyApi::class.java)
}
