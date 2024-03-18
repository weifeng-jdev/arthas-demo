package com.yzf.arthasdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggerService {
    public void log() {
        log.debug("这是loggerService的Debug日志");
        log.info("这是loggerService的Info日志");
    }
}
