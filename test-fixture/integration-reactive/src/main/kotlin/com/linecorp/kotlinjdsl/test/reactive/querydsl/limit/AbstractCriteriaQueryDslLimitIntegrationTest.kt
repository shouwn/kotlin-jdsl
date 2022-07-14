package com.linecorp.kotlinjdsl.test.reactive.querydsl.limit

import com.linecorp.kotlinjdsl.listQuery
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.test.entity.order.Order
import com.linecorp.kotlinjdsl.test.reactive.CriteriaQueryDslIntegrationTest
import com.linecorp.kotlinjdsl.test.reactive.blockingDetect
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class AbstractCriteriaQueryDslLimitIntegrationTest<S> : CriteriaQueryDslIntegrationTest<S> {
    private val order1 = order { }
    private val order2 = order { }
    private val order3 = order { }

    @BeforeEach
    fun setUp(): Unit = blockingDetect {
        persistAll(order1, order2, order3)

    }

    @Test
    fun offset(): Unit = blockingDetect {
        // when
        val orderIds = withFactory { queryFactory ->
            queryFactory.listQuery<Long> {
                select(col(Order::id))
                from(entity(Order::class))
                orderBy(col(Order::id).asc())
                offset(1)
            }
        }

        // then
        assertThat(orderIds).isEqualTo(listOf(order1.id, order2.id, order3.id).sorted().drop(1))
    }

    @Test
    fun maxResults(): Unit = blockingDetect {
        // when
        val orderIds = withFactory { queryFactory ->
            queryFactory.listQuery<Long> {
                select(col(Order::id))
                from(entity(Order::class))
                orderBy(col(Order::id).asc())
                maxResults(1)
            }
        }

        // then
        assertThat(orderIds).containsOnly(listOf(order1.id, order2.id, order3.id).minOrNull())
    }

    @Test
    fun limit(): Unit = blockingDetect {
        // when
        val orderIds = withFactory { queryFactory ->
            queryFactory.listQuery<Long> {
                select(col(Order::id))
                from(entity(Order::class))
                orderBy(col(Order::id).asc())
                limit(1, 1)
            }
        }

        // then
        assertThat(orderIds).containsOnly(listOf(order1.id, order2.id, order3.id).sorted()[1])
    }
}
