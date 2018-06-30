package com.android.csiapp.PcSocketTransmission;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.android.csiapp.Crime.utils.ZipUtils;
import com.android.csiapp.RestartService;
import com.android.csiapp.XmlHandler.DataInitial;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by JOWE on 2016/9/29.
 */

public class ThreadReadWriterIOSocket implements Runnable {

    public static final String TAG = "ThreadRWIOSocket";
    String mMapPath = Environment.getExternalStorageDirectory()+"/amapsdk/offlineMap/data";
    String mMapCachePath = Environment.getExternalStorageDirectory()+"/Amap.zip";
    String mInitDevicePath = Environment.getExternalStorageDirectory()+"/InitDeviceCmd.xml";
    String mSceneListPath = Environment.getExternalStorageDirectory()+"/getSceneListCmd.xml";
    String mWriteSceneIdPath = Environment.getExternalStorageDirectory()+"/writeSceneIdCmd.xml";
    String mDeleteSceneInfoPath = Environment.getExternalStorageDirectory()+"/deleteSceneInfoCmd.xml";
    private Socket client;
    private Context context;

    public ThreadReadWriterIOSocket(Context context, Socket client) {
        this.client = client;
        this.context = context;
    }

    @Override
    public void run() {
        Log.d(TAG, "A client has connected to server!");
        BufferedOutputStream out;
        BufferedInputStream in;
        boolean result = false;
        try {
            /* PC端发来的数据msg */
            String currCMD = "";
            int[] currcmdinfo;
            String receiveString ="";
            byte[] filebytes;
            //String currCMD = new String[4];
            out = new BufferedOutputStream(client.getOutputStream());
            in = new BufferedInputStream(client.getInputStream());
            SocketService.ioThreadFlag = true;
            while (SocketService.ioThreadFlag) {
                try {
                    if (!client.isConnected()) {
                        Log.v(TAG, "client didn't connect!");
                        break;
                    }
                    /* 接收PC发来的数据 */
                    Log.v(TAG, Thread.currentThread().getName() + "---->" + "will read......");

                    /* 读操作命令 */
                    currcmdinfo = readCMDFromSocket(in);

                    Log.v(TAG, Thread.currentThread().getName() + "---->" + "**currCMD ==== " + currCMD);

                    DataInitial dataInitial = new DataInitial(context);
                    /* 根据命令分别处理数据 */
                    if (currcmdinfo != null) {
                        Log.d(TAG, "Command = "+currcmdinfo[1]);
                        switch (currcmdinfo[1]) {
                            case 0: //斷開Socket
                                SocketService.ioThreadFlag=false;
                            case 1: //获取设备信息命令
                                dataInitial.createDeviceMsgXml();
                                File file = FileHelper.newFile("DeviceMsg.xml");
                                if (file.exists() == true) {
                                    byte[] abyte = FileHelper.readFile(file);
                                    concatCmdline(out, currcmdinfo, abyte.length);
                                    sendDeviceinfo(out, currcmdinfo, file);
                                } else {
                                    String errstr= "File Not Found";
                                    byte [] errbyte = errstr.getBytes("UTF-8");
                                    concatCmdline(out, currcmdinfo, errbyte.length);
                                    sendErrorString(out, errbyte);
                                }
                                SocketService.ioThreadFlag=false;
                                break;
                            case 2: //设备初始化命令
                                File Initfile = new File(mInitDevicePath);
                                if(Initfile.exists()) deleteFiles(Initfile);
                                filebytes = receiveDataFromSocketByte(in, currcmdinfo);
                                if(dataInitial.InitialDevice()){
                                    concatCmdline(out, currcmdinfo, 1);
                                    sendResult(out,true);
                                }else{
                                    String errstr= "Prase Fail";
                                    byte [] errbyte = errstr.getBytes("UTF-8");
                                    concatCmdline(out, currcmdinfo, errbyte.length);
                                    sendErrorString(out, errbyte);
                                }
                                SocketService.ioThreadFlag=false;
                                break;
                            case 3: //App更新命令
                                break;
                            case 4: //地图更新命令
                                Log.d(TAG, "Map Update!!");
                                File Uzfile = new File(mMapCachePath);
                                filebytes = receiveDataFromSocketByte(in, currcmdinfo);
                                try {
                                    ZipUtils.upZipFile(Uzfile, mMapPath);
                                    deleteFiles(Uzfile);
                                    result = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                concatCmdline(out, currcmdinfo, 1);
                                sendResult(out, result);
                                SocketService.ioThreadFlag=false;

                                break;
                            case 11: //获取现场列表命令
                                File getfile = new File(mSceneListPath);
                                if(getfile.exists()) deleteFiles(getfile);
                                filebytes = receiveDataFromSocketByte(in, currcmdinfo);
                                //組成BaseMsg.xml
                                result = dataInitial.CreateBaseMsg();

                                //获取BaseMsg.xml
                                File fileBaseMsgs = FileHelper.newFile("ScenesMsg.xml");
                                if (result && fileBaseMsgs.exists() == true) {
                                    byte[] abyte = FileHelper.readFile(fileBaseMsgs);
                                    concatCmdline(out, currcmdinfo, abyte.length);
                                    sendDeviceinfo(out, currcmdinfo, fileBaseMsgs);
                                } else {
                                    String errstr= "Login Fail";
                                    byte [] errbyte = errstr.getBytes("UTF-8");
                                    concatCmdline(out, currcmdinfo, errbyte.length);
                                    sendErrorString(out, errbyte);
                                }
                                SocketService.ioThreadFlag=false;
                                break;
                            case 12: //获取现场信息命令
                                receiveString = receiveDataFromSocket(in, currcmdinfo);

                                //組成單一BaseMsg.xml
                                if (dataInitial.CreateBaseMsgIdZip(receiveString)) {
                                    File fileBaseMsg = FileHelper.newFile("SceneMsg.zip");
                                    byte[] abyte = FileHelper.readFile(fileBaseMsg);
                                    concatCmdline(out, currcmdinfo, abyte.length);
                                    sendDeviceinfoZip(out, currcmdinfo, fileBaseMsg);
                                } else {
                                    Log.d("Anita","File Not Found");
                                    //String errstr= "File Not Found";
                                    //byte [] errbyte = errstr.getBytes("UTF-8");
                                    concatCmdline(out, currcmdinfo, 0);
                                    //sendErrorString(out, errbyte);
                                }
                                //Wait command 13
                                //SocketService.ioThreadFlag=false;
                                break;
                            case 13: //回写现勘编号命令
                                File writefile = new File(mWriteSceneIdPath);
                                if(writefile.exists()) deleteFiles(writefile);
                                filebytes = receiveDataFromSocketByte(in, currcmdinfo);

                                if(dataInitial.WriteSceneNo()){
                                    concatCmdline(out, currcmdinfo, 1);
                                    sendResult(out,true);
                                }else{
                                    Log.d("Anita","Prase Fail");
                                    //String errstr= "Prase Fail";
                                    //byte [] errbyte = errstr.getBytes("UTF-8");
                                    concatCmdline(out, currcmdinfo, 0);
                                    //sendErrorString(out, errbyte);
                                }
                                //Wait command 0
                                //SocketService.ioThreadFlag=false;
                                break;
                            case 14: //删除现场信息命令
                                File deletefile = new File(mDeleteSceneInfoPath);
                                if(deletefile.exists()) deleteFiles(deletefile);
                                filebytes = receiveDataFromSocketByte(in, currcmdinfo);
                                result = dataInitial.deleteSceneInfo();
                                //获取BaseMsg.xml
                                File fileSuccessToDelete = FileHelper.newFile("SuccessToDelete.xml");
                                if(result && fileSuccessToDelete.exists() == true){
                                    byte[] abyte = FileHelper.readFile(fileSuccessToDelete);
                                    concatCmdline(out, currcmdinfo, abyte.length);
                                    sendDeviceinfo(out, currcmdinfo, fileSuccessToDelete);
                                }else{
                                    //String errstr= "Prase Fail";
                                    //byte [] errbyte = errstr.getBytes("UTF-8");
                                    concatCmdline(out, currcmdinfo, 0);
                                    //sendErrorString(out, errbyte);
                                }
                                SocketService.ioThreadFlag=false;
                                break;
                            case 21: //获取现场基站列表命令
                                break;
                            case 22: //获取现场基站信息命令
                                break;
                            case 23: //回写现场基站状态命令
                                receiveDataFromSocket(in, currcmdinfo);
                                concatCmdline(out, currcmdinfo,1);
                                sendResult(out,false);
                                break;
                            case 24: //删除现场基站信息命令
                                break;
                            default:
                                Log.e(TAG, "incorrect cmd =" + currcmdinfo[1]);
                                SocketService.ioThreadFlag=false;
                                break;
                        }
                        //SocketService.ioThreadFlag=false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.close();
            in.close();
            client.close();
        } catch (Exception e) {
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
        int MAX_BUFFER_BYTES = 2048;
        byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
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
        //int count = 0;
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
        int ch = 0;
        int count = 0;
        int receivedatalength = cmdinfo[3];
        Log.d(TAG, "Revived Data Length =" + receivedatalength);

        byte[] filebytes = null;

        int pos = 0;
        int rcvLen =0;
        String path = "";
        switch (cmdinfo[1]){
            case 2:
                path = mInitDevicePath;
                break;
            case 4:
                path = mMapCachePath;
                break;
            case 11:
                path = mSceneListPath;
                break;
            case 13:
                path = mWriteSceneIdPath;
                break;
            case 14:
                path = mDeleteSceneInfoPath;
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

    public void sendDeviceinfoZip(BufferedOutputStream out, int[] cmdinfo, File file){
        Log.d(TAG,"Enter sendDeviceinfoZip");
        int byteRead=0;

        try {
            long length = file.length();
            byte[] buffer = new byte[(int)length];
            int offset = 0;
            FileInputStream fileInputStream = new FileInputStream(file);

            while (offset < buffer.length && ((byteRead = fileInputStream.read(buffer, offset, buffer.length-offset)) >= 0)) {
                offset += byteRead;
//out.write(buffer, 0, byteRead);
                Log.d(TAG, "offset="+offset);
            }
            out.write(buffer, 0, offset);
            fileInputStream.close();
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendDeviceinfo(BufferedOutputStream out, int[] cmdinfo, File file) {
        Log.d(TAG,"Enter sendDeviceinfo");
        byte[] buffer;
        int bufferSize, byteAvailable, byteRead;
        int maxBufferSize=1*1024*1024; //1024k

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byteAvailable = fileInputStream.available();
            Log.d(TAG,"byteAvailable(1)="+byteAvailable);
            bufferSize = Math.min(byteAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            byteRead = fileInputStream.read(buffer,0,bufferSize);

            while(byteRead > 0 ){
                Log.d(TAG,"byteRead="+byteRead);
                out.write(buffer,0,bufferSize);
                byteAvailable = fileInputStream.available();
                Log.d(TAG,"byteAvailable(2)="+byteAvailable);
                bufferSize = Math.min(byteAvailable, maxBufferSize);
                byteRead = fileInputStream.read(buffer,0,bufferSize);
            }
            fileInputStream.close();
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public byte[] concatCmdline(BufferedOutputStream out, int[] cmdinfo, int datalen) {

        Log.d(TAG,"Enter CombineCmdline");
        byte[] sendCmdline = new byte [10];
        byte[] cmdid = Utils.intToByteBySize(cmdinfo[0],4);
        byte[] cmd =  Utils.intToByteBySize(cmdinfo[1],1);
        byte[] isfile = Utils.intToByteBySize(cmdinfo[2],1);
        byte[] datalength = Utils.intToByteBySize(datalen,4);

        //cmdid = Utils.InttoByteBySize(cmdinfo[0],4);
        //cmd = Utils.InttoByteBySize(cmdinfo[1],1);
        //isfile = Utils.InttoByteBySize(cmdinfo[2],1);
        //datalength = Utils.InttoByteBySize(cmdinfo[3],4);

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

    public void sendResult(BufferedOutputStream out, boolean result) {
        byte[] resultbyte = new byte[]{(byte) (result?1:0)};
        try {
            out.write(resultbyte);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void sendErrorString(BufferedOutputStream out, byte[] errbyte) {
        try {
            out.write(errbyte, 0, errbyte.length);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFiles(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFiles(childFiles[i]);
            }
            file.delete();
        }
    }

    public void restartApp() {
        Intent mServiceIntent = new Intent(context, RestartService.class);
        context.startService(mServiceIntent);
    }
}
