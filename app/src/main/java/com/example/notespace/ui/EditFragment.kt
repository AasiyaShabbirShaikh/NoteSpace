package com.example.notespace.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils.formatDateTime
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.room.Delete
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.notespace.CameraHelper
import com.example.notespace.R
import com.example.notespace.databinding.AddReminderDialogBoxBinding
import com.example.notespace.databinding.DeleteDialogBoxBinding
import com.example.notespace.databinding.FragmentEditBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding
import com.example.notespace.model.Notes
import com.example.notespace.utils.ReminderNotificationWorker
import com.example.notespace.viewModel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditFragment : Fragment() {

    private var _binding : FragmentEditBinding? = null
    private val binding get() = _binding!!

    private var note : Notes? = null
    private var imageUri: Uri? = null
    private var deleteDialogbox : Dialog? = null

    private lateinit var editDialogBinding: AddReminderDialogBoxBinding
    private var editReminderDialogBox : Dialog? = null

    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        note = arguments?.getParcelable("note")
        fetchNoteContent()

        binding.newEditfClockReminderCardview.setOnClickListener {
            showEditReminderDialog()
        }

        binding.newEditfImageView.setOnClickListener {
            showDeleteDialogBox()
        }
        return binding.root
    }

    private fun fetchNoteContent(){
        note?.let{
            binding.apply {
                binding.newEditfTitleEditText.setText(it.noteTitle)
                binding.newEditfDescriptionEditText.setText(it.noteDescription)

//                if (note?.noteImageUri != null) {
//                    imageUri = Uri.parse(note?.noteImageUri)
//                    newEditfImageView.setImageURI(imageUri)
//                    newEditfImageView.visibility = View.VISIBLE
//                }

                it.noteImageUri?.let { uri ->
                    imageUri = Uri.parse(uri)
                    newEditfImageView.setImageURI(imageUri)
                    newEditfImageView.visibility = View.VISIBLE
                }

//                if(note?.reminderTime != null){
//                    newEditfClockReminderCardview.visibility = View.VISIBLE
//                    val reminderDate = formatReminderDate(it.reminderTime)
//                    val reminderTime = formatReminderTime(it.reminderTime)
//                    newEditfDayText.text = reminderDate
//                    newEditfTimeText.text = reminderTime
//            }
                it.reminderTime?.let{ reminderTime ->
                    newEditfClockReminderCardview.visibility = View.VISIBLE
                    val reminderFormattedDate = formatReminderDate(reminderTime)
                    val reminderFormattedTime = formatReminderTime(reminderTime)
                    newEditfDayText.text = reminderFormattedDate
                    newEditfTimeText.text = reminderFormattedTime

                }
            }
        }
    }

    private fun formatReminderDate(reminderTime: Long) : String{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = reminderTime
        val date = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
        return date.format(calendar.time)
    }

    private fun formatReminderTime(reminderTime: Long) : String{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis= reminderTime
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return time.format(calendar.time)
    }

    private fun showEditReminderDialog(){
        editDialogBinding = AddReminderDialogBoxBinding.inflate(layoutInflater)
        editReminderDialogBox = Dialog(requireContext())
        editReminderDialogBox?.apply {
            setContentView(editDialogBinding.root)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(true)
        }

        editDialogBinding.apply {
            dateLayout.setOnClickListener {
                chooseDateMenuOption(dateLayout)
            }
            timeLayout.setOnClickListener {
                chooseTimeMenuOption(timeLayout)
            }
            doesNotRepeatLayout.setOnClickListener {
                chooseRepeatMenuOption(doesNotRepeatLayout)
            }
            addReminderDeleteTextButton.setOnClickListener {
                binding.newEditfClockReminderCardview.visibility=View.GONE
                editReminderDialogBox?.dismiss()
            }
            addReminderCancelTextButton.setOnClickListener {
                editReminderDialogBox?.dismiss()
            }
            saveButton.setOnClickListener {
                validateOptedTime()
                val selectedDateText = editDialogBinding.dateText.text.toString()
                val selectedTimeText = editDialogBinding.timeText.text.toString()
                if (selectedDateText.isNotEmpty() && selectedTimeText.isNotEmpty()) {
                    binding.newEditfDayText.text = selectedDateText
                    binding.newEditfTimeText.text = selectedTimeText
                }
                val reminderTime = parseReminderDateTime(selectedDateText, selectedTimeText)
                if (reminderTime != null && reminderTime > System.currentTimeMillis()) {
                    scheduleReminder(binding.newEditfTitleEditText.text.toString(), binding.newEditfDescriptionEditText.text.toString(), reminderTime)
                }
                editReminderDialogBox?.dismiss()
            }
        }
        editReminderDialogBox?.show()
    }

    private fun chooseDateMenuOption(view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.choose_date_days_menu, popupMenu.menu)

        val todaysCalendar = Calendar.getInstance()
        val todaysWeekDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(todaysCalendar.time)
        val nextWeekdayMenuItem = popupMenu.menu.findItem(R.id.next_weekday)
        nextWeekdayMenuItem.title = "Next ${todaysWeekDay}"

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.today -> {
                    val calendar = Calendar.getInstance()
                    val today = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(calendar.time)
                    editDialogBinding.dateText.text = today
                    true
                }
                R.id.tomorrow -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                    val tomorrow = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(calendar.time)
                    editDialogBinding.dateText.text = tomorrow
                    true
                }
                R.id.next_weekday -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, 7)
                    val nextWeekday = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(calendar.time)
                    editDialogBinding.dateText.text = nextWeekday
                    true
                }
                R.id.pick_a_date -> {
                    showDatePickerDialogBox()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showDatePickerDialogBox() {
        editDialogBinding.timePassedText.visibility = View.GONE
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialogBox = DatePickerDialog(
            requireContext(),
            {
//            view, selectedYear, selectedMonth, selectedDayOfMonth ->
//                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
//                dialogBinding.dateText.text = selectedDate

                    _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)

                val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDateCalendar.time)

                editDialogBinding.dateText.text = formattedDate
            },
            year, month, dayOfMonth
        )
        datePickerDialogBox.show()
    }

    private fun chooseTimeMenuOption(view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.pick_time_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.morning_option -> {
                    editDialogBinding.timeText.text = "8:00 AM"
                    true
                }
                R.id.afternoon_option -> {
                    editDialogBinding.timeText.text = "1:00 PM"
                    true
                }
                R.id.evening_option -> {
                    editDialogBinding.timeText.text = "6:00 PM"
                    true
                }
                R.id.night_option -> {
                    editDialogBinding.timeText.text = "8:00 PM"
                    true
                }
                R.id.pick_a_time -> {
                    showTimePickerDialogBox()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showTimePickerDialogBox() {
        editDialogBinding.timePassedText.visibility = View.GONE
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                val formattedTime = timeFormat.format(calendar.time)
                editDialogBinding.timeText.text = formattedTime
            },
            hour, minute, false
        )

        timePickerDialog.show()
    }

    private fun chooseRepeatMenuOption(view: View){
        editDialogBinding.timePassedText.visibility = View.GONE
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.does_not_repeat_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.does_not_repeat -> {
                    editDialogBinding.doesNotRepeatText.text = getString(R.string.does_not_repeat)
                    true
                }
                R.id.daily -> {
                    editDialogBinding.doesNotRepeatText.text = getString(R.string.repeats_daily)
                    true
                }
                R.id.weekly -> {
                    editDialogBinding.doesNotRepeatText.text = getString(R.string.repeats_weekly)
                    true
                }
                R.id.monthly -> {
                    editDialogBinding.doesNotRepeatText.text = getString(R.string.repeats_monthly)
                    true
                }
                R.id.yearly -> {
                    editDialogBinding.doesNotRepeatText.text = getString(R.string.repeats_yearly)
                    true
                }
                R.id.custom -> {
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun validateOptedTime() {
        val selectedDateText = editDialogBinding.dateText.text.toString()
        val selectedTimeText = editDialogBinding.timeText.text.toString()
        println(" selected--- ${selectedDateText}  ${selectedTimeText}")

        if (selectedDateText.isNotEmpty() && selectedTimeText.isNotEmpty()) {
            try {
                val dateFormat = SimpleDateFormat("MMMM dd yyyy hh:mm a", Locale.getDefault())
//                dateFormat = if (selectedTimeText.contains(":") && selectedTimeText.contains("PM") || selectedTimeText.contains("AM")) {
//                    SimpleDateFormat("MMMM dd yyyy hh:mm a", Locale.getDefault()) // 12-hour format
//                }
//                else {
//                    SimpleDateFormat("MMMM dd yyyy HH:mm", Locale.getDefault()) // 24-hour format
//                }

                val selectedDateTimeString = "$selectedDateText $selectedTimeText"
                println("AddFragment $selectedDateTimeString")
                val selectedDateTime = dateFormat.parse(selectedDateTimeString)

                if (selectedDateTime != null) {
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.time = selectedDateTime

                    val currentCalendar = Calendar.getInstance()
                    println("Current time: ${currentCalendar.time}, Selected time: ${selectedCalendar.time}")

                    if (selectedCalendar.before(currentCalendar)) {
                        println("Selected time is in the past")
                        editDialogBinding.timePassedText.visibility = View.VISIBLE
                    } else {
                        println("Selected time is in the future.")
                        editDialogBinding.timePassedText.visibility = View.GONE
                    }
                } else {
                    Log.e("AddFragment", "Failed to parse date and time")
                }
            } catch (e: ParseException) {
                Log.e("AddFragment", "Date parse error: ${e.message}")
            }
        } else {
            Toast.makeText(requireContext(), "Please select both date and time", Toast.LENGTH_SHORT).show()
            editDialogBinding.timePassedText.visibility = View.GONE
        }
    }

    private fun parseReminderDateTime(dateText: String, timeText: String): Long? {
        val dateFormat = SimpleDateFormat("MMMM dd yyyy hh:mm a", Locale.getDefault())
        return try {
            dateFormat.parse("$dateText $timeText")?.time
        } catch (e: ParseException) {
            Log.e("AddFragment", "Failed to parse reminder date and time: ${e.message}")
            null
        }
    }

    private fun scheduleReminder(title: String, message: String, reminderTime: Long) {
        val delay = reminderTime - System.currentTimeMillis()
        var time = reminderTime.toString()
        if (delay > 0) {
            val data = Data.Builder()
                .putString("title", title)
                .putString("message", message)
                .putString("time", time)
                .build()

            val reminderRequest = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
                .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()

            WorkManager.getInstance(requireContext()).enqueue(reminderRequest)
            Toast.makeText(requireContext(), "Reminder set!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Selected time is in the past!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        if(isNoteModified()){
            updateNote()
        }
    }

    private fun isNoteModified(): Boolean{
        val currentTitle = binding.newEditfTitleEditText.text.toString()
        val currentDescription = binding.newEditfDescriptionEditText.text.toString()
        val currentReminderDate = binding.newEditfDayText.text.toString()
        val currentReminderTime = binding.newEditfTimeText.text.toString()
        val currentImageUri = imageUri
        return currentTitle != note?.noteTitle || currentDescription != note?.noteDescription || currentReminderDate != note?.reminderTime?.let { formatReminderDate(it) } || currentReminderTime != note?.reminderTime?.let{ formatReminderTime(it)} || currentImageUri != Uri.parse(note?.noteImageUri)
    }

    private fun updateNote(){
        val updatedNote = note?.copy(
            noteTitle = binding.newEditfTitleEditText.text.toString(),
            noteDescription = binding.newEditfDescriptionEditText.text.toString(),
            reminderTime = parseReminderDateTime(binding.newEditfDayText.text.toString(), binding.newEditfTimeText.text.toString()),
            noteImageUri = imageUri?.toString()
        )

        updatedNote?.let {
            notesViewModel.updateNote(it)
        }
    }

    private fun showDeleteDialogBox(){
        val deleteDialogBinding = DeleteDialogBoxBinding.inflate(layoutInflater)
        deleteDialogbox = Dialog(requireContext())

        deleteDialogbox?.apply {
            setContentView(deleteDialogBinding.root)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(true)
        }

        deleteDialogBinding.apply {
            deleteCancelButton.setOnClickListener {
                deleteDialogbox!!.dismiss()
            }
            deleteDeleteButton.setOnClickListener {
                deleteImage()
                deleteDialogbox!!.dismiss()
            }

        }
        deleteDialogbox?.show()
    }

    private fun deleteImage(){
        imageUri = null
        binding.newEditfImageView.setImageURI(null)
        note?.noteImageUri = null
        binding.newEditfImageView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}