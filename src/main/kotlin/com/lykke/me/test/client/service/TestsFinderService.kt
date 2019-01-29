package com.lykke.me.test.client.service

import com.lykke.me.test.client.entity.TestMethodEntity

interface TestsFinderService {
    fun getTestMethods(names: Set<String>): List<TestMethodEntity>
    fun getAllTestMethods(): List<TestMethodEntity>
    fun getTestNames(): Set<String>
}