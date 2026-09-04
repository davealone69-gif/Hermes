package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AgentConfig
import com.example.data.AppRepository
import com.example.data.ChatMessage
import com.example.data.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    val messages: StateFlow<List<ChatMessage>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val agentConfig: StateFlow<AgentConfig?> = repository.agentConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // Initialize config if null
        viewModelScope.launch {
            if (repository.agentConfig.firstOrNull() == null) {
                repository.updateConfig(AgentConfig())
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.insertMessage(ChatMessage(text = text, isUser = true))
            
            // Mock AI response for now
            delay(1000)
            val currentConfig = agentConfig.value ?: AgentConfig()
            val replyText = "As ${currentConfig.name} (${currentConfig.modelType}), I received your message: '$text'."
            repository.insertMessage(ChatMessage(text = replyText, isUser = false))
        }
    }
    
    fun clearChat() {
        viewModelScope.launch {
            repository.clearMessages()
        }
    }

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertTask(Task(title = title, description = description))
        }
    }

    fun toggleTaskCompletion(id: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateTaskStatus(id, isCompleted)
        }
    }
    
    fun deleteTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    fun updateConfig(name: String, modelType: String) {
        viewModelScope.launch {
            repository.updateConfig(AgentConfig(name = name, modelType = modelType))
        }
    }
}

class MainViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
