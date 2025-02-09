package com.plcoding.core.connectivity.data

import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.plcoding.core.connectivity.domain.DeviceNode
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WearNodeDiscoveryTest {

    private lateinit var capabilityClient: CapabilityClient
    private val listeners = mutableSetOf<CapabilityClient.OnCapabilityChangedListener>()

    @Before
    fun setup() {
        capabilityClient = mockk(relaxed = true)

        every { capabilityClient.addListener(any(), any()) } answers {
            val listener = firstArg<CapabilityClient.OnCapabilityChangedListener>()
            listeners.add(listener)
            Tasks.forResult(null)
        }

        every { capabilityClient.removeListener(any()) } answers {
            val listener = firstArg<CapabilityClient.OnCapabilityChangedListener>()
            listeners.remove(listener)
            Tasks.forResult(null)
        }
    }

    @Test
    fun `removeListener fails when using lambda`() = runBlocking {
        val receivedUpdates = mutableListOf<Set<DeviceNode>>()

        // The listener is a Kotlin function type (CapabilityInfo) -> Unit,
        // NOT an instance of CapabilityClient.OnCapabilityChangedListener.
        val listenerLambda: (CapabilityInfo) -> Unit = { info ->
            receivedUpdates.add(info.nodes.map { it.toDeviceNode() }.toSet())
        }

        // When passing listenerLambda to addListener(),
        // Kotlin will box it into an anonymous class.
        capabilityClient.addListener(listenerLambda, "runique_wear_app")

        // When calling removeListener(), it expects the exact same instance.
        // However, since listenerLambda was boxed into a new anonymous class,
        // removeListener does NOT remove the original listener.
        capabilityClient.removeListener(listenerLambda)

        // Simulate an event - the listener should have been removed,
        // but it still gets invoked.
        val fakeCapabilityInfo = mockk<CapabilityInfo>(relaxed = true)
        listeners.forEach { it.onCapabilityChanged(fakeCapabilityInfo) }

        assertEquals(
            "Listener should have been removed but was still triggered.",
            0,
            receivedUpdates.size
        )
    }

    @Test
    fun `removeListener succeeds when using explicit listener`() = runBlocking {
        val receivedUpdates = mutableListOf<Set<DeviceNode>>()

        // The listener is explicitly declared as an instance
        // of OnCapabilityChangedListener.
        val listener = CapabilityClient.OnCapabilityChangedListener { info ->
            receivedUpdates.add(info.nodes.map { it.toDeviceNode() }.toSet())
        }

        // This ensures the exact same instance is used for
        // both addListener and removeListener.
        capabilityClient.addListener(listener, "runique_wear_app")
        capabilityClient.removeListener(listener)

        // Simulate an event - the listener should NOT be triggered.
        val fakeCapabilityInfo = mockk<CapabilityInfo>(relaxed = true)
        listeners.forEach { it.onCapabilityChanged(fakeCapabilityInfo) }

        assertEquals(
            "Listener was correctly removed and did not receive updates.",
            0,
            receivedUpdates.size
        )
    }
}
