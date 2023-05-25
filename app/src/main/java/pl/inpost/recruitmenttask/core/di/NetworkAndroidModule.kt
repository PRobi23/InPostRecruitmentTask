package pl.inpost.recruitmenttask.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.inpost.recruitmenttask.data.remote.ApiTypeAdapter
import pl.inpost.recruitmenttask.data.remote.api.MockShipmentApi
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository

@InstallIn(SingletonComponent::class)
@Module
class NetworkAndroidModule {

    @Provides
    fun shipmentApi(
        @ApplicationContext context: Context,
        apiTypeAdapter: ApiTypeAdapter
    ) = MockShipmentApi(context, apiTypeAdapter)
}
