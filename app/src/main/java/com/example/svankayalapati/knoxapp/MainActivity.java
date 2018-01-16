package com.example.svankayalapati.knoxapp;


import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.LocationPolicy;
import android.app.enterprise.PhoneRestrictionPolicy;
import android.app.enterprise.RestrictionPolicy;
import android.app.enterprise.WifiPolicy;
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

import com.sec.enterprise.knox.container.KnoxContainerManager;
import com.sec.enterprise.knox.license.KnoxEnterpriseLicenseManager;

import static com.sec.enterprise.knox.container.KnoxContainerManager.CONTAINER_CREATION_REQUEST_ID;
import static com.sec.enterprise.knox.container.KnoxContainerManager.CONTAINER_CREATION_STATUS_CODE;
import static com.sec.enterprise.knox.container.KnoxContainerManager.ERROR_INTERNAL_ERROR;

import org.json.JSONArray;

/**
 * This app was created to demonstrate the B2B SDKs functionality.
 *
 */
public class MainActivity extends Activity {

    static TextView logView;
    private Button btn1;
    private Button btn2;
    private Button buttonELM;
    private Button buttonKLM;
    private Button buttonContainer;
    static KnoxContainerManager kcm;

    static final int DEVICE_ADMIN_ADD_RESULT_ENABLE = 1;
    static Context ctx;

