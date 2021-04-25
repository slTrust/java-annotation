package com.demo.log01;

public class Main {
    public static void main(String[] args) {
        MyService service = new MyService();
        service.queryDatabase(1);
        service.provideHttpResponse("123");

        // 手动在方法 内部 加 start - end

        // 假设你有 100个方法呢？
        // 假设打印的内容修改了呢？
        // 假设打印的地方修改了呢？
    }
}

class MyService {
    public void queryDatabase(int param){
        System.out.println("start");
        System.out.println("query db" + param);
        System.out.println("end");
    }

    public void provideHttpResponse(String param){
        System.out.println("start");
        System.out.println("provide http service" + param);
        System.out.println("end");
    }
}

