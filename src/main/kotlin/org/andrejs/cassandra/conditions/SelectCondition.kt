package org.andrejs.cassandra.conditions

import org.apache.cassandra.cql3.QueryOptions
import org.apache.cassandra.cql3.statements.ParsedStatement
import org.apache.cassandra.cql3.statements.SelectStatement
import org.apache.cassandra.service.QueryState

class SelectCondition(val table: String) : Condition {

    override fun applies(state: QueryState, options: QueryOptions, prepared: ParsedStatement.Prepared): Boolean {
        val statement = prepared.statement
        return if(statement is SelectStatement) {
            table == statement.columnFamily()
        } else {
            false
        }
    }

}