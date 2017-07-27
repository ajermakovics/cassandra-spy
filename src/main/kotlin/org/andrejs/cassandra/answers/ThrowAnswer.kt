package org.andrejs.cassandra.answers

class ThrowAnswer(val ex: Throwable): Answer {

    override fun get(): org.apache.cassandra.transport.messages.ResultMessage {
        throw ex
    }
}