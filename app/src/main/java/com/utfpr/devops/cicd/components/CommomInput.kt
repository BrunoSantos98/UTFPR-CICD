package com.utfpr.devops.cicd.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState

@Composable
fun CommomInput(
    modifier: Modifier = Modifier,
    commomInputvalue: String = "",
    commomInputonValueChange: (String) -> Unit,
    commomInputActualLen: Int,
    commomInputMinLength: Int,
    commomInputMaxLength: Int,
    commomInputenabled: Boolean = true,
    commomInputreadOnly: Boolean = false,
    commomInputOnBlur: (() -> Unit)? = null,
    commomInputlabel: (@Composable () -> Unit)? = null,
    commomInputplaceholder: (@Composable () -> Unit)? = null,
    commomInputleadingIcon: (@Composable () -> Unit)? = null,
    commomInputtrailingIcon: (@Composable () -> Unit)? = null,
    commomInputShowInformations: Boolean = true,
    commomInputIsError: Boolean = false,
    commomInputErrorMessage: String,
    commomInputvisualTransformation: VisualTransformation = VisualTransformation.None,
    commomInputkeyboardType: KeyboardType = KeyboardType.Text,
    commomInputimeAction: ImeAction = ImeAction.Next
){
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    LaunchedEffect(isFocused) {
        if (!isFocused) {
            commomInputOnBlur?.invoke()
        }
    }
    
    Column {
        OutlinedTextField(
            modifier = modifier,
            value = commomInputvalue,
            onValueChange = commomInputonValueChange,
            enabled = commomInputenabled,
            readOnly = commomInputreadOnly,
            label = commomInputlabel,
            placeholder = commomInputplaceholder,
            leadingIcon = commomInputleadingIcon,
            trailingIcon = commomInputtrailingIcon,
            isError = commomInputIsError,
            supportingText = {
                when {
                    commomInputIsError -> AditionalInformationsInput(
                        isError = commomInputIsError,
                        errorMessage = commomInputErrorMessage,
                        actualLength = commomInputActualLen,
                        maxLength = commomInputMaxLength
                    )
                    commomInputShowInformations -> AditionalInformationsInput(
                        isError = commomInputIsError,
                        errorMessage = commomInputErrorMessage,
                        actualLength = commomInputActualLen,
                        maxLength = commomInputMaxLength
                    )
                    else -> {}
                }
            },
            visualTransformation = commomInputvisualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = commomInputkeyboardType,
                imeAction = commomInputimeAction
            ),
            singleLine = true,
            interactionSource = interactionSource
        )
    }
}

@Composable
fun AditionalInformationsInput(isError: Boolean, errorMessage: String, actualLength: Int, maxLength: Int){

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    )
    {
        if(isError){
            Text(errorMessage, color = Color.Red)
            Text("$actualLength/$maxLength", color = Color.Red)
        }
        else{
            Spacer(modifier = Modifier.padding(0.dp))
            Text("$actualLength/$maxLength")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommomInputPreview() {
    var textTyped by remember { mutableStateOf("") }

    CommomInput(
        commomInputvalue = "Valor aqui",
        commomInputonValueChange = {textTyped = it},
        commomInputtrailingIcon = {Icon(imageVector = Icons.Rounded.Build, contentDescription = "asdasdasd")},
        commomInputimeAction = ImeAction.Next,
        commomInputMaxLength = 60,
        commomInputMinLength = 3,
        commomInputActualLen = 13,
        commomInputErrorMessage = "Por favor digite algo entre 3 e 60 caracteres"
    )
}

@Preview(showBackground = true)
@Composable
fun CommomInputErrorPreview() {
    var textTyped by remember { mutableStateOf("") }

    CommomInput(
        commomInputvalue = "Valor aqui",
        commomInputonValueChange = {textTyped = it},
        commomInputtrailingIcon = {Icon(imageVector = Icons.Rounded.Build, contentDescription = "asdasdasd")},
        commomInputimeAction = ImeAction.Next,
        commomInputIsError = true,
        commomInputMaxLength = 60,
        commomInputMinLength = 3,
        commomInputActualLen = 13,
        commomInputErrorMessage = " Minimo 3 e Máximo 60 caracteres"
    )
}

@Preview(showBackground = true)
@Composable
fun CommomInputAditionalInformationPreview() {
    AditionalInformationsInput(
        isError = false,
        errorMessage = "Deu erro",
        actualLength = 14,
        maxLength = 60
    )
}

@Preview(showBackground = true)
@Composable
fun CommomInputAditionalInformationErrorPreview() {
    AditionalInformationsInput(
        isError = true,
        errorMessage = "Deu erro",
        actualLength = 2,
        maxLength = 60
    )
}