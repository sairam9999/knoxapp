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

/**
 * Example of a do-nothing admin class. When enabled, it lets you control
 * some of its policy and reports when there is interesting activity.
 */

public class SampleAdminReceiver extends DeviceAdminReceiver {

    private TextView logView;

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: enabled");

       // MainActivity.klmManager.activateLicense("KLM06-HXHE8-BGH70-3O3K5-7Y3HF-2XUNP");

        //MainActivity.elm.activateLicense("6DEEF1797015B5478FF953C6DD9E6D03B52AB657A2F9AF2C827ECDC9A0E8A64BF0B9A6C503E1F37EE9F523BAE7F58102B50EFD82E379C33B0102294E8746F416");


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
