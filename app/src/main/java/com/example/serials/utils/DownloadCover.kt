package com.example.serials.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.appcompat.widget.DialogTitle
import com.example.serials.utils.PermissionHelper.hasStoragePermission
import com.example.serials.utils.PermissionHelper.requestStoragePermission

object DownloadCover {

    fun downloadCover(context: Context, imageUrl: String, title: String) {
        if(hasStoragePermission(context)) {
            val request = DownloadManager.Request(Uri.parse(imageUrl))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "$title.jpg")
                .setTitle("Скачивание постера $title")
                .setDescription("Пожалуйста, подождите...")

            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        }
        else {
            requestStoragePermission(context)
        }
    }
}