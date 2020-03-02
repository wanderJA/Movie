package com.wander.movie.bean

data class GodMovieItem(
    var id: String = "",
    var name: String = "",
    var time: String = "",
    var tort: Int
)

data class GodMovieList(var list: List<GodMovieList>, var pageCount: Int)