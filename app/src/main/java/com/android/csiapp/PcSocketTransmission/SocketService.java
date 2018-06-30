package com.android.csiapp.PcSocketTransmission;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by JOWE on 2016/9/29.
 */

public class SocketService extends Service {
    public static final String TAG = "SocketService";
    public static Boolean mainThreadFlag = true;
    public static Boolean ioThreadFlag = true;
    public int mServerPort = 10086;
    ServerSocket mServerSocket = null;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SocketService--->onCreate()");

		/* 创建内部类sysBroadcastReceiver 并注册registerReceiver */
    }

    private void doListen(int socketport) {
        try {

            while (mainThreadFlag) {
                Socket socket = mServerSocket.accept();
                Log.d(TAG, "Accept Successfully");
                new Thread(new ThreadReadWriterIOSocket(this, socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SocketService----->onStartCommand()");
        if (intent != null) {
            int port = intent.getIntExtra("port", 10086);
            Log.d(TAG, "Socket Port=" + port);
            mServerPort = port;
        }
        try {
            if (mServerSocket == null) {
                mServerSocket = new ServerSocket();
                mServerSocket.setReuseAddress(true);
                mServerSocket.bind(new InetSocketAddress(mServerPort));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mainThreadFlag = true;
        new Thread() {
            public void run() {
                doListen(mServerPort);
            };
        }.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 关闭线程
        mainThreadFlag = false;
        ioThreadFlag = false;

        // 关闭服务器
        try
        {
            Log.v(TAG, Thread.currentThread().getName() + "---->" + "serverSocket.close()");
            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v(TAG, Thread.currentThread().getName() + "---->" + "onDestroy");
    }
}
