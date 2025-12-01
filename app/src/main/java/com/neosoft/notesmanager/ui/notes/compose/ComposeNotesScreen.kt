package com.neosoft.notesmanager.ui.notes.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neosoft.notesmanager.data.localstorage.Notes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNotesScreen(
    notes: List<Notes>,
    onAdd: (String, String, String) -> Unit,
    onDelete: (Notes) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF2563EB),
                onClick = { showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Title
            Text(
                "Notes Manager",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF1F2937)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "Organize your notes with tags and search",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280)
            )

            Spacer(Modifier.height(16.dp))

            SearchBarStyled()

            Spacer(Modifier.height(12.dp))

            Text(
                "Filter by tags:",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF374151)
            )

            Spacer(Modifier.height(8.dp))

            FilterTagRow(notes)

            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(notes) { note ->
                    NoteCard(note, onDelete)
                }
            }
        }
    }

    if (showDialog) {
        AddNoteDialog(
            onAdd = { t, b, tag -> onAdd(t, b, tag); showDialog = false },
            onDismiss = { showDialog = false }
        )
    }
}
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBarStyled() {
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Search notes by title, body, or tags...") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }

@Composable
fun FilterTagRow(notes: List<Notes>) {
    val tags = notes.flatMap { it.tags.split(",") }.distinct()

    Row(
        Modifier
            .horizontalScroll(rememberScrollState())
            .padding(end = 8.dp)
    ) {
        tags.forEach { tag ->
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFEEF1F6))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(tag, color = Color(0xFF4D5566))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteCard(note: Notes, onDelete: (Notes) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(note.title, style = MaterialTheme.typography.titleMedium)

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.clickable { onDelete(note) }
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(note.body, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                note.tags.split(",").forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFEEF1F6))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(tag, color = Color(0xFF4D5566))
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                java.text.SimpleDateFormat("MMM dd, yyyy")
                    .format(java.util.Date(note.createdAt)),
                modifier = Modifier.align(Alignment.End),
                color = Color(0xFF6B7280)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(
    onAdd: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "Add Note",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF1F2937)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Body") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title, body, tags)
                    }
                }
            ) {
                Text("Save", color = Color(0xFF2563EB))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}



