package org.andrejs.cassandra;

import com.datastax.driver.core.Cluster
import org.andrejs.cassandra.conditions.InsertCondition
import org.andrejs.cassandra.conditions.SelectCondition
import org.apache.cassandra.db.ConsistencyLevel.ONE
import org.apache.cassandra.db.WriteType.SIMPLE
import org.apache.cassandra.exceptions.ReadTimeoutException
import org.apache.cassandra.exceptions.WriteTimeoutException
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.cassandraunit.utils.EmbeddedCassandraServerHelper.*
import org.junit.rules.ExternalResource

class CassandraSpy(private val configYml : String = "cassandra-spy.yml") : ExternalResource() {

    private val tempDir by lazy { createTempDir("cassandra", "spy") }
    val cluster by lazy { Cluster.Builder().addContactPoints("localhost").withPort(getPort()).build() }
    val session by lazy { cluster.newSession() }

    override public fun before() {
        super.before()
        System.setProperty("cassandra.storagedir", tempDir.absolutePath)
        System.setProperty("cassandra.custom_query_handler_class", QuerySpy::class.java.name)

        startEmbeddedCassandra(configYml, tempDir.absolutePath)
    }

    override public fun after() {
        super.after()
        resetSpy()
        cleanEmbeddedCassandra()
        tempDir.deleteRecursively()
    }

    fun getHost(): String = EmbeddedCassandraServerHelper.getHost()

    fun getPort(): Int = getNativeTransportPort()

    fun resetSpy() = QuerySpy.reset()

    fun `when`(condition: Condition): Stubbing {
        return Stubbing(condition)
    }

    companion object {
        @JvmStatic fun inserts(table: String): Condition = InsertCondition(table)
        @JvmStatic fun selects(table: String): Condition = SelectCondition(table)
        @JvmStatic fun readTimeout() = ReadTimeoutException(ONE, 0, 0, false)
        @JvmStatic fun writeTimeout() = WriteTimeoutException(SIMPLE, ONE, 0, 0)
    }
}
