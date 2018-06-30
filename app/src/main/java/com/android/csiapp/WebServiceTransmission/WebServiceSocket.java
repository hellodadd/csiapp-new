package com.android.csiapp.WebServiceTransmission;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by JOWE on 2016/9/29.
 */

public class WebServiceSocket extends Service {
    public static final String TAG = "WebServiceSocket";
    public static Boolean mainThreadFlag = true;
    public static Boolean ioThreadFlag = true;
    public String mServerIp = "http://192.168.0.185";
    public int mServerPort = 80;
    public String mMethod = "";
    public String mId = "";
    Socket mSocket = null;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "WebServiceSocket--->onCreate()");
		/* 创建内部类sysBroadcastReceiver 并注册registerReceiver */
    }

    private void doListen() {
        try {
            if (mSocket == null || !mSocket.isConnected()) {
                if(mSocket==null) mSocket = new Socket();
                Log.d(TAG, "mServerIp = "+mServerIp+", mServerPort = "+mServerPort);
                InetSocketAddress inetSocketAddress = new InetSocketAddress(mServerIp, mServerPort);
                mSocket.connect(inetSocketAddress);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        while (mainThreadFlag && mSocket!=null && mSocket.isConnected()) {
            Log.d(TAG, "Accept Successfully");
            new Thread(new ThreadReadWriterIOWebServiceSocket(this, mSocket, mMethod, mId)).start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "WebServiceSocket----->onStartCommand()");
        if (intent != null) {
            String Ip = intent.getStringExtra("ip");
            String Port = intent.getStringExtra("port");
            String method = intent.getStringExtra("method");
            String id = intent.getStringExtra("id");
            Log.d(TAG, "Socket Ip="+ Ip +", Port=" + Port+", Method="+method+", SceneId="+id);
            if(!Ip.isEmpty()) mServerIp = Ip;
            if(!Port.isEmpty()) mServerPort = Integer.valueOf(Port);
            if(!method.isEmpty()) mMethod = method;
            if(!id.isEmpty()) mId = id;
        }
        mainThreadFlag = true;
        new Thread() {
            public void run() {
                doListen();
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
            Log.v(TAG, Thread.currentThread().getName() + "---->" + "WebServiceSocket.close()");
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v(TAG, Thread.currentThread().getName() + "---->" + "onDestroy");
    }
}
