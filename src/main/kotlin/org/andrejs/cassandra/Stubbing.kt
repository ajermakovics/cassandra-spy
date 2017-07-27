package org.andrejs.cassandra

import org.andrejs.cassandra.answers.Answer
import org.andrejs.cassandra.answers.ThrowAnswer
import org.andrejs.cassandra.conditions.Condition

class Stubbing(val condition: Condition) {
    fun willAnswer(answer: Answer) {
        QuerySpy.answers[condition] = answer
    }

    fun willThrow(ex: Throwable) {
        willAnswer(ThrowAnswer(ex))
    }
}