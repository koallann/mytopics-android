package me.koallann.mytopics.data.user

import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.mytopics.domain.Credentials
import me.koallann.mytopics.domain.User

interface UserLocalDataSource {

    fun hasUserSigned(): Boolean

    fun getSignedUser(): User?

    fun signInUser(credentials: Credentials): Single<User>

    fun createUser(user: User): Single<User>

    fun checkUserExists(email: String): Single<Boolean>

    fun signOutUser(): Completable

}
