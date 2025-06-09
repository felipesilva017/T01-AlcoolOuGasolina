package com.example.gasoralchool.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openInMap(context: Context, latitude: Double?, longitude: Double?) {
    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(Posto)")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
