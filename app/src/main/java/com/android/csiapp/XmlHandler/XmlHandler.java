package com.android.csiapp.XmlHandler;

/**
 * Created by JOWE on 2016/9/29.
 */
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.dom4j.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class XmlHandler {

    private final static String TAG = "XmlHandler";
    private void createDeviceMsgXmlFile(Object obj) {
        XmlSerializer xmlSerializer = null;
        FileOutputStream fileOutputStream = null;
        String enter = System.getProperty("line.separator");
        List<Device> devices = (List<Device>)obj;
        //List<Dictionary> dictionaryList = createData();
        //List<Dictionary> dictionaryList = createData();

        try {
            //获取xmlSerializer
            xmlSerializer = Xml.newSerializer();
            File file = new File(Environment.getExternalStorageDirectory(), "DeviceMsg.xml");
            fileOutputStream = new FileOutputStream(file);

            String encoding = "utf-8";
            xmlSerializer.setOutput(fileOutputStream, encoding);
            xmlSerializer.startDocument(encoding, null);
            changeLine(xmlSerializer, enter);
            //根节点开始

            xmlSerializer.startTag(null, "device");
            changeLine(xmlSerializer, enter);

            //内容结点
            for(Device d: devices) {

                xmlSerializer.startTag(null, d.getDeviceId()[0]);
                xmlSerializer.text(d.getDeviceId()[1]);
                xmlSerializer.endTag(null, d.getDeviceId()[0]);
                changeLine(xmlSerializer, enter);

                xmlSerializer.startTag(null, d.getInitStatus()[0]);
                xmlSerializer.text(d.getInitStatus()[1]);
                xmlSerializer.endTag(null, d.getInitStatus()[0]);
                changeLine(xmlSerializer, enter);

                xmlSerializer.startTag(null, d.getSwVersion()[0]);
                xmlSerializer.text(d.getSwVersion()[1]);
                xmlSerializer.endTag(null, d.getSwVersion()[0]);
                changeLine(xmlSerializer, enter);

                xmlSerializer.startTag(null, d.getMapVersion()[0]);
                xmlSerializer.text(d.getMapVersion()[1]);
                xmlSerializer.endTag(null, d.getMapVersion()[0]);
                changeLine(xmlSerializer, enter);

            }

            //根节点结束
            xmlSerializer.endTag(null, "device");
            xmlSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createSuccessDeleteMsgFile(Object obj) {
        XmlSerializer xmlSerializer = null;
        FileOutputStream fileOutputStream = null;
        String enter = System.getProperty("line.separator");
        List<String> result = (List<String>) obj;

        try {
            //获取xmlSerializer
            xmlSerializer = Xml.newSerializer();
            File file = new File(Environment.getExternalStorageDirectory(), "SuccessToDelete.xml");
            fileOutputStream = new FileOutputStream(file);

            String encoding = "utf-8";
            xmlSerializer.setOutput(fileOutputStream, encoding);
            xmlSerializer.startDocument(encoding, null);
            changeLine(xmlSerializer, enter);
            //根节点开始

            xmlSerializer.startTag(null, "delete");
            changeLine(xmlSerializer, enter);

            //内容结点
            for (String d : result) {
                xmlSerializer.startTag(null, "sceneid");
                xmlSerializer.text(d);
                xmlSerializer.endTag(null, "sceneid");
                changeLine(xmlSerializer, enter);
            }

            //根节点结束
            xmlSerializer.endTag(null, "delete");
            xmlSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void changeLine(XmlSerializer serializer, String enter){
        try{
            serializer.text(enter);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void createDeviceMsg(String deviceid, String initstatus, String swversion, String mapversion ) {
        List<Device> devices = new ArrayList<Device>();
        Device device = new Device();

        device.setDeviceId(deviceid);
        device.setInitStatus(initstatus);
        device.setSwVersion(swversion);
        device.setMapVersion(mapversion);
        devices.add(device);
        createDeviceMsgXmlFile(devices);
    }

    public Object[] getInitialDeviceCmd() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "InitDeviceCmd.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fileInputStream, "utf-8");
            int eventType = parser.getEventType();
            Dictionary dictionary =null;
            User user = null;
            List<Dictionary> dictionarys = null;
            List<User> users = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("dictionarys")) {
                            dictionarys = new ArrayList<Dictionary>();
                        } else if (name.equalsIgnoreCase("dictionary")) {
                            dictionary = new Dictionary();
                        } else if (name.equalsIgnoreCase("dictkey")) {
                            eventType = parser.next();
                            dictionary.setDictKey(parser.getText());
                        } else if (name.equalsIgnoreCase("parentkey")) {
                            eventType = parser.next();
                            dictionary.setParentKey(parser.getText());
                        } else if (name.equalsIgnoreCase("rootkey")) {
                            eventType = parser.next();
                            dictionary.setRootKey(parser.getText());
                        } else if (name.equalsIgnoreCase("dictvalue")) {
                            eventType = parser.next();
                            dictionary.setDictValue(parser.getText());
                        } else if (name.equalsIgnoreCase("dictremark")) {
                            eventType = parser.next();
                            dictionary.setDictRemark(parser.getText());
                        } else if (name.equalsIgnoreCase("dictspell")) {
                            eventType = parser.next();
                            dictionary.setDictSpell(parser.getText());
                        } else if (name.equalsIgnoreCase("users")) {
                            users = new ArrayList<User>();
                        } else if (name.equalsIgnoreCase("user")) {
                            user = new User();
                        } else if (name.equalsIgnoreCase("loginname")) {
                            eventType = parser.next();
                            user.setLoginName(parser.getText());
                        } else if (name.equalsIgnoreCase("password")) {
                            eventType = parser.next();
                            user.setPassword(parser.getText());
                        } else if (name.equalsIgnoreCase("username")) {
                            eventType = parser.next();
                            user.setUserName(parser.getText());
                        } else if (name.equalsIgnoreCase("unitcode")) {
                            eventType = parser.next();
                            user.setUnitCode(parser.getText());
                        } else if (name.equalsIgnoreCase("unitname")) {
                            eventType = parser.next();
                            user.setUnitName(parser.getText());
                        } else if (name.equalsIgnoreCase("idcardno")) {
                            eventType = parser.next();
                            user.setIdCardNo(parser.getText());
                        } else if (name.equalsIgnoreCase("contact")) {
                            eventType = parser.next();
                            user.setContact(parser.getText());
                        } else if (name.equalsIgnoreCase("duty")) {
                            eventType = parser.next();
                            user.setDuty(parser.getText());
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equalsIgnoreCase("dictionary")) {
                            if (dictionarys!=null) {
                                dictionarys.add(dictionary);
                            }
                            dictionary = null;
                        } else if(parser.getName().equalsIgnoreCase("user")) {
                            if (users != null) {
                                users.add(user);
                            }
                            user = null;
                        }
                        break;
                }

                eventType = parser.next();

            }
            fileInputStream.close();
            return new Object[]{dictionarys, users};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public  Object[] getInitialDeviceCmd(Document doc) {
        try{
            Dictionary dictionary = new Dictionary();
            User user = new User();
            List<Dictionary> dictionarys = new ArrayList<Dictionary>();
            List<User> users = new ArrayList<User>();
            Element rootElt = doc.getRootElement();

            Iterator dictionarys_iter = rootElt.elementIterator("dictionarys");
            Element dictionarysEle = (Element) dictionarys_iter.next();
            Iterator dictionary_iter = dictionarysEle.elementIterator("dictionary");
            while (dictionary_iter.hasNext()){
                Element dictionaryEle = (Element) dictionary_iter.next();
                dictionary.setDictKey(dictionaryEle.elementTextTrim("dictkey"));
                dictionary.setParentKey(dictionaryEle.elementTextTrim("parentkey"));
                dictionary.setRootKey(dictionaryEle.elementTextTrim("rootkey"));
                dictionary.setDictValue(dictionaryEle.elementTextTrim("dictvalue"));
                dictionary.setDictRemark(dictionaryEle.elementTextTrim("dictremark"));
                dictionary.setDictSpell(dictionaryEle.elementTextTrim("dictspell"));
                dictionarys.add(dictionary);
                dictionary = new Dictionary();
            }

            Iterator users_iter = rootElt.elementIterator("users");
            Element usersEle = (Element) users_iter.next();
            Iterator user_iter = usersEle.elementIterator("user");
            while (user_iter.hasNext()){
                Element userEle = (Element) user_iter.next();
                user.setLoginName(userEle.elementTextTrim("loginname"));
                user.setPassword(userEle.elementTextTrim("password"));
                user.setUserName(userEle.elementTextTrim("username"));
                user.setUnitCode(userEle.elementTextTrim("unitcode"));
                user.setUserName(userEle.elementTextTrim("unitname"));
                user.setIdCardNo(userEle.elementTextTrim("idcardno"));
                users.add(user);
                user = new User();
            }
            return new Object[]{dictionarys, users};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getSceneListCmd() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "getSceneListCmd.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fileInputStream, "utf-8");
            int eventType = parser.getEventType();
            String loginName = "";
            String unitCode = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("loginname")) {
                            parser.next();
                            loginName = parser.getText();
                        } else if (name.equalsIgnoreCase("unitcode")) {
                            parser.next();
                            unitCode = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();

            }
            fileInputStream.close();
            return new String[]{loginName, unitCode};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] writeSceneIdCmd() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "writeSceneIdCmd.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fileInputStream, "utf-8");
            int eventType = parser.getEventType();
            String sceneId = "";
            String sceneNo = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("sceneid")) {
                            eventType = parser.next();
                            sceneId = parser.getText();
                        } else if (name.equalsIgnoreCase("sceneno")) {
                            eventType = parser.next();
                            sceneNo = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
            fileInputStream.close();
            return new String[]{sceneId, sceneNo};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> deleteSceneInfoCmd() {
        List<String> sceneId = new ArrayList<String>();
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "deleteSceneInfoCmd.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fileInputStream, "utf-8");
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("sceneid")) {
                            eventType = parser.next();
                            sceneId.add(parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sceneId;
    }

    public void createSceneInfoXmlFile(Object[] obj) {
        XmlSerializer xmlSerializer;
        FileOutputStream fileOutputStream = null;

        if (obj.length < 5) {
            Log.d(TAG, "Data length  is incorrect");
            return;
        }

        try {
            xmlSerializer = Xml.newSerializer();
            File file = new File(Environment.getExternalStorageDirectory(), "ScenesMsg.xml");
            fileOutputStream = new FileOutputStream(file);

            String encoding = "utf-8";
            xmlSerializer.setOutput(fileOutputStream, encoding);
            xmlSerializer.startDocument(encoding, null);

            xmlSerializer.startTag(null, "datas");

            for (int i=0; i < obj.length; i++) {
                createSubSceneInfo (xmlSerializer, (List<HashMap<String, String>>) obj[i], getSceneTypes().get(String.valueOf(i)));
            }

            xmlSerializer.endTag(null, "datas");
            xmlSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createSubSceneInfo(XmlSerializer xmlSerializer, List<HashMap<String, String>> info, String tagstring) {

        if(info.size() < 0) {
            Log.d(TAG, "The Sub Scene information is incorrect !!");
            return;
        }
        try {
            xmlSerializer.startTag(null, tagstring+"s");
            for (int i = 0; i < info.size(); i++) {
                HashMap<String, String> map = info.get(i);
                xmlSerializer.startTag(null, tagstring);
                for (HashMap.Entry<String, String> entry : map.entrySet()) {
                    xmlSerializer.startTag(null, entry.getKey());
                    xmlSerializer.text(entry.getValue());
                    xmlSerializer.endTag(null, entry.getKey());
                }
            xmlSerializer.endTag(null, tagstring);
            }
            xmlSerializer.endTag(null, tagstring+"s");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> getSceneTypes() {

        LinkedHashMap<String, String> sceneTypes = new LinkedHashMap<>();

        sceneTypes.put("0","baseinfo");
        sceneTypes.put("1","peopleinfo");
        sceneTypes.put("2","goodsinfo");
        sceneTypes.put("3","traceinfo");
        sceneTypes.put("4","attachinfo");

        return sceneTypes;
    }
}
