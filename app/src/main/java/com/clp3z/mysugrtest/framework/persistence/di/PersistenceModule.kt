package com.clp3z.mysugrtest.framework.persistence.di

import android.app.Application
import androidx.room.Room
import com.clp3z.mysugrtest.framework.persistence.Database
import com.clp3z.mysugrtest.framework.persistence.dao.GlucoseMeasurementDAO
import com.clp3z.mysugrtest.framework.persistence.datasources.LocalGlucoseMeasurementDataSource
import com.clp3z.mysugrtest.framework.persistence.datasources.LocalGlucoseMeasurementDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    private const val DATABASE_NAME = "glucose-database"

    @Provides
    @Singleton
    fun provideRoomDatabase(application: Application): Database =
        Room.databaseBuilder(
            context = application,
            klass = Database::class.java,
            name = DATABASE_NAME,
        ).build()

    @Provides
    @Singleton
    fun provideGlucoseMeasurementDAO(database: Database) =
        database.glucoseMeasurementDao()

    @Provides
    @Singleton
    fun provideLocalDataSource(glucoseMeasurementDAO: GlucoseMeasurementDAO): LocalGlucoseMeasurementDataSource =
        LocalGlucoseMeasurementDataSourceImpl(glucoseMeasurementDAO)
}
