package com.lykke.me.test.client.web.controllers

import com.lykke.me.test.client.service.MessageRatePolicy
import com.lykke.me.test.client.service.RunTestsPolicy
import com.lykke.me.test.client.service.TestMetricService
import com.lykke.me.test.client.service.TestsService
import com.lykke.me.test.client.web.dto.AvailableTestsDto
import com.lykke.me.test.client.web.dto.TestSessionsDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.CollectionUtils
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/tests")
@Api(description = "Endpoint that enables to run tests on target ME instance")
class TestsController {

    @Autowired
    private lateinit var testService: TestsService

    @Autowired
    private lateinit var testMetricService: TestMetricService

    @PostMapping
    @ApiOperation("Run tests on target ME instance, use this endpoint to start all tests or only selected tests")
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 500, message = "Internal server error occurred")
    )
    fun runTest(@RequestParam(required = false) testNames: HashSet<String>?,
                @RequestParam(required = false) messageDelayMs: Long?,
                @RequestParam(required = false) runTestsPolicy: RunTestsPolicy?,
                @RequestParam(required = false) messageRatePolicy: MessageRatePolicy?): String? {
        if (messageRatePolicy == MessageRatePolicy.MANUAL_MESSAGE_RATE &&
                messageDelayMs == null) {
            throw IllegalArgumentException("For manual message rate policy should be set 'messageDelayMs' parameter")
        }

        return if (CollectionUtils.isEmpty(testNames)) {
            testService.startAllTests(runTestsPolicy, messageRatePolicy, messageDelayMs)
        } else {
            testService.startTestsByNames(testNames!!, runTestsPolicy, messageRatePolicy, messageDelayMs)
        }
    }

    @PostMapping("groups")
    @ApiOperation("Run tests of given groups, use this endpoint to start all tests or only selected test groups")
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 500, message = "Internal server error occurred")
    )
    fun runTestGroup(@RequestParam(required = false) testGroups: HashSet<String>?,
                     @RequestParam(required = false) messageDelayMs: Long?,
                     @RequestParam(required = false) runTestsPolicy: RunTestsPolicy?,
                     @RequestParam(required = false) messageRatePolicy: MessageRatePolicy?): String? {
        if (messageRatePolicy == MessageRatePolicy.MANUAL_MESSAGE_RATE &&
                messageDelayMs == null) {
            throw IllegalArgumentException("For manual message rate policy should be set 'messageDelayMs' parameter")
        }

        return if (CollectionUtils.isEmpty(testGroups)) {
            testService.startAllTests(runTestsPolicy, messageRatePolicy, messageDelayMs)
        } else {
            testService.startTestsByGroups(testGroups!!,
                    runTestsPolicy, messageRatePolicy, messageDelayMs)
        }
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation("Get information for currently running test session")
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 500, message = "Internal server error occurred")
    )
    fun getTestSessions(): List<TestSessionsDto> {
        return testService.getTestSessions()
    }

    @GetMapping("available", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation("Get available test names")
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 500, message = "Internal server error occurred")
    )
    fun getAvailableTests(): AvailableTestsDto {
        return testService.getAllTests()
    }

    @GetMapping("throughput")
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 500, message = "Internal server error occurred")
    )
    @ApiOperation("Get current throughput")
    fun getThroughput(): Long {
        return testMetricService.getCurrentThroughput()
    }

    @DeleteMapping
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 400, message = "Session with provided session id does not exist"),
            ApiResponse(code = 500, message = "Internal server error occurred")
    )
    @ApiOperation("Stop test session by session id")
    fun stopTestSession(sessionId: String) {
        testService.stopTestSession(sessionId)
    }

    @DeleteMapping("all")
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 500, message = "Internal server error occurred")
    )
    @ApiOperation("Stop all test sessions")
    fun stopAllTestSessions() {
        testService.stopAllTestSessions()
    }

    @ExceptionHandler
    private fun handleWalletNotFoundException(request: HttpServletRequest, exception: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity("Bad request, ${exception.message}", HttpStatus.BAD_REQUEST)
    }
}