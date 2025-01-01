package com.example.nelo.di

import android.app.Application
import android.content.Context
import com.example.nelo.data.local.history.HistoryDao
import com.example.nelo.data.local.history.HistoryDatabase
import com.example.nelo.data.local.library.LibraryDao
import com.example.nelo.data.local.library.LibraryDatabase
import com.example.nelo.data.repositories.HistoryRepositoryImpl
import com.example.nelo.data.remote.scraper.Parser
import com.example.nelo.data.remote.scraper.WebScraper
import com.example.nelo.data.repositories.LibraryRepositoryImpl
import com.example.nelo.data.repositories.MangaRepositoryImpl
import com.example.nelo.domain.repositories.HistoryRepository
import com.example.nelo.domain.repositories.LibraryRepository
import com.example.nelo.domain.repositories.MangaRepository
import com.example.nelo.domain.usecases.AddHistoryUseCase
import com.example.nelo.domain.usecases.AddLibraryUseCase
import com.example.nelo.domain.usecases.ClearHistoryUseCase
import com.example.nelo.domain.usecases.DeleteHistoryUseCase
import com.example.nelo.domain.usecases.DeleteLibraryUseCase
import com.example.nelo.domain.usecases.ExistHistoryUseCase
import com.example.nelo.domain.usecases.ExistLibraryUseCase
import com.example.nelo.domain.usecases.GetAllHistoryUseCase
import com.example.nelo.domain.usecases.GetAllLibraryUseCase
import com.example.nelo.domain.usecases.GetChapterDetailsUseCase
import com.example.nelo.domain.usecases.GetFilteredMangasUseCase
import com.example.nelo.domain.usecases.GetLatestMangasUseCase
import com.example.nelo.domain.usecases.GetMangaDetailsUseCase
import com.example.nelo.domain.usecases.GetNewestMangasUseCase
import com.example.nelo.domain.usecases.GetPopularMangasUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideWebScraper(): WebScraper {
        return WebScraper()
    }

    @Provides
    fun provideParser(): Parser {
        return Parser()
    }

    @Provides
    fun provideMangaRepository(
        pageScraper: WebScraper,
        parser: Parser
    ): MangaRepository {
        return MangaRepositoryImpl(pageScraper = pageScraper, parser = parser)
    }

    //use cases
    @Provides
    fun provideMangaDetailsUseCase(
        mangaRepository: MangaRepository
    ):GetMangaDetailsUseCase {
        return GetMangaDetailsUseCase(repository = mangaRepository)
    }

    @Provides
    fun provideChapterDetailsUseCase(
        mangaRepository: MangaRepository
    ):GetChapterDetailsUseCase {
        return GetChapterDetailsUseCase(repository = mangaRepository)
    }

    @Provides
    fun providePopularMangasUseCase(
        mangaRepository: MangaRepository
    ):GetPopularMangasUseCase {
        return GetPopularMangasUseCase(repository = mangaRepository)
    }

    @Provides
    fun provideLatestMangasUseCase(
        mangaRepository: MangaRepository
    ):GetLatestMangasUseCase {
        return GetLatestMangasUseCase(repository = mangaRepository)
    }

    @Provides
    fun provideNewestMangasUseCase(
        mangaRepository: MangaRepository
    ):GetNewestMangasUseCase {
        return GetNewestMangasUseCase(repository = mangaRepository)
    }

    @Provides
    fun provideFilteredMangasUseCase(
        mangaRepository: MangaRepository
    ):GetFilteredMangasUseCase {
        return GetFilteredMangasUseCase(repository = mangaRepository)
    }

    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideHistoryDatabase(context: Context): HistoryDatabase {
        return HistoryDatabase.getDatabase(context)
    }

    @Provides
    fun provideHistoryDao(
        database: HistoryDatabase
    ): HistoryDao {
        return database.historyDao()
    }

    @Provides
    fun provideHistoryRepository(
        historyDao: HistoryDao
    ):HistoryRepository {
        return HistoryRepositoryImpl(historyDao = historyDao)
    }

    @Provides
    fun provideAddHistoryUseCase(
        historyRepository: HistoryRepository
    ):AddHistoryUseCase {
        return AddHistoryUseCase(historyRepository = historyRepository)
    }

    @Provides
    fun provideClearHistoryUseCase(
        historyRepository: HistoryRepository
    ):ClearHistoryUseCase {
        return ClearHistoryUseCase(historyRepository = historyRepository)
    }

    @Provides
    fun provideDeleteHistoryUseCase(
        historyRepository: HistoryRepository
    ):DeleteHistoryUseCase {
        return DeleteHistoryUseCase(historyRepository = historyRepository)
    }

    @Provides
    fun provideExistHistoryUseCase(
        historyRepository: HistoryRepository
    ):ExistHistoryUseCase {
        return ExistHistoryUseCase(historyRepository = historyRepository)
    }

    @Provides
    fun provideGetAllHistoryUseCase(
        historyRepository: HistoryRepository
    ):GetAllHistoryUseCase {
        return GetAllHistoryUseCase(historyRepository = historyRepository)
    }

    @Provides
    @Singleton
    fun provideLibraryDatabase(context: Context): LibraryDatabase {
        return LibraryDatabase.getDatabase(context)
    }

    @Provides
    fun provideLibraryDao(
        libraryDatabase: LibraryDatabase
    ): LibraryDao {
        return libraryDatabase.libraryDao()
    }

    @Provides
    fun provideLibraryRepository(
        libraryDao: LibraryDao
    ): LibraryRepository {
        return LibraryRepositoryImpl(libraryDao = libraryDao)
    }

    @Provides
    fun provideAddLibraryRepository(
        libraryRepository: LibraryRepository
    ):AddLibraryUseCase {
        return AddLibraryUseCase(libraryRepository = libraryRepository)
    }

    @Provides
    fun provideDeleteLibraryRepository(
        libraryRepository: LibraryRepository
    ):DeleteLibraryUseCase {
        return DeleteLibraryUseCase(libraryRepository = libraryRepository)
    }

    @Provides
    fun provideExistLibraryRepository(
        libraryRepository: LibraryRepository
    ):ExistLibraryUseCase {
        return ExistLibraryUseCase(libraryRepository = libraryRepository)
    }

    @Provides
    fun provideGetAllLibraryRepository(
        libraryRepository: LibraryRepository
    ):GetAllLibraryUseCase {
        return GetAllLibraryUseCase(libraryRepository = libraryRepository)
    }
}