package com.ylq.example.fiangle.server;

import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import com.twitter.util.Duration;
import com.twitter.util.ExecutorServiceFuturePool;
import com.twitter.util.Function0;
import com.ylq.example.fiangle.Detect;
import com.ylq.example.fiangle.ServicePing;
import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

import java.lang.reflect.*;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class description goes here
 * Created by ylq on 15/1/4 下午7:11.
 */
public class FinagleServerHandler implements InvocationHandler {
    //线程池
    private ExecutorServiceFuturePool futurePool = null;

    private BrandServiceImpl brandService = new BrandServiceImpl();

    private Integer port = 11005;

    private Integer threads = 1;

    public static void main(String[] args) {
        FinagleServerHandler finagleServerHandler = new FinagleServerHandler();
        try {
            finagleServerHandler.start();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public FinagleServerHandler() {
        super();
    }

    /**
     * * 启动服务
     *
     * @return
     */
    public void start() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        //finagle接口文件父类
        Class thriftApi = Class.forName("com.ylq.example.fiangle.BrandServ");
        //提供服务的接口
        Class serviceIface = null;
        Class serviceImpl = null;
        //启动服务的实现类
        Class serviceClass = null;
        for (Class c : thriftApi.getDeclaredClasses()) {
            if ("Service".equals(c.getSimpleName())) {
                serviceClass = c;
            }
            if ("ServiceIface".equals(c.getSimpleName())) {
                serviceIface = c;
            }
            if ("ServiceToClient".equals(c.getSimpleName())) {
                serviceImpl = c;
            }
        }


        //接口中添加这个service
        Class[] iC = new Class[serviceImpl.getInterfaces().length + 1];
        for (int i = 0; i < iC.length - 1; i++) {
            iC[i] = serviceImpl.getInterfaces()[i];
        }
        iC[iC.length - 1] = Detect.ServiceIface.class;

        //生成代理类（此处借用finagle生成ServiceToClient类。）
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), iC, this);
        //获取构造函数
        Constructor con = serviceClass.getConstructor(serviceIface, TProtocolFactory.class);
        //反射生成service对象(相当于shell类)
        Service service = (Service) con.newInstance(proxy, new TBinaryProtocol.Factory());
        //创建finagle启动服务需要的对象
        ServerBuilder serverBuilder = ServerBuilder.get()
                .name("BrandServ")
                .codec(ThriftServerFramedCodec.get())
                .requestTimeout(new Duration(30000 * Duration.NanosPerMillisecond()))//请求超时时间
                .keepAlive(true)
                .bindTo(new InetSocketAddress(port));
        ServicePing servicePing = new ServicePing((Detect.ServiceIface) proxy, new TBinaryProtocol.Factory());

        Field field = serviceClass.getDeclaredField("functionMap");
        field.setAccessible(true);
        ((Map) field.get(service)).put("ping", servicePing.getFunmap());


        //添加zipkin监控
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        futurePool = new ExecutorServiceFuturePool(executorService);
        //启动服务
        ServerBuilder.safeBuild(service, serverBuilder);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final long invokeBeginTime = System.currentTimeMillis();
        this.invokeBefore();
        Object result = null;
        try {
            System.out.println(method.getName());
            result = futurePool.apply(new FinagleProxy(method.getName(), args));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.invokeAfter();
        return result;
    }


    private void invokeBefore() {

    }

    private void invokeAfter() {

    }


    /**
     * 供thrift接口壳调用，内部调用具体实现类，传入参数为对象类型
     */
    private class FinagleProxy extends Function0 {

        //private Object service;
        private Object[] params;
        private String methodName;

        public FinagleProxy(String methodName, Object... params) {
            //this.service = service;
            this.methodName = methodName;
            this.params = params;
        }

        @Override
        public Object apply() {
            Object result = null;
            Class[] paramsClass = null;
            if (params != null && params.length > 0) {
                paramsClass = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    //如果传递参数为null,会报nullpoint异常,需要做判断
                    Object paramObj = params[i];
                    if (paramObj != null) {
                        paramsClass[i] = paramObj.getClass();
                    } else {
                        paramsClass[i] = null;
                    }
                }
            }
            //solved param is a super class error
            try {
                if ("ping".equals(methodName)) {
                    return "SUCCESS";
                }
                result = MethodUtils.invokeMethod(brandService, methodName, params, paramsClass);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Loom调用抛出ReflectiveOperationException", e);
            }
            return result;
        }

    }

}
