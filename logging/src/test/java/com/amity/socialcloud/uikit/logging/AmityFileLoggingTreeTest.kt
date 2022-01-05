package com.amity.socialcloud.uikit.logging

import android.content.Context
import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AmityFileLoggingTreeTest {

    @Test
    fun show_logs_on_terminal_test() {
        var ret = 0
        mockkStatic(Log::class)
        val context: Context = mockk()
        every { Log.d(any(), any()) } answers {
            ret = 3
            ret
        }

        every { Log.e(any(), any()) } answers {
            ret = 6
            ret
        }

        every { Log.w(any(), any<String>()) } answers {
            ret = 5
            ret
        }

        every { Log.i(any(), any()) } answers {
            ret = 4
            ret
        }

        every { Log.v(any(), any()) } answers {
            ret = 2
            ret
        }

        val fileLoggingTree = AmityFileLoggingTree(context)
        fileLoggingTree.showLogsOnTerminal("timeStamp", "TAG", 3, "MESSAGE")
        assertTrue(ret == 3)

        fileLoggingTree.showLogsOnTerminal("timeStamp", "TAG", 6, "MESSAGE")
        assertTrue(ret == 6)

        fileLoggingTree.showLogsOnTerminal("timeStamp", "TAG", 5, "MESSAGE")
        assertTrue(ret == 5)

        fileLoggingTree.showLogsOnTerminal("timeStamp", "TAG", 4, "MESSAGE")
        assertTrue(ret == 4)

        fileLoggingTree.showLogsOnTerminal("timeStamp", "TAG", 2, "MESSAGE")
        assertTrue(ret == 2)
    }

    @Test
    fun getPriorityStringTest() {
        val context: Context = mockk()
        var res = ""
        val fileLoggingTree = AmityFileLoggingTree(context)
        res = fileLoggingTree.priorityString(Log.VERBOSE)
        assertEquals(res, AmityLoggingConfig.VERBOSE)

        res = fileLoggingTree.priorityString(Log.DEBUG)
        assertEquals(res, AmityLoggingConfig.DEBUG)

        res = fileLoggingTree.priorityString(Log.INFO)
        assertEquals(res, AmityLoggingConfig.INFO)

        res = fileLoggingTree.priorityString(Log.WARN)
        assertEquals(res, AmityLoggingConfig.WARN)

        res = fileLoggingTree.priorityString(Log.ERROR)
        assertEquals(res, AmityLoggingConfig.ERROR)

        res = fileLoggingTree.priorityString(1)
        assertEquals(res, AmityLoggingConfig.ERROR)
    }
}