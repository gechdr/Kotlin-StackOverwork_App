package com.example.tugasm7_6958

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class AnswerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "idQuestion") val idQuestion: Int,
    @ColumnInfo(name = "answer") val answer: String,
    @ColumnInfo(name = "owner") val owner: String,
){
    override fun toString(): String {
        return "$id - $idQuestion - $answer - $owner"
    }
}