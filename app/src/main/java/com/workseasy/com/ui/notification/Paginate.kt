package com.workseasy.com.ui.notification

data class Paginate(
    val count: Int,
    val current_page: Int,
    val has_more_pages: Boolean,
    val next_page_url: Any,
    val per_page: Int,
    val previous_page_url: Any,
    val total: Int,
    val total_pages: Int
)