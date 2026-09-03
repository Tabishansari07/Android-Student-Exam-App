package com.example.onlyforca
//package com.example.sevensem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Doctor(
    val name: String,
    val specialization: String
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                DoctorAppointmentApp()
            }
        }
    }
}

@Composable
fun DoctorAppointmentApp() {

    val doctors = listOf(
        Doctor("Dr. Rahul Sharma", "Cardiologist"),
        Doctor("Dr. Priya Patel", "Dentist"),
        Doctor("Dr. Aman Verma", "Dermatologist"),
        Doctor("Dr. Neha Singh", "Neurologist"),
        Doctor("Dr. Arjun Mehta", "Orthopedic")
    )

    var selectedDoctor by remember {
        mutableStateOf<Doctor?>(null)
    }

    var appointmentBooked by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Doctor Appointment",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(doctors) { doctor ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = doctor.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = doctor.specialization
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                selectedDoctor = doctor
                            }
                        ) {
                            Text("Book Appointment")
                        }

                    }

                }

            }

        }

        if (appointmentBooked && selectedDoctor != null) {

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Appointment Confirmed",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Doctor : ${selectedDoctor!!.name}")

                    Text("Specialization : ${selectedDoctor!!.specialization}")

                }

            }

        }

    }
    if (selectedDoctor != null && !appointmentBooked) {

        AlertDialog(

            onDismissRequest = {
                selectedDoctor = null
            },

            title = {
                Text("Confirm Appointment")
            },

            text = {
                Text("Do you want to book an appointment with ${selectedDoctor!!.name}?")
            },

            confirmButton = {

                Button(
                    onClick = {
                        appointmentBooked = true
                    }
                ) {
                    Text("Yes")
                }

            },

            dismissButton = {

                OutlinedButton(
                    onClick = {
                        selectedDoctor = null
                    }
                ) {
                    Text("No")
                }

            }

        )

    }

}