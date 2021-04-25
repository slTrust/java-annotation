package com.demo.log02;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatcher;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        // 通过注解
        MyService service2 = enhanceByAnnotation();
        service2.queryDatabase(1);
        service2.provideHttpResponse("abc");
        service2.noLog();
    }

    private static MyService enhanceByAnnotation() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return new ByteBuddy()
                .subclass(MyService.class)
                .method(new FilterMethodAnnotatedWithLogMatcher())
                .intercept(MethodDelegation.to(LoggerInterceptor.class))
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .getConstructor()
                .newInstance();
    }

    public static class LoggerInterceptor {
        public static List<String> log(@SuperCall Callable<List<String>> zuper)
                throws Exception {
            System.out.println("before method called");
            try {
                return zuper.call();
            } finally {
                System.out.println("method end");
            }
        }
    }

    static class FilterMethodAnnotatedWithLogMatcher implements ElementMatcher<MethodDescription>{

        @Override
        public boolean matches(MethodDescription target) {
            List<String> methodsWithLog = Stream.of(MyService.class.getMethods())
                    .filter(FilterMethodAnnotatedWithLogMatcher::isAnnotatedWithLog)
                    .map(Method::getName)
                    .collect(Collectors.toList());

            return methodsWithLog.contains(target.getName());
        }

        private static boolean isAnnotatedWithLog(Method method){
            return method.getAnnotation(Log.class)!=null;
        }

    }


}


