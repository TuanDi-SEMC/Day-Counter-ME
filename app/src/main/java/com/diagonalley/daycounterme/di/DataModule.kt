package com.diagonalley.daycounterme.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideFirestore() = Firebase.firestore

    @Singleton
    @Provides
    @Named("users")
    fun provideUserCollection(firestore: FirebaseFirestore) = firestore.collection("users")

    @Singleton
    @Provides
    @Named("DataCollection")
    fun provideDataCollection(firestore: FirebaseFirestore) = firestore.collection("data")
}