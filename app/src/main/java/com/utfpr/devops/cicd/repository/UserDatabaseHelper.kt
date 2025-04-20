package com.utfpr.devops.cicd.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.utfpr.devops.cicd.models.PersonModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class UserDatabaseHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val db: SQLiteDatabase by lazy { 
        try {
            this.writableDatabase
        } catch (e: Exception) {
            throw RuntimeException("Erro ao inicializar o banco de dados: ${e.message}")
        }
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    companion object{
        private const val DATABASE_NAME = "registerDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "Users"
        private const val COL_ID = "id"
        private const val COL_NAME = "nome"
        private const val COL_EMAIL = "email"
        private const val COL_PHONE = "telefone"
        private const val COL_BIRTHDAY = "dataNascimento"
        private const val COL_FATHERS_NAME = "nomePai"
        private const val COL_MOTHERS_NAME = "nomeMae"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val query = "CREATE TABLE IF NOT EXISTS $TABLE_USERS (" +
                    "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COL_NAME TEXT NOT NULL, " +
                    "$COL_EMAIL TEXT NOT NULL UNIQUE, " +
                    "$COL_PHONE TEXT NOT NULL, " +
                    "$COL_BIRTHDAY DATE NOT NULL, " +
                    "$COL_FATHERS_NAME TEXT NOT NULL, " +
                    "$COL_MOTHERS_NAME TEXT NOT NULL);"
            db?.execSQL(query)
        } catch (e: Exception) {
            throw RuntimeException("Erro ao criar tabela: ${e.message}")
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        try {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
            onCreate(db)
        } catch (e: Exception) {
            throw RuntimeException("Erro ao atualizar banco de dados: ${e.message}")
        }
    }

    fun convertDateInString(someDate: LocalDate): String{
        return someDate.format(formatter)
    }

    fun convertStringInDate(someDate: String): LocalDate{
        return LocalDate.parse(someDate, formatter)
    }

    @SuppressLint("Recycle", "Range")
    fun readDataById(email: String): PersonModel? {
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))

        var pessoa: PersonModel? = null
        if(cursor.moveToFirst())
        {
            val birthDate = cursor.getString(cursor.getColumnIndex(COL_BIRTHDAY))
            val convertedBirthDate = convertStringInDate(birthDate)
            pessoa = PersonModel(
                id = cursor.getInt(cursor.getColumnIndex(COL_ID)),
                nome = cursor.getString(cursor.getColumnIndex(COL_NAME)),
                email = cursor.getString(cursor.getColumnIndex(COL_EMAIL)),
                telefone = cursor.getString(cursor.getColumnIndex(COL_PHONE)),
                dataNascimento = convertedBirthDate,
                nomePai = cursor.getString(cursor.getColumnIndex(COL_FATHERS_NAME)),
                nomeMae = cursor.getString(cursor.getColumnIndex(COL_MOTHERS_NAME)),
            )
        }
        cursor.close()
        return pessoa
    }

    fun insertData(pessoa: PersonModel){
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, pessoa.nome)
        contentValues.put(COL_EMAIL, pessoa.email)
        contentValues.put(COL_PHONE, pessoa.telefone)
        contentValues.put(COL_BIRTHDAY, convertDateInString(pessoa.dataNascimento))
        contentValues.put(COL_FATHERS_NAME, pessoa.nomePai)
        contentValues.put(COL_MOTHERS_NAME, pessoa.nomeMae)

        db.insert(TABLE_USERS, null, contentValues)
    }

    fun updateData(pessoa: PersonModel){
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, pessoa.nome)
        contentValues.put(COL_EMAIL, pessoa.email)
        contentValues.put(COL_PHONE, pessoa.telefone)
        contentValues.put(COL_BIRTHDAY, convertDateInString(pessoa.dataNascimento))
        contentValues.put(COL_FATHERS_NAME, pessoa.nomePai)
        contentValues.put(COL_MOTHERS_NAME, pessoa.nomeMae)

        db.update(TABLE_USERS, contentValues, "id = ?", arrayOf(pessoa.id.toString()))
    }

    fun deleteData(pessoa: PersonModel){
        db.delete(TABLE_USERS, "id = ?", arrayOf(pessoa.id.toString()))
    }

    @SuppressLint("Recycle", "Range")
    fun getAllPeople(): List<PersonModel> {
        val people = mutableListOf<PersonModel>()
        val query = "SELECT * FROM $TABLE_USERS"
        val cursor = db.rawQuery(query, null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        val birthDate = cursor.getString(cursor.getColumnIndex(COL_BIRTHDAY))
                        val convertedBirthDate = convertStringInDate(birthDate)
                        val person = PersonModel(
                            id = cursor.getInt(cursor.getColumnIndex(COL_ID)),
                            nome = cursor.getString(cursor.getColumnIndex(COL_NAME)),
                            email = cursor.getString(cursor.getColumnIndex(COL_EMAIL)),
                            telefone = cursor.getString(cursor.getColumnIndex(COL_PHONE)),
                            dataNascimento = convertedBirthDate,
                            nomePai = cursor.getString(cursor.getColumnIndex(COL_FATHERS_NAME)),
                            nomeMae = cursor.getString(cursor.getColumnIndex(COL_MOTHERS_NAME))
                        )
                        people.add(person)
                    } catch (e: Exception) {
                        // Loga o erro mas continua processando os demais registros
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            throw RuntimeException("Erro ao ler dados do banco: ${e.message}")
        } finally {
            cursor.close()
        }
        return people
    }
}