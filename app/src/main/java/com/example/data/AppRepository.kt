package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val chatDao: ChatDao,
    private val taskDao: TaskDao,
    private val configDao: ConfigDao
) {
    val allMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    val agentConfig: Flow<AgentConfig?> = configDao.getConfig()

    suspend fun insertMessage(message: ChatMessage) = chatDao.insertMessage(message)
    suspend fun clearMessages() = chatDao.clearMessages()

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)
    suspend fun updateTaskStatus(id: Int, isCompleted: Boolean) = taskDao.updateTaskStatus(id, isCompleted)
    suspend fun deleteTask(id: Int) = taskDao.deleteTask(id)

    suspend fun updateConfig(config: AgentConfig) = configDao.updateConfig(config)
}
