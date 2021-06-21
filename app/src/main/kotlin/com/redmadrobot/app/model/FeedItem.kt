package com.redmadrobot.app.model

sealed class FeedItem

data class FeedHeader(val header: String) : FeedItem()

data class FeedPost(
    val id: String,
    val text: String,
    val imageUrl: String,
    val geo: String,
    val authorNick: String,
    val liked: Boolean
) : FeedItem()

data class FeedEmpty(val itemEmpty: ItemEmpty) : FeedItem()

data class FeedListError(val itemError: ItemEmpty) : FeedItem()
