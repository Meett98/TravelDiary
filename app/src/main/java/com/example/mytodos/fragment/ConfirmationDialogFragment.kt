package com.example.mytodos.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.mytodos.activities.DownloadActivity

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setMessage("Are you sure you want to download?")
            .setPositiveButton("Yes") { _, _ ->
                // Start the download process here
                val idownload = Intent(requireActivity(), DownloadActivity::class.java)
                startActivity(idownload)
            }
            .setNegativeButton("No", null)
            .create()
    }
}