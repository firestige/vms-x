package io.firestige.iris.vms.gateway.gb28181;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SipMethodTest {
    @Test
    void should_success_when_resolve_sip_method() {
        assertEquals(SipMethod.REGISTER, SipMethod.resolve("register"));
    }
}