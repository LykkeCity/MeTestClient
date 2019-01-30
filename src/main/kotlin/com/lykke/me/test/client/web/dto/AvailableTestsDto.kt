package com.lykke.me.test.client.web.dto

class AvailableTestsDto(val groups: List<TestGroup>)
class TestGroup(val groupName: String, val testNames: Set<String>)