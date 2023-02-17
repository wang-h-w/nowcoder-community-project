package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoggerTests {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTests.class);

    @Test
    public void testLogger() {
        System.out.println(logger.getName());
        logger.debug("Debug log");
        logger.info("Info log");
        logger.warn("Warn log");
        logger.error("Error log");
    }
}
