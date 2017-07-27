package org.andrejs.cassandra.answers

import org.apache.cassandra.transport.messages.ResultMessage

class VoidAnswer : Answer {
    override fun get(): ResultMessage = ResultMessage.Void()
}