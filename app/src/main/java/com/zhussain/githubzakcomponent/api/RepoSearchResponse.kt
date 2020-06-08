package com.zhussain.githubzakcomponent.api

import com.google.gson.annotations.SerializedName
import com.zhussain.githubzakcomponent.dataclass.Repo

/**
 * Simple object to hold repo search responses. This is different from the Entity in the database
 * because we are keeping a search result in 1 row and denormalizing list of results into a single
 * column.
 */

data class RepoSearchResponse(
    @field:SerializedName("total_count")
    val total: Int,
    @field:SerializedName("items")
    val items: List<Repo>
){
    var nextPage : Int? = null
}