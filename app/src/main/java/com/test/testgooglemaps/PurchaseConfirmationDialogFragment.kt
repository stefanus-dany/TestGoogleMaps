package com.test.testgooglemaps

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.test.testgooglemaps.databinding.DialogViewBinding

class PurchaseConfirmationDialogFragment : DialogFragment() {

    private lateinit var binding: DialogViewBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogViewBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(R.layout.dialog_view)
            .setNegativeButton("Cancel") { _, _ ->
                dismiss()
            }
            .setPositiveButton("Open Maps") { _, _ ->
                val mapIntent = Intent(Intent.ACTION_VIEW)
                mapIntent.setPackage("com.google.android.apps.maps")
                activity?.let { it1 ->
                    mapIntent.resolveActivity(it1.packageManager)?.let {
                        startActivity(mapIntent)
                    }
                }
            }

//        binding.cancelBtn.setOnClickListener {
//            dismiss()
//        }
//        binding.openMapsBtn.setOnClickListener {
//            val mapIntent = Intent(Intent.ACTION_VIEW)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            activity?.let { it1 ->
//                mapIntent.resolveActivity(it1.packageManager)?.let {
//                    startActivity(mapIntent)
//                }
//            }
//        }
        return builder.create()
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}