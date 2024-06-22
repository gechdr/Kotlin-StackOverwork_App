package com.example.tugasm7_6958

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AnswerDao {
    @Insert
    fun insert(answer:AnswerEntity)

    @Update
    fun update(answer: AnswerEntity)

    @Delete
    fun delete(answer: AnswerEntity)

    @Query("DELETE FROM answers where id = :id")
    fun deleteQuery(id: Int):Int

    @Query("SELECT * FROM answers")
    fun fetch():MutableList<AnswerEntity>

    @Query("SELECT * FROM answers where idQuestion = :idQuestion")
    fun getByQuestion(idQuestion:Int):MutableList<AnswerEntity>?
}