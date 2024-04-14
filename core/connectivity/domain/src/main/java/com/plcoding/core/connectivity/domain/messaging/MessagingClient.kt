package com.plcoding.core.connectivity.domain.messaging

import com.plcoding.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface MessagingClient {
    fun connectToNode(nodeId: String): Flow<MessagingAction>
    suspend fun sendOrQueueAction(action: MessagingAction): EmptyResult<MessagingError>
}