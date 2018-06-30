package com.android.csiapp.WebServiceTransmission;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemProperties;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.R;
import com.android.csiapp.XmlHandler.DataInitial;

import org.dom4j.io.SAXReader;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import org.dom4j.*;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by user on 2017/1/10.
 */
public class WebService {
    private static String TAG = "WebService";
    private static String mTCPAddress;
    //定义获取手机信息的SoapAction与命名空间,作为常量
    private static String AddressnameSpace = "";
    private static String mNameSpace="http://tempuri.org/";
    //相关参数
    private static String KKWebServiceUrl = "";

    private static final String GetServerAddressMethod = "GetServerAddress";
    private static final String GetServerAddressSoapAction = "http://tempuri.org/GetServerAddress";
    private static final String DevInitialiseMethod = "DevInitialise";
    private static final String DevInitialiseSoapAction = "http://tempuri.org/DevInitialise";
    private static final String AppUpdateVersionMethod = "AppUpdateVersion";
    private static final String AppUpdateVersionSoapAction = "http://tempuri.org/AppUpdateVersion";
    private static final String SubmitSceneInfoMethod = "ReportSceneData";
    private static final String SubmitSceneInfoSoapAction = "http://tempuri.org/ReportSceneData";
    private static final String GetSceneNoMethod = "GetSceneNo";
    private static final String GetSceneNoSoapAction = "http://tempuri.org/GetSceneNo";

    private Context mContext;

    public WebService(Context context){
        this.mContext = context;
        //设置网络地址
        //AddressnameSpace=DictionaryInfo.getDictValue(DictionaryInfo.mSystemKey,"SERVICEURL");
        //调试环境
        AddressnameSpace="http://192.168.0.107:7233/ScenePCService.asmx";
        KKWebServiceUrl=AddressnameSpace;//+"?wsdl";

    }

    public void DeviceInitial(View processView, View mainView){
        Log.d(TAG,"DeviceInitial");

        if(!isDeviceOnline()){
            Log.d(TAG, "No network connection available.");
            String msg = mContext.getResources().getString(R.string.unavailablenetwork);
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            return;
        }else{
            new DeviceInitialTask(processView, mainView).execute();
        }
    }

    public class DeviceInitialTask extends AsyncTask<Void, Void, Boolean> {
        private View mProcessView = null, mMainView = null;
        DeviceInitialTask(View processView, View mainView){
            this.mProcessView = processView;
            this.mMainView = mainView;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //Web Service
            String result = WebService.DevInitialise();

            //Initial Device
            if(!result.isEmpty()){
                try {
                    Document doc = DocumentHelper.parseText(result);
                    DataInitial dataInitial = new DataInitial(mContext);
                    Boolean InitResult = dataInitial.InitialDevice(doc);
                    Log.d(TAG,"InitResult = "+InitResult);
                }catch (DocumentException e){
                    e.printStackTrace();
                    return false;
                }
                return true;
            }else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false, mProcessView, mMainView);
            if(success) {
                String msg = mContext.getResources().getString(R.string.device_initial_success);
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }else {
                String msg = mContext.getResources().getString(R.string.device_initial_failed);
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false, mProcessView, mMainView);
            String msg = mContext.getResources().getString(R.string.device_initial_failed);
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void AppUpdate(View processView, View mainView){
        Log.d(TAG,"AppUpdate");

        if(!isDeviceOnline()){
            Log.d(TAG, "No network connection available.");
            String msg = mContext.getResources().getString(R.string.unavailablenetwork);
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            return;
        }else{
            new AppUpdateTask(processView, mainView).execute();
        }
    }

