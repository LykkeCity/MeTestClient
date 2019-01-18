package com.lykke.me.test.client.web

import com.lykke.me.test.client.service.TestsService
import com.lykke.me.test.client.web.dto.RunTestsRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.CollectionUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/tests")
@Api("Endpoint that enables to run tests on target ME instance")
class TestsController {

    @Autowired
    private lateinit var testService: TestsService

    @PostMapping
    @ApiOperation("Start running tests on target ME instance")
    @ApiResponses(
            ApiResponse(code = 200, message = "Success"),
            ApiResponse(code = 500, message = "Internal server error occurred")
    )
    fun test(@Valid
             @RequestBody
             runTestsRequest: RunTestsRequest) {
        if (CollectionUtils.isEmpty(runTestsRequest.testNames)) {
            if (runTestsRequest.runTestsPolicy != null) {
                testService.startAllTests(runTestsRequest.runTestsPolicy)
            } else {
                testService.startAllTests()
            }
        } else {
            if (runTestsRequest.runTestsPolicy != null) {
                testService.startTests(runTestsRequest.testNames!!, runTestsRequest.runTestsPolicy)
            } else {
                testService.startTests(runTestsRequest.testNames!!)
            }
        }
    }
}