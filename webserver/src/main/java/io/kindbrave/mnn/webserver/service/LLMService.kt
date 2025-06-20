// Created by ruoyi.sjd on 2024/12/25.
// Copyright (c) 2024 Alibaba Group Holding Limited All rights reserved.
// Created by KindBrave on 2025/03/26.
package io.kindbrave.mnn.webserver.service

import android.text.TextUtils
import com.alibaba.mls.api.ModelItem
import io.kindbrave.mnn.webserver.annotation.LogAfter
import io.kindbrave.mnn.server.engine.AsrSession
import io.kindbrave.mnn.server.engine.ChatSession
import io.kindbrave.mnn.server.engine.EmbeddingSession
import io.kindbrave.mnn.server.engine.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LLMService @Inject constructor() {
    private val chatSessionMap = mutableMapOf<String, ChatSession>()
    private val embeddingSessionMap = mutableMapOf<String, EmbeddingSession>()
    private val asrSessionMap = mutableMapOf<String, AsrSession>()
    private val _loadedModelsState: MutableStateFlow<MutableMap<String, ModelItem>> = MutableStateFlow(mutableMapOf<String, ModelItem>())
    val loadedModelsState: StateFlow<Map<String, ModelItem>> = _loadedModelsState

    @LogAfter("")
    suspend fun createChatSession(
        modelId: String,
        modelDir: String,
        sessionId: String,
        modelItem: ModelItem,
    ): ChatSession {
        var finalSessionId = sessionId
        if (TextUtils.isEmpty(finalSessionId)) {
            finalSessionId = System.currentTimeMillis().toString()
        }

        val session = ChatSession(
            modelId = modelId,
            sessionId = finalSessionId,
            configPath = "$modelDir/config.json"
        )

        session.load()

        chatSessionMap[modelId] = session
        _loadedModelsState.update { currentMap ->
            currentMap.toMutableMap().apply {
                put(modelId, modelItem)
            }
        }
        return session
    }

    @LogAfter("")
    suspend fun createEmbeddingSession(
        modelId: String,
        modelDir: String,
        sessionId: String,
        modelItem: ModelItem,
    ): EmbeddingSession {
        var finalSessionId = sessionId
        if (TextUtils.isEmpty(finalSessionId)) {
            finalSessionId = System.currentTimeMillis().toString()
        }

        val session = EmbeddingSession(
            modelId = modelId,
            sessionId = finalSessionId,
            configPath = "$modelDir/config.json",
        )
        session.load()

        embeddingSessionMap[modelId] = session
        _loadedModelsState.update { currentMap ->
            currentMap.toMutableMap().apply {
                put(modelId, modelItem)
            }
        }
        return session
    }

    @LogAfter("")
    suspend fun createAsrSession(
        modelId: String,
        modelDir: String,
        sessionId: String,
        modelItem: ModelItem,
    ): AsrSession {
        var finalSessionId = sessionId
        if (TextUtils.isEmpty(finalSessionId)) {
            finalSessionId = System.currentTimeMillis().toString()
        }

        val session = AsrSession(
            modelId = modelId,
            sessionId = finalSessionId,
            configPath = "$modelDir/config.json",
        )
        session.load()

        asrSessionMap[modelId] = session
        _loadedModelsState.update { currentMap ->
            currentMap.toMutableMap().apply {
                put(modelId, modelItem)
            }
        }
        return session
    }

    fun getChatSession(modelId: String): ChatSession? {
        return chatSessionMap[modelId]
    }

    fun getEmbeddingSession(modelId: String): EmbeddingSession? {
        return embeddingSessionMap[modelId]
    }

    fun getAsrSession(modelId: String): AsrSession? {
        return asrSessionMap[modelId]
    }

    suspend fun removeChatSession(modelId: String) {
        chatSessionMap[modelId]?.release()
        chatSessionMap.remove(modelId)
        _loadedModelsState.update { currentMap ->
            currentMap.toMutableMap().apply {
                remove(modelId)
            }
        }
    }

    suspend fun removeEmbeddingSession(modelId: String) {
        embeddingSessionMap[modelId]?.release()
        embeddingSessionMap.remove(modelId)
        _loadedModelsState.update { currentMap ->
            currentMap.toMutableMap().apply {
                remove(modelId)
            }
        }
    }

    suspend fun removeAsrSession(modelId: String) {
        asrSessionMap[modelId]?.release()
        asrSessionMap.remove(modelId)
        _loadedModelsState.update { currentMap ->
            currentMap.toMutableMap().apply {
                remove(modelId)
            }
        }
    }

    fun getAllSessions(): List<Session> {
        return chatSessionMap.values.toList() + embeddingSessionMap.values.toList() + asrSessionMap.values.toList()
    }

    fun getAllChatSessions(): List<ChatSession> {
        return chatSessionMap.values.toList()
    }

    fun getAllEmbeddingSessions(): List<EmbeddingSession> {
        return embeddingSessionMap.values.toList()
    }

    fun getAllAsrSessions(): List<AsrSession> {
        return asrSessionMap.values.toList()
    }

    suspend fun releaseAllSessions() {
        chatSessionMap.values.forEach { it.release() }
        chatSessionMap.clear()
        embeddingSessionMap.values.forEach { it.release() }
        embeddingSessionMap.clear()
        asrSessionMap.values.forEach { it.release() }
        asrSessionMap.clear()
        _loadedModelsState.emit(mutableMapOf())
    }

    fun isModelLoaded(modelId: String): Boolean {
        return chatSessionMap.containsKey(modelId) || embeddingSessionMap.containsKey(modelId) || asrSessionMap.containsKey(modelId)
    }
}