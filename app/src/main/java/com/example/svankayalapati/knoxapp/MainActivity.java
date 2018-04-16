package com.example.svankayalapati.knoxapp;


import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.app.enterprise.ApplicationPolicy;
import android.app.enterprise.BluetoothSecureModePolicy;
import android.app.enterprise.BluetoothSecureModeWhitelistConfig;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.LocationPolicy;
import android.app.enterprise.PhoneRestrictionPolicy;
import android.app.enterprise.RestrictionPolicy;
import android.app.enterprise.WifiPolicy;
import android.app.enterprise.kioskmode.KioskMode;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.bluetooth.BluetoothClass;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.sec.enterprise.knox.container.KnoxContainerManager;
import com.sec.enterprise.knox.license.KnoxEnterpriseLicenseManager;
import com.android.internal.telephony.*;

import static com.sec.enterprise.knox.container.KnoxContainerManager.CONTAINER_CREATION_REQUEST_ID;
import static com.sec.enterprise.knox.container.KnoxContainerManager.CONTAINER_CREATION_STATUS_CODE;
import static com.sec.enterprise.knox.container.KnoxContainerManager.ERROR_INTERNAL_ERROR;

import org.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


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
    private final static String demoELMKey = "4A8C019C25B9D64343EA4CEF8A141CD7D419169224B65D676A1E4DD664DDB76BDECF2B374D63B090298449459A2B8DEEC46CC7D553A68ED4AB78AB8F6E353646";
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

        /*

        try {
				// Get the container type selected by user
				String type = (String) Controller.getSharedPrefData(
						SAConstants.TYPE, SAConstants.STRING_DATATYPE);
				// Create the container of the user selected type. If the user
				// did not select any container type, knox-b2b is selected
				// as type by default
				int requestid = KnoxContainerManager.createContainer(type);

				// If an error is thrown
				if (requestid < 0) {
					// Get the message (to be displayed to user), by passing in
					// the error code
					String messageToUser = SACodeUtils.getMessage(requestid,
							mContext);
					// show toast message to user displaying the issue
					Controller.showToast(messageToUser);
				}
				// if container is created successfully
				else {
					Log.d(TAG, SAConstants.CONTAINER_CREATION_PROGRESS
							+ requestid);
					Controller
							.showToast(SAConstants.CONTAINER_CREATION_PROGRESS
									+ requestid);
				}
			} catch (SecurityException e) {
				Log.e(TAG, e.getMessage());
			}
         */



        // IntentFilter filter = new IntentFilter();
        //filter.addAction(KnoxContainerManager.INTENT_CONTAINER_CREATION_STATUS);
        // context.registerReceiver(mCreationStatusReceiver, filter);

        log("Create Container 2");


