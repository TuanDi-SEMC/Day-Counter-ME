package com.diagonalley.daycounterme.repository.impl

import com.diagonalley.daycounterme.data.model.UserInfo
import com.diagonalley.daycounterme.repository.FirestoreRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class FirestoreRepositoryImpl @Inject constructor(@Named("UserCollection") private val collectionReference: CollectionReference) :
    FirestoreRepository {
    override fun saveUserInfo(userInfo: UserInfo, callback: FirestoreCallback<Void?>) {
        collectionReference.document(userInfo.uid!!).set(userInfo).handleSet(callback)
    }

    override fun getUserInfoByUid(uid: String, callback: FirestoreCallback<UserInfo?>) {
        collectionReference.document(uid).get().handleGet(callback)
    }
}

/**
 * An extension function on the Task<Void> class that takes a FirestoreCallback<Void> object.
 * It sets up listeners to handle the success or failure of a Firestore set operation and calls the appropriate callback function.
 * @param callback object representing the callback functions to be called in case of success or failure.
 * */
fun Task<Void>.handleSet(callback: FirestoreCallback<Void?>) {
    this.addOnSuccessListener {
        callback.onSuccess(it)
    }.addOnFailureListener { exception ->
        Timber.d("Error getting documents: ", exception)
        callback.onFailure(exception)
    }
}

inline fun <reified T> Task<DocumentSnapshot>.handleGet(callback: FirestoreCallback<T?>) {
    this.addOnSuccessListener {
        val data = if (it.exists()) {
            it.toObject(T::class.java)
        } else {
            null
        }
        callback.onSuccess(data)
    }.addOnFailureListener { exception ->
        Timber.d("Error getting documents: ", exception)
        callback.onFailure(exception)
    }
}

interface FirestoreCallback<T> {
    fun onSuccess(result: T)
    fun onFailure(exception: Exception)
}