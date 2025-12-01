package com.neosoft.notesmanager.ui.notes.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neosoft.notesmanager.data.localstorage.Notes

@Composable
fun ComposeNotesScreen(
    notes: List<Notes>,
    onAdd: (String, String, String) -> Unit,
    onDelete: (Notes) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(
            text = { Text("Add") },
            onClick = { showDialog = true },
            icon = { Icon(Icons.Default.Add, "") })
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SearchBar {  }
            TagRow(notes)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notes) { note ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(note.title, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text(note.body, style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(6.dp))
                            FlowRowTags(note.tags)
                            Text(
                                java.text.SimpleDateFormat("MMM dd, yyyy")
                                    .format(java.util.Date(note.createdAt)),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "Delete",
                                modifier = Modifier.padding(top = 8.dp)
                                    .clickable { onDelete(note) },
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
    if (showDialog) {
        AddNoteDialog(
            onAdd = { t, b, tag -> onAdd(t, b, tag); showDialog = false },
            onDismiss = { showDialog = false })
    }
}

@Composable
fun SearchBar(onQuery: (String) -> Unit) { TextField(
    value = "",
    onValueChange = { onQuery(it) },
    placeholder = { Text("Search") },
    modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
)
}

@Composable
fun TagRow(notes: List<Notes>) {
}

@Composable
fun FlowRowTags(tags: String) {
}

@Composable
fun AddNoteDialog(onAdd: (String, String, String) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Note") },
        text = {
            Column {
                OutlinedTextField(title, { title = it }, label = { Text("Title") })
                OutlinedTextField(body, { body = it }, label = { Text("Body") })
                OutlinedTextField(tags, { tags = it }, label = { Text("Tags (comma)") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank()) onAdd(
                    title,
                    body,
                    tags
                )
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
