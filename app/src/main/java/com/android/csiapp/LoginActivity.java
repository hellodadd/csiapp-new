package com.android.csiapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.csiapp.Crime.utils.AppInfo;
import com.android.csiapp.Crime.utils.RestoreListDialog;
import com.android.csiapp.Databases.IdentifyProvider;
import com.android.csiapp.WebServiceTransmission.WebService;
import com.android.csiapp.XmlHandler.DataInitial;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

/**
 * A login screen that offers login via user/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Context context = null;

    private IdentifyProvider mIdentify;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mDataRecorvy;
    private Button mInitialDevice;

    //app version
    private TextView mAppVersionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.login);
        getWindow().setBackgroundDrawableResource(R.drawable.locked_background);
        context = this.getApplicationContext();
        // Set up the login form.
        mUserView = (EditText) findViewById(R.id.user);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mIdentify = new IdentifyProvider(context);
        getLastUserName();

        Button mUserSignInButton = (Button) findViewById(R.id.user_sign_in_button);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mDataRecorvy  = (Button) findViewById(R.id.data_recorvy_button);
        mDataRecorvy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RestoreListDialog restoreListDialog = new RestoreListDialog(LoginActivity.this);
                restoreListDialog.createRestoreListDialog();
            }
        });
        mInitialDevice = (Button) findViewById(R.id.device_initial_button);
        mInitialDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService webService = new WebService(context);
                webService.DeviceInitial(mProgressView, mLoginFormView);
            }
        });

        mAppVersionTv = (TextView) findViewById(R.id.display_app_version);
        String appVerStr = "版本:" + AppInfo.getAppVersionName(context);
        mAppVersionTv.setText(appVerStr);
    }

    private void getLastUserName() {
        if (mIdentify.getCount() == 0) {
            mIdentify.sample();
            mUserView.setText(mIdentify.getUser());
            return;
        }

        SharedPreferences prefs = context.getSharedPreferences("LoginName", 0);
        mUserView.setText(prefs.getString("loginname", ""));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid user id, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
        password = MD5(password);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user.
        if (TextUtils.isEmpty(user)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if (!isUserValid(user)) {
            mUserView.setError(getString(R.string.error_invalid_user));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(user, password);
            mAuthTask.execute((Void) null);
        }
    }

    //MD5加密，32位
    public String MD5(String str)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for(int i = 0; i < charArray.length; i++)
        {
            byteArray[i] = (byte)charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for( int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int)md5Bytes[i])&0xff;
            if(val < 16)
            {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    private boolean isUserValid(String user) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /*
     *直接使用本地文件初始化
     * add by liwei 2017.2.25
     * ==========================================================
     */
    public void onBtnTestClick(View view) {
        new DeviceInitialLocalTask(mProgressView,mLoginFormView).execute();
    }

    public class DeviceInitialLocalTask extends AsyncTask<Void, Void, Boolean> {
        private View mProcessView1 = null, mMainView1 = null;
        DeviceInitialLocalTask(View processView, View mainView){
            this.mProcessView1 = processView;
            this.mMainView1 = mainView;
        }
        private String getLocalBaseInfo(){
            AssetManager am= getResources().getAssets();
            String content = "";
            try
            {
                InputStream instream=am.open("baseinfo.info");
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line; //分行读取
                    while (( line = buffreader.readLine()) != null)
                    {
                        content += line;
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                String s=e.getMessage();
            } catch (IOException e)
            {
                String s=e.getMessage();
            }
            return content;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //Web Service
            String result = getLocalBaseInfo();
            //Initial Device
            if(!result.isEmpty()){
                try {
                    Document doc = DocumentHelper.parseText(result);
                    DataInitial dataInitial = new DataInitial(context);
                    Boolean InitResult = dataInitial.InitialDevice(doc);
                }catch (DocumentException e){
                    e.printStackTrace();
                    return false;
                }
                return true;
            }else {
                return false;
            }
        }
    }
    //===============================================================

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;

        UserLoginTask(String user, String password) {
            mUser = user;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUser)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            SharedPreferences prefs = context.getSharedPreferences("InitialDevice", 0);
            String initstatus = prefs.getString("Initial", "0");

            if (success && mIdentify.checkPasswordFromName(mUser, mPassword) && initstatus.equalsIgnoreCase("1")) {
                SharedPreferences.Editor editor1 = context.getSharedPreferences("LoginName", 0).edit();
                editor1.putString("loginname", mUser);
                editor1.commit();

                IdentifyProvider identifyProvider = new IdentifyProvider(context);

                String UnitCode = identifyProvider.getUnitCode(mUser);
                SharedPreferences.Editor editor2 = context.getSharedPreferences("UnitCode", 0).edit();
                editor2.putString("unitcode", UnitCode);
                editor2.commit();

                String UserName = identifyProvider.getUserName(mUser);
                SharedPreferences.Editor editor3 = context.getSharedPreferences("UserName", 0).edit();
                editor3.putString("username", UserName);
                editor3.commit();

                String UnitName = identifyProvider.getUnitName(mUser);
                SharedPreferences.Editor editor4 = context.getSharedPreferences("UnitName", 0).edit();
                editor4.putString("unitname", UnitName);
                editor4.commit();

                Intent it = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            } else if(!initstatus.equalsIgnoreCase("1")){
                mPasswordView.setError(getString(R.string.error_initial_device));
                mPasswordView.requestFocus();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

