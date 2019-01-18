package com.lykke.me.test.client.web.dto

import com.lykke.me.test.client.service.RunTestsPolicy

class RunTestsRequest (val testNames: Set<String>?,
                       val runTestsPolicy: RunTestsPolicy?)