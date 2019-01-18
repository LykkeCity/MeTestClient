package com.lykke.me.test.client.entity

class TestSessionEntity(val sessionId: String,
                        var progress: Double,
                        var alreadyRunned: MutableSet<String>,
                        var testsToBeRun: MutableSet<String>,
                        var currentlyRunningTest: String?)