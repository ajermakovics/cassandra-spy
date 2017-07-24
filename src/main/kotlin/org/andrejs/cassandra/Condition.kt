package org.andrejs.cassandra

import org.apache.cassandra.cql3.CQLStatement
import org.apache.cassandra.cql3.QueryOptions
import org.apache.cassandra.service.QueryState

interface Condition {
    fun applies(statement: CQLStatement, state: QueryState, options: QueryOptions) : Boolean
}