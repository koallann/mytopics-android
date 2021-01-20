package me.koallann.mytopics

import android.content.Context
import androidx.room.EmptyResultSetException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.koallann.mytopics.data.user.UserRepository
import me.koallann.mytopics.data.user.UserRepositoryImpl
import me.koallann.mytopics.data_db.AppDatabase
import me.koallann.mytopics.data_db.user.UserDaoClient
import me.koallann.mytopics.data_db.user.UserEntity
import me.koallann.mytopics.domain.Credentials
import me.koallann.mytopics.domain.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInDbTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = UserRepositoryImpl(UserDaoClient(context, database.getUserDao()))
    }

    @After
    fun finish() {
        database.clearAllTables()
    }

    @Test
    fun shouldNotSignInWhenUserNotExists() {
        val credentials = Credentials("john.doe@acme.com", "123456")

        repository.signInUser(credentials)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertNotComplete()
            .assertError { it is EmptyResultSetException }
    }

    @Test
    fun shouldNotSignInWhenUserExistsAndCredentialsAreInvalid() {
        val credentials = Credentials("john.doe@acme.com", "123456")

        UserEntity().apply {
            name = "John Doe"
            email = credentials.email
            password = credentials.password
        }.also {
            database.getUserDao().insert(it).subscribe()
        }

        repository.signInUser(credentials.copy(password = "invalid"))
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertNotComplete()
            .assertError { it is IllegalArgumentException }
    }

    @Test
    fun shouldSignInWhenUserExistsAndCredentialsAreValid() {
        val credentials = Credentials("john.doe@acme.com", "123456")
        val user = User(1, "John Doe", credentials.email, User.Secret(credentials.password))

        UserEntity().apply {
            name = user.name
            email = user.email
            password = user.secret!!.password
        }.also {
            database.getUserDao().insert(it).subscribe()
        }

        repository.signInUser(credentials)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertComplete()
            .assertValue { it == user.copy(secret = null) }
    }

}
