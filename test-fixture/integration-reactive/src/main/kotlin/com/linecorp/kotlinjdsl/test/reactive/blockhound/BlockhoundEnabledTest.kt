package com.linecorp.kotlinjdsl.test.reactive.blockhound

import com.linecorp.kotlinjdsl.test.reactive.blockingDetect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.blockhound.BlockingOperationError

@Suppress("FunctionName")
class BlockhoundEnabledTest : WithAssertions {
    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    fun blockhoundCoroutineWorks() {
        assertThatThrownBy { blockingDetect { Thread.sleep(0) } }
            .isInstanceOf(BlockingOperationError::class.java)
    }

    @Test
    fun `blockhound does not detect any coroutine suspend method actions that are not blocked`() {
        assertDoesNotThrow {
            blockingDetect {
                delay(1)
                println("1234")
            }
        }
    }

    @Test
    fun `blockhound does not even detect blocking behavior when executed on an IO thread in a coroutine suspend method`() {
        assertDoesNotThrow {
            blockingDetect(Dispatchers.IO) { Thread.sleep(0) }
        }
    }
}
