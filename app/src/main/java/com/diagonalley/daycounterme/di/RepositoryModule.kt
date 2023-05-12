package com.diagonalley.daycounterme.di

import android.content.Context
import com.diagonalley.daycounterme.global.SharedPreps
import com.diagonalley.daycounterme.repository.FirebaseAuthRepository
import com.diagonalley.daycounterme.repository.FirestoreRepository
import com.diagonalley.daycounterme.repository.impl.FirebaseAuthRepositoryImpl
import com.diagonalley.daycounterme.repository.impl.FirestoreRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideFirestoreRepository(@Named("users") collection: CollectionReference): FirestoreRepository {
        return FirestoreRepositoryImpl(collection)
    }

    @Provides
    fun provideFirebaseAuthRepository(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth,
        sharedPreps: SharedPreps,
    ): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl(context, firebaseAuth, sharedPreps)
    }
}