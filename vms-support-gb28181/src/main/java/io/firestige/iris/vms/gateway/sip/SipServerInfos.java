package io.firestige.iris.vms.gateway.sip;

import reactor.netty.http.server.ConnectionInformation;

public interface SipServerInfos extends SipInfos, ConnectionInformation {
}
