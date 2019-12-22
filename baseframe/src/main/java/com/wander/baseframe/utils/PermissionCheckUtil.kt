package com.wander.baseframe.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.KeyEvent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.wander.baseframe.BaseApp
import com.wander.baseframe.R
import java.util.*

/**
 * Created by qiaorongzhu on 2016/1/20.
 */
object PermissionCheckUtil {
    private val CHECK_OP_NO_THROW = "checkOpNoThrow"
    private val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"


    fun checkPermission(
        activity: Activity,
        permission: String,
        REQUEST_CODE_ASK_PERMISSIONS: Int,
        dialogTitle: String,
        dialogContent: String,
        cancelTip: String,
        callBack: FunctionCallBack?,
        cancelExecute: Boolean
    ) {

        val hasPermission = ContextCompat.checkSelfPermission(activity, permission)

        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDialog(activity,
                    dialogTitle,
                    dialogContent,
                    cancelTip,
                    if (cancelExecute) callBack else null,
                    DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(permission),
                            REQUEST_CODE_ASK_PERMISSIONS
                        )
                        dialog.dismiss()
                    })
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
            }

            return
        } else {
            callBack?.doAfterPermission()
        }
    }

    fun checkPermissions(
        activity: Activity, permissions: Array<String>?, REQUEST_CODE_ASK_PERMISSIONS: Int,
        callBack: FunctionCallBack?
    ) {
        if (permissions == null || permissions.size <= 0) {
            return
        }

        val permissionsList = ArrayList<String>()

        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permissions[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsList.add(permissions[i])
            }
        }

        if (permissionsList.size > 0) {
            ActivityCompat.requestPermissions(
                activity, permissionsList.toTypedArray(),
                REQUEST_CODE_ASK_PERMISSIONS
            )

        } else {
            callBack?.doAfterPermission()
        }
    }

    fun checkPermissions(
        fragment: Fragment,
        permissions: Array<String>?,
        REQUEST_CODE_ASK_PERMISSIONS: Int,
        callBack: FunctionCallBack?
    ) {
        if (permissions == null || permissions.size <= 0) {
            return
        }

        val permissionsList = ArrayList<String>()

        for (i in permissions.indices) {
            if (fragment.activity?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        permissions[i]
                    )
                } != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permissions[i])
            }
        }

        if (permissionsList.size > 0) {
            fragment.requestPermissions(permissions, REQUEST_CODE_ASK_PERMISSIONS)
        } else {
            callBack?.doAfterPermission()
        }
    }


    private fun showPermissionDialog(
        activity: Activity,
        dialogTitle: String,
        dialogContent: String,
        cancelTip: String,
        callBack: FunctionCallBack?,
        okListener: DialogInterface.OnClickListener
    ) {
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(dialogTitle)
            .setMessage(dialogContent)
            .setPositiveButton("确定", okListener)
            .setNegativeButton("取消") { dialog, which ->
                dialog.dismiss()
                rejectPermission(cancelTip, callBack)
            }.create()
        alertDialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
                rejectPermission(cancelTip, callBack)
                true
            } else {
                false
            }
        }

        try {
            activity.runOnUiThread { alertDialog.show() }

        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    private fun rejectPermission(cancelTip: String, callBack: FunctionCallBack?) {
        Toast.makeText(BaseApp.getInstance(), cancelTip, Toast.LENGTH_LONG).show()
        callBack?.doAfterPermission()
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value [PackageManager.PERMISSION_GRANTED].
     *
     * @see Activity.onRequestPermissionsResult
     */
    fun verifyPermissions(grantResults: IntArray): Boolean {
        if (grantResults.size < 1) {
            return false
        }

        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


    fun allPermissionIsOpen(activity: Activity, permissions: Array<String>): Boolean {
        var allPermissionIsOpen = true

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                allPermissionIsOpen = false
                break
            }
        }
        return allPermissionIsOpen
    }


    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    fun showMissingPermissionDialog(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.notifyTitle)
        builder.setMessage(R.string.notifyMsg)

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
            DialogInterface.OnClickListener { dialog, which -> activity.finish() })

        builder.setPositiveButton(R.string.setting,
            DialogInterface.OnClickListener { dialog, which -> startAppSettings(activity) })

        builder.setCancelable(false)

        builder.show()
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    fun startAppSettings(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        )
        intent.data = Uri.parse("package:${activity.packageName}")
        activity.startActivity(intent)
    }


    interface FunctionCallBack {

        fun doAfterPermission()

    }
}
