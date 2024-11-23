package com.harisfi.listo.models.services.modules

import com.harisfi.listo.models.services.AccountService
import com.harisfi.listo.models.services.StorageService
import com.harisfi.listo.models.services.impl.AccountServiceImpl
import com.harisfi.listo.models.services.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
}