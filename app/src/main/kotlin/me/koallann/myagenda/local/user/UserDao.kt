package me.koallann.myagenda.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM users_user WHERE email = :email LIMIT 1")
    fun findByEmail(email: String): Single<UserEntity>

    @Insert
    fun insert(user: UserEntity): Completable

}
