package com.example.onlyforca
//package com.example.sevensem
//package com.example.sevensem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// ---------------- DATA ----------------

data class Exam(
    val name: String,
    val rollNo: String,
    val subject: String,
    val marks: Int,
    val grade: String
)

// ---------------- DATABASE ----------------

class ExamDao {

    fun getExamData(): Flow<List<Exam>> = flow {

        // Simulating database delay
        delay(2000)

        emit(
            listOf(
                Exam("Rahul", "101", "Android", 88, "A"),
                Exam("Priya", "102", "Android", 92, "A+"),
                Exam("Amit", "103", "Android", 76, "B"),
                Exam("Neha", "104", "Android", 85, "A"),
                Exam("Arjun", "105", "Android", 69, "B"),
                Exam("Sneha", "106", "Android", 95, "A+"),
                Exam("Vikram", "107", "Android", 72, "B")
            )
        )
    }
}

// ---------------- ACTIVITY ----------------

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExamApp()
        }
    }
}

// ---------------- COMPOSE ----------------

@Composable
fun ExamApp() {

    val dao = remember {
        ExamDao()
    }

    var exams by remember {
        mutableStateOf<List<Exam>>(emptyList())
    }

    var loading by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Student Examination",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // -------- LOAD BUTTON --------

        Button(
            onClick = {

                scope.launch {

                    loading = true
                    error = null

                    try {

                        // Structured Concurrency
                        coroutineScope {

                            // Database work
                            // runs on IO dispatcher
                            withContext(Dispatchers.IO) {

                                dao.getExamData()
                                    .collect { data ->

                                        exams = data
                                    }
                            }
                        }

                    } catch (e: CancellationException) {

                        // Let cancellation propagate
                        throw e

                    } catch (e: Exception) {

                        error = e.message

                    } finally {

                        // Stop loading
                        // after success/failure/cancellation
                        loading = false
                    }
                }
            }
        ) {
            Text("Load Exam Data")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // -------- LOADING --------

        if (loading) {

            CircularProgressIndicator()

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text("Loading examination data...")
        }

        // -------- ERROR --------

        error?.let {

            Text(
                text = "Error: $it"
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // -------- EXAM DATA --------

        LazyColumn {

            items(exams) { exam ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Name: ${exam.name}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Roll No: ${exam.rollNo}"
                        )

                        Text(
                            text = "Subject: ${exam.subject}"
                        )

                        Text(
                            text = "Marks: ${exam.marks}"
                        )

                        Text(
                            text = "Grade: ${exam.grade}"
                        )
                    }
                }
            }
        }
    }
}