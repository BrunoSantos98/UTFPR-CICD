package com.utfpr.devops.cicd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.utfpr.devops.cicd.components.CommonButton
import com.utfpr.devops.cicd.components.CommonTopBar
import com.utfpr.devops.cicd.ui.theme.CICDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CICDTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable("Home") {
            HomeScreen(navController)
        }
        composable("MainHomeScreen") {
            MainHomeScreen(navController)
        }
        composable("RegisterFormScreen") {
            RegisterFormScreen(navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CommonTopBar(
                appBarTitle = "Cadastro Pessoas / UTFPR",
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Seja bem vindo a aplicação de fazer cadastros, que tal começar fazendo o seu?",
                fontSize = 20.sp,
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(top = 48.dp))
            CommonButton(
                buttonText = "Acessar Cadastro",
                buttonOnClick = {
                    navController.navigate("MainHomeScreen")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CICDTheme {
        HomeScreen(navController = rememberNavController())
    }
}