package com.ylq.example.fiangle;

import org.apache.thrift.protocol.TProtocolFactory;

/**
 * Class description goes here
 * Created by ylq on 16/4/14 下午5:50.
 */
public class ServicePing extends Detect.Service {


    public ServicePing(Detect.ServiceIface iface, TProtocolFactory protocolFactory) {
        super(iface, protocolFactory);
    }

    public Object getFunmap() {
        return this.functionMap.get("ping");
    }
}
