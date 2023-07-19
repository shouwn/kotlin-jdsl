package com.linecorp.kotlinjdsl.dsl.jpql.select

import com.linecorp.kotlinjdsl.dsl.jpql.AbstractJpqlDslTest
import com.linecorp.kotlinjdsl.querymodel.jpql.Paths
import com.linecorp.kotlinjdsl.querymodel.jpql.Queries
import com.linecorp.kotlinjdsl.querymodel.jpql.path.JoinType
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import org.junit.jupiter.api.Test

class FromDslTest : AbstractJpqlDslTest() {
    @Test
    fun `from entity`() {
        // when
        val select = testJpql {
            select(
                entity(TestTable1::class),
            ).from(
                entity(TestTable1::class),
            )
        }.toQuery()

        val actual: SelectQuery<TestTable1> = select // for type check

        // then
        val expected = Queries.select<TestTable1>(
            returnType = TestTable1::class,
            select = listOf(Paths.entity(TestTable1::class)),
            distinct = false,
            from = listOf(
                Paths.entity(TestTable1::class),
            ),
            where = null,
            groupBy = null,
            having = null,
            orderBy = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `from entity entity`() {
        // when
        val select = testJpql {
            select(
                entity(TestTable1::class),
            ).from(
                entity(TestTable1::class),
                entity(TestTable2::class),
            )
        }.toQuery()

        val actual: SelectQuery<TestTable1> = select // for type check

        // then
        val expected = Queries.select<TestTable1>(
            returnType = TestTable1::class,
            select = listOf(Paths.entity(TestTable1::class)),
            distinct = false,
            from = listOf(
                Paths.entity(TestTable1::class),
                Paths.entity(TestTable2::class),
            ),
            where = null,
            groupBy = null,
            having = null,
            orderBy = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `from collection entity entity`() {
        // when
        val select = testJpql {
            select(
                entity(TestTable1::class),
            ).from(
                listOf(
                    entity(TestTable1::class),
                    entity(TestTable2::class),
                ),
            )
        }.toQuery()

        val actual: SelectQuery<TestTable1> = select // for type check

        // then
        val expected = Queries.select<TestTable1>(
            returnType = TestTable1::class,
            select = listOf(Paths.entity(TestTable1::class)),
            distinct = false,
            from = listOf(
                Paths.entity(TestTable1::class),
                Paths.entity(TestTable2::class),
            ),
            where = null,
            groupBy = null,
            having = null,
            orderBy = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `from entity join path`() {
        // when
        val select = testJpql {
            select(
                entity(TestTable1::class),
            ).from(
                entity(TestTable1::class).join(path(TestTable1::table1)),
            )
        }.toQuery()

        val actual: SelectQuery<TestTable1> = select // for type check

        // then
        val expected = Queries.select<TestTable1>(
            returnType = TestTable1::class,
            select = listOf(Paths.entity(TestTable1::class)),
            distinct = false,
            from = listOf(
                Paths.join(
                    left = Paths.entity(TestTable1::class),
                    right = Paths.path(TestTable1::table1),
                    on = null,
                    joinType = JoinType.INNER,
                    fetch = false,
                ),
            ),
            where = null,
            groupBy = null,
            having = null,
            orderBy = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `from entity join nullable path`() {
        // when
        val select = testJpql {
            select(
                entity(TestTable1::class),
            ).from(
                entity(TestTable1::class).join(path(TestTable1::nullableTable1)),
            )
        }.toQuery()

        val actual: SelectQuery<TestTable1> = select // for type check

        // then
        val expected = Queries.select<TestTable1>(
            returnType = TestTable1::class,
            select = listOf(Paths.entity(TestTable1::class)),
            distinct = false,
            from = listOf(
                Paths.join(
                    left = Paths.entity(TestTable1::class),
                    right = Paths.path(TestTable1::nullableTable1),
                    on = null,
                    joinType = JoinType.INNER,
                    fetch = false,
                ),
            ),
            where = null,
            groupBy = null,
            having = null,
            orderBy = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `from entity entity join path`() {
        // when
        val select = testJpql {
            select(
                entity(TestTable1::class),
            ).from(
                entity(TestTable1::class),
                entity(TestTable2::class).join(path(TestTable2::table1)),
            )
        }.toQuery()

        val actual: SelectQuery<TestTable1> = select // for type check

        // then
        val expected = Queries.select<TestTable1>(
            returnType = TestTable1::class,
            select = listOf(Paths.entity(TestTable1::class)),
            distinct = false,
            from = listOf(
                Paths.entity(TestTable1::class),
                Paths.join(
                    left = Paths.entity(TestTable2::class),
                    right = Paths.path(TestTable2::table1),
                    on = null,
                    joinType = JoinType.INNER,
                    fetch = false,
                ),
            ),
            where = null,
            groupBy = null,
            having = null,
            orderBy = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `from entity entity join nullable path`() {
        // when
        val select = testJpql {
            select(
                entity(TestTable1::class),
            ).from(
                entity(TestTable1::class),
                entity(TestTable2::class).join(path(TestTable2::nullableTable1)),
            )
        }.toQuery()

        val actual: SelectQuery<TestTable1> = select // for type check

        // then
        val expected = Queries.select<TestTable1>(
            returnType = TestTable1::class,
            select = listOf(Paths.entity(TestTable1::class)),
            distinct = false,
            from = listOf(
                Paths.entity(TestTable1::class),
                Paths.join(
                    left = Paths.entity(TestTable2::class),
                    right = Paths.path(TestTable2::nullableTable1),
                    on = null,
                    joinType = JoinType.INNER,
                    fetch = false,
                ),
            ),
            where = null,
            groupBy = null,
            having = null,
            orderBy = null,
        )

        assertThat(actual).isEqualTo(expected)
    }

    private class TestTable1 {
        val table1: OtherTable = OtherTable()

        val nullableTable1: OtherTable? = null
    }

    private class TestTable2 {
        val table1: OtherTable = OtherTable()

        val nullableTable1: OtherTable? = null
    }

    private class OtherTable
}