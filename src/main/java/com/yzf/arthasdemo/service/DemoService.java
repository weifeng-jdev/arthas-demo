package com.yzf.arthasdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DemoService {
    private final LoggerService loggerService;
    private static final List<byte[]> memoryLeakingList = new ArrayList<>();

    private static final Map<String, String> map = new HashMap<>();

    static {
        map.put("key", "value");
    }

    /**
     * trace追踪方法的调用链路耗时：trace + 全限定类名 + 方法名 [-n + 监控次数]  [-m + 匹配次数] [ognl表达式过滤条件] [--skipJDKMethod false 跳过jdk方法]
     * trace com.yzf.arthasdemo.service.DemoService trace  -n 5 --skipJDKMethod false
     */
    public String trace() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        this.methodA();
        for (int i = 0; i < 10; i++) {
            this.methodB();
        }
        stopWatch.stop();
        return String.valueOf(stopWatch.getTotalTimeMillis());
    }

    public void methodA() {
        try {
            log.info("methodA sleep...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("线程睡眠异常", e);
        } finally {
            log.info("methodA sleep...end");
        }
    }

    private void methodB() {
        try {
            log.info("methodB sleep...");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            log.error("线程睡眠异常", e);
        } finally {
            log.info("methodB sleep...end");
        }
    }

    /**
     * 方便的监控函数的调用情况。如入参、返回值、异常信息等。
     * watch demo.MathGame primeFactors {params 参数,returnObj 返回值, throwExp 异常信息, target 当前对象} [-x + 监控结果属性的遍历层数]
     *
     * @param param
     * @return
     */
    public String watch(String param) {
        return "test watch ：" + param;
    }

    /**
     * vmtool -x 3 --action getInstances --className com.yzf.arthasdemo.service.DemoService  --express 'instances[0].exec(0,0)'
     *
     * @param a
     * @param b
     * @return
     */
    public String exec(int a, int b) {
        return a + "+" + b + "=" + (a + b);
    }

    /**
     * monitor com.yzf.arthasdemo.service.DemoService trace  -n 10
     *
     * @param a
     * @return
     */
    public String monitor(int a) {
        if (a % 2 == 0) {
            throw new RuntimeException("失败");
        }
        return "success";
    }

    /**
     * logger -n com.yzf.arthasdemo.service.LoggerService#log
     *
     * @return
     */
    public String logger() {
        log.debug("这是demoService的Debug日志");
        loggerService.log();
        log.info("这是demoService的Info日志");
        return "success";
    }

    public String stack() {
        log.info("stack");
        A();
        return "success";
    }

    private void A() {
        log.info("A");
        B();
    }

    private void B() {
        log.info("B");
        int i = new Random().nextInt();
        if (i % 2 == 0) {
            C1();
        } else {
            C2();
        }
    }

    private void C1() {
        log.info("C1");
    }

    private void C2() {
        log.info("C2");
    }

    public String leakMemory() {
        // 每次调用都生成1MB大小的随机数据
        byte[] data = new byte[1024 * 1024];
        new Random().nextBytes(data);
        // 将数据添加到列表中
        memoryLeakingList.add(data);
        return "Memory leaked! Current memory usage: " + getMemoryUsage() + " MB";
    }

    private long getMemoryUsage() {
        // 获取当前JVM的内存使用情况
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (totalMemory - freeMemory) / (1024 * 1024); // 转换为MB
    }
}
