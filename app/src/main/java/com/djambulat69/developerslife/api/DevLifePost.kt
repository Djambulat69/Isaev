package com.djambulat69.developerslife.api

data class DevLifePost(
    val id: Int,
    val description: String,
    val votes: Int,
    val author: String,
    val date: String,
    val gifURL: String,
    val gifSize: Int,
    val previewURL: String,
    val videoURL: String,
    val videoPath: String,
    val videoSize: Int,
    val type: String,
    val width: Int,
    val height: Int,
    val commentsCount: Int,
    val fileSize: Int,
    val canVote: Boolean
)