package com.jess.eaiclubnasa.usecase

import com.jess.eaiclubnasa.model.ApodResult
import com.jess.eaiclubnasa.repository.ApodRepository
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

class ApodUseCase(private val repository: ApodRepository) {

    private fun getListDate(date: String): List<String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentCalendar = Calendar.getInstance()
        val listDate = arrayListOf<String>()
        val data = dateFormat.parse(date)
        currentCalendar.time = data

        for (i in 1..5) {
            currentCalendar.add(Calendar.DATE, -1)
            val toDate = dateFormat.format(currentCalendar.time)
            listDate.add(toDate)
        }
        return listDate
    }

    suspend fun getApodList(date: String): MutableList<ApodResult> {
        val listApod = mutableListOf<ApodResult>()
        val listDate = getListDate(date)

        listDate.forEach {
            val apod = repository.getApod(it)
            listApod.add(apod)
        }

        return listApod
    }
}