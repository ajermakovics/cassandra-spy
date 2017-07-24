package org.andrejs.cassandra

import org.apache.cassandra.transport.messages.ResultMessage

class ThrowAnswer(val ex: Throwable): Answer {

    override fun get(): ResultMessage {
        throw ex
    }
}