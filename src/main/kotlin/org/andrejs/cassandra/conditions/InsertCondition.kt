package org.andrejs.cassandra.conditions

import org.andrejs.cassandra.Condition
import org.apache.cassandra.cql3.CQLStatement
import org.apache.cassandra.cql3.QueryOptions
import org.apache.cassandra.cql3.statements.UpdateStatement
import org.apache.cassandra.service.QueryState

class InsertCondition : Condition {
    override fun applies(statement: CQLStatement, state: QueryState, options: QueryOptions): Boolean {
        return statement is UpdateStatement
    }
}