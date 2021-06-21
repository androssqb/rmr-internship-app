package com.redmadrobot.app.di.modules

import com.redmadrobot.domain.di.BottomNavigationEvents
import com.redmadrobot.extensions.lifecycle.EventQueue
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {

    @Singleton
    @Provides
    @BottomNavigationEvents
    fun provideBottomNavigationEvents(): EventQueue = EventQueue()
}
