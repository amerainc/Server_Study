package com.webserver.core;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


/**
 * @Author rainc
 * @create 2019/10/5 17:03
 * 主线程类用于启动服务器和接收客户端请求
 */
public class Server {
    private ServerSocket serverSocket;
    //判断服务器是否已启动
    private boolean isRunning;
    //线程池
    private ExecutorService threadPool;

    public static void main(String[] args) {
        Server server = new Server();
        //启动服务
        server.start();
    }

    /**
     * 启动服务
     */
    public void start() {
        try {
            //初始化ServerSocket对象并配置端口
            serverSocket = new ServerSocket(8888);
            //初始化线程池
            threadPool = new ThreadPoolExecutor(10, 200, 0, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(512), // 使用有界队列，避免OOM
                    new ThreadPoolExecutor.DiscardPolicy());
            isRunning = true;
            //接收连接进行处理
            receive();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
            //若启动异常停止服务器
            stop();
        }
    }

    /**
     * 接受连接处理
     */
    public void receive() {
        //循环处理使得服务器能一直运行
        while (isRunning) {
            try {
                //通过accept方法接受建立新的连接并获取到Socket对象
                Socket client = serverSocket.accept();
                System.out.println("一个客户端建立了连接");
                //多线程处理每当有新连接就初始化一个Dispatcher对象并将Socket传入Dispatcher对象中进行详细的处理
                threadPool.execute(new Dispatcher(client));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("客户端错误");
            }
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        isRunning = false;
        try {
            this.serverSocket.close();
            System.out.println("服务器已停止");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}