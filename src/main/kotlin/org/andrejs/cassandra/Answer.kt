package org.andrejs.cassandra

import org.apache.cassandra.transport.messages.ResultMessage

interface Answer {
    fun get() : ResultMessage
}