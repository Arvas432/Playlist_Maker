package com.example.playlistmaker.data.search.network.impl

import com.example.playlistmaker.data.search.dto.ITunesSearchRequest
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.search.network.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesService: ITunesApi): NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if(dto is ITunesSearchRequest){
            return withContext(Dispatchers.IO){
                try {
                    val resp = iTunesService.search(dto.expression)
                    resp.apply { resultCode = 200}
                } catch(e: Throwable){
                    Response().apply { resultCode = 500 }
                }
            }
        }else{
            return Response().apply { resultCode=400 }
        }
    }

}