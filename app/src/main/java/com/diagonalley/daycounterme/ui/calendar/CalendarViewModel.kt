package com.diagonalley.daycounterme.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.diagonalley.daycounterme.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
) : BaseViewModel() {

    private val _eventByDate = MutableLiveData<List<HorizontalEvent>>()
    val eventsByDate: LiveData<List<HorizontalEvent>> get() = _eventByDate


    fun setEvent(event: CalendarEvent) {
        _event.value = event
    }

    private val _event = MutableLiveData<CalendarEvent>()
    val event: LiveData<CalendarEvent> get() = _event

    fun selectDate(date: LocalDate) {
        val size = (15..30).random()
        val newEventByList = arrayListOf<HorizontalEvent>()
        for (i in 0 until size) {
            newEventByList.add(HorizontalEvent(i, "Event by id $i"))
        }
        _eventByDate.value = newEventByList
    }
}

enum class CalendarEvent {
    CHECKED_VERSION, AD_INITIALIZED, LOAD_AD_FAILED, LOAD_AD_SUCCESS, SHOW_AD_FAILED, DISMISS_AD
}
