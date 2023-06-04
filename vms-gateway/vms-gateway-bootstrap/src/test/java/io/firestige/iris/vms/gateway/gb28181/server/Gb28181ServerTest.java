package io.firestige.iris.vms.gateway.gb28181.server;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class Gb28181ServerTest {
    @Test
    void test() {
        Gb28181Server server = new NettyGb28181Server();
        server.start().addListener(onClose());
        server.shutdown();
        assertFalse(server.isRunning());
        // todo assert onClose has call onece and server is stoped;
    }
}
