package com.example.notespace.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.AddReminderDialogBoxBinding
import com.example.notespace.databinding.BottomMenuPopUpLayoutBinding
import com.example.notespace.databinding.BottomRemindPopUpLayoutBinding
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentEditNoteBinding
import com.example.notespace.model.Notes
import com.example.notespace.utils.ReminderNotificationWorker
import com.example.notespace.viewModel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditNoteFragment : Fragment(){
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var addOnDialog : BottomSheetDialog
    private lateinit var menuDialog: BottomSheetDialog
    private lateinit var remindMeDialog : BottomSheetDialog
    private var addReminderDialogBox : Dialog? = null
    private lateinit var dialogBinding: AddReminderDialogBoxBinding

    private val notesViewModel: NotesViewModel by viewModels()
    private val args : EditNoteFragmentArgs by navArgs()
    private var imageUri: Uri? = null
    private var note: Notes? = null

    private var isNoteModified = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)

        setUpActionBar()
        setUpBottomNavBar()

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()
        (activity as MainActivity).hideBottomNavBar()

        note = arguments?.getParcelable("note")

        binding.editClockReminderCardview.setOnClickListener {
            showAddReminderDialogBox()
        }
        dialogBinding = AddReminderDialogBoxBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            editTitleEditText.setText(note?.noteTitle)
            editDescriptionEditText.setText(note?.noteDescription)
            if (note?.noteImageUri != null) {
                imageUri = Uri.parse(note?.noteImageUri)
                editImageView.setImageURI(imageUri)
                editImageView.visibility = View.VISIBLE
            }
            val reminderTimeValue = note?.reminderTime // Assign to local variable
            if (reminderTimeValue != null && reminderTimeValue > System.currentTimeMillis()) {
                val reminderDate = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(Date(reminderTimeValue))
                val reminderTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(reminderTimeValue))

                editDayText.text = reminderDate
                editTimeText.text = reminderTime
                editClockReminderCardview.visibility = View.VISIBLE
            }
            editTitleEditText.addTextChangedListener {
                isNoteModified = true
            }
            editDescriptionEditText.addTextChangedListener {
                isNoteModified = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (isNoteModified) {
                updateNote()
            } else {
                requireActivity().onBackPressed()
            }
//        }
    }

    override fun onPause() {
        super.onPause()
        updateNote()
    }

    private fun updateNote(){
        val noteTitle = binding.editTitleEditText.text.toString().trim()
        val noteDescription = binding.editDescriptionEditText.text.toString().trim()
        val selectedDateText = dialogBinding.dateText.text.toString()
        val selectedTimeText = dialogBinding.timeText.text.toString()
        val reminderTime = parseReminderDateTime(selectedDateText, selectedTimeText)

        isNoteModified = reminderTime != note?.reminderTime

        if(noteTitle.isNotEmpty() && isNoteModified){
            val note = Notes(note?.noteId ?:0, noteTitle, noteDescription, System.currentTimeMillis(), false, note?.noteImageUri, reminderTime )
            notesViewModel.updateNote(note)
            isNoteModified = false
        }
    }

    fun setUpActionBar(){
        binding.editToolbar.apply {
            backArrowIcon.setOnClickListener {
                requireActivity().onBackPressed()
            }
            pinIcon.setOnClickListener {
            }
            remindMeIcon.setOnClickListener {
                showRemindMeBottomPopUpDialog()
            }
            archiveIcon.setOnClickListener {
            }
        }
    }

    fun setUpBottomNavBar(){
        binding.apply{
            editAddOnIcon.setOnClickListener {
                showAddOnBottomPopUpDialog()
            }
            editColorPaleteIcon.setOnClickListener {
            }
            editTextStylePickerIcon.setOnClickListener {
            }
            editMenuIcon.setOnClickListener {
                showMenuBottomPopUpDialog()
            }
        }
    }

    fun showRemindMeBottomPopUpDialog(){
        val remindMeViewBinding = BottomRemindPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        remindMeDialog = BottomSheetDialog(requireContext())
        remindMeDialog.setContentView(remindMeViewBinding.root)

        remindMeViewBinding.apply {
            remindLaterTodayLayout.setOnClickListener {
                binding.editClockReminderCardview.visibility = View.VISIBLE
                val laterDayText = remindLaterTodayText.text.toString()
                val laterDayTime = "," + remindLaterTodayTimeText.text.toString()
                binding.editDayText.text = laterDayText
                binding.editTimeText.text = laterDayTime
                remindMeDialog.dismiss()
            }
            remindTomorrowLayout.setOnClickListener {
                binding.editClockReminderCardview.visibility = View.VISIBLE
                val tomorrowDayText = remindTomorrowText.text.toString()
                val tomorrowDayTime = "," + remindTomorrowTimeText.text.toString()
                binding.editDayText.text = tomorrowDayText
                binding.editTimeText.text = tomorrowDayTime
                remindMeDialog.dismiss()
            }
            remindNextDayLayout.setOnClickListener {
                binding.editClockReminderCardview.visibility = View.VISIBLE
                val nextDayText = remindNextDayText.text.toString()
                val nextDayTime = "," + remindNextDayTimeText.text.toString()
                binding.editDayText.text = nextDayText
                binding.editTimeText.text = nextDayTime
                remindMeDialog.dismiss()
            }
            remindPickDateTimeLayout.setOnClickListener {
                remindMeDialog.dismiss()
            }
            remindPickPlaceLayout.setOnClickListener {
                remindMeDialog.dismiss()
            }
        }
        remindMeDialog.show()
    }

    fun showAddOnBottomPopUpDialog(){
        val addOnViewBinding = BottomSheetPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        addOnDialog = BottomSheetDialog(requireContext())
        addOnDialog.setContentView(addOnViewBinding.root)
        addOnViewBinding.popupCheckboxesLayout.visibility = View.GONE

        addOnViewBinding.apply {
            popupTakePhotoLayout.setOnClickListener {
                addOnDialog.dismiss()
            }
            popupAddImageLayout.setOnClickListener {
                addOnDialog.dismiss()
            }
            popupDrawingLayout.setOnClickListener {
                addOnDialog.dismiss()
            }
            popupRecordingLayout.setOnClickListener {
                addOnDialog.dismiss()
            }
        }
        addOnDialog.show()

    }

    fun showMenuBottomPopUpDialog(){
        val menuViewBinding = BottomMenuPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        menuDialog = BottomSheetDialog(requireContext())
        menuDialog.setContentView(menuViewBinding.root)

        menuViewBinding.apply {
            menuDeleteLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuMakeACopyLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuSendLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuCollaboratorLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuLabelsLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuHelpFeedbackLayout.setOnClickListener {
                menuDialog.dismiss()
            }
        }
        menuDialog.show()
    }

    private fun showAddReminderDialogBox(){
        dialogBinding = AddReminderDialogBoxBinding.inflate(layoutInflater)
        addReminderDialogBox = Dialog(requireContext())
        addReminderDialogBox?.apply {
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(true)
        }

        dialogBinding.apply {
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
                binding.editClockReminderCardview.visibility=View.GONE
                addReminderDialogBox?.dismiss()
            }
            addReminderCancelTextButton.setOnClickListener {
                addReminderDialogBox?.dismiss()
            }
            saveButton.setOnClickListener {
                validateOptedTime()
                val selectedDateText = dialogBinding.dateText.text.toString()
                val selectedTimeText = dialogBinding.timeText.text.toString()
                if (selectedDateText.isNotEmpty() && selectedTimeText.isNotEmpty()) {
                    binding.editDayText.text = selectedDateText
                    binding.editTimeText.text = selectedTimeText
                }
                val reminderTime = parseReminderDateTime(selectedDateText, selectedTimeText)
                if (reminderTime != null && reminderTime > System.currentTimeMillis()) {
                    scheduleReminder(binding.editTitleEditText.text.toString(), binding.editDescriptionEditText.text.toString(), reminderTime)
                }
                addReminderDialogBox?.dismiss()
            }
        }
        addReminderDialogBox?.show()
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
                    dialogBinding.dateText.text = today
                    true
                }
                R.id.tomorrow -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                    val tomorrow = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(calendar.time)
                    dialogBinding.dateText.text = tomorrow
                    true
                }
                R.id.next_weekday -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, 7)
                    val nextWeekday = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(calendar.time)
                    dialogBinding.dateText.text = nextWeekday
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
        dialogBinding.timePassedText.visibility = View.GONE
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

                val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()) // "MMMM" is full month name, "dd" is day
                val formattedDate = dateFormat.format(selectedDateCalendar.time)

                dialogBinding.dateText.text = formattedDate
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
                    dialogBinding.timeText.text = "8:00 AM"
                    true
                }
                R.id.afternoon_option -> {
                    dialogBinding.timeText.text = "1:00 PM"
                    true
                }
                R.id.evening_option -> {
                    dialogBinding.timeText.text = "6:00 PM"
                    true
                }
                R.id.night_option -> {
                    dialogBinding.timeText.text = "8:00 PM"
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
        dialogBinding.timePassedText.visibility = View.GONE
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
                dialogBinding.timeText.text = formattedTime
            },
            hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun chooseRepeatMenuOption(view: View){
        dialogBinding.timePassedText.visibility = View.GONE
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.does_not_repeat_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.does_not_repeat -> {
                    dialogBinding.doesNotRepeatText.text = getString(R.string.does_not_repeat)
                    true
                }
                R.id.daily -> {
                    dialogBinding.doesNotRepeatText.text = getString(R.string.repeats_daily)
                    true
                }
                R.id.weekly -> {
                    dialogBinding.doesNotRepeatText.text = getString(R.string.repeats_weekly)
                    true
                }
                R.id.monthly -> {
                    dialogBinding.doesNotRepeatText.text = getString(R.string.repeats_monthly)
                    true
                }
                R.id.yearly -> {
                    dialogBinding.doesNotRepeatText.text = getString(R.string.repeats_yearly)
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
        val selectedDateText = dialogBinding.dateText.text.toString()
        val selectedTimeText = dialogBinding.timeText.text.toString()
        println(" selected--- ${selectedDateText}  ${selectedTimeText}")
        if (selectedDateText.isNotEmpty() && selectedTimeText.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("MMMM dd yyyy hh:mm a", Locale.getDefault()) // Format for "November 20 6:00 PM"
            try {
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
                        dialogBinding.timePassedText.visibility = View.VISIBLE
                    } else {
                        println("Selected time is in the future.")
                        dialogBinding.timePassedText.visibility = View.GONE
                    }
                } else {
                    Log.e("AddFragment", "Failed to parse date and time")
                }
            } catch (e: ParseException) {
                Log.e("AddFragment", "Date parse error: ${e.message}")
            }
        }
        else {
            Toast.makeText(requireContext(), "Please select both date and time", Toast.LENGTH_SHORT).show()
            dialogBinding.timePassedText.visibility = View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        (activity as MainActivity).showBottomNavBar()
        _binding = null
    }

}