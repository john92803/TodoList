package com.example.todolist.ui.dashboard

import android.app.DatePickerDialog
import androidx.fragment.app.FragmentActivity
import java.util.Calendar

class MyDatePickerDialog(
    // context needed to create the dialog
    activity: FragmentActivity,
    // callback for the caller of this dialog
    onShowDateClicked: (Int, Int, Int) -> Unit
) {
    private val calendar = Calendar.getInstance()
    val year: Int
        get() = calendar.get(Calendar.YEAR)
    val month: Int
        get() = calendar.get(Calendar.MONTH)
    val day: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)
    private val dialog =
        DatePickerDialog(activity).apply {
            setOnDateSetListener { _, year, month, day ->
                onShowDateClicked.invoke(year, month, day)
            }
        }


    fun show() {
        dialog.show()
    }
}