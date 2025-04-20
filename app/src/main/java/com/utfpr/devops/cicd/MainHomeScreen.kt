package com.utfpr.devops.cicd

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.utfpr.devops.cicd.components.CommonButton
import com.utfpr.devops.cicd.components.CommonTopBar
import com.utfpr.devops.cicd.components.PersonListItem
import com.utfpr.devops.cicd.models.PersonModel
import com.utfpr.devops.cicd.repository.UserDatabaseHelper
import com.utfpr.devops.cicd.ui.theme.CICDTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MainHomeScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { UserDatabaseHelper(context) }
    var people by remember { mutableStateOf<List<PersonModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var retryTrigger by remember { mutableStateOf(0) }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadPeopleData() {
        try {
            withContext(Dispatchers.IO) {
                    people = db.getAllPeople()
            }
        } catch (e: Exception) {
            error = "Erro ao carregar pessoas: ${e.message ?: "Erro desconhecido"}"
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(retryTrigger) {
        loadPeopleData()
    }

    fun onRetryClick() {
        isLoading = true
        error = null
        retryTrigger++
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                appBarTitle = "Cadastro Pessoas / UTFPR",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("RegisterFormScreen")
                }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = "Criar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Criar")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        CommonButton(
                            buttonText = "Tentar Novamente",
                            buttonOnClick = { onRetryClick() }
                        )
                    }
                }
                people.isEmpty() -> {
                    Text(
                        text = "Nenhuma pessoa cadastrada",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(people) { person ->
                            PersonListItem(person = person)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeScreenPreview() {
    CICDTheme {
        MainHomeScreen(navController = rememberNavController())
    }
}