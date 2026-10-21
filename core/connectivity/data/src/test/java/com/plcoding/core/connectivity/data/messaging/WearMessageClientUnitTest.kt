package com.plcoding.core.connectivity.data.messaging

import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.collections.forEach

class WearMessageClientListenerTest {

    private lateinit var messageClient: MessageClient
    private val listeners = mutableSetOf<MessageClient.OnMessageReceivedListener>()

    @Before
    fun setup() {
        messageClient = mockk(relaxed = true)

        every { messageClient.addListener(any()) } answers {
            val listener = firstArg<MessageClient.OnMessageReceivedListener>()
            listeners.add(listener)
            Tasks.forResult(null)
        }

        every { messageClient.removeListener(any()) } answers {
            val listener = firstArg<MessageClient.OnMessageReceivedListener>()
            listeners.remove(listener)
            Tasks.forResult(null)
        }
    }

    /**
     * This test demonstrates a bug in the removeListener() method.
     */
    @Test
    fun `removeListener fails when using lambda`() = runBlocking {
        val receivedMessages = mutableListOf<MessageEvent>()

        // The listener is a Kotlin function type (MessageEvent) -> Unit,
        // NOT an instance of MessageClient.OnMessageReceivedListener.
        val listenerLambda: (MessageEvent) -> Unit = { receivedMessages.add(it) }

        // When passing listenerLambda to addListener(),
        // Kotlin will box it into an anonymous class.
        messageClient.addListener(listenerLambda)

        // When calling removeListener(), it expects the exact same instance.
        // However, since listenerLambda was boxed into a new anonymous class,
        // removeListener does NOT remove the original listener.
        messageClient.removeListener(listenerLambda)

        // Simulate an event - the listener should have been removed,
        // but it still gets invoked.
        val fakeEvent = mockk<MessageEvent>(relaxed = true)
        listeners.forEach { it.onMessageReceived(fakeEvent) }

        assertEquals(
            "Listener should have been removed but was still triggered.",
            0,
            receivedMessages.size
        )
    }

    /**
     * This test demonstrates the correct way to remove a listener.
     */
    @Test
    fun `removeListener succeeds when using explicit listener`() = runBlocking {
        val receivedMessages = mutableListOf<MessageEvent>()

        // Correct way to create a listener using the expected interface
        val listener = MessageClient.OnMessageReceivedListener { receivedMessages.add(it) }

        // Add the listener
        messageClient.addListener(listener)

        // Remove it properly
        messageClient.removeListener(listener)

        // Simulate an event - should NOT be received
        val fakeEvent = mockk<MessageEvent>(relaxed = true)
        listeners.forEach { it.onMessageReceived(fakeEvent) }

        // This test passes this check
        assertEquals("Listener should have been removed!", 0, receivedMessages.size)
    }
}