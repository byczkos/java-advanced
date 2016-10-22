package workshop.java.intermediate.fancystuff;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayWithThreadLocalAndProxy {

    private static final ThreadLocal<String> threadLocalString = ThreadLocal.withInitial(() -> "Init");
    private static final ThreadLocal<Context> threadLocalTransaction = new ThreadLocal<>();


    @Test
    void name() {

        CompletableFuture
                .runAsync(() -> {
                    System.out.println("then: " + threadLocalString.get());
                    threadLocalString.set("local in thread 1");
                    System.out.println("then: " + threadLocalString.get());
                    threadLocalString.set("cleared");

                })
        ;

        CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("then: " + threadLocalString.get());
                    threadLocalString.set("local in thread 2");
                    System.out.println("then: " + threadLocalString.get());
                    threadLocalString.set("cleared");
                    return "";
                })
                .join()
        ;
    }

    @Test
    void testPolymorphismWithDecorator() {
        BusinessInterface object = new BusinessClass();

        object.businessMethod(13);

        BusinessInterface decorated = new Decorator(object);

        decorated.businessMethod(16);
    }

    @Test
    void testJavaProxy() {
        BusinessInterface object = new BusinessClass();

        BusinessInterface proxy = createProxy(object, BusinessInterface.class);
        List listProxy = createProxy(new ArrayList<>(), List.class);

        proxy.businessMethod(13);

        listProxy.add("michal");

    }

    private <T> T createProxy(T object, Class<T> interfaceClass) {
        return interfaceClass.cast(Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{interfaceClass},
                new LoggingProxy(object)));
    }

    interface BusinessInterface {
        String businessMethod(int id);
    }

    public static class BusinessClass implements BusinessInterface {

        @Override
        public String businessMethod(int id) {
            System.out.println("from business method for " + id);
            return "business";
        }
    }

    private static class LoggingProxy implements InvocationHandler {

        private final Object object;

        public LoggingProxy(Object object) {
            this.object = object;
        }

        @Override
        public Object invoke(Object proxyIns, Method method, Object[] args) throws Throwable {
            Context context = threadLocalTransaction.get();
            boolean opened = false;
            try {
                if (context == null) {
                    // begin
                    threadLocalTransaction.set(new Context());
                    opened = true;
                }

                Object value = method.invoke(object, args);

                if (opened) {
                    // commit
                }
                return value;
            } catch (Throwable t) {
                // rollback
                throw t;
            }
        }
    }

    private static class Context {
    }

    private class Decorator implements BusinessInterface {

        private BusinessInterface delegate;

        public Decorator(BusinessInterface delegate) {
            this.delegate = delegate;
        }

        @Override
        public String businessMethod(int id) {
            System.out.println("before");
            String value = delegate.businessMethod(id);
            System.out.println("after");
            return value;
        }
    }
}
