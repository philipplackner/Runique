package com.plcoding.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}