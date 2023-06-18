package io.firestige.iris.vms.support.gb28181.server;

import io.netty.buffer.UnpooledByteBufAllocator;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ReactorServerSipResponseTest
 *
 * @author firestige
 * @createAt 2023/6/18
 **/
class ReactorServerSipResponseTest {
    @Test
    void test() {
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.submit(() -> run(() -> {
            try {
                latch.await();
                new Person();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "person"));
        pool.submit(() -> run(() -> {
            try {
                latch.await();
                new Student();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "student"));
        latch.countDown();

    }

    private void run(Runnable runnable, String name) {
        Thread thread = new Thread(runnable, name);
        thread.start();
        while (thread.isAlive()) {
            LocalDateTime now = LocalDateTime.now();
            System.out.println(now.format(DateTimeFormatter.ISO_DATE_TIME) + ", thread: " + name + ", state: " + thread.getState());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Person {
        static Student student = new Student();

        public Person() {
        }
    }

    static class Student extends Person{

    }

}