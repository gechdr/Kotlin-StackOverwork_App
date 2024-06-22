package com.example.tugasm7_6958

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuestionDao {
    @Insert
    fun insert(question:QuestionEntity)

    @Update
    fun update(question:QuestionEntity)

    @Delete
    fun delete(question: QuestionEntity)

    @Query("DELETE FROM questions where id = :id")
    fun deleteQuery(id: Int):Int

    @Query("SELECT * FROM questions")
    fun fetch():MutableList<QuestionEntity>

    @Query("SELECT * FROM questions where id = :id")
    fun get(id:Int):QuestionEntity?

    @Query("SELECT * FROM questions where owner = :owner")
    fun getByOwner(owner:String):MutableList<QuestionEntity>?

    @Query("SELECT * FROM questions where title like '%' || :search || '%'")
    fun search(search:String):MutableList<QuestionEntity>?

    @Query("SELECT * FROM questions where owner = :owner and title like '%' || :search || '%'")
    fun searchByOwner(owner:String, search:String):MutableList<QuestionEntity>?
}