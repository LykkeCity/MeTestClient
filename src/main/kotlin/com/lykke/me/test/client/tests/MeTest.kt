package com.lykke.me.test.client.tests

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MeTest(val repeat: Int = 1)