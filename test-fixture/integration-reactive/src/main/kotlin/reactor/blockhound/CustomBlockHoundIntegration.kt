package reactor.blockhound

import com.linecorp.kotlinjdsl.querydsl.hint.SqlReactiveQueryHintClauseProvider
import io.vertx.core.impl.VertxThread
import io.vertx.core.impl.WorkerPool
import io.vertx.jdbcclient.impl.AgroalCPDataSourceProvider
import org.springframework.data.jpa.repository.query.QueryUtils
import reactor.blockhound.BlockHound.Builder
import reactor.blockhound.integration.BlockHoundIntegration

class CustomBlockHoundIntegration : BlockHoundIntegration {
    override fun applyTo(builder: Builder) {
        builder.allowBlockingCallsInside(
            AgroalCPDataSourceProvider::class.qualifiedName,
            AgroalCPDataSourceProvider::getDataSource.name
        )
        builder.allowBlockingCallsInside(SqlReactiveQueryHintClauseProvider::class.qualifiedName, "provide")
        builder.allowBlockingCallsInside(QueryUtils::class.qualifiedName, QueryUtils::toOrders.name)
        builder.allowBlockingCallsInside(WorkerPool::class.qualifiedName, "close")
        builder.blockingMethodCallback { method: BlockingMethod ->
            val currentThread = Thread.currentThread()
            if (currentThread is VertxThread && currentThread.isWorker) {
                return@blockingMethodCallback
            }

            val error: Error = BlockingOperationError(method)

            // Strip BlockHound's internal noisy frames from the stacktrace to not mislead the users
            val length = error.stackTrace.size
            error.stackTrace
                .forEachIndexed { i, stackTraceElement ->
                    if (BlockHoundRuntime::class.qualifiedName != stackTraceElement.className) {
                        return@forEachIndexed
                    }
                    if (stackTraceElement.methodName == BlockHoundRuntime::checkBlocking.name) {
                        if (i + 1 < length) {
                            error.stackTrace = error.stackTrace.drop(i).toTypedArray()
                        }
                        throw error
                    }
                }
        }
    }
    inline fun <reified T : Any>T.logTag() = T::class.java.simpleName
}
