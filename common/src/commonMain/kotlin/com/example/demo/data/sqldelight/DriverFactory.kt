package com.example.demo.data.sqldelight

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.demo.db.Click
import com.example.demo.db.Database
import com.example.demo.db.NetworkClick
import com.example.demo.di.NamedCoroutineScopeIO
import org.koin.core.annotation.Single

expect suspend fun provideSqlDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): SqlDriver

private suspend fun createDatabase(): Database {
    val driver = provideSqlDriver(Database.Schema)
    val database = Database(
        driver,
        ClickAdapter = Click.Adapter(numberAdapter = IntColumnAdapter),
        NetworkClickAdapter = NetworkClick.Adapter(numberAdapter = IntColumnAdapter)
    )

    Napier.d("Database created")
    return database
}

@Single
fun provideDatabaseFlow(@NamedCoroutineScopeIO coroutineScopeIO: CoroutineScope): StateFlow<Database?> {
    val stateFlow = MutableStateFlow<Database?>(null)
    coroutineScopeIO.launch {
        createDatabase().apply { stateFlow.update { this } }
    }
    return stateFlow
}
