package com.example.playlistmaker.data.playlists.local

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

class PlaylistImageStorageHandlerImpl(
    private val filePath: File,
    private val contentResolver: ContentResolver
) : PlaylistImageStorageHandler {
    override suspend fun createImageFile(uri: Uri): String {
        val fileName = "${System.currentTimeMillis()}.jpg"
        val file = File(filePath, fileName)
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return fileName
    }

    override suspend fun readUriFromFilePath(path: String): Uri {
        val file = File(filePath, path)
        return file.toUri()
    }

    override suspend fun deleteFileByFilePath(path: String){
        val file = File(filePath, path)
        file.delete()
    }

}