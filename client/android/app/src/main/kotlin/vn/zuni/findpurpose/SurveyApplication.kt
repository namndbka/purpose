package vn.zuni.findpurpose

import android.app.Application
import android.content.Context
import android.os.StrictMode
import vn.zuni.findpurpose.di.components.AppComponent
import vn.zuni.findpurpose.di.components.DaggerAppComponent
import vn.zuni.findpurpose.di.modules.AppModule
import vn.zuni.findpurpose.di.modules.NetModule
import vn.zuni.findpurpose.extensions.ComponentReflectionInjector
import vn.zuni.findpurpose.extensions.Injector
import vn.zuni.findpurpose.extensions.LocaleHelper
import vn.zuni.findpurpose.extensions.unSafeLazy


/**
 *
 * Created by namnd on 02-Jan-18.
 */
class SurveyApplication : Application(), Injector {
    private lateinit var injector: ComponentReflectionInjector<AppComponent>

    companion object {
        lateinit var instance: SurveyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        instance = this
        val component: AppComponent by unSafeLazy { DaggerAppComponent.builder().netModule(NetModule(this)).appModule(AppModule(this)).build() }
        injector = ComponentReflectionInjector(AppComponent::class.java, component)
    }

    override fun inject(target: Any) {
        injector.inject(target)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "vi"))
    }
}