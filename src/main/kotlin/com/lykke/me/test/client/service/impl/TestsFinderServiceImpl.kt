package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.service.TestsFinderService
import com.lykke.me.test.client.tests.MeTest
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import java.lang.reflect.Modifier
import javax.annotation.PostConstruct
import kotlin.collections.ArrayList
import com.lykke.me.test.client.entity.TestMethodEntity
import com.lykke.me.test.client.tests.TestGroup
import java.lang.reflect.Method

@Component
class TestsFinderServiceImpl : TestsFinderService {
    companion object {
        private val TEST_PACKAGE = "com.lykke.me.test.client.tests"
    }

    private lateinit var testMethods: List<TestMethodEntity>

    @PostConstruct
    fun init() {
        testMethods = getTestMethods()
    }

    override fun getTestMethodsByTestNames(names: Set<String>): List<TestMethodEntity> {
        return testMethods.filter { names.contains(it.method.name) }
    }

    override fun getTestMethodsByGroupNames(groupNames: Set<String>): List<TestMethodEntity> {
        return testMethods.filter { groupNames.contains(it.testGroup) }
    }

    override fun getAllTestMethods(): List<TestMethodEntity> {
        return testMethods
    }

    private fun getTestMethods(): List<TestMethodEntity> {
        val reflections = Reflections(TEST_PACKAGE, MethodAnnotationsScanner(),
                TypeAnnotationsScanner(),
                SubTypesScanner())
        val result = ArrayList<TestMethodEntity>()

        result.addAll(reflections.getMethodsAnnotatedWith(MeTest::class.java).map {
            val runCount = it.getAnnotation(MeTest::class.java).repeat
            TestMethodEntity(getTestGroup(it), runCount, it)
        })

        val testClasses = reflections.getTypesAnnotatedWith(MeTest::class.java)

        if (!CollectionUtils.isEmpty(testClasses)) {
            result.addAll(getTestMethodsFromTestClasses(testClasses))
        }

        return result
    }

    private fun getTestMethodsFromTestClasses(testClasses: Set<Class<*>>): List<TestMethodEntity> {
        val result = ArrayList<TestMethodEntity>()

        testClasses.forEach { clazz -> val methods = clazz.declaredMethods.filter { method -> Modifier.isPublic(method.modifiers) && !method.name.contains("$") }
            val runCount = clazz.getAnnotation(MeTest::class.java).repeat
            result.addAll(methods.map {TestMethodEntity(getTestGroup(it), runCount, it)})
        }

        return result
    }

    private fun getTestGroup(method: Method): String {
        val annotation = method.declaringClass.getAnnotation(TestGroup::class.java) ?: return ""
         return annotation.name
    }
}