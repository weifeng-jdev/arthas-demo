package com.yzf.arthasdemo.controller;

import com.yzf.arthasdemo.service.DemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo")
@Slf4j
@RequiredArgsConstructor
public class DemoController {
    private final DemoService demoService;

    @GetMapping("/trace")
    public String trace() {
        return demoService.trace();
    }

    @GetMapping("/stack")
    public String stack() {
        return demoService.stack();
    }

    @GetMapping("/watch")
    public String watch(String param) {
        return demoService.watch(param);
    }

    @GetMapping("/monitor")
    public String monitor(int param) {
        return demoService.monitor(param);
    }

    @GetMapping("/logger")
    public String logger() {
        return demoService.logger();
    }

    @GetMapping("/leak-memory")
    public String leakMemory() {
        // 将数据添加到列表中
        return demoService.leakMemory();
    }

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            15,
            2,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(50),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @GetMapping("/threadLock")
    public String threadLock() {
        Object resourceA = new Object();
        Object resourceB = new Object();
        executor.submit(() -> {
            synchronized (resourceA) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resourceB) {
                }
            }
        });
        executor.submit(() -> {
            synchronized (resourceB) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resourceA) {
                }
            }
        });
        return "success";
    }
}
