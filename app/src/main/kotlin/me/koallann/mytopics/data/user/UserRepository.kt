package me.koallann.mytopics.data.user

import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.mytopics.domain.Credentials
import me.koallann.mytopics.domain.User

interface UserRepository {

    fun hasUserSigned(): Boolean

    fun getSignedUser(): User?

    fun signInUser(credentials: Credentials): Single<User>

    fun signUpUser(user: User): Single<User>

    fun sendRecoveryEmail(toEmail: String): Completable

    fun signOutUser(): Completable

}
