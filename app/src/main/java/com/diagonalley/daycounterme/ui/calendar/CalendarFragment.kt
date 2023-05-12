package com.diagonalley.daycounterme.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.CalendarDayLayoutBinding
import com.diagonalley.daycounterme.databinding.FragmentCalendarBinding
import com.diagonalley.daycounterme.databinding.ItemHorizontalEventBinding
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.utils.setOnSingleClickListener
import com.diagonalley.daycounterme.utils.smoothToPosition
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthScrollListener
import com.kizitonwose.calendar.view.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {
    @Inject
    lateinit var appConfig: AppConfig

    private var selectedDate: LocalDate? = null
    private val viewModel: CalendarViewModel by viewModels()
    private val mAdapter by lazy {
        HorizontalEventAdapter {

        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.apply {
                eventsByDate.observe(viewLifecycleOwner) {
                    mAdapter.submitList(it)
                }
            }
            rcvEvent.apply {
                adapter = mAdapter
            }
            appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (verticalOffset == -collapsingToolbar.height + toolbar.height) {
                    //toolbar is collapsed here
                    //write your code here
                    toolbar.alpha = 1f
                } else {
                    toolbar.alpha = 0f
                }
            }

            val currentMonth = YearMonth.now()
            val currentDate = LocalDate.now()
            val endMonth = currentMonth.plusMonths(100)  // Adjust as needed

            tvYear.text = currentMonth.year.toString()
            tvMonth.text = currentMonth.month.toString()

            calendarView.monthScrollListener = object : MonthScrollListener {
                override fun invoke(calendarMonth: CalendarMonth) {
                    val year = calendarMonth.yearMonth.year
                    val month = calendarMonth.yearMonth.month
                    tvYear.text = year.toString()
                    tvMonth.text = month.toString()
                    tvTitle.text = "$year $month"
                }
            }
            calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
                // Called only when a new container is needed.
                override fun create(view: View) = DayViewContainer(view) { selectedDate ->
                    viewModel.selectDate(selectedDate)
                    calendarView.notifyDateChanged(selectedDate)
                    rcvEvent.smoothToPosition(mAdapter)
                }

                // Called every time we need to reuse a container.
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.day = data
                    val textView = container.textView
                    textView.text = data.date.dayOfMonth.toString()
                    if (data.position == DayPosition.MonthDate) {
                        if (currentDate > data.date) {
                            textView.setTextColor(
                                resources.getColor(
                                    R.color.disableTextColor, null
                                )
                            )
                            textView.setBackgroundResource(R.drawable.bg_date_view)
                        } else {
                            // Show the month dates. Remember that views are reused!
                            if (data.date == selectedDate) {
                                // If this is the selected date, show a round background and change the text color.
                                textView.setTextColor(resources.getColor(R.color.white, null))
                                textView.setBackgroundResource(R.drawable.bg_selected_date_view)
                            } else {
                                // If this is NOT the selected date, remove the background and reset the text color.
                                textView.setTextColor(
                                    resources.getColor(
                                        R.color.primaryTextColor, null
                                    )
                                )
                                textView.setBackgroundResource(R.drawable.bg_disable_date_view)
                            }
                        }
                    } else {
                        // Hide in and out dates
                        textView.text = null
                        textView.background = null
                    }
                }
            }
            imgToggle.setOnSingleClickListener {
                appbar.setExpanded(true, true)
            }
            val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
            calendarView.setup(currentMonth, endMonth, daysOfWeek.first())
            calendarView.scrollToMonth(currentMonth)
            titlesContainer.children.map { it as TextView }.forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }
        }
    }

    inner class DayViewContainer(view: View, onNotifyDateChange: (LocalDate) -> Unit) :
        ViewContainer(view) {
        // With ViewBinding
        val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
        lateinit var day: CalendarDay

        init {
            view.setOnClickListener {
                // Check the day position as we do not want to select in or out dates.
                if (day.position == DayPosition.MonthDate) {
                    // Keep a reference to any previous selection
                    // in case we overwrite it and need to reload it.
                    val currentSelection = selectedDate
                    if (currentSelection == day.date) {
                        // If the user clicks the same date, clear selection.
                        selectedDate = null
                        // Reload this date so the dayBinder is called
                        // and we can REMOVE the selection background.
                        onNotifyDateChange(currentSelection)
                    } else {
                        selectedDate = day.date
                        // Reload the newly selected date so the dayBinder is
                        // called and we can ADD the selection background.
                        onNotifyDateChange(day.date)
                        if (currentSelection != null) {
                            // We need to also reload the previously selected
                            // date so we can REMOVE the selection background.
                            onNotifyDateChange(currentSelection)
                        }
                    }
                }
            }
        }
    }
}


data class HorizontalEvent(
    val id: Int,
    val eventName: String,
)

class HorizontalEventAdapter constructor(
    private val onClick: (HorizontalEvent) -> Unit,
) : ListAdapter<HorizontalEvent, HorizontalEventAdapter.HorizontalEventViewHolder>(
    HorizontalEventDiffCallback()
) {

    inner class HorizontalEventViewHolder(
        private val binding: ItemHorizontalEventBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HorizontalEvent) {
            binding.apply {
                tvTitle.text = item.eventName
                root.setOnSingleClickListener {
                    onClick(item)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalEventViewHolder {
        return HorizontalEventViewHolder(
            ItemHorizontalEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HorizontalEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HorizontalEventDiffCallback : DiffUtil.ItemCallback<HorizontalEvent>() {

    override fun areItemsTheSame(oldItem: HorizontalEvent, newItem: HorizontalEvent): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HorizontalEvent, newItem: HorizontalEvent): Boolean {
        return oldItem.id == newItem.id
    }
}


