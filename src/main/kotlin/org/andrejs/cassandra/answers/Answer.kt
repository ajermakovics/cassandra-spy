package org.andrejs.cassandra.answers

import org.apache.cassandra.transport.messages.ResultMessage

interface Answer {
    fun get() : ResultMessage
}