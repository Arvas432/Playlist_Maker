package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.SearchResultType
import com.example.playlistmaker.domain.models.TracksSearchResult
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            if(expression.isNotEmpty()){
                consumer.consume(TracksSearchResult(emptyList(), SearchResultType.LOADING))
                try {
                    val tracks = repository.searchTracks(expression)
                    if(tracks.isNotEmpty()){
                        consumer.consume(TracksSearchResult(tracks,SearchResultType.SUCCESS))
                    }else{
                        consumer.consume(TracksSearchResult(emptyList(), SearchResultType.EMPTY))
                    }
                }catch (e: Throwable){
                    consumer.consume(TracksSearchResult(emptyList(), SearchResultType.ERROR))
                }

            }

        }
    }

}