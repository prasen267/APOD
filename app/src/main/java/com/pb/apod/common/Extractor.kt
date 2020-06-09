package com.pb.apod.common

import java.util.regex.Pattern

fun extractYoutubeVideoId(ytUrl: String?): String? {
    var vId: String? = null
    val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
    val compiledPattern = Pattern.compile(pattern)
    val matcher = compiledPattern.matcher(ytUrl)
    if (matcher.find()) {
        vId = matcher.group()
    }
    return vId
}