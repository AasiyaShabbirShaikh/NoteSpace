package com.example.notespace.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.notespace.CameraHelper
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.AddReminderDialogBoxBinding
import com.example.notespace.databinding.BottomMenuPopUpLayoutBinding
import com.example.notespace.databinding.BottomRemindPopUpLayoutBinding
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentAddBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding
import com.example.notespace.model.Notes
import com.example.notespace.viewModel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        _binding = FragmentAddBinding.inflate(layoutInflater)

        setUpActionBar()
        setUpBottomNavBar()

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()

        // Use the same key "image" as in MainActivity
        imageUri = arguments?.getParcelable("image")
        println("Received: $imageUri")

        if (imageUri != null) {
            binding.addImageView.visibility = View.VISIBLE
            binding.addImageView.setImageURI(imageUri)
        }

        binding.clockReminderCardview.setOnClickListener {
            showAddReminderDialogBox()
        }

//        onReminderDayClicks()
    }

    fun setUpActionBar(){
        binding.baseToolbar.apply {
            backArrowIcon.setOnClickListener {
                requireActivity().onBackPressed()
            }
            pinIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "Pinned Clicked", Toast.LENGTH_LONG).show()
            }
            remindMeIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "Remind Clicked", Toast.LENGTH_LONG).show()
                showRemindMeBottomPopUpDialog()
            }
            archiveIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "Archive Clicked", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun showRemindMeBottomPopUpDialog(){
        val remindMeViewBinding = BottomRemindPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        remindMeDialog = BottomSheetDialog(requireContext())
        remindMeDialog.setContentView(remindMeViewBinding.root)

        remindMeViewBinding.apply {
            remindLaterTodayLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "tomorrow one clicked",Toast.LENGTH_LONG).show()
                binding.clockReminderCardview.visibility = View.VISIBLE
                val laterDayText = remindLaterTodayText.text.toString()
                val laterDayTime = "," + remindLaterTodayTimeText.text.toString()
                binding.dayText.text = laterDayText
                binding.timeText.text = laterDayTime
                remindMeDialog.dismiss()
            }

            remindTomorrowLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "tomorrow two clicked",Toast.LENGTH_LONG).show()
                binding.clockReminderCardview.visibility = View.VISIBLE
                val tomorrowDayText = remindTomorrowText.text.toString()
                val tomorrowDayTime = "," + remindTomorrowTimeText.text.toString()
                binding.dayText.text = tomorrowDayText
                binding.timeText.text = tomorrowDayTime
                remindMeDialog.dismiss()
            }

            remindNextDayLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "tomorrow three clicked",Toast.LENGTH_LONG).show()
                binding.clockReminderCardview.visibility = View.VISIBLE
                val nextDayText = remindNextDayText.text.toString()
                val nextDayTime = "," + remindNextDayTimeText.text.toString()
                binding.dayText.text = nextDayText
                binding.timeText.text = nextDayTime
                remindMeDialog.dismiss()
            }

            remindPickDateTimeLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "pick date time clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }

            remindPickPlaceLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "pick place clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
        }
        remindMeDialog.show()
    }

    fun setUpBottomNavBar(){
        binding.apply{
            baseAddOnIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "add on Clicked", Toast.LENGTH_SHORT).show()
                showAddOnBottomPopUpDialog()
            }
            baseColorPaleteIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "color palete Clicked", Toast.LENGTH_SHORT).show()

            }
            baseTextStylePickerIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "text style Clicked", Toast.LENGTH_SHORT).show()

            }
            baseMenuIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "menu Clicked", Toast.LENGTH_SHORT).show()
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
//                Toast.makeText(requireContext(), "take photo clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
            popupAddImageLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "add image clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
            popupDrawingLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "drawing clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
            popupRecordingLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "recording clicked",Toast.LENGTH_LONG).show()
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
//                Toast.makeText(requireContext(), "delete clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuMakeACopyLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "make a copy clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuSendLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "send clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuCollaboratorLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "collaborator clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuLabelsLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "labels clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuHelpFeedbackLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "helpFeedback clicked",Toast.LENGTH_LONG).show()
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

        val note = Notes(0, title, description, System.currentTimeMillis(),false, imageUri?.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val currentId = notesViewModel.addNote(note)

                if (title.isEmpty() && description.isEmpty() && imageUri == null) {
                    delay(3000)
                    if (isAdded) {
                        notesViewModel.deleteNoteById(currentId)
//                        Toast.makeText(requireContext(), getString(R.string.note_discarded), Toast.LENGTH_LONG).show()
                    }
                } else {
                    findNavController().navigate(R.id.action_addFragment_to_dashboardFragment)
                }
            } catch (e: Exception) {
            }
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
//                Toast.makeText(requireContext(), "date selected", Toast.LENGTH_SHORT).show()
                chooseDateMenuOption(dateLayout)
