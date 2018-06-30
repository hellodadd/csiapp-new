package com.android.csiapp.WebServiceTransmission;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.csiapp.PcSocketTransmission.FileHelper;
import com.android.csiapp.PcSocketTransmission.SocketService;
import com.android.csiapp.PcSocketTransmission.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by user on 2017/1/13.
 */

public class ThreadReadWriterIOWebServiceSocket implements Runnable {
    public static final String TAG = "RWIOWebServiceSocket";
    private Socket client;
    private Context context;
    private String method;
    private String id;
    private Boolean waitCmdResult = false;

    String mAppUpdatePath = Environment.getExternalStorageDirectory()+"/csi-app.apk";
    String mSceneMsgPath = Environment.getExternalStorageDirectory()+"/SceneMsg.zip";

    public ThreadReadWriterIOWebServiceSocket(Context context, Socket client, String method, String id) {
        this.client = client;
        this.context = context;
        this.method = method;
        this.id = id;
    }

    @Override
    public void run() {
        Log.d(TAG, "A client has connected to server!");
        BufferedOutputStream out;
        BufferedInputStream in;
        int[] currcmdinfo = new int[4];
        int currCmd = -1;
        String filelength = "";
        long receivelength = 0;
        byte[] filebytes;
        try {
            out = new BufferedOutputStream(client.getOutputStream());
            in = new BufferedInputStream(client.getInputStream());
            WebServiceSocket.ioThreadFlag = true;
            while (WebServiceSocket.ioThreadFlag){
                try {
                    if (!client.isConnected()) {
                        Log.v(TAG, "client didn't connect!");
                        break;
                    }

                    if(method.isEmpty()){
                        Log.v(TAG, "method is empty!");
                        break;
                    }

                    Log.v(TAG, Thread.currentThread().getName() + "---->" + "will write or read......");
                    if(waitCmdResult) {
                        currcmdinfo = readCMDFromSocket(in);
                        currCmd = currcmdinfo[1];
                    }

                    /* 寫操作命令 */
                    if(method.equalsIgnoreCase("AppUpdate")){
                        //Initial cmd
                        if(currCmd == -1) currCmd = 1;

                        switch (currCmd) {
                            case 1:
                                if(!waitCmdResult) {
                                    byte[] DeviceIdByte = id.getBytes("UTF-8");
                                    currcmdinfo = new int[4];
                                    currcmdinfo[0] = 1;
                                    currcmdinfo[1] = 1;
                                    currcmdinfo[2] = 0;
                                    concatCmdline(out, currcmdinfo, DeviceIdByte.length);
                                    sendString(out, DeviceIdByte);
                                    waitCmdResult = true;
                                }else{
                                    if(currcmdinfo[3]==0) {
                                        WebServiceSocket.ioThreadFlag = false;
                                    }else{
                                        filelength = receiveDataFromSocket(in, currcmdinfo);
                                        waitCmdResult = false;
                                        currCmd = 2;
                                    }
                                }
                                break;
                            case 2:
                                if(!waitCmdResult){
                                    byte[] ApplengthByte = "0".getBytes("UTF-8"); //Todo 斷點
                                    currcmdinfo = new int[4];
                                    currcmdinfo[0] = 2;
                                    currcmdinfo[1] = 2;
                                    currcmdinfo[2] = 0;
                                    concatCmdline(out, currcmdinfo, ApplengthByte.length);
                                    sendString(out, ApplengthByte);
                                    waitCmdResult = true;
                                }else {
                                    if(currcmdinfo[3]==0) {
                                        WebServiceSocket.ioThreadFlag = false;
                                    }else{
                                        filebytes = receiveDataFromSocketByte(in, currcmdinfo);
                                        receivelength = receivelength + currcmdinfo[3];
                                        if(receivelength>=Integer.valueOf(filelength)) {
                                            waitCmdResult = false;
                                            currCmd = 3;
                                        }
                                    }
                                }
                                break;
                            case 3:
                                currcmdinfo = new int[4];
                                currcmdinfo[0] = 3;
                                currcmdinfo[1] = 3;
                                currcmdinfo[2] = 0;
                                if(receivelength == Integer.valueOf(filelength)) {
                                    concatCmdline(out, currcmdinfo, 1);
                                }else {
                                    concatCmdline(out, currcmdinfo, 0);
                                }
                                break;
                            default:
                                Log.e(TAG, "incorrect cmd =" + currCmd);
                                SocketService.ioThreadFlag=false;
                                break;
                        }
                    }

                    if(method.equalsIgnoreCase("UploadScene")){
                        //Initial cmd
                        if(currCmd == -1) currCmd = 11;

                        switch (currCmd) {
                            case 0:
                                currcmdinfo[0] = 0;
                                currcmdinfo[1] = 0;
                                currcmdinfo[2] = 0;
                                currcmdinfo[3] = 0;
                                WebServiceSocket.ioThreadFlag = false;
                                break;
                            case 11:
                                if(!waitCmdResult) {
                                    byte [] DeviceIdByte = id.getBytes("UTF-8");
                                    currcmdinfo = new int[4];
                                    currcmdinfo[0] = 11;
                                    currcmdinfo[1] = 11;
                                    currcmdinfo[2] = 0;
                                    concatCmdline(out, currcmdinfo, DeviceIdByte.length);
                                    sendString(out, DeviceIdByte);
                                    waitCmdResult = true;
                                }else{
                                    if(currcmdinfo[3]==0) {
                                        WebServiceSocket.ioThreadFlag = false;
                                    }else{
                                        waitCmdResult = false;
                                        currCmd = 12;
                                    }
                                }
                                break;
                            case 12:
                                if(!waitCmdResult){
                                    File fileBaseMsg = new File(mSceneMsgPath);
                                    byte[] ApplengthByte = FileHelper.readFile(fileBaseMsg);
                                    currcmdinfo = new int[4];
                                    currcmdinfo[0] = 12;
                                    currcmdinfo[1] = 12;
                                    currcmdinfo[2] = 0;
                                    concatCmdline(out, currcmdinfo, ApplengthByte.length);
                                    sendString(out, ApplengthByte);
                                    waitCmdResult = true;
                                }else {
                                    receivelength = receivelength + currcmdinfo[3];
                                    if(receivelength<=Integer.valueOf(filelength)) {
                                        waitCmdResult = false;
                                        currCmd = 13;
                                    }
                                }
                                break;
                            case 13:
                                if(!waitCmdResult) {
                                    currcmdinfo = new int[4];
                                    currcmdinfo[0] = 3;
                                    currcmdinfo[1] = 3;
                                    currcmdinfo[2] = 0;
                                    File fileBaseMsg = new File(mSceneMsgPath);
                                    sendFile(out, fileBaseMsg); //Todo 斷點
                                    waitCmdResult = true;
                                }else {
                                    waitCmdResult = false;
                                    if(true){//Todo need to check scene id
                                        currCmd = 11;
                                    }else {
                                        currCmd = 0;
                                    }
                                }
                                break;
                            default:
                                Log.e(TAG, "incorrect cmd =" + currCmd);
                                SocketService.ioThreadFlag=false;
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    Log.v(TAG, Thread.currentThread().getName() + "---->" + "client.close()");
                    client.close();
                }
            } catch (IOException e) {
                Log.e(TAG, Thread.currentThread().getName() + "---->" + "read write error");
                e.printStackTrace();
            }
            //restartApp();
        }
    }

    public int[] readCMDFromSocket(InputStream in) {
        Log.d(TAG,"readCMDFromSocket");
        int[] cmdinfo = new int[4]; //int[0]:CmdId, int[1]:Cmd, int[2]:is File, int[3]:Data Length
        byte[] cmdidbytes = new byte[4];
        byte[] datalength = new byte[4];
        int ch = 0;
        int count = 0;
        int COMMAND_LENGTH=10;
        byte[] commandbytes = new byte[10];

        try {
            while ((ch = in.read()) != -1) {
                commandbytes[count] =(byte) ch;
                count++ ;
                if (count == COMMAND_LENGTH) {
                    Log.d(TAG,"Command Received Finished");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            /* cmdline:10byte, byte 1~4:Cmd, byte 5:Cmd, byte 6:is File, byte 7~10:Data Length*/
            System.arraycopy(commandbytes,0,cmdidbytes,0,4);
            cmdinfo[0] = Utils.bytesToInt(cmdidbytes);
            cmdinfo[1] = commandbytes[4];
            cmdinfo[2] = commandbytes[5];
            System.arraycopy(commandbytes,6,datalength,0,4);
            cmdinfo[3] = Utils.bytesToInt(datalength);

            for(int i=0;i<4;i++) {
                Log.d(TAG,"cmdinfo["+i+"]="+cmdinfo[i]);
            }
        } catch (Exception e) {
            Log.v(TAG, Thread.currentThread().getName() + "---->" + "readFromSocket error");
            e.printStackTrace();
            cmdinfo = null;
        }
        return cmdinfo;
    }

    public String receiveDataFromSocket(BufferedInputStream in, int[] cmdinfo) {
        int ch = 0;
        int receivedatalength = cmdinfo[3];
        int count=0;
        String receiveString = "";

        StringBuilder receiveStream = new StringBuilder();
        try {
            while ((ch = in.read()) != -1) {
                receiveStream.append((char)ch);
                count++;
                if(count == receivedatalength) {
                    Log.d(TAG,"Receive Finished: count="+count);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        receiveString = receiveStream.toString();
        Log.d(TAG,"ReceiveStream="+receiveString);

        return receiveString;
    }

    public byte[] receiveDataFromSocketByte(BufferedInputStream in, int[] cmdinfo) {
        int receivedatalength = cmdinfo[3];
        Log.d(TAG, "Revived Data Length =" + receivedatalength);

        byte[] filebytes = null;

        String path = "";
        switch (cmdinfo[1]){
            case 2:
                path = mAppUpdatePath;
                break;
            default:
                Log.d(TAG,"Error : cannot get path!");
                return filebytes;
        }

        try {
            filebytes = new byte[receivedatalength];
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));

            int bytesRead = in.read(filebytes,0,filebytes.length);
            int current = bytesRead;

            do {
                bytesRead = in.read(filebytes, current, (filebytes.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(current < receivedatalength);
            bos.write(filebytes, 0 , current);

            Log.d(TAG,"Received Data Finished");
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filebytes;
    }

    public byte[] concatCmdline(BufferedOutputStream out, int[] cmdinfo, int datalen) {
        Log.d(TAG,"Enter CombineCmdline");
        byte[] sendCmdline = new byte [10];
        byte[] cmdid = Utils.intToByteBySize(cmdinfo[0], 4);
        byte[] cmd =  Utils.intToByteBySize(cmdinfo[1], 1);
        byte[] isfile = Utils.intToByteBySize(cmdinfo[2], 1);
        byte[] datalength = Utils.intToByteBySize(datalen, 4);

        System.arraycopy(cmdid,0,sendCmdline,0,cmdid.length);
        System.arraycopy(cmd,0,sendCmdline,cmdid.length,cmd.length);
        System.arraycopy(isfile,0,sendCmdline,cmdid.length+cmd.length,isfile.length);
        System.arraycopy(datalength,0,sendCmdline,cmdid.length+cmd.length+isfile.length,datalength.length);

        Log.d(TAG,"CombineCmdline sendCmdline len="+sendCmdline.length);
        try {
            out.write(sendCmdline, 0, 10);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sendCmdline;
    }

    public void sendString(BufferedOutputStream out, byte[] Stringbyte) {
        try {
            out.write(Stringbyte, 0, Stringbyte.length);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(BufferedOutputStream out, File file){
        Log.d(TAG,"Enter sendFile");
        int byteRead=0;

        try {
            long length = file.length();
            byte[] buffer = new byte[(int)length];
            int offset = 0;
            FileInputStream fileInputStream = new FileInputStream(file);

            while (offset < buffer.length && ((byteRead = fileInputStream.read(buffer, offset, buffer.length-offset)) >= 0)) {
                offset += byteRead;
                Log.d(TAG, "offset="+offset);
            }
            out.write(buffer, 0, offset);
            fileInputStream.close();
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
