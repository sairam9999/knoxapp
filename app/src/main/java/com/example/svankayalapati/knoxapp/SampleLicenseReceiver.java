package com.example.svankayalapati.knoxapp;


import android.app.enterprise.RestrictionPolicy;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SampleLicenseReceiver extends BroadcastReceiver {

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(EnterpriseLicenseManager.ACTION_LICENSE_STATUS)) {
            String result = intent.getStringExtra(EnterpriseLicenseManager.EXTRA_LICENSE_STATUS);
            //String log = intent.getStringExtra(EnterpriseLicenseManager.LICENSE_LOG_API);
            int errorCode = intent.getIntExtra(EnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE, -888);
            showToast(context, "License activation: " + result);
            showToast(context, "License activation: " + "error#"+errorCode);

            MainActivity.applyRestrictions();

            // showToast(context, log);
        }
    }
}