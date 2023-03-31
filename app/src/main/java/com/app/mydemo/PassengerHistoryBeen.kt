package com.app.mydemo

data class PassengerHistoryBeen(
    val data: List<PassengerHistoryData>,
    val totalPages: Int,
    val totalPassengers: Int
)