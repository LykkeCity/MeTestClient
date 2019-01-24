package com.lykke.me.test.client.socket

import java.util.concurrent.locks.ReentrantLock

class SendMessagesLatch(private val threshold: Int) {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private var responsesReceived = 0L

    fun await(currentlySendItemsCount: Long) {
        try {
            lock.lock()
            while (currentlySendItemsCount - responsesReceived >= threshold) {
                condition.await()
            }
        } finally {
            lock.unlock()
        }
    }

    fun incrementResponseCount() {
        try {
            lock.lock()
            responsesReceived++
            if (responsesReceived % 10 == 0L) {
                condition.signal()
            }
        } finally {
            lock.unlock()
        }
    }

    fun getResponsesReceived(): Long {
        return responsesReceived
    }
}