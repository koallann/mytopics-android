package me.koallann.myagenda.data.user

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import me.koallann.myagenda.domain.Credentials
import me.koallann.myagenda.domain.User
import java.util.concurrent.TimeUnit

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

    override fun createUser(user: User): Single<User> {
        return localDataSource.createUser(user)
    }

    override fun sendRecoveryEmail(toEmail: String): Completable {
        return Completable.timer(2, TimeUnit.SECONDS)
            .andThen(localDataSource.checkUserExists(toEmail))
            .flatMapCompletable { exists ->
                if (exists)
                    Completable.complete()
                else
                    Completable.error(IllegalArgumentException("User doesn't exists"))
            }
    }

}
