package com.workseasy.com.ui.notification

data class Notification(
    val body: String,
    val created_at: String,
    val id: Int,
    val is_new: Boolean,
    val title: String,
    val updated_at: String,
    val user_id: Int
)