package io.firestige.iris.vms.gateway.gb28181.bind.annotation;

import io.firestige.iris.vms.gateway.gb28181.SipMethod;

public @interface RequestMapping {
    String cmdType() default "";
    SipMethod method();
}
