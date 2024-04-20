package com.example.playlistmaker.data.search.network.impl

import com.example.playlistmaker.data.search.dto.ITunesSearchRequest
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.search.network.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    override fun doRequest(dto: Any): Response {
        if(dto is ITunesSearchRequest){
            val resp = iTunesService.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        }else{
            return Response().apply { resultCode=400 }
        }
    }

}