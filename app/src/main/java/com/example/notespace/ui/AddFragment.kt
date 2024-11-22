package com.example.notespace.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.utils.ReminderNotificationWorker
import com.example.notespace.databinding.AddReminderDialogBoxBinding
import com.example.notespace.databinding.BottomMenuPopUpLayoutBinding
import com.example.notespace.databinding.BottomRemindPopUpLayoutBinding
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentAddBinding
import com.example.notespace.model.Notes
import com.example.notespace.viewModel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddFragment: Fragment() {

    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val notesViewModel: NotesViewModel by viewModels()

    private lateinit var addOnDialog : BottomSheetDialog
    private lateinit var menuDialog: BottomSheetDialog
    private lateinit var remindMeDialog : BottomSheetDialog
    private var addReminderDialogBox : Dialog? = null

    private lateinit var dialogBinding: AddReminderDialogBoxBinding
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        setUpActionBar()
        setUpBottomNavBar()

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()
        (activity as MainActivity).hideMainBottomNavLayout()
        (activity as MainActivity).hideMainBottomNavLayout()

        imageUri = arguments?.getParcelable("image")
        println("Received: $imageUri")

        if (imageUri != null) {
            binding.addImageView.visibility = View.VISIBLE
            binding.addImageView.setImageURI(imageUri)
        }

        binding.clockReminderCardview.setOnClickListener {
            showAddReminderDialogBox()
        }

        dialogBinding = AddReminderDialogBoxBinding.inflate(LayoutInflater.from(context))
    }

    fun setUpActionBar(){
        binding.baseToolbar.apply {
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

    fun showRemindMeBottomPopUpDialog(){
        val remindMeViewBinding = BottomRemindPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        remindMeDialog = BottomSheetDialog(requireContext())
        remindMeDialog.setContentView(remindMeViewBinding.root)
        remindMeViewBinding.apply {
            remindLaterTodayLayout.setOnClickListener {
                binding.clockReminderCardview.visibility = View.VISIBLE
                val laterDayText = remindLaterTodayText.text.toString()
                val laterDayTime = "," + remindLaterTodayTimeText.text.toString()
                binding.dayText.text = laterDayText
                binding.timeText.text = laterDayTime
                remindMeDialog.dismiss()
            }

            remindTomorrowLayout.setOnClickListener {
                binding.clockReminderCardview.visibility = View.VISIBLE
                val tomorrowDayText = remindTomorrowText.text.toString()
                val tomorrowDayTime = "," + remindTomorrowTimeText.text.toString()
                binding.dayText.text = tomorrowDayText
                binding.timeText.text = tomorrowDayTime
                remindMeDialog.dismiss()
            }

            remindNextDayLayout.setOnClickListener {
                binding.clockReminderCardview.visibility = View.VISIBLE
                val nextDayText = remindNextDayText.text.toString()
                val nextDayTime = "," + remindNextDayTimeText.text.toString()
                binding.dayText.text = nextDayText
                binding.timeText.text = nextDayTime
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

    fun setUpBottomNavBar(){
        binding.apply{
            baseAddOnIcon.setOnClickListener {
                showAddOnBottomPopUpDialog()
            }
            baseColorPaleteIcon.setOnClickListener {

            }
            baseTextStylePickerIcon.setOnClickListener {

            }
            baseMenuIcon.setOnClickListener {
                showMenuBottomPopUpDialog()
            }
        }
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

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val title = binding.addTitleEditText.text.toString()
        val description = binding.addDescriptionEditText.text.toString()
        val reminderTime = parseReminderDateTime(dialogBinding.dateText.text.toString(), dialogBinding.timeText.text.toString())

        val note = Notes(0, title, description, System.currentTimeMillis(),false, imageUri?.toString(), reminderTime)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val currentId = notesViewModel.addNote(note)

                if (title.isEmpty() && description.isEmpty() && imageUri == null) {
                    delay(3000)
                    if (isAdded) {
                        notesViewModel.deleteNoteById(currentId)
                    }
                } else {
                    val reminderTime = parseReminderDateTime(dialogBinding.dateText.text.toString(),dialogBinding.timeText.text.toString())
                    if(reminderTime != null && reminderTime > System.currentTimeMillis()){
                        val notificationTitle: String
                        val notificationDescription : String

                        when {
                            title.isEmpty() && description.isEmpty() -> {
                                notificationTitle = "Untitled note"
                                notificationDescription = "Reminder set for: ${dialogBinding.dateText.text}, ${dialogBinding.timeText.text}"
                            }
                            title.isNotEmpty() && description.isEmpty() -> {
                                notificationTitle = title
                                notificationDescription = "Reminder set for: ${dialogBinding.dateText.text} ${dialogBinding.timeText.text}"
                            }
                            else -> {
                                notificationTitle = title
                                notificationDescription = description
                            }
                        }
                        println("notification ${notificationTitle} ,${notificationDescription}, ${reminderTime}")
                        scheduleReminder(notificationTitle, notificationDescription, reminderTime)
                    }
                    findNavController().navigate(R.id.action_addFragment_to_dashboardFragment)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun parseReminderDateTime(dateText: String, timeText: String): Long? {
        val dateFormat = SimpleDateFormat("MMMM dd yyyy hh:mm a", Locale.getDefault())
        return try {
            dateFormat.parse("$dateText $timeText")?.time
        } catch (e: ParseException) {
//            Log.e("AddFragment", "Failed to parse reminder date and time: ${e.message}")
            null
        }
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
                    binding.clockReminderCardview.visibility=View.GONE
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
                        binding.dayText.text = selectedDateText
                        binding.timeText.text = selectedTimeText
                    }
                    val reminderTime = parseReminderDateTime(selectedDateText, selectedTimeText)
                    if (reminderTime != null && reminderTime > System.currentTimeMillis()) {
                        scheduleReminder(binding.addTitleEditText.text.toString(), binding.addDescriptionEditText.text.toString(), reminderTime)
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

                val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
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

    private fun validateOptedTime() {
        val selectedDateText = dialogBinding.dateText.text.toString()
        val selectedTimeText = dialogBinding.timeText.text.toString()
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
        } else {
            Toast.makeText(requireContext(), "Please select both date and time", Toast.LENGTH_SHORT).show()
            dialogBinding.timePassedText.visibility = View.GONE
        }
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

    private fun scheduleReminder(title: String, message: String, reminderTime: Long) {
        println(" schedule gets ${title},  ${message}, ${reminderTime}")
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
        _binding = null
    }
}