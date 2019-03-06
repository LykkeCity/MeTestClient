package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.incoming.response.Response
import com.lykke.me.test.client.service.TestMetricService
import com.lykke.utils.notification.Listener
import com.lykke.utils.notification.Subscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct

@Service
class TestMetricServiceImpl: TestMetricService {
    @Autowired
    private lateinit var meResponseListenerForSyncInteraction: Listener<Response>

    @Autowired
    private lateinit var meResponseListener: Listener<Response>

    private val currentMessageCount = AtomicLong(0)

    @Volatile
    private var previousMessageCount = 0L

    @Volatile
    private var curThroughput = 0L


    @PostConstruct
    private fun init() {
        meResponseListenerForSyncInteraction.subscribe(object: Subscriber<Response> {
            override fun notify(message: Response) {
                currentMessageCount.incrementAndGet()
            }
        })

        meResponseListener.subscribe(object: Subscriber<Response> {
            override fun notify(message: Response) {
                currentMessageCount.incrementAndGet()
            }
        })
    }

    override fun getCurrentThroughput(): Long {
        return curThroughput
    }

    @Scheduled(fixedRate = 1000)
    private fun calculateCurrentThroughput() {
        val curMessageCount =  currentMessageCount.get()
        val prevMessageCount = previousMessageCount

        previousMessageCount = curMessageCount

        curThroughput = curMessageCount - prevMessageCount
    }
}