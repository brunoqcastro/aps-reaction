package com.reaction.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

data class ConfigResponse(
    val min_ms: Int,
    val max_ms: Int
)

data class UpdateConfigRequest(
    val min_ms: Int,
    val max_ms: Int
)

data class ReactionRecord(
    val id: Int,
    val reaction_time: Int,
    val created_at: String
)

interface ApiService {
    @GET("/config")
    suspend fun getConfig(): ConfigResponse

    @PUT("/config")
    suspend fun updateConfig(
        @Body body: UpdateConfigRequest
    ): ConfigResponse

    @GET("/reactions/top10")
    suspend fun getTop10(): List<ReactionRecord>

    @GET("/reactions/{id}")
    suspend fun getReactionById(@Path("id") id: Int): ReactionRecord
}
