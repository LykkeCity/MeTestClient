package com.lykke.me.test.client.service

import com.lykke.me.test.client.entity.TestMethodEntity

interface TestsFinderService {
    fun getTestMethodsByTestNames(names: Set<String>): List<TestMethodEntity>
    fun getTestMethodsByGroupNames(groupNames: Set<String>): List<TestMethodEntity>
    fun getAllTestMethods(): List<TestMethodEntity>
}