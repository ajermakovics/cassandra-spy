package org.andrejs.cassandra

import org.apache.cassandra.cql3.*
import org.apache.cassandra.cql3.statements.BatchStatement
import org.apache.cassandra.cql3.statements.ParsedStatement
import org.apache.cassandra.service.QueryState
import org.apache.cassandra.transport.messages.ResultMessage
import org.apache.cassandra.utils.MD5Digest
import java.util.concurrent.ConcurrentHashMap

class QuerySpy : QueryHandler {

    companion object {
        val processor: QueryProcessor = QueryProcessor.instance
        val answers: MutableMap<Condition, Answer> = ConcurrentHashMap()

        fun reset() = answers.clear()

        fun findAnswer(statement: CQLStatement, state: QueryState, options: QueryOptions): Answer? {
            return answers.filterKeys { it.applies(statement, state, options) }.values.firstOrNull()
        }
    }

    override fun processPrepared(statement: CQLStatement, state: QueryState, options: QueryOptions): ResultMessage {
        val answer = findAnswer(statement, state, options)
        return answer?.get() ?: processor.processStatement(statement, state, options)
    }

    override fun process(query: String, state: QueryState, options: QueryOptions): ResultMessage {
        return processor.process(query, state, options)
    }

    override fun prepare(query: String, state: QueryState): ResultMessage.Prepared {
        return processor.prepare(query, state)
    }

    override fun getPrepared(id: MD5Digest): ParsedStatement.Prepared {
        return processor.getPrepared(id)
    }

    override fun processBatch(statement: BatchStatement, state: QueryState, options: BatchQueryOptions): ResultMessage {
        return processor.processBatch(statement, state, options)
    }

    override fun getPreparedForThrift(id: Int): ParsedStatement.Prepared {
        return processor.getPreparedForThrift(id)
    }
}