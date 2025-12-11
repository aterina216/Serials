package com.example.serials.di

import android.app.Application
import android.content.Context
import android.view.View
import androidx.room.Room
import com.example.serials.data.db.dao.SerialDao
import com.example.serials.data.db.database.SerialsDatabase
import com.example.serials.data.mapper.ConverterResponseFromEntity
import com.example.serials.data.remote.api.BASE_URL
import com.example.serials.data.remote.api.OMDbApi
import com.example.serials.data.remote.api.RetrofitClient
import com.example.serials.data.repository.SerialsRepository
import com.example.serials.ui.viewmodel.SerialsViewModel
import com.example.serials.ui.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    fun providesApplication(): Application = app

    @Singleton
    @Provides
    fun provideAppContext(app: Application): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun provideDB(context: Context): SerialsDatabase {
        return Room.databaseBuilder(
            context,
            SerialsDatabase::class.java,
            "serials_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: SerialsDatabase): SerialDao {
        return db.dao()
    }

    @Singleton
    @Provides
    fun provideMapper(): ConverterResponseFromEntity {
        return ConverterResponseFromEntity()
    }

    @Singleton
    @Provides
    fun provideApi(): OMDbApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OMDbApi::class.java)
    }

    @Singleton
    @Provides
    fun providesRepository(dao: SerialDao, api: OMDbApi, mapper: ConverterResponseFromEntity, ): SerialsRepository
    {
        return SerialsRepository(
            dao = dao,
            api = api,
            mapper = mapper
        )
    }

    @Singleton
    @Provides
    fun providesViewModelFactory(repo: SerialsRepository): ViewModelFactory {
        return ViewModelFactory(repo)
    }

    @Singleton
    @Provides
    fun providesViewModel(repository: SerialsRepository): SerialsViewModel {
        return SerialsViewModel(repository)
    }
}