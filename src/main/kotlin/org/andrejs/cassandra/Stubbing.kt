package org.andrejs.cassandra

class Stubbing(val condition: Condition) {
    fun willAnswer(answer: Answer) {
        QuerySpy.answers[condition] = answer
    }
    fun willThrow(ex: Throwable) {
        willAnswer(ThrowAnswer(ex))
    }
}