package com.lykke.me.test.client

import com.lykke.me.test.client.incoming.response.Response

interface MeResponseSubscriber {
    fun notify(response: Response)
}