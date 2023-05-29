package pl.inpost.recruitmenttask.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import pl.inpost.recruitmenttask.core.analytics.Analytics
import pl.inpost.recruitmenttask.core.analytics.AnalyticsImpl
import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtils
import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtilsImpl
import pl.inpost.recruitmenttask.core.util.Constants
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.data.local.entity.ShipmentsDao
import pl.inpost.recruitmenttask.data.local.entity.ShipmentsDatabase
import pl.inpost.recruitmenttask.data.remote.api.MockShipmentApi
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.data.repository.ShipmentRepositoryImpl
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.mapper.Mapper
import pl.inpost.recruitmenttask.domain.mapper.ShipmentDtoToShipmentEntityMapper
import pl.inpost.recruitmenttask.domain.mapper.ShipmentEntityToShipmentMapper
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository
import javax.inject.Qualifier
import javax.inject.Singleton

/***
 * RecruitmentTask App module.
 * This object is used for DI.
 */
@Module
@InstallIn(SingletonComponent::class)
object ShipmentsAppModule {
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
    fun providesDateTimeUtils(): DateTimeUtils = DateTimeUtilsImpl()

    @Provides
    fun providesShipmenEntityToShipmentMapper(dateTimeUtils: DateTimeUtils): Mapper<ShipmentEntity, Shipment> =
        ShipmentEntityToShipmentMapper(dateTimeUtils)

    @Provides
    fun providesShipmenDTOToShipmentEntityMapper(dateTimeUtils: DateTimeUtils): Mapper<ShipmentDTO, ShipmentEntity> =
        ShipmentDtoToShipmentEntityMapper(dateTimeUtils)

    @Provides
    fun providesShipmentDao(appDatabase: ShipmentsDatabase): ShipmentsDao {
        return appDatabase.getShipmentDao
    }

    @Provides
    @Singleton
    fun provideShipmentDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ShipmentsDatabase::class.java, Constants.SHIPMENT_DB)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideAnalytics(): Analytics = AnalyticsImpl()

    @Provides
    fun provideShipmentRepository(
        mockShipmentApi: MockShipmentApi,
        mapperShipmentEntityToShipmentMapper: Mapper<ShipmentEntity, Shipment>,
        shipmentDtoToShipmentEntityMapper: Mapper<ShipmentDTO, ShipmentEntity>,
        analytics: Analytics,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
        shipmentsDao: ShipmentsDao
    ): ShipmentRepository = ShipmentRepositoryImpl(
        mockShipmentApi, mapperShipmentEntityToShipmentMapper,
        shipmentDtoToShipmentEntityMapper, analytics,
        dispatcher, shipmentsDao
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