package xyz.firestige.vmsx.gateway.api;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CommonHandler
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/16
 **/
@Component
public class CommonHandler implements InitializingBean {
    public String version() {
        return "0.1.0";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        Executors.newFixedThreadPool(1).submit(() -> {
//            try {
//                TimeUnit.MINUTES.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }
}
