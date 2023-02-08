package com.diagonalley.daycounterme.utils

import timber.log.Timber

/**
 * Created by TuanTM on 11/20/20.
 */
class DebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "TuanTM/(${element.fileName}:${element.lineNumber})#${element.methodName}"
    }
}