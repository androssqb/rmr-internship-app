package com.redmadrobot.app.ui.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.redmadrobot.app.R

class ProgressLoaderDialog : DialogFragment(R.layout.dialog_progress_loader) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog?.setCancelable(false)
    }
}
