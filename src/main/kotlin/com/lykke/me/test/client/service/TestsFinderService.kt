package com.lykke.me.test.client.service

import java.lang.reflect.Method

interface TestsFinderService {
    fun getTestMethods(names: Set<String>): List<Method>
    fun getAllTestMethods(): List<Method>
    fun getTestNames(): Set<String>
}