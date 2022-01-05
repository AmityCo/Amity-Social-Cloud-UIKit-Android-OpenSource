package com.amity.socialcloud.uikit.community.members

import android.content.Context
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmityMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class AmityMembershipItemViewModel : AmityBaseViewModel() {

    var communityId = ""

    fun getBottomSheetMemberTab(
        context: Context,
        communityMembership: AmityCommunityMember
    ): Flowable<ArrayList<AmityMenuItem>> {
        val itemList = arrayListOf<AmityMenuItem>()
        return Flowable.combineLatest(
            checkModeratorPermission(AmityPermission.EDIT_COMMUNITY_USER),
            checkModeratorPermission(AmityPermission.REMOVE_COMMUNITY_USER),
            BiFunction { hasEditPermission, hasRemovePermission ->
                if (hasEditPermission && !isUserBanned(communityMembership)) {
                    itemList.add(
                        AmityMenuItem(
                            AmityConstants.ID_PROMOTE_MODERATOR,
                            context.getString(R.string.amity_promote_moderator)
                        )
                    )
                }

                communityMembership.getUser()?.let { user ->
                    if (user.isFlaggedByMe()) {
                        itemList.add(
                            AmityMenuItem(
                                AmityConstants.ID_UN_REPORT_USER,
                                context.getString(R.string.amity_undo_report)
                            )
                        )
                    } else {
                        itemList.add(
                            AmityMenuItem(
                                AmityConstants.ID_REPORT_USER,
                                context.getString(R.string.amity_report)
                            )
                        )
                    }
                }

                if (hasRemovePermission) {
                    itemList.add(
                        AmityMenuItem(
                            AmityConstants.ID_REMOVE_USER,
                            context.getString(R.string.amity_remove_from_community),
                            true
                        )
                    )
                }

                itemList
            })
    }

    fun getBottomSheetModeratorTab(
        context: Context,
        communityMembership: AmityCommunityMember
    ): Flowable<ArrayList<AmityMenuItem>> {
        val itemList = arrayListOf<AmityMenuItem>()
        return Flowable.combineLatest(
            checkModeratorPermission(AmityPermission.EDIT_COMMUNITY_USER),
            checkModeratorPermission(AmityPermission.REMOVE_COMMUNITY_USER),
            BiFunction { hasEditPermission, hasRemovePermission ->
                if (hasEditPermission) {
                    itemList.add(
                        AmityMenuItem(
                            AmityConstants.ID_REMOVE_MODERATOR,
                            context.getString(R.string.amity_remove_moderator)
                        )
                    )
                }

                communityMembership.getUser()?.let { user ->
                    if (user.isFlaggedByMe()) {
                        itemList.add(
                            AmityMenuItem(
                                AmityConstants.ID_UN_REPORT_USER,
                                context.getString(R.string.amity_undo_report)
                            )
                        )
                    } else {
                        itemList.add(
                            AmityMenuItem(
                                AmityConstants.ID_REPORT_USER,
                                context.getString(R.string.amity_report)
                            )
                        )
                    }
                }

                if (hasRemovePermission) {
                    itemList.add(
                        AmityMenuItem(
                            AmityConstants.ID_REMOVE_USER,
                            context.getString(R.string.amity_remove_from_community)
                        )
                    )
                }
                itemList
            })

    }

    private fun isUserBanned(communityMembership: AmityCommunityMember): Boolean {
        return communityMembership.isBanned()
                || communityMembership.getUser()?.isGlobalBan() ?: false
    }

    private fun checkModeratorPermission(permission: AmityPermission): Flowable<Boolean> {
        return hasPermissionAtCommunity(permission, communityId)
    }

    fun reportUser(ekoUser: AmityUser): Completable {
        return ekoUser.report().flag()
    }

    fun unReportUser(ekoUser: AmityUser): Completable {
        return ekoUser.report().unflag()
    }

    fun getUser(userId: String): Flowable<AmityUser> {
        val userRepository = AmityCoreClient.newUserRepository()
        return userRepository.getUser(userId)
    }

    fun removeUser(list: List<String>): Completable {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        return communityRepository.membership(communityId).removeMembers(list)
    }

    fun assignRole(roles: List<String>, userIdList: List<String>): Completable {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        return communityRepository.moderation(communityId)
            .addRoles(roles, userIdList)
    }

    fun removeRole(roles: List<String>, userIdList: List<String>): Completable {
        val communityRepository = AmitySocialClient.newCommunityRepository()
        return communityRepository.moderation(communityId)
            .removeRoles(roles, userIdList)
    }

    fun getCommunityDetail(): Flowable<AmityCommunity> {
        return AmitySocialClient.newCommunityRepository().getCommunity(communityId)
    }
}