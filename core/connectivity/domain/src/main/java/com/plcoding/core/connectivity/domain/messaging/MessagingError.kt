package com.plcoding.core.connectivity.domain.messaging

import com.plcoding.core.domain.util.Error

enum class MessagingError: Error {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}