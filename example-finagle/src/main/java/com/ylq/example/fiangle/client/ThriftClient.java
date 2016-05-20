package com.ylq.example.fiangle.client;

import com.ylq.example.fiangle.Detect;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Class description goes here
 * Created by ylq on 16/4/13 下午3:25.
 */
public class ThriftClient {
    public static void main(String[] args) {
        TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 11005));
        TProtocol protocol = new TBinaryProtocol(transport);
        Detect.Client client = new Detect.Client(protocol);
//        BrandServ.Client client=new BrandServ.Client(protocol);
        try {
            transport.open();
//            System.out.println(client.getBrandCodeById(1005));
            System.out.println(client.ping());
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }
}
