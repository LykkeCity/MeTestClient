package com.lykke.me.test.client.socket

import java.util.concurrent.locks.ReentrantLock

class SendMessagesLatch(private val threshold: Int) {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private var responsesReceived = 0

    fun await(currentlySendItemsCount: Int) {
        try {
            lock.lock()
            while (true) {
                condition.await()
                if (currentlySendItemsCount - responsesReceived <= threshold) {
                    return
                }
            }
        } finally {
            lock.unlock()
        }
    }

    fun incrementResponseCount() {
        try {
            lock.lock()
            responsesReceived++
            if (responsesReceived % 10 == 0) {
                condition.signal()
            }
        } finally {
            lock.unlock()
        }
    }
}