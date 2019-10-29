package vn.zuni.findpurpose.di.modules


import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 * Created by namnd on 1/11/18.
 */

@Module
class AppModule(private val application: Application)  {

    @Singleton
    @Provides
    fun provideContext(): Context = application.applicationContext

}