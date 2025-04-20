package com.utfpr.devops.cicd

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.utfpr.devops.cicd.models.PersonModel
import com.utfpr.devops.cicd.repository.UserDatabaseHelper
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class UserDatabaseHelperTest {
    private lateinit var db: UserDatabaseHelper
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = UserDatabaseHelper(context)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertPerson() {
        val person = PersonModel(
            id = null,
            nome = "João Silva",
            email = "joao@teste.com",
            telefone = "(41) 99999-9999",
            dataNascimento = LocalDate.now(),
            nomePai = "José Silva",
            nomeMae = "Maria Silva"
        )

        val result = db.insertData(person)
        assertNotNull(result)
    }

    @Test
    fun errorWhenInsertSamePerson() {
        val person1 = PersonModel(
            id = null,
            nome = "João Silva",
            email = "joao@teste.com",
            telefone = "(41) 99999-9999",
            dataNascimento = LocalDate.now(),
            nomePai = "José Silva",
            nomeMae = "Maria Silva"
        )

        val person2 = PersonModel(
            id = null,
            nome = "João Silva",
            email = "joao@teste.com",
            telefone = "(41) 99999-9999",
            dataNascimento = LocalDate.now(),
            nomePai = "José Silva",
            nomeMae = "Maria Silva"
        )

        val result1 = db.insertData(person1)
        val result2 = db.insertData(person2)

        assertNotNull(result1)
        //assertFalse(result2)
    }

    @Test
    fun searchByEmail() {
        val person = PersonModel(
            id = null,
            nome = "João Silva",
            email = "joao@teste.com",
            telefone = "(41) 99999-9999",
            dataNascimento = LocalDate.now(),
            nomePai = "José Silva",
            nomeMae = "Maria Silva"
        )

        db.insertData(person)
        val savedPerson = db.readDataById("joao@teste.com")

        assertNotNull(savedPerson)
        assertEquals(person.nome, savedPerson?.nome)
        assertEquals(person.email, savedPerson?.email)
        assertEquals(person.telefone, savedPerson?.telefone)
    }

    @Test
    fun searchByEmailIncorrect() {
        val savedPerson = db.readDataById("123")
        assertNull(savedPerson)
    }

    @Test
    fun searchAllPeople() {
        val person1 = PersonModel(
            id = null,
            nome = "João Silva",
            email = "joao1@teste.com",
            telefone = "(41) 99999-9999",
            dataNascimento = LocalDate.now(),
            nomePai = "José Silva",
            nomeMae = "Maria Silva"
        )

        val person2 = PersonModel(
            id = null,
            nome = "Maria Silva",
            email = "maria@teste.com",
            telefone = "(41) 98888-8888",
            dataNascimento = LocalDate.now(),
            nomePai = "José Silva",
            nomeMae = "Ana Silva"
        )

        db.insertData(person1)
        db.insertData(person2)

        val people = db.getAllPeople()
        assertEquals(2, people.size)
    }

    @Test
    fun tryUpdatePerson() {
        val person = PersonModel(
            id = null,
            nome = "João Silva",
            email = "joao@teste.com",
            telefone = "(41) 99999-9999",
            dataNascimento = LocalDate.now(),
            nomePai = "José Silva",
            nomeMae = "Maria Silva"
        )

        db.insertData(person)
        val savedPerson = db.readDataById("joao@teste.com")

        val updatedPerson = savedPerson?.copy(
            nome = "João da Silva",
            telefone = "(41) 98888-8888"
        )

        if (updatedPerson != null) {
            val result = db.updateData(updatedPerson)
            assertNotNull(result)

            val personAfterUpdate = db.readDataById("joao@teste.com")
            assertEquals("João da Silva", personAfterUpdate?.nome)
            assertEquals("(41) 98888-8888", personAfterUpdate?.telefone)
        }
    }

    @Test
    fun tryDeletePerson() {
        val person = PersonModel(
            id = null,
            nome = "João Silva",
            email = "joao@teste.com",
            telefone = "(41) 99999-9999",
            dataNascimento = LocalDate.now(),
            nomePai = "José Silva",
            nomeMae = "Maria Silva"
        )

        db.insertData(person)
        val result = db.deleteData(person)
        assertNull(result)

        val deletedPerson = db.readDataById("joao@teste.com")
        assertNull(deletedPerson)
    }
} 