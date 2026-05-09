package com.example.secondbrain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThoughtViewModel(private val dao: ThoughtDao) : ViewModel() {

    val thoughts = dao.getActiveThoughts(System.currentTimeMillis())
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addThought(content: String, summary: String, expiresAt: Long) {
        viewModelScope.launch {
            dao.insert(
                ThoughtEntity(
                    content = content,
                    summary = summary,
                    createdAt = System.currentTimeMillis(),
                    expiresAt = expiresAt
                )
            )
        }
    }
}
