package org.andrejs.cassandra.conditions

import org.andrejs.cassandra.Condition
import org.apache.cassandra.cql3.CQLStatement
import org.apache.cassandra.cql3.QueryOptions
import org.apache.cassandra.cql3.statements.SelectStatement
import org.apache.cassandra.service.QueryState

class SelectCondition : Condition {
    override fun applies(statement: CQLStatement, state: QueryState, options: QueryOptions): Boolean {
        return statement is SelectStatement
    }
}