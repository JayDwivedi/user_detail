package com.jay.typicodeapp.di

import com.jay.typicodeapp.services.networking.MainApiHelperImpl
import com.jay.typicodeapp.services.networking.MainApiHelperInterface
import com.jay.typicodeapp.services.networking.MainApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MainApiService =
        retrofit.create(MainApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(mainApiHelper: MainApiHelperImpl): MainApiHelperInterface =
        mainApiHelper
}
