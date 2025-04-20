package com.utfpr.devops.cicd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.utfpr.devops.cicd.components.CommomInput
import com.utfpr.devops.cicd.components.CommonButton
import com.utfpr.devops.cicd.components.CommonTopBar
import com.utfpr.devops.cicd.models.PersonModel
import com.utfpr.devops.cicd.models.states.RegisterFormState
import com.utfpr.devops.cicd.repository.UserDatabaseHelper
import com.utfpr.devops.cicd.ui.theme.CICDTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterFormScreen(navController: NavController) {
    var formState by remember { mutableStateOf(RegisterFormState()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    var hasAttemptedRegistration by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    val context = LocalContext.current
    val db = remember { UserDatabaseHelper(context) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(showLoading) {
        if (!showLoading && hasAttemptedRegistration) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                appBarTitle = "Cadastre-se",
                appBarNavigationIcon = Icons.AutoMirrored.Rounded.ArrowBack,
                appBarNavigationIconContextDescription = "Person Icon",
                appBarNavigationIconOnClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo Nome
            CommomInput(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                commomInputvalue = formState.name.value,
                commomInputonValueChange = { formState = formState.copy(name = formState.name.copy(value = it)) },
                commomInputlabel = { Text(formState.name.label) },
                commomInputplaceholder = { Text("Digite seu nome aqui") },
                commomInputIsError = formState.name.isError,
                commomInputMaxLength = formState.name.maxLength,
                commomInputMinLength = formState.name.minLength,
                commomInputActualLen = formState.name.value.length,
                commomInputErrorMessage = "Mínimo ${formState.name.minLength} e Máximo ${formState.name.maxLength} caracteres"
            )

            // Campo Email
            CommomInput(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                commomInputvalue = formState.email.value,
                commomInputonValueChange = { formState = formState.copy(email = formState.email.copy(value = it)) },
                commomInputlabel = { Text(formState.email.label) },
                commomInputplaceholder = { Text("Digite seu email aqui") },
                commomInputkeyboardType = KeyboardType.Email,
                commomInputIsError = formState.email.isError,
                commomInputMaxLength = formState.email.maxLength,
                commomInputMinLength = formState.email.minLength,
                commomInputActualLen = formState.email.value.length,
                commomInputErrorMessage = "Mínimo ${formState.email.minLength} e Máximo ${formState.email.maxLength} caracteres"
            )

            // Campo Telefone
            CommomInput(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                commomInputvalue = formState.phone.value,
                commomInputonValueChange = { 
                    val digits = it.replace(Regex("[^0-9]"), "")
                    val limitedDigits = if (digits.length > 11) digits.substring(0, 11) else digits
                    formState = formState.copy(phone = formState.phone.copy(value = limitedDigits))
                },
                commomInputlabel = { Text(formState.phone.label) },
                commomInputplaceholder = { Text("Digite seu telefone aqui") },
                commomInputkeyboardType = KeyboardType.Phone,
                commomInputIsError = formState.phone.isError,
                commomInputMaxLength = formState.phone.maxLength,
                commomInputMinLength = formState.phone.minLength,
                commomInputActualLen = formState.phone.value.length,
                commomInputErrorMessage = "Por favor digite um telefone válido",
                commomInputOnBlur = {
                    val phone = formState.phone.value.replace(Regex("[^0-9]"), "")
                    val formattedPhone = when {
                        phone.length == 11 -> "(${phone.substring(0, 2)}) ${phone.substring(2, 7)}-${phone.substring(7)}"
                        phone.length == 10 -> "(${phone.substring(0, 2)}) ${phone.substring(2, 6)}-${phone.substring(6)}"
                        else -> phone
                    }
                    formState = formState.copy(phone = formState.phone.copy(value = formattedPhone))
                }
            )

            // Campo Data de Nascimento
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) {
                CommomInput(
                    commomInputvalue = selectedDate,
                    commomInputonValueChange = { },
                    commomInputlabel = { Text("Data de nascimento") },
                    commomInputreadOnly = true,
                    commomInputtrailingIcon = {
                        IconButton(onClick = { showDatePicker = !showDatePicker }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Selecione sua data de nascimento"
                            )
                        }
                    },
                    commomInputMaxLength = 0,
                    commomInputMinLength = 0,
                    commomInputActualLen = 0,
                    commomInputShowInformations = false,
                    commomInputErrorMessage = "Por favor digite algo entre 3 e 60 caracteres",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp)
                        .padding(horizontal = 0.dp)
                )

                if (showDatePicker) {
                    Popup(
                        onDismissRequest = { showDatePicker = false },
                        alignment = Alignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 42.dp)
                                .shadow(elevation = 4.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false
                            )
                        }
                    }
                }
            }

            CommomInput(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                commomInputvalue = formState.fatherName.value,
                commomInputonValueChange = { formState = formState.copy(fatherName = formState.fatherName.copy(value = it)) },
                commomInputlabel = { Text(formState.fatherName.label) },
                commomInputplaceholder = { Text("Digite nome de seu pai aqui") },
                commomInputIsError = formState.fatherName.isError,
                commomInputMaxLength = formState.fatherName.maxLength,
                commomInputMinLength = formState.fatherName.minLength,
                commomInputActualLen = formState.fatherName.value.length,
                commomInputErrorMessage = "Mínimo ${formState.fatherName.minLength} e Máximo ${formState.fatherName.maxLength} caracteres"
            )

            CommomInput(
                modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                commomInputvalue = formState.motherName.value,
                commomInputonValueChange = { formState = formState.copy(motherName = formState.motherName.copy(value = it)) },
                commomInputlabel = { Text(formState.motherName.label) },
                commomInputplaceholder = { Text("Digite nome de sua mãe aqui") },
                commomInputimeAction = ImeAction.Done,
                commomInputIsError = formState.motherName.isError,
                commomInputMaxLength = formState.motherName.maxLength,
                commomInputMinLength = formState.motherName.minLength,
                commomInputActualLen = formState.motherName.value.length,
                commomInputErrorMessage = "Mínimo ${formState.motherName.minLength} e Máximo ${formState.motherName.maxLength} caracteres"
            )

            CommonButton(
                buttonText = "Confirmar cadastro",
                buttonOnClick = {
                    val updatedName = formState.name.copy().apply { validate() }
                    val updatedEmail = formState.email.copy().apply { validate() }
                    val updatedPhone = formState.phone.copy().apply { validate() }
                    val updatedFatherName = formState.fatherName.copy().apply { validate() }
                    val updatedMotherName = formState.motherName.copy().apply { validate() }

                    // Atualiza o estado com os resultados da validação
                    formState = formState.copy(
                        name = updatedName,
                        email = updatedEmail,
                        phone = updatedPhone,
                        fatherName = updatedFatherName,
                        motherName = updatedMotherName
                    )

                    if (!updatedName.isError && !updatedEmail.isError && !updatedPhone.isError &&
                        !updatedFatherName.isError && !updatedMotherName.isError) {
                        
                        hasAttemptedRegistration = true
                        showLoading = true
                        
                        scope.launch {
                            try {
                                val pessoa = PersonModel(
                                    id = null,
                                    nome = updatedName.value,
                                    email = updatedEmail.value,
                                    telefone = updatedPhone.value,
                                    nomeMae = updatedMotherName.value,
                                    nomePai = updatedFatherName.value,
                                    dataNascimento = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                                )
                                
                                withContext(Dispatchers.IO) {
                                    db.insertData(pessoa)
                                }
                            } finally {
                                showLoading = false
                            }
                        }
                    }
                },
                buttonIcon = Icons.Rounded.Check,
                iconButtonContentDescription = "Check"
            )
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Preview
@Composable
fun RegisterFormScreenPreview() {
    CICDTheme {
        RegisterFormScreen(navController = rememberNavController())
    }
}