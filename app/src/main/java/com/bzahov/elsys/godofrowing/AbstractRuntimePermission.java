package com.bzahov.elsys.godofrowing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.SparseIntArray;
import android.widget.Toast;

import java.security.Permission;

/**
 * Created by bobo-pc on 12/24/2016.
 */


// Using EasyPemissions so this class is USELESS
// TODO: REMOVE
public abstract class AbstractRuntimePermission extends FragmentActivity {

    private SparseIntArray mErorrStr;
    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErorrStr = new SparseIntArray();
    }

    public abstract void onPermissionGranted(int requestCode);

    public void onPermissionRequest(final String[] requestedPermissions,final int strId, final int requestCode){
        mErorrStr.put(requestCode,strId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        boolean showRequestPermission = false;
        for (String permission: requestedPermissions){

            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermission = showRequestPermission || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            if(showRequestPermission){
                Toast.makeText(this.getBaseContext(),"Please Give Permission",Toast.LENGTH_SHORT);
                ActivityCompat.requestPermissions(AbstractRuntimePermission.this,requestedPermissions,requestCode);
            }
        }else onPermissionGranted(requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults){
            permissionCheck = permissionCheck + permission;
        }

        if ((grantResults.length > 0 ) && PackageManager.PERMISSION_GRANTED == permissionCheck)
        {
            onPermissionGranted(requestCode);
        }else {
            Toast.makeText(this.getBaseContext(), mErorrStr.get(requestCode),Toast.LENGTH_SHORT);
            Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(Uri.parse("package "+ getPackageName()));
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(i);
        }
    }
}