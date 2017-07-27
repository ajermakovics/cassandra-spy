package org.andrejs.cassandra.conditions

import org.apache.cassandra.cql3.ColumnSpecification
import org.apache.cassandra.cql3.QueryOptions
import org.apache.cassandra.cql3.statements.ParsedStatement
import org.apache.cassandra.cql3.statements.UpdateStatement
import org.apache.cassandra.db.marshal.AbstractType
import org.apache.cassandra.service.QueryState
import java.nio.ByteBuffer

class InsertCondition(val table : String, vararg val expectedValues: Any) : Condition {

    override fun applies(state: QueryState, options: QueryOptions, prepared: ParsedStatement.Prepared): Boolean {
        val statement = prepared.statement

        return if(statement is UpdateStatement) {
            table == statement.columnFamily() && valuesMatch(prepared.boundNames, options.values)
        } else {
            false
        }
    }

    private fun valuesMatch(columns: List<ColumnSpecification>, insertedValues: List<ByteBuffer>): Boolean {
        if(expectedValues.isEmpty())
            return true
        if(expectedValues.size != columns.size)
            return false

        val mismatches = expectedValues.filterIndexed { i, expectedValue ->
            ! equal(expectedValue, insertedValues[i], columns[i])
        }

        return mismatches.isEmpty()
    }

    private fun equal(expectedValue: Any, insertedValueBuffer: ByteBuffer, column: ColumnSpecification): Boolean {
        val columnType = column.type as AbstractType<Any>
        val expectedValueBuffer = columnType.decompose(expectedValue)
        return expectedValueBuffer == insertedValueBuffer
    }
}