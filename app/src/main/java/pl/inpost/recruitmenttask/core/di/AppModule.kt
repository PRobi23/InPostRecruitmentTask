package pl.inpost.recruitmenttask.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import pl.inpost.recruitmenttask.data.remote.api.MockShipmentApi
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.data.remote.repository.ShipmentRepositoryImpl
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.mapper.Mapper
import pl.inpost.recruitmenttask.domain.mapper.ShipmentDtoToShipmentMapper
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository
import javax.inject.Qualifier
import javax.inject.Singleton

/***
 * Date App module.
 * This object is used for DI.
 */
@Module
@InstallIn(SingletonComponent::class)
object DateAppModule {
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideShipmentMapper(): Mapper<ShipmentDTO, Shipment> = ShipmentDtoToShipmentMapper()

    @Provides
    fun provideShipmentRepository(
        mockShipmentApi: MockShipmentApi,
        mapper: Mapper<ShipmentDTO, Shipment>
    ): ShipmentRepository = ShipmentRepositoryImpl(
        mockShipmentApi, mapper
    )
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher