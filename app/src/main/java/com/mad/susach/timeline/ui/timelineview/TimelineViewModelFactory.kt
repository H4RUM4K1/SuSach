package com.mad.susach.timeline.ui.timelineview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad.susach.timeline.data.repository.EraRepository
import com.mad.susach.timeline.data.repository.TimelineRepository

class TimelineViewModelFactory(
    private val timelineRepository: TimelineRepository = TimelineRepository(),
    private val eraRepository: EraRepository = EraRepository(),
    private val eraId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimelineViewModel(
                repository = timelineRepository,
                eraRepository = eraRepository,
                eraId = eraId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
