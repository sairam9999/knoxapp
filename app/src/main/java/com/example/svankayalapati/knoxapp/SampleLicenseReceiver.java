package com.example.svankayalapati.knoxapp;


import android.app.enterprise.RestrictionPolicy;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.sec.enterprise.knox.license.KnoxEnterpriseLicenseManager;

public class SampleLicenseReceiver extends BroadcastReceiver {

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       /* String action = intent.getAction();
        if (action.equals(EnterpriseLicenseManager.ACTION_LICENSE_STATUS)) {
            String result = intent.getStringExtra(EnterpriseLicenseManager.EXTRA_LICENSE_STATUS);
            //String log = intent.getStringExtra(EnterpriseLicenseManager.LICENSE_LOG_API);
            int errorCode = intent.getIntExtra(EnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE, -888);
            showToast(context, "License activation: " + result);
            showToast(context, "License activation: " + "error#"+errorCode);




           // MainActivity.applyRestrictions();

            // showToast(context, log);
        }*/

        if (intent != null) {
            String action = intent.getAction();
            if (action == null) {
                System.out.println("action == null");
                return;
            }
            // If ELM activation result intent is obtained
            else if (action.equals(EnterpriseLicenseManager.ACTION_LICENSE_STATUS)) {
                System.out.println("action == ELM ACTION_LICENSE_STATUS");
                int errorCode = intent.getIntExtra(EnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE, -888);

                // if License is successfully activated
                if (errorCode == EnterpriseLicenseManager.ERROR_NONE) {
                    System.out.println("errorCode == ERROR_NONE");
                    // SAUIHelper.showToast(mContext, SAConstants.ELM_ACTIVATION_SUCCESS);
                    // if activity is running
                   // ELMActivated = true;
                    Toast.makeText(context, "ELM License Activation Succesfull", Toast.LENGTH_SHORT).show();
                   /* if (LicenseReceiver.activityObj != null) {
                        // set the UI states in Activity
                        LicenseReceiver.activityObj.setUIStates(SAConstants.RESULT_ELM_ACTIVATED);
                    }
                    // if activity is not running
                    else {
                        saveState(SAConstants.RESULT_ELM_ACTIVATED);
                    }*/
                }
                // if license activation failed
                else {
                    //SAUIHelper.showToast(mContext,
                    //  SAConstants.ELM_ACTIVATION_FAILURE + errorCode);

                    Toast.makeText(context, "ELM License Activation Failed", Toast.LENGTH_SHORT).show();
                }
            }
            // If KLM activation result intent is obtained
            else if (action.equals(KnoxEnterpriseLicenseManager.ACTION_LICENSE_STATUS)) {
                System.out.println("action == KLM ACTION_LICENSE_STATUS");
                int errorCode = intent.getIntExtra(
                        KnoxEnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE,
                        -888);

                // if License is successfully activated
                if (errorCode == KnoxEnterpriseLicenseManager.ERROR_NONE) {
                    //SAUIHelper.showToast(mContext,
                    //      SAConstants.KLM_ACTIVATION_SUCCESS);
                    // if activity is running

                    //KLMActivated = true;
                    Toast.makeText(context, "KLM License Activation Succesfull", Toast.LENGTH_SHORT).show();

                    /*
                    if (LicenseReceiver.activityObj != null) {
                        // set the UI states in Activity
                        LicenseReceiver.activityObj
                                .setUIStates(SAConstants.RESULT_KLM_ACTIVATED);
                    }
                    // if activity is not running
                    else {
                        saveState(SAConstants.RESULT_KLM_ACTIVATED);
                    }*/
                }
                // if license activation failed
                else {
                    Toast.makeText(context, "KLM License Activation Failed", Toast.LENGTH_SHORT).show();


                    //String msg = SACodeUtils.getMessage(errorCode,context);

                   /* if(msg != null && !msg.equalsIgnoreCase(""))
                        SAUIHelper.showToast(context,msg);
                    else{

                                             //  SAUIHelper.showToast(context,context.getString(R.string.err_unknown));
                    }*/
                }
            }

          /*  if (ELMActivated && KLMActivated) {
                Log.d("Sairam", "ELM and KLM Activated Successfully");
                Utils ui = new Utils(MainActivity.edm, null);

                // ui.applyRestrictions();
                // ui.createContainer();
            }*/

        }


    }
}
