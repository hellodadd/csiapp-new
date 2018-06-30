package com.android.csiapp.PcSocketTransmission;

/**
 * Created by JOWE on 2016/9/29.
 */

public class Utils {
    /* byte[]转Int */
    public static int bytesToInt(byte[] bytes) {
        int addr = bytes[3] & 0xFF;
        addr |= ((bytes[2] << 8) & 0xFF00);
        addr |= ((bytes[1] << 16) & 0xFF0000);
        addr |= ((bytes[0] << 24) & 0xFF000000);
        return addr;

    }

    /* Int转byte[] */
    public static byte[] intToByte(int i) {
        byte[] abyte0 = new byte[4];
        abyte0[3] = (byte) (0xff & i);
        abyte0[2] = (byte) ((0xff00 & i) >> 8);
        abyte0[1] = (byte) ((0xff0000 & i) >> 16);
        abyte0[0] = (byte) ((0xff000000 & i) >> 24);
        return abyte0;
    }


    public static byte[] intToByteBySize(int Num, int ByteSize) {
        byte[] OutByte = new byte[ByteSize];
        for (int i = 0; (i < 4) && (i < ByteSize); i++) {
            OutByte[ByteSize - 1 - i] = (byte) (Num >> 8 * i & 0xFF);
        }
        return OutByte;
    }

    /*
    public static int ByteArray2Int(byte[] b){
        int value=0;
        for(int i=3;i>-1;i--){
            value+=(b[i]&0x000000ff)<<(8*(4-1-i));
        }
        return value;
    } */
}
