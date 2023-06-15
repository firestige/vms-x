package io.firestige.iris.vms.gateway;

import io.firestige.iris.vms.support.gb28181.server.SipHandler;
import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * BootStrap
 * 应用启动类
 *
 * @version 0.1.0
 * @author firestige
 * @createAt 2023-6-13
 */
@SpringBootApplication
public class Bootstrap {
    @Bean
    SipHandler sipHandler() {
        return (request, response) -> Mono.empty();
    }
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }
}
