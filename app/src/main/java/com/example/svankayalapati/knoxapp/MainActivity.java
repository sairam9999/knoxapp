package com.example.svankayalapati.knoxapp;


import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.RestrictionPolicy;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sec.enterprise.knox.license.KnoxEnterpriseLicenseManager;

import org.json.JSONArray;

/**
 * This app was created to demonstrate the B2B SDKs functionality.
 *
 * It simply toggles the camera availability ON/OFF.
 *
 * @author support@samsung.com
 *
 */
public class MainActivity extends Activity {

    private TextView logView;
    private Button btn1;
    private Button btn2;

    static final int DEVICE_ADMIN_ADD_RESULT_ENABLE = 1;

    // IMPORTANT:
    // You need to get your own Samsung ELM License from seap.samsung.com/developer
    // Replace the string below with your actual ELM License key.
    //
    // DO NOT HARDCODE YOUR LICENSE CODE IN PRODUCTION!
    // Android apps can be decompiled, exposing your unique license key.
    // Normally you will send the ELM License key to the app over an encrypted connection
    // from your solution's cloud service.
    private final static String demoELMKey = "51727267860CF70DEE5F6F990C742FF2B42DB124B0A5848503D88DC91BC0AF7E042F9954C837DE80AEE37BA25CFFCB5A58906932B0314C7FFCF3617FE403A6B1";
    private DevicePolicyManager dpm;
    private EnterpriseDeviceManager edm;
    private EnterpriseLicenseManager elm;
    private  KnoxEnterpriseLicenseManager klmManager;
    private ComponentName mDeviceAdmin;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("MainActivity.onActivityResult()");

        if (requestCode == DEVICE_ADMIN_ADD_RESULT_ENABLE) {

            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    log("Request failed.");
                    break;
                case Activity.RESULT_OK:
                    log("Device administrator activated.");
                    btn2.setEnabled(true);
                    btn1.setEnabled(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        klmManager=KnoxEnterpriseLicenseManager.getInstance(this);
        edm = new EnterpriseDeviceManager(this);
        elm = EnterpriseLicenseManager.getInstance(this);

        logView = (TextView) findViewById(R.id.log_id);
        logView.setMovementMethod(new ScrollingMovementMethod());

        processOne();


        klmManager.activateLicense("KLM06-HXHE8-BGH70-3O3K5-7Y3HF-2XUNP");

        elm.activateLicense("6DEEF1797015B5478FF953C6DD9E6D03B52AB657A2F9AF2C827ECDC9A0E8A64BF0B9A6C503E1F37EE9F523BAE7F58102B50EFD82E379C33B0102294E8746F416");

        RestrictionPolicy rp = edm.getRestrictionPolicy();

        boolean cameraEnabled = rp.isCameraEnabled(false);

        // Toggle camera functionality
        try {
            if(cameraEnabled) {
                rp.setCameraState(!cameraEnabled);
            }
            log("Set camera enabled to: " + !cameraEnabled);
        } catch (SecurityException e) {
            log("Exception: " + e);
            log("Activating license.");
            log("Have you remembered to change the demoELMKey in the source code?");
            // This exception indicates that the ELM policy has not been activated, so we activate
            // it now. Note that embedding the license in the code is unsafe and it is done here for
            // demonstration purposes only.
        }
     logView = (TextView) findViewById(R.id.log_id);
        logView.setMovementMethod(new ScrollingMovementMethod());

        btn1 = (Button) findViewById(R.id.btn1_id);
        btn2 = (Button) findViewById(R.id.btn2_id);

        mDeviceAdmin = new ComponentName(MainActivity.this, SampleAdminReceiver.class);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (dpm.isAdminActive(mDeviceAdmin)) {
            btn1.setEnabled(false);
            btn2.setEnabled(true);
        } else {
            btn1.setEnabled(true);
            btn2.setEnabled(false);
        }
        klmManager=KnoxEnterpriseLicenseManager.getInstance(this);
        edm = new EnterpriseDeviceManager(this);
        elm = EnterpriseLicenseManager.getInstance(this);

        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                processOne();
            }
        });

        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                processTwo();
            }
        });

        log("Activity log started. Scroll it up and down.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("inside on destroy");

        RestrictionPolicy rp = edm.getRestrictionPolicy();

        boolean cameraEnabled = rp.isCameraEnabled(false);

        // Toggle camera functionality
        try {
            if(!cameraEnabled) {
                rp.setCameraState(!cameraEnabled);
            }
            log("Set camera enabled to: " + !cameraEnabled);
        } catch (SecurityException e) {
            log("Exception: " + e);
            log("Activating license.");
            log("Have you remembered to change the demoELMKey in the source code?");
            // This exception indicates that the ELM policy has not been activated, so we activate
            // it now. Note that embedding the license in the code is unsafe and it is done here for
            // demonstration purposes only.
        }


    }

    /**
     * This method is invoked when Button#1 is pressed
     */
    private void processOne() {
        log("Activate new device administrator.");

        // This activity asks the user to grant device administrator rights to the app.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
        startActivityForResult(intent, DEVICE_ADMIN_ADD_RESULT_ENABLE);
    }

    /**
     * This method is invoked when Button#2 is pressed
     */
    private void processTwo() {

        RestrictionPolicy rp = edm.getRestrictionPolicy();

        boolean cameraEnabled = rp.isCameraEnabled(false);

        // Toggle camera functionality
        try {
            rp.setCameraState(!cameraEnabled);
            log("Set camera enabled to: " + !cameraEnabled);
        } catch (SecurityException e) {
            log("Exception: " + e);
            log("Activating license.");
            log("Have you remembered to change the demoELMKey in the source code?");
            // This exception indicates that the ELM policy has not been activated, so we activate
            // it now. Note that embedding the license in the code is unsafe and it is done here for
            // demonstration purposes only.

// Get an instance of the Knox License Manager

// Activate the Samsung Knox License key
            klmManager.activateLicense("KLM06-HXHE8-BGH70-3O3K5-7Y3HF-2XUNP");
// To optimize security, never hardcode your license key into your app; get it from a back-end server.
           // The example above is a simple demo only.

// Listen for broadcast intent edm.intent.action.knox_license.status

// Get an instance of the License Manager
            //EnterpriseLicenseManager elmManager = EnterpriseLicenseManager.getInstance(context);
// Activate the backwards-compatible backwards-compatible ELM license key
            elm.activateLicense("6DEEF1797015B5478FF953C6DD9E6D03B52AB657A2F9AF2C827ECDC9A0E8A64BF0B9A6C503E1F37EE9F523BAE7F58102B50EFD82E379C33B0102294E8746F416");
// To optimize security, never hardcode your license key into your app as shown above.
                   // Always retrieve it from a back-end server.

// Listen for broadcast intent edm.intent.action.license.status

           // elm.activateLicense(demoELMKey,this.getPackageName());



           /* JSONArray arr = elm.getApiCallDataByAdmin(this.getPackageName());
            if (arr == null){
                log("EnterpriseLicenseManager.getApiCallDataByAdmin() failed");
                return;
            }
            String result = arr.toString();
            log("EnterpriseLicenseManager.getApiCallDataByAdmin() result = " + result);*/


        } catch (Exception e) {
            log("Exception: " + e);
        }
    }

    /**
     * Logs a line of text to the app log.
     *
     * @param text
     */
    public void log(String text) {
        logView.append(text);
        logView.append("\n");
        logView.invalidate();
    }
}