//        try {
//            String KNOX_B2B = "knox-b2b-lwc";
//
//            mRequestid = KnoxContainerManager.createContainer(KNOX_B2B);
//            log("mRequestID: " +mRequestid);
//            if(mRequestid < 0) {
//                switch(mRequestid) {
//                    case ERROR_INTERNAL_ERROR:
//                }
//            } else {
//                log("Container creation in progress with id:" + mRequestid);
//            }
//
//        } catch (SecurityException e) {
//            e.printStackTrace();
//            log("############" +e.getMessage());
//
//        }
//        log("Create Container 3");

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
    public void onResume() {
        super.onResume();  // Always call the superclass method first


        // Get the Camera instance as the activity achieves full user focus

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = this.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(this);
            }
            boolean abc = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            MainActivity.log("Hide Soft : " +abc);

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
               // MainActivity.elm.activateLicense("4A8C019C25B9D64343EA4CEF8A141CD7D419169224B65D676A1E4DD664DDB76BDECF2B374D63B090298449459A2B8DEEC46CC7D553A68ED4AB78AB8F6E353646");

            }
        });

        buttonContainer.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View view) {

               createContainer();
                /*ApplicationPolicy appPolicy = edm.getApplicationPolicy();
                String testpackageName = "com.example.svankayalapati.knoxapp";
                EnterpriseDeviceManager edm = (EnterpriseDeviceManager) getSystemService(edm.ENTERPRISE_POLICY_SERVICE);
                ApplicationPolicy appPolicy = edm.getApplicationPolicy();
                try {
                    boolean result = appPolicy.installApplication(testpackageName);
                    if (true == result) {
                        log("Installing an application package has been successful!");
                    } else {
                       log("Installing an application package has failed.");
                    }

                } catch (SecurityException e) {
                    log("SecurityException: " + e);
                }*/


            }
        });


        buttonKLM.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

               // activateKLMWithAdminEnabled("KLM03-64XNS-ZT6V9-LFMHZ-4MJ9E-9R6JA");
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

            activateELMWithAdminEnabled("4A8C019C25B9D64343EA4CEF8A141CD7D419169224B65D676A1E4DD664DDB76BDECF2B374D63B090298449459A2B8DEEC46CC7D553A68ED4AB78AB8F6E353646");
            activateKLMWithAdminEnabled("KLM03-64XNS-ZT6V9-LFMHZ-4MJ9E-9R6JA");

            applyRestrictions();
