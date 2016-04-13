package com.ylq.example.fiangle.server;

import com.ylq.example.fiangle.BrandServ;
import org.apache.thrift.TException;

import java.util.Random;

/**
 * Class description goes here
 * Created by ylq on 15/1/9 上午10:30.
 */
public class BrandServiceImpl implements BrandServ.Iface {

    @Override
    public String getBrandCodeById(int id) throws TException {
        return String.valueOf(id);
    }

    @Override
    public int getIdByBrandCode(String brandCode) throws TException {
        return new Random().nextInt();
    }
}
