package com.jay.typicodeapp.di

import android.content.Context
import com.jay.typicodeapp.services.NetworkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext appContext: Context) = appContext

    @Provides
    @Singleton
    fun provideNetworkHandler(context: Context) = NetworkHandler(context)
}
