package com.example.tugasm7_6958

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username") val username:String,
    @ColumnInfo(name = "password") var password:String,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "phoneNumber") val phoneNumber:String,
) {
    override fun toString(): String {
        return "$name - $username - ($phoneNumber)"
    }
}