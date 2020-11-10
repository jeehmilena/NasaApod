package com.jess.eaiclubnasa.usecase

import com.jess.eaiclubnasa.Constants.DATE_PATTERN
import com.jess.eaiclubnasa.model.ApodResult
import com.jess.eaiclubnasa.repository.ApodRepository
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

class ApodUseCase(private val repository: ApodRepository) {

    private fun getListDate(date: String): List<String> {
        val listDate = arrayListOf<String>()
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        val currentCalendar = Calendar.getInstance()
        val data = dateFormat.parse(date)
        currentCalendar.time = data

        for (i in 0..5) {
            currentCalendar.add(Calendar.DATE, -1)
            val toDate = dateFormat.format(currentCalendar.time)
            listDate.add(toDate)
        }
        return listDate
    }

    suspend fun getApodList(date: String): MutableList<ApodResult> {
        return getListDate(date).map {
            repository.getApod(it)
        }.toMutableList()
    }
}