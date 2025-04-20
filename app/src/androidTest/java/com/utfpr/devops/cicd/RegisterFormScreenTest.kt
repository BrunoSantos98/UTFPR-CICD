package com.utfpr.devops.cicd

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterFormScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun validateEmptyField() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Nome").performTextInput("")
        composeTestRule.onNodeWithText("Confirmar").performClick()

        composeTestRule.onNodeWithText("O nome deve ter entre 3 e 50 caracteres")
            .assertExists()
    }

    @Test
    fun validateShortField() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Nome").performTextInput("Jo")
        composeTestRule.onNodeWithText("Confirmar").performClick()

        composeTestRule.onNodeWithText("O nome deve ter entre 3 e 50 caracteres")
            .assertExists()
    }

    @Test
    fun validateLongField() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Nome").performTextInput("João da Silva Santos Oliveira Pereira de Souza")
        composeTestRule.onNodeWithText("Confirmar").performClick()

        composeTestRule.onNodeWithText("O nome deve ter entre 3 e 50 caracteres")
            .assertExists()
    }

    @Test
    fun validateEmailField() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Email").performTextInput("email.invalido")
        composeTestRule.onNodeWithText("Confirmar").performClick()

        composeTestRule.onNodeWithText("Email inválido")
            .assertExists()
    }

    @Test
    fun validatePhoneField() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Telefone").performTextInput("123")
        composeTestRule.onNodeWithText("Confirmar").performClick()

        composeTestRule.onNodeWithText("O telefone deve ter entre 10 e 15 caracteres")
            .assertExists()
    }

    @Test
    fun validateFathersNameField() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Nome do Pai").performTextInput("")
        composeTestRule.onNodeWithText("Confirmar").performClick()

        composeTestRule.onNodeWithText("O nome do pai deve ter entre 3 e 50 caracteres")
            .assertExists()
    }

    @Test
    fun validateMothersNameField() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Nome da Mãe").performTextInput("")
        composeTestRule.onNodeWithText("Confirmar").performClick()

        composeTestRule.onNodeWithText("O nome da mãe deve ter entre 3 e 50 caracteres")
            .assertExists()
    }

    @Test
    fun validateBirthdateField() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Data de Nascimento").performTextInput("31/02/2023")
        composeTestRule.onNodeWithText("Confirmar").performClick()

        composeTestRule.onNodeWithText("Data de nascimento inválida")
            .assertExists()
    }

    @Test
    fun validateEmptyForm() {
        composeTestRule.setContent {
            RegisterFormScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Nome").performTextInput("João Silva")
        composeTestRule.onNodeWithText("Email").performTextInput("joao@teste.com")
        composeTestRule.onNodeWithText("Telefone").performTextInput("(41) 99999-9999")
        composeTestRule.onNodeWithText("Data de Nascimento").performTextInput("01/01/1990")
        composeTestRule.onNodeWithText("Nome do Pai").performTextInput("José Silva")
        composeTestRule.onNodeWithText("Nome da Mãe").performTextInput("Maria Silva")

        composeTestRule.onNodeWithText("Confirmar").performClick()

        // Verifica se não há mensagens de erro
        composeTestRule.onNodeWithText("O nome deve ter entre 3 e 50 caracteres")
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("Email inválido")
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("O telefone deve ter entre 10 e 15 caracteres")
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("O nome do pai deve ter entre 3 e 50 caracteres")
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("O nome da mãe deve ter entre 3 e 50 caracteres")
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("Data de nascimento inválida")
            .assertDoesNotExist()
    }
} 