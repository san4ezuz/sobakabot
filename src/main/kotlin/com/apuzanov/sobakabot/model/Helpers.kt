package com.apuzanov.sobakabot.model

data class GetOrCreateResult<T : Any>(val value: T, val created: Boolean)
