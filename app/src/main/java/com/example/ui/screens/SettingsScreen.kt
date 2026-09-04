package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val config by viewModel.agentConfig.collectAsStateWithLifecycle()
    
    var agentName by remember(config) { mutableStateOf(config?.name ?: "Hermes") }
    var expanded by remember { mutableStateOf(false) }
    var selectedModel by remember(config) { mutableStateOf(config?.modelType ?: "Nous (Local)") }
    
    val models = listOf("Nous (Local)", "Llama 3 (Local)", "Mistral (Local)", "Gemini Pro (Cloud)")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agent Settings", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            Text("Agent Identity", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            OutlinedTextField(
                value = agentName,
                onValueChange = { agentName = it },
                label = { Text("Agent Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Model Selection", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedModel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Active Model") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    models.forEach { model ->
                        DropdownMenuItem(
                            text = { Text(model) },
                            onClick = {
                                selectedModel = model
                                expanded = false
                            },
                            trailingIcon = if (model == selectedModel) {
                                { Icon(Icons.Default.Check, contentDescription = null) }
                            } else null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.updateConfig(agentName, selectedModel)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Save Configuration")
            }
        }
    }
}
