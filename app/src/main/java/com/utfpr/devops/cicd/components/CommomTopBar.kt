package com.utfpr.devops.cicd.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    appBarTitle: String = "App Cadastro",
    appBarNavigationIcon: ImageVector? = null,
    appBarNavigationIconContextDescription: String? = null,
    appBarNavigationIconOnClick: (() -> Unit)? = null,
){
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(appBarTitle)
                },
        navigationIcon = {
        if(appBarNavigationIcon != null && appBarNavigationIconOnClick != null) {
                IconButton(appBarNavigationIconOnClick) {
                    Icon(
                        imageVector = appBarNavigationIcon,
                        contentDescription = appBarNavigationIconContextDescription
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CommomTopBarPreview() {
    CommonTopBar(
        appBarTitle = "Titulo aqui",
    ){}
}