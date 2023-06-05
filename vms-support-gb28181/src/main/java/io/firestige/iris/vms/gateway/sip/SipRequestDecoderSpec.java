package io.firestige.iris.vms.gateway.sip;

public final class SipRequestDecoderSpec extends SipDecoderSpec<SipRequestDecoderSpec> {
    @Override
    public SipRequestDecoderSpec get() {
        return this;
    }

    SipRequestDecoderSpec build() {
        SipRequestDecoderSpec decoder = new SipRequestDecoderSpec();
        decoder.initialBufferSize = initialBufferSize;
        decoder.maxInitialLineLength = maxInitialLineLength;
        return decoder;
    }
}
