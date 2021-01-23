package com.stackstech.honeybee.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value ="/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {
}
