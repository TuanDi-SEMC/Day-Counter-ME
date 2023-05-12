package com.diagonalley.daycounterme.data.model

import com.google.firebase.firestore.DocumentId


data class UserInfo(
    var sign_in_type: String = "",
    var sign_in_account: String = "",
    @DocumentId var uid: String? = null,
)