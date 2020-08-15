package me.koallann.myagenda.data.user

import io.reactivex.Maybe
import io.reactivex.Single
import me.koallann.myagenda.domain.Credentials
import me.koallann.myagenda.domain.User

class UserRepositoryImpl(private val localDataSource: UserLocalDataSource) : UserRepository {

    override fun hasUserSigned(): Boolean {
        return localDataSource.hasUserSigned()
    }

    override fun getSignedUser(): Maybe<User> {
        return localDataSource.getSignedUser()
    }

    override fun signInUser(credentials: Credentials): Single<User> {
        return localDataSource.signInUser(credentials)
    }

}
