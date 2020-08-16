package me.koallann.myagenda.data.user

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import me.koallann.myagenda.domain.Credentials
import me.koallann.myagenda.domain.User

interface UserRepository {

    fun hasUserSigned(): Boolean

    fun getSignedUser(): Maybe<User>

    fun signInUser(credentials: Credentials): Single<User>

    fun createUser(user: User): Single<User>

    fun sendRecoveryEmail(toEmail: String): Completable

}
