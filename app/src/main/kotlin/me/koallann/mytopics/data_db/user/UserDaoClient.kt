package me.koallann.mytopics.data_db.user

import android.content.Context
import androidx.room.EmptyResultSetException
import io.reactivex.Completable
import io.reactivex.Single
import me.koallann.mytopics.data.user.UserLocalDataSource
import me.koallann.mytopics.domain.Credentials
import me.koallann.mytopics.domain.User
import me.koallann.mytopics.data_db.AppDatabase
import me.koallann.support.content.SharedPreferences
import java.util.concurrent.TimeUnit

class UserDaoClient(context: Context, private val userDao: UserDao) : UserLocalDataSource {

    companion object {
        private const val KEY_SIGNED_USER_ID = "user_id"
        private const val KEY_SIGNED_USER_NAME = "user_name"
        private const val KEY_SIGNED_USER_EMAIL = "user_email"
    }

    private val preferences: SharedPreferences = SharedPreferences(context)

    override fun hasUserSigned(): Boolean {
        return preferences.getInt(KEY_SIGNED_USER_ID, -1) != -1
    }

    override fun getSignedUser(): User? {
        val id = preferences.getInt(KEY_SIGNED_USER_ID, -1)
        if (id == -1) {
            return null
        }
        return User(
            id,
            preferences.getString(KEY_SIGNED_USER_NAME, ""),
            preferences.getString(KEY_SIGNED_USER_EMAIL, "")
        )
    }

    override fun signInUser(credentials: Credentials): Single<User> {
        return Completable.timer(2, TimeUnit.SECONDS)
            .andThen(userDao.findByEmail(credentials.email))
            .flatMap {
                if (it.password == credentials.password) {
                    preferences.apply {
                        put(KEY_SIGNED_USER_ID, it.id)
                        put(KEY_SIGNED_USER_NAME, it.name)
                        put(KEY_SIGNED_USER_EMAIL, it.email)
                    }
                    Single.just(it)
                } else {
                    Single.error(IllegalArgumentException("Wrong password"))
                }
            }
            .map { it.toDomain() }
    }

    override fun createUser(user: User): Single<User> {
        val password = user.secret?.password ?: throw IllegalArgumentException("Password is not set")

        return Completable.timer(2, TimeUnit.SECONDS)
            .andThen(
                userDao.insert(
                    UserEntity().apply {
                        name = user.name
                        email = user.email
                        this.password = password
                    }
                )
            )
            .toSingle {
                user.secret = null
                user
            }
    }

    override fun checkUserExists(email: String): Single<Boolean> {
        return Single.create { emitter ->
            userDao.findByEmail(email).subscribe(
                { emitter.onSuccess(true) },
                {
                    if (it is EmptyResultSetException) {
                        emitter.onSuccess(false)
                    } else {
                        emitter.onError(it)
                    }
                }
            )
        }
    }

    override fun signOutUser(): Completable {
        return Completable.timer(2, TimeUnit.SECONDS)
            .andThen(Completable.fromCallable { preferences.clear() })
    }

}
