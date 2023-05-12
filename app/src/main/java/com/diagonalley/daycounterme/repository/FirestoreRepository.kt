package com.diagonalley.daycounterme.repository

import com.diagonalley.daycounterme.data.model.UserInfo
import com.diagonalley.daycounterme.repository.impl.FirestoreCallback

interface FirestoreRepository {
    fun saveUserInfo(userInfo: UserInfo, callback: FirestoreCallback<Void?>)
    fun getUserInfoByUid(uid: String, callback: FirestoreCallback<UserInfo?>)
}