package com.example.svankayalapati.knoxapp;

import android.app.enterprise.ApplicationPolicy;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.sec.enterprise.knox.EnterpriseKnoxManager;
import com.sec.enterprise.knox.container.KnoxContainerManager;
import com.sec.enterprise.knox.container.RCPPolicy;




public class ContainerCreationStatusReceiver extends BroadcastReceiver {
	//KnoxContainerManager kmcm;
	static int requestId;

	public ContainerCreationStatusReceiver() {
		// TODO Auto-generated constructor stub
		System.out.println("ContainerCreationStatusReceiver.ContainerCreationStatusReceiver()");
	}

	// The request id is passed in by the caller of the createContainer() API
	ContainerCreationStatusReceiver(int requestId) {
		System.out.println("ContainerCreationStatusReceiver.ContainerCreationStatusReceiver(requestId)");
		ContainerCreationStatusReceiver.requestId = requestId;
	}
	
	public static final String TAG = "Sairam";

	@Override
	public void onReceive(Context ctxt, Intent intent) {
		Bundle bundle = null;

		System.out.println("ContainerCreationStatusReceiver.onReceive()");

		//sample test package name
		String testpackageName = "com.example.svankayalapati.knoxapp";
		// When you create container successfully, containerID will be returned via intent.
		// Use this containerID in below API.
		//EnterpriseKnoxManager ekm = EnterpriseKnoxManager.getInstance();



		try {
			int requestId = -1;
			int statusCode = KnoxContainerManager.ERROR_INTERNAL_ERROR;
			Bundle extras = intent.getExtras();
			if (extras != null) {
				requestId = extras
						.getInt(KnoxContainerManager.CONTAINER_CREATION_REQUEST_ID);
				statusCode = extras
						.getInt(KnoxContainerManager.CONTAINER_CREATION_STATUS_CODE);
			}
			// if the createContainer() caller's request id and request id
			// received from this intent
			// does not match, then the intent is not correct
			if (ContainerCreationStatusReceiver.requestId != requestId) {
				Log.e(TAG, "SAConstants.FAKE_INTENT");
			}
			
			// If the container state has changed
			if (intent.getAction().equalsIgnoreCase(
					KnoxContainerManager.INTENT_CONTAINER_STATE_CHANGED)) {
				bundle = intent
						.getBundleExtra(KnoxContainerManager.INTENT_BUNDLE);
				// Get the container id
				int containerId = bundle
						.getInt(KnoxContainerManager.CONTAINER_ID);
				//KnoxContainerManager kmcm = ekm.getKnoxContainerManager(MainActivity.ctx, containerId);
				//ApplicationPolicy appPolicy = kmcm.getApplicationPolicy();

				EnterpriseKnoxManager ekm = EnterpriseKnoxManager.getInstance();
				KnoxContainerManager kcm = ekm.getKnoxContainerManager(ctxt,
						containerId);
				ApplicationPolicy appPolicy = kcm.getApplicationPolicy();
				RCPPolicy rcpPolicy = kcm.getRCPPolicy();

				// applying default policies on the container as an initial
				// setting

				// allowing move apps to container as an initial setting
				rcpPolicy.allowMoveAppsToContainer(true);
				// installing Camera app in container as an initial setting
				boolean ret = appPolicy.installApplication("com.example.svankayalapati.knoxapp");
				MainActivity.log("Moved App to Container" +ret);
				Log.i(TAG,
						"in On Receive of ContainerCreationStatusReceiver cont id "
								+ bundle.getInt(KnoxContainerManager.CONTAINER_ID));
				rcpPolicy.isMoveAppsToContainerAllowed();
				
			}

			// if the container is successfully created
			if (statusCode >= 0) {
				Log.d("Sairam", "Container Successfully Created");
				/*new Controller(ctxt);
				String doSelfUninstall = ((String)Controller
						.getSharedPrefData(SAConstants.DO_SELF_UNINSTALL, SAConstants.STRING_DATATYPE));
				// Check if the user wants to uninstall self
				if (doSelfUninstall != null
						&& doSelfUninstall.equalsIgnoreCase(SAConstants.YES)) {
					// uninstall self
					KnoxContainerManager.doSelfUninstall();
					// Reset user preference
					Controller.setSharedPrefData(SAConstants.DO_SELF_UNINSTALL,
							SAConstants.NO, SAConstants.STRING_DATATYPE);
				}*/

			}
			// if the container creation failed
			else {
				Log.e("Sairam", ".CONTAINER_CREATION_FAILED" + statusCode);
				//Controller.showToast(SACodeUtils.getMessage(statusCode, ctxt));
			}
			//boolean result = appPolicy.installApplication(testpackageName);
			/*if (true == result) {
				Log.d(TAG, "Installing an application package has been successful!");
			} else {
				Log.w(TAG, "Installing an application package has failed.");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}