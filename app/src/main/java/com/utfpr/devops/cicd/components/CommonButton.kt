package com.utfpr.devops.cicd.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CommonButton(
  buttonText: String,
  modifier: Modifier = Modifier,
  buttonOnClick: () -> Unit,
  buttonEnabled: Boolean = true,
  buttonIcon: ImageVector? = null,
  iconButtonContentDescription: String? = null,
){
    ElevatedButton(
        modifier = modifier,
        enabled = buttonEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        onClick = { buttonOnClick() }) {
        if(buttonIcon != null)
        {
            Icon(
                modifier = modifier.padding(end = 12.dp),
                imageVector = buttonIcon,
                contentDescription = iconButtonContentDescription
            )
        }
        Text(
            modifier = modifier,
            text = buttonText
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommomButtonPreview() {
    CommonButton(
        buttonText = "Stay with me",
        buttonIcon = Icons.Rounded.Phone,
        buttonOnClick = {},
    )
}