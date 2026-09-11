package com.amity.socialcloud.uikit.community.compose.livestream.room.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import java.lang.ref.WeakReference

/**
 * Tracks which post is currently playing in a floating window, so that tapping the same
 * livestream post again expands that window instead of starting a second playback session.
 *
 * Shared by both PiP hosts — the live room player and the recorded-livestream player — because
 * a viewer can re-tap either one, and a registry scoped to a single Activity would miss the
 * other. Only one floating window can exist at a time, so a single slot is enough.
 *
 * The Activity is held weakly: a floating window that has already been destroyed must never
 * keep its Activity alive, and a stale record must never be treated as a live session.
 */
object AmityPipSessionRegistry {

    private data class Session(
        val postId: String,
        val activity: WeakReference<Activity>,
    )

    @Volatile
    private var session: Session? = null

    /** Records [activity] as the owner of the floating window showing [postId]. */
    fun onEnteredPip(postId: String?, activity: Activity) {
        if (postId.isNullOrBlank()) return
        session = Session(postId, WeakReference(activity))
    }

    /**
     * Clears the session if [activity] still owns it. Guarded so a late teardown cannot wipe a
     * newer window opened by a different Activity.
     */
    fun onLeftPip(activity: Activity) {
        if (session?.activity?.get() === activity) {
            session = null
        }
    }

    /**
     * Ends the floating session if one is open for a **different** post than [postId].
     *
     * Only one stream may play at a time. Without this, opening a second livestream leaves the
     * first playing in its window while the new one plays full screen — two players, and both
     * audible, since nothing arbitrates audio focus between them. Replacing rather than stacking
     * matches what viewers expect from a mini-player.
     *
     * Finishing the owner tears down its player and its window; the session record is cleared by
     * the owner's own teardown. A null [postId] ends any open session, since a player with no
     * post cannot be the one already floating.
     */
    /**
     * Ends the floating session, whatever it is showing.
     *
     * Called when another video surface takes over — a clip, a story, a video post. Only one
     * thing should play at a time, and those surfaces are not PiP hosts, so nothing else would
     * stand the window down: the viewer would end up hearing both at once.
     */
    fun endSession() = endSessionForOtherPost(null)

    fun endSessionForOtherPost(postId: String?) {
        val current = session ?: return
        if (current.postId == postId) return
        val owner = current.activity.get()
        if (owner == null) {
            // Nothing left to finish; drop the stale record.
            session = null
            return
        }
        session = null
        if (!owner.isFinishing && !owner.isDestroyed) {
            owner.finish()
        }
    }

    /**
     * Brings the floating window showing [postId] back to full screen, if one is open.
     *
     * Returns true when an existing session was expanded, meaning the caller must not start a
     * second one.
     *
     * The window lives in its own pinned task. Starting an Activity into a pinned task is what
     * takes that task out of Picture-in-Picture, so the session is restored by re-launching the
     * owner's own component **from the owner itself** — `SINGLE_TOP` targets the instance
     * already at the top of that task, so playback carries on rather than restarting. Merely
     * moving the task to the front is not enough: it surfaces the window without unpinning it,
     * which is why the viewer still ended up on a second player.
     */
    fun expandExisting(context: Context, postId: String?): Boolean {
        if (postId.isNullOrBlank()) return false
        val current = session ?: return false
        if (current.postId != postId) return false
        val owner = current.activity.get() ?: run {
            // The owning Activity is gone; the record is stale.
            session = null
            return false
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false
        if (owner.isFinishing || owner.isDestroyed) {
            session = null
            return false
        }
        val expanded = try {
            owner.startActivity(
                Intent(owner, owner.javaClass).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                )
            )
            true
        } catch (e: Exception) {
            false
        }
        if (expanded) return true

        // Fall back to surfacing the task. Weaker — it may leave the window pinned — but it
        // still puts the viewer in front of the session they asked for.
        return try {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val task = manager.appTasks.firstOrNull {
                it.taskInfo.taskId == owner.taskId
            } ?: return false
            task.moveToFront()
            true
        } catch (e: Exception) {
            // Task lookup can fail while a session is tearing down. Fall back to starting
            // normally rather than leaving the viewer on a dead screen.
            false
        }
    }
}
