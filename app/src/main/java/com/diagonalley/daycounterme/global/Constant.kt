package com.diagonalley.daycounterme.global

object Constant {
    const val EMPTY: String = ""
    const val FEEDBACK_EMAIL = "diagonalley.studio@gmail.com"
    const val PRIVACY_URL = "https://sites.google.com/view/fun-push-policy/trang-ch%E1%BB%A7"
    const val CONDITION_URL =
        "https://sites.google.com/view/fun-push-terms-conditions/trang-ch%E1%BB%A7"

    const val SHARED_PREPS = "crypto_shared_prefs"

    const val DATABASE_NAME = "funny-push.db"
    const val DATABASE_VERSION = 4

    const val NOTIFICATION_ID = "notification-id"
    const val NOTIFICATION = "notification"
    const val CHANNEL = "channel"
    const val CHANNEL_ID = "channel-id"

    const val IMAGE_LAUNCHER = "image/*"

    const val CONFIG_IN_REVIEW = "in_review"
    const val CONFIG_MIN_VERSION_CODE = "min_app_version_code"
    const val CONFIG_LATEST_VERSION_CODE = "latest_app_version_code"
    const val CONFIG_REVIEW_VERSION_CODE = "review_app_version_code"
    const val CONFIG_SHOW_AD = "show_ad"
    const val FIRE_STORE_ICON = "icon"

    const val SAMSUNG = "samsung"
    const val XIAOMI = "xiaomi"
    const val HUAWEI = "huawei"
}

const val ADMOD_TAG = "admob"

const val WEDDING = 0
const val BIRTHDAY = 1

enum class Event {
    WEEDING, BIRTHDAY, DAY,
}

enum class CountType {
    COUNT, COUNT_DOWN
}