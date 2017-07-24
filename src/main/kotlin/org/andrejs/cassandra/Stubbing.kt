package org.andrejs.cassandra

class Stubbing(val given: Condition) {
    fun willAnswer(answer: Answer) {
        QuerySpy.answers[given] = answer
    }
    fun willThrow(ex: Throwable) {
        willAnswer(ThrowAnswer(ex))
    }
}