//                addReminderDialogBox?.dismiss()
            }
            timeLayout.setOnClickListener {
                chooseTimeMenuOption(timeLayout)
//                addReminderDialogBox?.dismiss()
            }
            doesNotRepeatLayout.setOnClickListener {
                chooseRepeatMenuOption(doesNotRepeatLayout)
//                addReminderDialogBox?.dismiss()
            }
            addReminderDeleteTextButton.setOnClickListener {
                //delete the reminder
                binding.clockReminderCardview.visibility=View.GONE
                addReminderDialogBox?.dismiss()
            }
            addReminderCancelTextButton.setOnClickListener {
                addReminderDialogBox?.dismiss()
            }
            saveButton.setOnClickListener {
                validateOptedTime()
//                addReminderDialogBox?.dismiss()
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
//                    Toast.makeText(requireContext(), "Today selected", Toast.LENGTH_SHORT).show()
                    val calendar = Calendar.getInstance()
                    val today = SimpleDateFormat("MMMM dd", Locale.getDefault()).format(calendar.time)
                    dialogBinding.dateText.text = today
                    true
                }
                R.id.tomorrow -> {
//                    Toast.makeText(requireContext(), "Tomorrow selected", Toast.LENGTH_SHORT).show()
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                    val tomorrow = SimpleDateFormat("MMMM dd", Locale.getDefault()).format(calendar.time)
                    dialogBinding.dateText.text = tomorrow
                    true
                }
                R.id.next_weekday -> {
//                    Toast.makeText(requireContext(), "Next Weekday selected", Toast.LENGTH_SHORT).show()
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, 7)
                    val nextWeekday = SimpleDateFormat("MMMM dd", Locale.getDefault()).format(calendar.time)
                    dialogBinding.dateText.text = nextWeekday
                    true
                }
                R.id.pick_a_date -> {
//                    Toast.makeText(requireContext(), "Pick a Date selected", Toast.LENGTH_SHORT).show()
                    showDatePickerDialogBox()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showDatePickerDialogBox() {
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

                val dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault()) // "MMMM" is full month name, "dd" is day
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
//                    Toast.makeText(requireContext(), "Today selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.timeText.text = "8:00 AM"
                    true
                }
                R.id.afternoon_option -> {
//                    Toast.makeText(requireContext(), "Tomorrow selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.timeText.text = "1:00 PM"
                    true
                }
                R.id.evening_option -> {
//                    Toast.makeText(requireContext(), "Next Weekday selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.timeText.text = "6:00 PM"
                    true
                }
                R.id.night_option -> {
//                    Toast.makeText(requireContext(), "Next Weekday selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.timeText.text = "8:00 PM"
                    true
                }
                R.id.pick_a_time -> {
//                    Toast.makeText(requireContext(), "Pick a Date selected", Toast.LENGTH_SHORT).show()
                    showTimePickerDialogBox()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showTimePickerDialogBox() {
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

        if (selectedDateText.isNotEmpty() && selectedTimeText.isNotEmpty()) {
            val selectedCalendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault()) // Ensure time format uses ":" instead of "."

            try {
                Log.d("AddFragment", "Date text: $selectedDateText, Time text: $selectedTimeText")
                val selectedDateTime = dateFormat.parse("$selectedDateText $selectedTimeText")
                selectedCalendar.time = selectedDateTime

                val currentCalendar = Calendar.getInstance()

                if (selectedCalendar.before(currentCalendar)) {
                    Log.d("AddFragment", "Selected time is in the past.")
                    dialogBinding.timePassedText.visibility = View.VISIBLE
                } else {
                    Log.d("AddFragment", "Selected time is in the future.")
                    dialogBinding.timePassedText.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("AddFragment", "Date parsing failed: ${e.message}")
                e.printStackTrace()
            }
        } else {
            Toast.makeText(requireContext(), "Please select both date and time", Toast.LENGTH_SHORT).show()
            dialogBinding.timePassedText.visibility = View.GONE
        }
    }


    private fun chooseRepeatMenuOption(view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.does_not_repeat_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.does_not_repeat -> {
//                    Toast.makeText(requireContext(), "Today selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.doesNotRepeatText.text = getString(R.string.does_not_repeat)
                    true
                }
                R.id.daily -> {
//                    Toast.makeText(requireContext(), "Tomorrow selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.doesNotRepeatText.text = getString(R.string.repeats_daily)
                    true
                }
                R.id.weekly -> {
//                    Toast.makeText(requireContext(), "Next Weekday selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.doesNotRepeatText.text = getString(R.string.repeats_weekly)
                    true
                }
                R.id.monthly -> {
//                    Toast.makeText(requireContext(), "Next Weekday selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.doesNotRepeatText.text = getString(R.string.repeats_monthly)
                    true
                }
                R.id.yearly -> {
//                    Toast.makeText(requireContext(), "Pick a Date selected", Toast.LENGTH_SHORT).show()
                    dialogBinding.doesNotRepeatText.text = getString(R.string.repeats_yearly)
                    true
                }
                R.id.custom -> {
//                    Toast.makeText(requireContext(), "Pick a Date selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AddFragment", "Fragment is destroyed")
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        _binding = null
    }
}