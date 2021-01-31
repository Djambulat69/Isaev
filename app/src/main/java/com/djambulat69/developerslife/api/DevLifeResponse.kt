package com.djambulat69.developerslife.api

data class DevLifeResponse(
    val result: List<DevLifePost>,
    val totalCount: Int
)