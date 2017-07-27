package org.andrejs.cassandra;

import com.datastax.driver.core.Cluster
import org.andrejs.cassandra.conditions.Condition
import org.andrejs.cassandra.conditions.InsertCondition
import org.andrejs.cassandra.conditions.SelectCondition
import org.apache.cassandra.db.ConsistencyLevel.ONE
import org.apache.cassandra.db.WriteType.SIMPLE
import org.apache.cassandra.exceptions.ReadTimeoutException
import org.apache.cassandra.exceptions.WriteTimeoutException
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.cassandraunit.utils.EmbeddedCassandraServerHelper.*
import org.junit.rules.ExternalResource

/**
 *
 */
class CassandraSpy(private val configYml : String = "cassandra-spy.yml") : ExternalResource() {

    private val tempDir by lazy { createTempDir("cassandra", "spy") }
    val cluster by lazy { Cluster.Builder().addContactPoints("localhost").withPort(getPort()).build() }
    val session by lazy { cluster.newSession() }

    /**
     * Start embedded cassandra with data in a temporary directory.
     * Does not start if it is already running.
     **/
    override public fun before() {
        super.before()
        System.setProperty("cassandra.storagedir", tempDir.absolutePath)
        System.setProperty("cassandra.custom_query_handler_class", QuerySpy::class.java.name)

        startEmbeddedCassandra(configYml, tempDir.absolutePath)
    }

    /** Remove all temporary data. Note: does not stop the embedded Cassandra **/
    override public fun after() {
        super.after()
        resetSpy()
        cleanEmbeddedCassandra()
        tempDir.deleteRecursively()
    }

    /** Embedded Cassandra host **/
    fun getHost(): String = EmbeddedCassandraServerHelper.getHost()

    /** Embedded Cassandra port **/
    fun getPort(): Int = getNativeTransportPort()

    /** Reset all priming **/
    fun resetSpy() = QuerySpy.reset()

    /**
     * Prime Cassandra spy.
     * <p>
     * Example:
     * <p>
     * <pre class="code"><code class="java">
     * when(inserts("users)).willThrow(writeTimeout());
     * </code></pre>
     *
     **/
    fun `when`(condition: Condition): Stubbing {
        return Stubbing(condition)
    }

    companion object {
        @JvmStatic fun inserts(table: String, vararg values: Any) = InsertCondition(table, *values)
        @JvmStatic fun selects(table: String) = SelectCondition(table)
        @JvmStatic fun readTimeout() = ReadTimeoutException(ONE, 0, 0, false)
        @JvmStatic fun writeTimeout() = WriteTimeoutException(SIMPLE, ONE, 0, 0)
    }
}
