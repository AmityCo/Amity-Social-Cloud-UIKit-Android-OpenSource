package com.amity.socialcloud.uikit.common.config

import com.google.gson.Gson
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * What the module gate costs per call.
 *
 * isExcluded runs on every page, component and element, on every recomposition.
 * It was called a nit twice without a number behind it, which is not a judgement,
 * it is a guess. Measured: 1725–2435 ns a call uncached, 23–35 ns with the result
 * memoised per config id. The guess was two orders of magnitude out.
 *
 * Not a benchmark harness — a JVM unit test with no JIT warmup guarantees and no
 * device. It answers one question only: is this cost in the nanosecond range
 * where it disappears under Compose, or the microsecond range where 50 elements
 * a frame starts to matter.
 */
class AmityModuleGateCostTest {

    private fun configure(json: String) {
        val cfg = Gson().fromJson(
            """{"features": $json, "excludes": []}""",
            AmityUIKitConfig::class.java,
        )
        AmityUIKitConfigController.setConfigForTesting(cfg)
    }

    private fun timeNs(runs: Int, block: () -> Unit): Long {
        repeat(runs / 10) { block() }          // rough warmup
        val start = System.nanoTime()
        repeat(runs) { block() }
        return (System.nanoTime() - start) / runs
    }

    @Test
    fun `report the per-call cost of the three lookups`() {
        val runs = 200_000
        configure("{}")

        // A clip page: an owner-map hit plus the internal clip tier check.
        val deepest = timeNs(runs) { AmityUIKitConfigController.isExcluded("clip_feed_page/*/*") }
        // An id no table claims — two misses and a flag read.
        val unowned = timeNs(runs) { AmityUIKitConfigController.isExcluded("social_home_page/*/*") }
        // The element segment, which is the one that runs per list item.
        val element = timeNs(runs) {
            AmityUIKitConfigController.isExcluded("social_home_page/*/clipsfeed_button")
        }

        println("isExcluded ns/call — deepest chain: $deepest, unowned: $unowned, element: $element")

        // Guards the cache rather than the constant: uncached this was ~1725 ns,
        // so anything in that range means the memo was removed or its key stopped
        // matching. Loose enough not to fail on a slow CI box.
        assertTrue(
            "isExcluded costs $deepest ns per call — uncached it measured ~1725 ns, so " +
                "the per-config-id memo is gone or no longer hit",
            deepest < 400,
        )
    }
}
