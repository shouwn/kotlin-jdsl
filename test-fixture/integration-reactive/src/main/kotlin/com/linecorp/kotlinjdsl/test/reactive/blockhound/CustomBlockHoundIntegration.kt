package com.linecorp.kotlinjdsl.test.reactive.blockhound

import com.linecorp.kotlinjdsl.querydsl.hint.SqlReactiveQueryHintClauseProvider
import io.vertx.core.impl.VertxThread
import io.vertx.core.impl.WorkerPool
import io.vertx.jdbcclient.impl.AgroalCPDataSourceProvider
import org.springframework.data.jpa.repository.query.QueryUtils
import reactor.blockhound.BlockHound.Builder
import reactor.blockhound.BlockingMethod
import reactor.blockhound.BlockingOperationError
import reactor.blockhound.integration.BlockHoundIntegration
import java.util.*

class CustomBlockHoundIntegration : BlockHoundIntegration {
    override fun applyTo(builder: Builder) {
        builder.allowBlockingCallsInside(
            "io.vertx.jdbcclient.impl.AgroalCPDataSourceProvider",
            AgroalCPDataSourceProvider::getDataSource.name
        )
        builder.allowBlockingCallsInside(SqlReactiveQueryHintClauseProvider::class.qualifiedName, "provide")
        builder.allowBlockingCallsInside(QueryUtils::class.qualifiedName, "toOrders")
        builder.allowBlockingCallsInside(WorkerPool::class.qualifiedName, "close")
        builder.blockingMethodCallback { method: BlockingMethod ->
            val currentThread = Thread.currentThread()
            if (currentThread is VertxThread && currentThread.isWorker) {
                return@blockingMethodCallback
            }

            val error: Error = BlockingOperationError(method)

            // Strip BlockHound's internal noisy frames from the stacktrace to not mislead the users
            val stackTrace = error.stackTrace
            val length = stackTrace.size
            for (i in 0 until length) {
                val stackTraceElement = stackTrace[i]
                if ("reactor.blockhound.BlockHoundRuntime" != stackTraceElement.className) {
                    continue
                }
                if ("checkBlocking" == stackTraceElement.methodName) {
                    if (i + 1 < length) {
                        error.stackTrace = Arrays.copyOfRange(
                            stackTrace,
                            i + 1,
                            length
                        )
                    }
                    break
                }
            }
            throw error
        }
    }
}
