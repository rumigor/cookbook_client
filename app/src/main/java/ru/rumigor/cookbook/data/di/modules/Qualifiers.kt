package ru.rumigor.cookbook.data.di.modules

import javax.inject.Qualifier

@Qualifier
annotation class Cloud

@Qualifier
annotation class Cache

@Qualifier
annotation class InMemory

@Qualifier
annotation class Persisted