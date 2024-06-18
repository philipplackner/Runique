package com.plcoding.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class UserDataValidatorTest {

    @Test
    fun testAddingTwoNumbers() {
        assertThat(2 + 2).isEqualTo(4)
    }
}