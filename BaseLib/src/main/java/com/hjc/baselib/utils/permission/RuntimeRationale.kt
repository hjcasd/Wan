package com.hjc.baselib.utils.permission

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import com.hjc.baselib.R
import com.yanzhenjie.permission.Rationale
import com.yanzhenjie.permission.RequestExecutor
import com.yanzhenjie.permission.runtime.Permission

class RuntimeRationale : Rationale<List<String?>?> {

    override fun showRationale(
        context: Context,
        permissions: List<String?>?,
        executor: RequestExecutor
    ) {
        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(
            R.string.message_permission_rationale,
            TextUtils.join("\n", permissionNames)
        )
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(R.string.tip)
            .setMessage(message)
            .setPositiveButton(R.string.resume) { dialog, which -> executor.execute() }
            .setNegativeButton(R.string.cancel) { dialog, which -> executor.cancel() }
            .show()
    }
}