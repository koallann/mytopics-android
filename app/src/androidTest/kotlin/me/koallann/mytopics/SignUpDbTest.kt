package me.koallann.mytopics

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.koallann.mytopics.data.user.UserRepository
import me.koallann.mytopics.data.user.UserRepositoryImpl
import me.koallann.mytopics.data_db.AppDatabase
import me.koallann.mytopics.data_db.user.UserDaoClient
import me.koallann.mytopics.data_db.user.UserEntity
import me.koallann.mytopics.domain.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpDbTest {

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
    fun shouldNotSignUpWhenUserEmailIsAlreadyInUse() {
        UserEntity().apply {
            name = "John Doe"
            email = "john@acme.com"
            password = "123456"
        }.also {
            database.getUserDao().insert(it).subscribe()
        }

        val user = User(name = "John Foo", email = "john@acme.com", secret = User.Secret("123456"))

        repository.signUpUser(user)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertNotComplete()
            .assertError { it is SQLiteConstraintException }
    }

    @Test
    fun shouldSignUpWhenUserEmailIsNotInUse() {
        UserEntity().apply {
            name = "John Doe"
            email = "john.doe@acme.com"
            password = "123456"
        }.also {
            database.getUserDao().insert(it).subscribe()
        }

        val user = User(name = "John Foo", email = "john@acme.com", secret = User.Secret("123456"))

        repository.signUpUser(user)
            .test()
            .also { it.awaitTerminalEvent() }
            .assertSubscribed()
            .assertComplete()
            .assertValue { it == user.copy(secret = null) }
    }

}
