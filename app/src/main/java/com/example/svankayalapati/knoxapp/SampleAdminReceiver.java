package com.example.svankayalapati.knoxapp;

import android.app.admin.DeviceAdminReceiver;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.sec.enterprise.knox.license.KnoxEnterpriseLicenseManager;


public class SampleAdminReceiver extends DeviceAdminReceiver {

    private TextView logView;

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: enabled");

       // MainActivity.klmManager.activateLicense("KLM03-64XNS-ZT6V9-LFMHZ-4MJ9E-9R6JA");

        //MainActivity.elm.activateLicense("4A8C019C25B9D64343EA4CEF8A141CD7D419169224B65D676A1E4DD664DDB76BDECF2B374D63B090298449459A2B8DEEC46CC7D553A68ED4AB78AB8F6E353646");


    }


    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Deactivating this app as a device administrator removes the ability of the app to control the device.";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: disabled");
    }



    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: pw changed");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: pw failed");
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: pw succeeded");
    }

}
