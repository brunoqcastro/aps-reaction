package com.reaction.repository

import com.reaction.data.ApiClient
import com.reaction.data.ConfigResponse
import com.reaction.data.ReactionRecord
import com.reaction.data.UpdateConfigRequest

class ReactionRepository {
    private val api = ApiClient.api

    suspend fun getConfig(): ConfigResponse = api.getConfig()

    suspend fun updateConfig(minMs: Int, maxMs: Int): ConfigResponse =
        api.updateConfig(UpdateConfigRequest(minMs, maxMs))

    suspend fun getTop10(): List<ReactionRecord> = api.getTop10()

    suspend fun getReactionById(id: Int): ReactionRecord = api.getReactionById(id)
    suspend fun getLastReaction(quantity: Int): List<ReactionRecord> = api.getLastReaction(quantity)
}
