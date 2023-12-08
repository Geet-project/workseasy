package com.workseasy.com.ui.notification

data class NotificationResponse(
    val notifications: ArrayList<Notification>,
    val paginate: Paginate
)