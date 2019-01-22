package com.lykke.me.test.client.web.dto

class TestSessionsDto(val sessionId: String,
                      val progress: Double,
                      val alreadyRunnedTests: Set<String>,
                      val testsToBeRunned: Set<String>,
                      val currentlyRunningTest: String)