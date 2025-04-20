package com.utfpr.devops.cicd.models

import java.time.LocalDate


data class PersonModel(
    val id: Int?,
    val nome: String,
    val email: String,
    val telefone: String,
    val dataNascimento: LocalDate,
    val nomePai: String,
    val nomeMae: String,
)