//            createContainer();

        }

        // public boolean setAutomaticConnectionToWifi(false);


    }


    static void applyRestrictions() {

        RestrictionPolicy rp = MainActivity.edm.getRestrictionPolicy();
        WifiPolicy wp = edm.getWifiPolicy();
        PhoneRestrictionPolicy prp = edm.getPhoneRestrictionPolicy();
        LocationPolicy lp = edm.getLocationPolicy();
        KioskMode kioskModeService = KioskMode.getInstance(ctx);
        BluetoothSecureModePolicy bluetoothSecureModePolicy = edm.getBluetoothSecureModePolicy();
        BluetoothSecureModeWhitelistConfig bluetoothSecureModeWhitelistConfig;

        /*KioskMode km = KioskMode.getInstance(ctx);
        ArrayList<Integer> arr = (ArrayList)km.getHardwareKeyList();
        for(Integer i : arr)
        {
            MainActivity.log("sairam" +i);
        }*/

        try {


            //Bluetooth WhiteList
            String name = "PERIPHERAL";
            int cod = BluetoothClass.Device.Major.UNCATEGORIZED;
            String uuids[] = {"00001101-0000-1000-8000-00805f9b34fb", "00001200-0000-1000-8000-00805f9b34fb"};
            BluetoothSecureModeWhitelistConfig whiteListConfig = new  BluetoothSecureModeWhitelistConfig();
            whiteListConfig.name = name;
            whiteListConfig.cod = cod;
            whiteListConfig.uuids = uuids;
           // boolean result = bluetoothSecureModePolicy.addBluetoothDevicesToWhiteList((List<BluetoothSecureModeWhitelistConfig>) whiteListConfig);

            // To disconnect the Outgoing Calls
            TelephonyManager telephonyManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
            Class clazz = Class.forName(telephonyManager.getClass().getName());
            Method method = clazz.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
            telephonyService.endCall();


            // To disable the Back Button
            List availableHwKeys = new ArrayList();
            availableHwKeys.add(new Integer(4)); //4 is keycode for Back Key
            //avaiableHwKeys.add(new Integer(25)); //25 is keycode for Volume Down
            availableHwKeys.add(new Integer(3));//3 is keycode for Windows key
            // disable provided keys
            List successBlockedKeys = kioskModeService.allowHardwareKeys(availableHwKeys, false);
            // successBlockedKeys is the list of the keys that were successfully blocked

               /* // enable blocked keys
               List successEnabledKeys = kioskModeService.allowHardwareKeys(availableHwKeys, true)) {
              //successEnabledKeys is the list of the keys that were successfully allowed*/

                } catch (SecurityException e) {
                    MainActivity.log( "SecurityException: " + e.getMessage());
                }

        catch (RemoteException re) {
            MainActivity.log("Exception: " + re);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (kioskModeService.allowMultiWindowMode(false)) {
            // multi window mode will be disabled.
        } else {
            // failure enabling policy
        }

        //Adding Incoming Call Functionality
        if (prp.addIncomingCallRestriction(".*")) {
            log( "Success adding incoming call restriction");
        } else {
            log("Failed adding incoming call restriction");
        }

        //Disabling the System Settings while using the App
        if (rp.allowSettingsChanges(false)) {
            // Settings application is disabled.
            MainActivity.log("Success allowSettingsChanges: " );
        } else {
            // Failure to restrict Settings app.
            MainActivity.log("Failure allowSettingsChanges: " );
        }

        // Toggle camera functionality
        boolean cameraEnabled = rp.isCameraEnabled(false);
        try {
            if (cameraEnabled) {
                rp.setCameraState(!cameraEnabled);
                MainActivity.log("Set camera enabled to: " + !cameraEnabled);
            }

            boolean ret = wp.isOpenWifiApAllowed();

            MainActivity.log("isOpenWifiAllowed: " + ret);

            // Dissallowing the Open WiFi
            ret = wp.allowOpenWifiAp(false);
            //MainActivity.log("allowOpenWifiApReturned: " + ret);

            ret = wp.isOpenWifiApAllowed();
           // MainActivity.log("isOpenWifiAllowed: " + ret);

            ret = wp.setAutomaticConnectionToWifi(false);
           // MainActivity.log("setAutomaticConnectionToWiFi: " + ret);

            ret = rp.allowWiFi(false);
           // MainActivity.log("allowWiFi: " + ret);

            ret = rp.allowWifiDirect(false);
            //MainActivity.log("allowWifiDirect: " + ret);

            ret = rp.setScreenCapture(true); // Make it False
            //MainActivity.log("setScreenCapture: " + ret);

            ret = rp.isScreenCaptureEnabled(true);
           // MainActivity.log("isScreenCaptureEnabled: " + ret);

          // ret = prp.allowIncomingSms(false);
            // MainActivity.log("allowIncomingSMS: " + ret);

            if (prp.blockSmsWithStorage(true)) {
                MainActivity.log("blockSmsWithStorage: " + ret);
            } else {
                MainActivity.log("blockSmsWithStorage: " + ret);
            }

            // hide status UI
            //kioskModeService.hideStatusBar(true);

            // hide system ui
            //kioskModeService.hideSystemBar(true);

            ret = prp.allowIncomingMms(false);
          //  MainActivity.log("allowIncomingMMS: " + ret);

            ret = prp.allowOutgoingSms(false);
           // MainActivity.log("allowOutgoingSMS: " + ret);

            ret = prp.allowOutgoingMms(false);
            //MainActivity.log("allowOutgoingMMS: " + ret);

            ret = lp.isGPSOn();
            //MainActivity.log("isGPSOn: " +ret);

            ret = lp.setGPSStateChangeAllowed(false);
          //  MainActivity.log("setGPSStateChangeAllowed: " + ret);

            ret = lp.startGPS(false);
          //  MainActivity.log("startGPS: " + ret);

        }

        catch (SecurityException e) {
//            MainActivity.log("Exception: " + e);
//            MainActivity.log("Activating license.");
//            MainActivity.log("Have you remembered to change the demoELMKey in the source code?");
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
        KioskMode kioskModeService = KioskMode.getInstance(ctx);
        WifiManager wifiManager = null;


        // Reverting Back Button
        try {
            List availableHwKeys = new ArrayList();
            availableHwKeys.add(new Integer(4)); //4 is keycode for Back Key
            availableHwKeys.add(new Integer(3));
            // disable provided keys
           // List successBlockedKeys = kioskModeService.allowHardwareKeys(availableHwKeys, false);
            // successBlockedKeys is the list of the keys that were successfully blocked*/

            // enable blocked keys
            List successEnabledKeys = kioskModeService.allowHardwareKeys(availableHwKeys, true);
            // successEnabledKeys is the list of the keys that were successfully allowed

        } catch (SecurityException e) {
//            MainActivity.log( "SecurityException: " + e.getMessage());
        }


        // enable
        if (kioskModeService.allowMultiWindowMode(true)) {
            // multi window mode will be enabled.
        } else {
            // failure disabling policy
        }

        // show status UI
       kioskModeService.hideStatusBar(false);

        // show system ui
        kioskModeService.hideSystemBar(false);

        // Reverting back the System Settings
        if (rp.allowSettingsChanges(true)) {
            // Settings application is enabled.
//            MainActivity.log("Success Disable allowSettingsChanges: " );
        } else {
            // Failure to remove restriction on Settings app.
//            MainActivity.log("Success Disable allowSettingsChanges: " );
        }

        if (wifiManager != null) {
            wifiManager.setWifiEnabled(true);
        }

        //Reverting Incoming Call Functionality

        if (prp.removeIncomingCallRestriction()) {
            log( "success removing incoming call restriction");
        } else {
            log("failed removing incoming call restriction");
        }


        // Toggle camera functionality
        boolean cameraEnabled = rp.isCameraEnabled(false);
              try {
            if (!cameraEnabled) {
                rp.setCameraState(!cameraEnabled);
//                MainActivity.log("Set camera enabled to: " + !cameraEnabled);
            }

            boolean ret = wp.isOpenWifiApAllowed();

//            MainActivity.log("isOpenWifiAllowed: " + ret);

//          Dissallowing the Open Wi=Fi
            ret = wp.allowOpenWifiAp(true);
//          MainActivity.log("allowOpenWifiApReturned: " + ret);

            ret = wp.isOpenWifiApAllowed();
//          MainActivity.log("isOpenWifiAllowed: " + ret);

            ret = wp.setAutomaticConnectionToWifi(true);
//          MainActivity.log("setAutomaticConnectionToWiFi: " + ret);

            ret = rp.allowWiFi(true);
//          MainActivity.log("allowWiFi: " + ret);

            ret = rp.allowWifiDirect(true);
//          MainActivity.log("allowWifiDirect: " + ret);

            ret = rp.setScreenCapture(true);
//          MainActivity.log("setScreenCapture: " + ret);

            ret = rp.isScreenCaptureEnabled(false);
//          MainActivity.log("isScreenCaptureEnabled: " + ret);

           // ret = prp.allowIncomingSms(true)
                  // MainActivity.log("allowIncomingSMS: " + ret);

                  if (prp.blockSmsWithStorage(false)) {
                      MainActivity.log("blockSmsWithStorage: " + ret);
                  } else {
                      MainActivity.log("blockSmsWithStorage: " + ret);
                  }

            ret = prp.allowIncomingMms(true);
//            MainActivity.log("allowIncomingMMS: " + ret);

            ret = prp.allowOutgoingSms(true);
//            MainActivity.log("allowOutgoingSMS: " + ret);

            ret = prp.allowOutgoingMms(true);
//            MainActivity.log("allowOutgoingMMS: " + ret);

            ret = lp.isGPSOn();
//            MainActivity.log("isGPSOn: " + ret);

            ret = lp.setGPSStateChangeAllowed(true);
//            MainActivity.log("setGPSStateChangeAllowed: " + ret);

            ret = lp.startGPS(true);
//            MainActivity.log("startGPS: " + ret);

        }
        catch (SecurityException e) {
//            MainActivity.log("Exception: " + e);
//            MainActivity.log("Activating license.");
//            MainActivity.log("Have you remembered to change the demoELMKey in the source code?");
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
//        Log.d("Hello", "######");

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
