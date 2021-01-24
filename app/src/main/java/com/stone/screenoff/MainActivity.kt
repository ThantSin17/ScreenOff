package com.stone.screenoff

import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var deviceManger: DevicePolicyManager
    lateinit var compName: ComponentName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //get Device Manager
        deviceManger=getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager

        //get component
        compName= ComponentName(this, DeviceAdmin::class.java)

        val enable=deviceManger.isAdminActive(compName)
        if (enable){
            //kill app and exit from recent
            val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val appTasks = am!!.getAppTasks()
                if (appTasks.size > 0) {
                    val appTask = appTasks.get(0)
                    appTask.finishAndRemoveTask()
                }
            }
            //lock screen
            deviceManger.lockNow()


        }else{
            //permission from user
            val intent=Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "If you only activate, this app can perform screen off !!!")
            startActivityForResult(intent, 100)
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        if (requestCode==100){
            if (requestCode== RESULT_OK){
                Log.i("MainActivity", "Success")
            }else{
                Log.i("MainActivity", "Fail")
            }
        }
    }
}
//val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
//intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
//intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!")
//startActivityForResult(intent, enableResult)