    // IMPORTANT:
    // You need to get your own Samsung ELM License from seap.samsung.com/developer
    // Replace the string below with your actual ELM License key.
    //
    // DO NOT HARDCODE YOUR LICENSE CODE IN PRODUCTION!
    // Android apps can be decompiled, exposing your unique license key.
    // Normally you will send the ELM License key to the app over an encrypted connection
    // from your solution's cloud service.
    private final static String demoELMKey = "51727267860CF70DEE5F6F990C742FF2B42DB124B0A5848503D88DC91BC0AF7E042F9954C837DE80AEE37BA25CFFCB5A58906932B0314C7FFCF3617FE403A6B1";
    static DevicePolicyManager dpm;
    static EnterpriseDeviceManager edm;
    static EnterpriseLicenseManager elm;
    static KnoxEnterpriseLicenseManager klmManager;
    private ComponentName mDeviceAdmin;
    private KeyguardManager mKeyguardManager;

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
                    //btn2.setEnabled(true);
                    //btn1.setEnabled(false);
                    break;
            }
        }

        if (requestCode == 2) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                //if (tryEncrypt()) {
                //  showPurchaseConfirmation();
                //}
            } else {
                // The user canceled or didn’t complete the lock screen
                // operation. Go to error/cancellation flow.
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void createContainer() {
        int mRequestid;
        log("Create Container 1");

        // IntentFilter filter = new IntentFilter();
        //filter.addAction(KnoxContainerManager.INTENT_CONTAINER_CREATION_STATUS);
        // context.registerReceiver(mCreationStatusReceiver, filter);
        log("Create Container 2");


        try {
            mRequestid = KnoxContainerManager.createContainer("knox-b2b-lwc","com.example.svankayalapati.knoxapp");
            log("mRequestID: " +mRequestid);
            if(mRequestid < 0) {
                switch(mRequestid) {
                    case ERROR_INTERNAL_ERROR:
                }
            } else {
                log("Container creation in progress with id:" + mRequestid);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            log("############" +e.getMessage());

        }
        log("Create Container 3");

    }

    public void activateELMWithAdminEnabled(String elmKey) {
        System.out.println("BaseModel.activateELMWithAdminEnabled()");
        EnterpriseLicenseManager elmMgr = null;

        try {
           if (elmMgr == null) {
                elmMgr = EnterpriseLicenseManager.getInstance(this.getApplicationContext());
            }
       // MainActivity.elm.activateLicense(elmKey);
           elmMgr.activateLicense(elmKey);
        } catch (Exception e) {
            log(e.getMessage());

        }
    }

    public void activateKLMWithAdminEnabled(String klmsKey) {
        System.out.println("BaseModel.activateKLMWithAdminEnabled()");
        System.out.println("just about to activate KLM");

        KnoxEnterpriseLicenseManager klmsMgr = null;

        try {
            if (klmsMgr == null) {
                klmsMgr = KnoxEnterpriseLicenseManager.getInstance(this.getApplicationContext());
            }

          klmsMgr.activateLicense(klmsKey);
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();

        btn1 = (Button) findViewById(R.id.button);
        buttonELM = (Button) findViewById(R.id.buttonELM);
        buttonKLM = (Button) findViewById(R.id.buttonKLM);
        buttonContainer = (Button) findViewById(R.id.buttonContainer);



        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                revertRestrictions();
                finish();

            }
        });

        buttonELM.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                activateELMWithAdminEnabled("4A8C019C25B9D64343EA4CEF8A141CD7D419169224B65D676A1E4DD664DDB76BDECF2B374D63B090298449459A2B8DEEC46CC7D553A68ED4AB78AB8F6E353646");
               // MainActivity.elm.activateLicense("6DEEF1797015B5478FF953C6DD9E6D03B52AB657A2F9AF2C827ECDC9A0E8A64BF0B9A6C503E1F37EE9F523BAE7F58102B50EFD82E379C33B0102294E8746F416");

            }
        });

        buttonContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

               createContainer();
            }
        });


        buttonKLM.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

               // activateKLMWithAdminEnabled("KLM06-HXHE8-BGH70-3O3K5-7Y3HF-2XUNP");
                activateKLMWithAdminEnabled("KLM03-64XNS-ZT6V9-LFMHZ-4MJ9E-9R6JA");

            }
        });

        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        //showAuthenticationScreen();

        klmManager = KnoxEnterpriseLicenseManager.getInstance(this);
        edm = new EnterpriseDeviceManager(this);
        elm = EnterpriseLicenseManager.getInstance(this);

        logView = (TextView) findViewById(R.id.log_id);
        logView.setMovementMethod(new ScrollingMovementMethod());
        mDeviceAdmin = new ComponentName(MainActivity.this, SampleAdminReceiver.class);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (dpm.isAdminActive(mDeviceAdmin) == false) {
            processOne();
        } else {

            activateELMWithAdminEnabled("51727267860CF70DEE5F6F990C742FF2B42DB124B0A5848503D88DC91BC0AF7E042F9954C837DE80AEE37BA25CFFCB5A58906932B0314C7FFCF3617FE403A6B1");
            activateKLMWithAdminEnabled("KLM06-HXHE8-BGH70-3O3K5-7Y3HF-2XUNP");

           // applyRestrictions();
            createContainer();

        }

        // public boolean setAutomaticConnectionToWifi(false);


    }


    static void applyRestrictions() {

        RestrictionPolicy rp = MainActivity.edm.getRestrictionPolicy();
        WifiPolicy wp = edm.getWifiPolicy();
        PhoneRestrictionPolicy prp = edm.getPhoneRestrictionPolicy();
        LocationPolicy lp = edm.getLocationPolicy();


        boolean cameraEnabled = rp.isCameraEnabled(false);

        // Toggle camera functionality
        try {
            if (cameraEnabled) {
                rp.setCameraState(!cameraEnabled);
                MainActivity.log("Set camera enabled to: " + !cameraEnabled);
            }
            boolean ret = wp.isOpenWifiApAllowed();

            MainActivity.log("isOpenWifiAllowed: " + ret);

            // Dissallowing the Open Wi=Fi
            ret = wp.allowOpenWifiAp(false);
            MainActivity.log("allowOpenWifiApReturned: " + ret);

            ret = wp.isOpenWifiApAllowed();
            MainActivity.log("isOpenWifiAllowed: " + ret);

            ret = wp.setAutomaticConnectionToWifi(false);
            MainActivity.log("setAutomaticConnectionToWiFi: " + ret);

            ret = rp.allowWiFi(false);
            MainActivity.log("allowWiFi: " + ret);

            ret = rp.allowWifiDirect(false);
            MainActivity.log("allowWifiDirect: " + ret);

            ret = rp.setScreenCapture(true); // Make it False
            MainActivity.log("setScreenCapture: " + ret);

            ret = rp.isScreenCaptureEnabled(true);
            MainActivity.log("isScreenCaptureEnabled: " + ret);

            ret = prp.allowIncomingSms(false);
            MainActivity.log("allowIncomingSMS: " + ret);

            ret = prp.allowIncomingMms(false);
            MainActivity.log("allowIncomingMMS: " + ret);

            ret = prp.allowOutgoingSms(false);
            MainActivity.log("allowOutgoingSMS: " + ret);

            ret = prp.allowOutgoingMms(false);
            MainActivity.log("allowOutgoingMMS: " + ret);

            ret = lp.isGPSOn();
            MainActivity.log("isGPSOn: " +ret);

            ret = lp.setGPSStateChangeAllowed(false);
            MainActivity.log("setGPSStateChangeAllowed: " + ret);

            ret = lp.startGPS(false);
            MainActivity.log("startGPS: " + ret);


        } catch (SecurityException e) {
            MainActivity.log("Exception: " + e);
            MainActivity.log("Activating license.");
            MainActivity.log("Have you remembered to change the demoELMKey in the source code?");
            // This exception indicates that the ELM policy has not been activated, so we activate
            // it now. Note that embedding the license in the code is unsafe and it is done here for
            // demonstration purposes only.
        }
    }


    static void revertRestrictions() {

        RestrictionPolicy rp = MainActivity.edm.getRestrictionPolicy();
        WifiPolicy wp = edm.getWifiPolicy();
        PhoneRestrictionPolicy prp = edm.getPhoneRestrictionPolicy();
        LocationPolicy lp = edm.getLocationPolicy();

        boolean cameraEnabled = rp.isCameraEnabled(false);

        // Toggle camera functionality
        try {
            if (!cameraEnabled) {
                rp.setCameraState(!cameraEnabled);
                MainActivity.log("Set camera enabled to: " + !cameraEnabled);
            }

            boolean ret = wp.isOpenWifiApAllowed();

            MainActivity.log("isOpenWifiAllowed: " + ret);

            // Dissallowing the Open Wi=Fi
            ret = wp.allowOpenWifiAp(true);
            MainActivity.log("allowOpenWifiApReturned: " + ret);

            ret = wp.isOpenWifiApAllowed();
            MainActivity.log("isOpenWifiAllowed: " + ret);

            ret = wp.setAutomaticConnectionToWifi(true);
            MainActivity.log("setAutomaticConnectionToWiFi: " + ret);

            ret = rp.allowWiFi(true);
            MainActivity.log("allowWiFi: " + ret);

            ret = rp.allowWifiDirect(true);
            MainActivity.log("allowWifiDirect: " + ret);

            ret = rp.setScreenCapture(true);
            MainActivity.log("setScreenCapture: " + ret);

            ret = rp.isScreenCaptureEnabled(false);
            MainActivity.log("isScreenCaptureEnabled: " + ret);

            ret = prp.allowIncomingSms(true);
            MainActivity.log("allowIncomingSMS: " + ret);

            ret = prp.allowIncomingMms(true);
            MainActivity.log("allowIncomingMMS: " + ret);

            ret = prp.allowOutgoingSms(true);
            MainActivity.log("allowOutgoingSMS: " + ret);

            ret = prp.allowOutgoingMms(true);
            MainActivity.log("allowOutgoingMMS: " + ret);

            ret = lp.isGPSOn();
            MainActivity.log("isGPSOn: " + ret);

            ret = lp.setGPSStateChangeAllowed(true);
            MainActivity.log("setGPSStateChangeAllowed: " + ret);

            ret = lp.startGPS(true);
            MainActivity.log("startGPS: " + ret);


        } catch (SecurityException e) {
            MainActivity.log("Exception: " + e);
            MainActivity.log("Activating license.");
            MainActivity.log("Have you remembered to change the demoELMKey in the source code?");
            // This exception indicates that the ELM policy has not been activated, so we activate
            // it now. Note that embedding the license in the code is unsafe and it is done here for
            // demonstration purposes only.
        }

    }

    private void showAuthenticationScreen() {
        // Create the Confirm Credentials screen. You can customize the title and description. Or
        // we will provide a generic one for you if you leave it null
        Intent intent = mKeyguardManager.createConfirmDeviceCredentialIntent(null, null);
        if (intent != null) {
            startActivityForResult(intent, 2);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Hello", "######");
   /*
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
        }*/


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
     * Logs a line of text to the app log.
     *
     * @param text
     */
    public static void log(String text) {
        logView.append(text);
        logView.append("\n");
        logView.invalidate();
    }
}
