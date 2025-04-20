package com.utfpr.devops.cicd

import com.utfpr.devops.cicd.models.states.FieldState
import org.junit.Test
import org.junit.Assert.*

class RegisterFormStateTest {
    @Test
    fun emptyFieldTest() {
        val field = FieldState(
            value = "",
            minLength = 3,
            maxLength = 50,
            label = "Nome"
        )
        
        assertFalse(field.validate())
        assertTrue(field.isError)
    }

    @Test
    fun minLengthFieldTest() {
        val field = FieldState(
            value = "Jo",
            minLength = 3,
            maxLength = 50,
            label = "Nome"
        )
        
        assertFalse(field.validate())
        assertTrue(field.isError)
    }

    @Test
    fun maxLengthFieldTest() {
        val field = FieldState(
            value = "João da Silva Santos Oliveira Pereira de Souza",
            minLength = 3,
            maxLength = 30,
            label = "Nome"
        )
        
        assertFalse(field.validate())
        assertTrue(field.isError)
    }

    @Test
    fun validFieldTest() {
        val field = FieldState(
            value = "João Silva",
            minLength = 3,
            maxLength = 50,
            label = "Nome"
        )
        
        assertTrue(field.validate())
        assertFalse(field.isError)
    }

    @Test
    fun invalidEmailFieldTest() {
        val field = FieldState(
            value = "email.invalido",
            minLength = 5,
            maxLength = 100,
            label = "Email"
        )
        
        assertFalse(field.validate())
        assertTrue(field.isError)
    }

    @Test
    fun validEmailFieldTest() {
        val field = FieldState(
            value = "email@valido.com",
            minLength = 5,
            maxLength = 100,
            label = "Email"
        )
        
        assertTrue(field.validate())
        assertFalse(field.isError)
    }

    @Test
    fun invalidPhoneFieldTest() {
        val field = FieldState(
            value = "123",
            minLength = 10,
            maxLength = 15,
            label = "Telefone"
        )
        
        assertFalse(field.validate())
        assertTrue(field.isError)
    }

    @Test
    fun validPhoneFieldTest() {
        val field = FieldState(
            value = "(41) 99999-9999",
            minLength = 10,
            maxLength = 15,
            label = "Telefone"
        )
        
        assertTrue(field.validate())
        assertFalse(field.isError)
    }
} 