package com.utfpr.devops.cicd.models.states

data class RegisterFormState(
    val name: FieldState = FieldState(
        minLength = 3,
        maxLength = 50,
        label = "Nome"
    ),
    val email: FieldState = FieldState(
        minLength = 5,
        maxLength = 100,
        label = "Email"
    ),
    val phone: FieldState = FieldState(
        minLength = 10,
        maxLength = 15,
        label = "Telefone"
    ),
    val fatherName: FieldState = FieldState(
        minLength = 3,
        maxLength = 50,
        label = "Nome do Pai"
    ),
    val motherName: FieldState = FieldState(
        minLength = 3,
        maxLength = 50,
        label = "Nome da Mãe"
    ),
    val birthDate: FieldState = FieldState(
        minLength = 10,
        maxLength = 10,
        label = "Data de Nascimento"
    )
)