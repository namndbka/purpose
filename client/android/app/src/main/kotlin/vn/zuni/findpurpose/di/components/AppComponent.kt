package vn.zuni.findpurpose.di.components

import dagger.Component
import vn.zuni.findpurpose.di.modules.AppModule
import vn.zuni.findpurpose.di.modules.NetModule
import vn.zuni.findpurpose.fragment.FragmentVideo
import vn.zuni.findpurpose.presenters.HomePresenter
import vn.zuni.findpurpose.presenters.MainPresenter
import vn.zuni.findpurpose.presenters.SurveyPresenter
import javax.inject.Singleton

/**
 *
 * Created by namnd on 1/11/18.
 */

@Singleton
@Component(modules = [(AppModule::class), (NetModule::class)])
interface AppComponent {

    fun inject(presenter: HomePresenter)

    fun inject(presenter: MainPresenter)
    
    fun inject(presenter: SurveyPresenter)

    fun inject(fragment: FragmentVideo)
}
