package com.linecorp.kotlinjdsl.test.reactive

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

fun <T> blockingDetect(context: CoroutineContext = Dispatchers.Default, block: suspend () -> T): T = runBlocking(context) { block() }
