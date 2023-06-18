package io.firestige.iris.vms.gateway;

import static io.firestige.iris.vms.support.gb28181.SipStatus.Trying;

import io.firestige.iris.vms.support.gb28181.SipStatus;
import io.firestige.iris.vms.support.gb28181.server.AbstractServerSipResponse;
import io.firestige.iris.vms.support.gb28181.server.SipHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import org.slf4j.Logger;
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
    Logger logger = org.slf4j.LoggerFactory.getLogger(Bootstrap.class);
    @Bean
    SipHandler sipHandler() {
        return (request, response) -> {
//            AbstractServerSipResponse resp = (AbstractServerSipResponse) response;
//            HttpServerResponse res = resp.getNativeResponse();
//            return res.status(400).send();
            response.setStatusCode(Trying);
            return response.setComplete();
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }
}