    public class AppUpdateTask extends AsyncTask<Void, Void, Boolean> {
        private View mProcessView = null, mMainView = null;
        AppUpdateTask(View processView, View mainView){
            this.mProcessView = processView;
            this.mMainView = mainView;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //get swversion
            String swversion = "";
            try {
                PackageManager manager = mContext.getPackageManager();
                PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
                swversion = info.versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Web service
            String result = WebService.AppUpdateVersion(swversion);

            //Check update status
            Boolean isUpdate = false;
            String updatestatus = "", appversion = "";
            if(!result.isEmpty()){
                try {
                    Document doc = DocumentHelper.parseText(result);
                    Element rootElt = doc.getRootElement();
                    updatestatus = rootElt.elementTextTrim("updatestatus");
                    appversion = rootElt.elementTextTrim("appversion");
                    Log.d(TAG, "updatestatus = " + updatestatus + ", appversion = " + appversion);

                    switch(updatestatus){
                        case "0":
                            isUpdate = false;
                            break;
                        case "1":
                            isUpdate = true;
                            break;
                        case "9":
                            isUpdate = false;
                            break;
                        default:
                            isUpdate = false;
                            break;
                    }
                }catch (DocumentException e){
                    e.printStackTrace();
                }catch (NoSuchElementException e){
                    e.printStackTrace();
                }
            }

            //downlad and install app
            if(isUpdate){
                WebService.GetServerAddress();
                String[] removeHttp = mTCPAddress.split("/");
                String[] address_port = removeHttp[removeHttp.length-1].split(":");

                if(address_port.length!=2) return false;

                String ip = address_port[0];
                String port = address_port[1];
                //Log.d(TAG, "mTCPAddress = "+mTCPAddress+", ip = "+ip+", port = "+port);
                if(!ip.isEmpty() && !port.isEmpty()){
                    Intent intent = new Intent(mContext, WebServiceSocket.class);
                    intent.putExtra("ip", ip);
                    intent.putExtra("port", port);
                    intent.putExtra("method", "AppUpdate");
                    intent.putExtra("id", SystemProperties.get("ro.serialno"));
                    mContext.startService(intent);
                    //Todo : Start download App socket
                }
                return true;
            }else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false, mProcessView, mMainView);
            if(success){
                String msg = mContext.getResources().getString(R.string.app_update_success);
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }else {
                String msg = mContext.getResources().getString(R.string.app_update_failed);
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false, mProcessView, mMainView);
            String msg = mContext.getResources().getString(R.string.app_update_failed);
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    //定义一个获取TCP地址的方法：
    private static void GetServerAddress() {
        Log.d(TAG,"GetServerAddress");
        SoapObject soapObject = new SoapObject(mNameSpace, GetServerAddressMethod);
        soapObject.addProperty("deviceID", SystemProperties.get("ro.serialno"));
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(KKWebServiceUrl);
        try {
            httpTransportSE.call(GetServerAddressSoapAction, envelope);
            //Log.d(TAG,"调用WebService服务成功");
        } catch (Exception e) {
            //Log.d(TAG,"调用WebService服务失败");
            e.printStackTrace();
        }

        //获得服务返回的数据,并且开始解析
        try {
            SoapObject object = (SoapObject) envelope.bodyIn;
            //System.out.println("获得服务数据");
            mTCPAddress = object.getProperty(0).toString();
            if (null == mTCPAddress ||mTCPAddress.equals("anyType{}") || mTCPAddress.equals("[]")) {
                mTCPAddress="";
            }
            Log.d(TAG, "mTCPAddress : "+mTCPAddress);
        }catch (ClassCastException e){
            SoapFault fault = (SoapFault) envelope.bodyIn;
            Log.d(TAG, "Error message : "+fault.toString());
        }
    }

    //定义一个获取初始化信息的方法：
    private static String DevInitialise() {
        Log.d(TAG,"DevInitialise");
        String result = "";
        SoapObject soapObject = new SoapObject(mNameSpace, DevInitialiseMethod);
        soapObject.addProperty("deviceID", SystemProperties.get("ro.serialno"));
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(KKWebServiceUrl);
        try {
            httpTransportSE.call(DevInitialiseSoapAction, envelope);
            //Log.d(TAG,"调用WebService服务成功");
        } catch (Exception e) {
            //Log.d(TAG,"调用WebService服务失败");
            e.printStackTrace();
        }

        //获得服务返回的数据,并且开始解析
        try {
            SoapObject object = (SoapObject) envelope.bodyIn;
            //System.out.println("获得服务数据");
            result = object.getProperty(0).toString();
            if (null == result ||result.equals("anyType{}") || result.equals("[]")) {
                result="";
            }
            Log.d(TAG, "DevInitialise result : "+result);
        }catch (ClassCastException e){
            SoapFault fault = (SoapFault) envelope.bodyIn;
            Log.d(TAG, "Error message : "+fault.toString());
        }
        return result;
    }

    //定义一个检查APP更新版本信息的方法：
    private static String AppUpdateVersion(String swversion) {
        Log.d(TAG,"AppUpdateVersion");
        String result = "";
        SoapObject soapObject = new SoapObject(mNameSpace, AppUpdateVersionMethod);
        soapObject.addProperty("deviceID", SystemProperties.get("ro.serialno"));
        soapObject.addProperty("devVersion", swversion);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(KKWebServiceUrl);
        try {
            httpTransportSE.call(AppUpdateVersionSoapAction, envelope);
            //Log.d(TAG,"调用WebService服务成功");
        } catch (Exception e) {
            //Log.d(TAG,"调用WebService服务失败");
            e.printStackTrace();
        }

        //获得服务返回的数据,并且开始解析
        try {
            SoapObject object = (SoapObject) envelope.bodyIn;
            //System.out.println("获得服务数据");
            result = object.getProperty(0).toString();
            if (null == result ||result.equals("anyType{}") || result.equals("[]")) {
                result="";
            }
            Log.d(TAG, "AppUpdateVersion result : "+result);
        }catch (ClassCastException e){
            SoapFault fault = (SoapFault) envelope.bodyIn;
            Log.d(TAG, "Error message : "+fault.toString());
        }
        return result;
    }
    //定义一个上报现勘信息的方法：
    public String ReportSceneInfo(String sceneID,String unitCode,String userName,String userID,String deviceID,byte[] sceneData) {
        String result = "";
        SoapObject soapObject = new SoapObject(mNameSpace, SubmitSceneInfoMethod);
        soapObject.addProperty("sceneID", sceneID);
        soapObject.addProperty("unitCode", unitCode);
        soapObject.addProperty("userName", userName);
        soapObject.addProperty("userID", userID);
        soapObject.addProperty("deviceID", deviceID);
        soapObject.addProperty("sceneData", sceneData);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        //使用base64编码
        new MarshalBase64().register(envelope);
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(KKWebServiceUrl,300000);
        try {
            httpTransportSE.call(SubmitSceneInfoSoapAction, envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获得服务返回的数据,并且开始解析
        try {
            SoapObject object = (SoapObject) envelope.bodyIn;
            result = object.getProperty(0).toString();
            if (null == result ||result.equals("anyType{}") || result.equals("[]")) {
                result="";
            }
        }catch (Exception e){
            result="";
        }
        return result;
    }
    //定义一个获取现勘编号的方法：
    private static String GetSceneNo(String sceneId) {
        Log.d(TAG,"GetSceneNo");
        String result = "";
        SoapObject soapObject = new SoapObject(mNameSpace, GetSceneNoMethod);
        soapObject.addProperty("deviceID", SystemProperties.get("ro.serialno"));
        soapObject.addProperty("sceneID", sceneId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(KKWebServiceUrl);
        try {
            httpTransportSE.call(GetSceneNoSoapAction, envelope);
            //Log.d(TAG,"调用WebService服务成功");
        } catch (Exception e) {
            //Log.d(TAG,"调用WebService服务失败");
            e.printStackTrace();
        }

        //获得服务返回的数据,并且开始解析
        try {
            SoapObject object = (SoapObject) envelope.bodyIn;
            //System.out.println("获得服务数据");
            result = object.getProperty(0).toString();
            if (null == result ||result.equals("anyType{}") || result.equals("[]")) {
                result="";
            }
            Log.d(TAG, "GetSceneNo result : "+result);
        }catch (ClassCastException e){
            SoapFault fault = (SoapFault) envelope.bodyIn;
            Log.d(TAG, "Error message : "+fault.toString());
        }
        return result;
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showProgress(final boolean show, View processView, View mainView) {
        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        processView.setVisibility(show ? View.VISIBLE : View.GONE);
        mainView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
