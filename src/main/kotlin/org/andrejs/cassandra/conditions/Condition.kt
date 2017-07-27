package org.andrejs.cassandra.conditions

import org.apache.cassandra.cql3.QueryOptions
import org.apache.cassandra.cql3.statements.ParsedStatement
import org.apache.cassandra.service.QueryState

interface Condition {
    fun applies(state: QueryState, options: QueryOptions, prepared: ParsedStatement.Prepared) : Boolean
}