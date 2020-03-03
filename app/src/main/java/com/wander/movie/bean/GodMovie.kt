package com.wander.movie.bean

data class GodMovieItem(
    var id: String = "",
    var name: String = "",
    var time: String = "",
    var tort: Int
)

data class GodMovieList(var list: List<GodMovieItem>, var pageCount: Int)

data class GodMovieDetail(
    var content: String = "",
    var downloadurls: List<String>? = null,
    var id: String = "",
    var name: String = "",
    var time: String = "",
    var imgurls: List<String> = ArrayList()

)