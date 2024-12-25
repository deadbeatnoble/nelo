package com.example.nelo.di

import com.example.nelo.data.remote.scraper.Parser
import com.example.nelo.data.remote.scraper.WebScraper
import com.example.nelo.data.repositories.MangaRepositoryImpl
import com.example.nelo.domain.repositories.MangaRepository
import com.example.nelo.domain.usecases.GetChapterDetailsUseCase
import com.example.nelo.domain.usecases.GetLatestMangasUseCase
import com.example.nelo.domain.usecases.GetMangaDetailsUseCase
import com.example.nelo.domain.usecases.GetNewestMangasUseCase
import com.example.nelo.domain.usecases.GetPopularMangasUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
}