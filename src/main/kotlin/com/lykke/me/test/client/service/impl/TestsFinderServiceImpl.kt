package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.service.TestsFinderService
import com.lykke.me.test.client.tests.MeTest
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import javax.annotation.PostConstruct
import kotlin.collections.ArrayList

@Component
class TestsFinderServiceImpl : TestsFinderService {
    companion object {
        private val TEST_PACKAGE = "com.lykke.me.test.client.tests"
    }

    private lateinit var testMethods: List<Method>

    @PostConstruct
    fun init() {
        testMethods = getTestMethods()
    }

    override fun getTestMethods(names: Set<String>): List<Method> {
        return testMethods.filter { names.contains(it.name) }
    }

    override fun getAllTestMethods(): List<Method> {
        return testMethods
    }

    override fun getTestNames(): Set<String> {
        return testMethods.map { it.name }.toSet()
    }

    private fun getTestMethods(): List<Method> {
        val reflections = Reflections(TEST_PACKAGE, MethodAnnotationsScanner(),
                TypeAnnotationsScanner(),
                SubTypesScanner())
        val result = ArrayList<Method>()

        result.addAll(reflections.getMethodsAnnotatedWith(MeTest::class.java))
        val testClasses = reflections.getTypesAnnotatedWith(MeTest::class.java)
        if (!CollectionUtils.isEmpty(testClasses)) {
            result.addAll(getTestMethodsFromTestClasses(testClasses))
        }

        return result
    }

    private fun getTestMethodsFromTestClasses(testClasses: Set<Class<*>>): List<Method> {
        val result = ArrayList<Method>()
        testClasses.forEach { classes -> result.addAll(classes.declaredMethods.filter { method -> Modifier.isPublic(method.modifiers) && !method.name.contains("$") }) }
        return result
    }
}