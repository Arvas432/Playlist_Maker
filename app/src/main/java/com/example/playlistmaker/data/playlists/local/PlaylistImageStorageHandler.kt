package com.example.playlistmaker.data.playlists.local

import android.net.Uri

interface PlaylistImageStorageHandler {
    suspend fun createImageFile(uri: Uri): String
    suspend fun readUriFromFilePath(path: String): Uri
    suspend fun deleteFileByFilePath(path: String)
}