package com.fc.rain.freecreate.utils

import android.app.Dialog
import android.content.Context
import com.fc.rain.freecreate.R

/**
 *
 * Describe #
 *
 * Created by Rain on 2017/11/21.
 */
class LoadDialogUtils(var context: Context) {
    var loadDialog: Dialog? = null

    init {
        loadDialog = Dialog(context, R.style.LoadingDialog)
        loadDialog?.setContentView(R.layout.loading_view)
        loadDialog?.setCanceledOnTouchOutside(false)
        loadDialog?.setCancelable(false)
    }

    fun showLoadingDialog() {
        loadDialog?.isShowing?.let {
            if (!it) {
                loadDialog?.show()
            }
        }
    }

    fun disMissDialog() {
        loadDialog?.isShowing?.let {
            if (it) {
                loadDialog?.dismiss()
            }
        }
    }
}