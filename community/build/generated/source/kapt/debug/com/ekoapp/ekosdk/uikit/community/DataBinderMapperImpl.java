package com.ekoapp.ekosdk.uikit.community;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityActivityCategoryListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityActivityCommunityDetailBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityActivityCommunitySettingBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityActivityCreateCommunityBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityActivityCreatePostBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityActivityPostReviewSettingsBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityActivitySelectMembersListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityCommentComposeBarBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentBaseFeedBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCategoryCommunityListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCategoryListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCategoryPreviewBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCommunityHomePageBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCommunityPageBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCreateCommunityBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentEditCommentBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentEditUserProfileBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentExploreBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentMembersBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentModeratorsBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentMyCommunityBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentNewsFeedBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentPostCreateBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentPostDetailBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentPostTargetSelectionBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentSelectMembersListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentUserProfilePageBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemAddedMemberBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCategoryCommunityListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCategoryListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommentFooterNewsFeedBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommentNewsFeedBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommentPostBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommunityBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommunityCategoryBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommunityMembershipBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommunitySelectionListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCreatePostFileBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCreatePostImageBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemDeletedCommentPostBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemDeletedNewsFeedReplyBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemHeaderSelectMemberBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemMultipleCreatePostImageBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemMyCommunityBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemPostFooterBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemPostHeaderBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemRecommCommBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSelectMemberBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSelectedMemberBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSeparateContentBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSettingsHeaderBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSettingsMarginBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSettingsNavContentBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSettingsRadioContentBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSettingsTextContentBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemSettingsToggleContentBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemTrendingCommunityListBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityItemViewPostFileBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityViewAddedMemberWithCountBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityViewGlobalFeedEmptyBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityViewMainSettingsContentBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityViewMyTimelineFeedEmptyBindingImpl;
import com.ekoapp.ekosdk.uikit.community.databinding.AmityViewOtherUserTimelineEmptyBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_AMITYACTIVITYCATEGORYLIST = 1;

  private static final int LAYOUT_AMITYACTIVITYCOMMUNITYDETAIL = 2;

  private static final int LAYOUT_AMITYACTIVITYCOMMUNITYSETTING = 3;

  private static final int LAYOUT_AMITYACTIVITYCREATECOMMUNITY = 4;

  private static final int LAYOUT_AMITYACTIVITYCREATEPOST = 5;

  private static final int LAYOUT_AMITYACTIVITYPOSTREVIEWSETTINGS = 6;

  private static final int LAYOUT_AMITYACTIVITYSELECTMEMBERSLIST = 7;

  private static final int LAYOUT_AMITYCOMMENTCOMPOSEBAR = 8;

  private static final int LAYOUT_AMITYFRAGMENTBASEFEED = 9;

  private static final int LAYOUT_AMITYFRAGMENTCATEGORYCOMMUNITYLIST = 10;

  private static final int LAYOUT_AMITYFRAGMENTCATEGORYLIST = 11;

  private static final int LAYOUT_AMITYFRAGMENTCATEGORYPREVIEW = 12;

  private static final int LAYOUT_AMITYFRAGMENTCOMMUNITYHOMEPAGE = 13;

  private static final int LAYOUT_AMITYFRAGMENTCOMMUNITYPAGE = 14;

  private static final int LAYOUT_AMITYFRAGMENTCREATECOMMUNITY = 15;

  private static final int LAYOUT_AMITYFRAGMENTEDITCOMMENT = 16;

  private static final int LAYOUT_AMITYFRAGMENTEDITUSERPROFILE = 17;

  private static final int LAYOUT_AMITYFRAGMENTEXPLORE = 18;

  private static final int LAYOUT_AMITYFRAGMENTMEMBERS = 19;

  private static final int LAYOUT_AMITYFRAGMENTMODERATORS = 20;

  private static final int LAYOUT_AMITYFRAGMENTMYCOMMUNITY = 21;

  private static final int LAYOUT_AMITYFRAGMENTNEWSFEED = 22;

  private static final int LAYOUT_AMITYFRAGMENTPOSTCREATE = 23;

  private static final int LAYOUT_AMITYFRAGMENTPOSTDETAIL = 24;

  private static final int LAYOUT_AMITYFRAGMENTPOSTTARGETSELECTION = 25;

  private static final int LAYOUT_AMITYFRAGMENTSELECTMEMBERSLIST = 26;

  private static final int LAYOUT_AMITYFRAGMENTUSERPROFILEPAGE = 27;

  private static final int LAYOUT_AMITYITEMADDEDMEMBER = 28;

  private static final int LAYOUT_AMITYITEMCATEGORYCOMMUNITYLIST = 29;

  private static final int LAYOUT_AMITYITEMCATEGORYLIST = 30;

  private static final int LAYOUT_AMITYITEMCOMMENTFOOTERNEWSFEED = 31;

  private static final int LAYOUT_AMITYITEMCOMMENTNEWSFEED = 32;

  private static final int LAYOUT_AMITYITEMCOMMENTPOST = 33;

  private static final int LAYOUT_AMITYITEMCOMMUNITY = 34;

  private static final int LAYOUT_AMITYITEMCOMMUNITYCATEGORY = 35;

  private static final int LAYOUT_AMITYITEMCOMMUNITYMEMBERSHIP = 36;

  private static final int LAYOUT_AMITYITEMCOMMUNITYSELECTIONLIST = 37;

  private static final int LAYOUT_AMITYITEMCREATEPOSTFILE = 38;

  private static final int LAYOUT_AMITYITEMCREATEPOSTIMAGE = 39;

  private static final int LAYOUT_AMITYITEMDELETEDCOMMENTPOST = 40;

  private static final int LAYOUT_AMITYITEMDELETEDNEWSFEEDREPLY = 41;

  private static final int LAYOUT_AMITYITEMHEADERSELECTMEMBER = 42;

  private static final int LAYOUT_AMITYITEMMULTIPLECREATEPOSTIMAGE = 43;

  private static final int LAYOUT_AMITYITEMMYCOMMUNITY = 44;

  private static final int LAYOUT_AMITYITEMPOSTFOOTER = 45;

  private static final int LAYOUT_AMITYITEMPOSTHEADER = 46;

  private static final int LAYOUT_AMITYITEMRECOMMCOMM = 47;

  private static final int LAYOUT_AMITYITEMSELECTMEMBER = 48;

  private static final int LAYOUT_AMITYITEMSELECTEDMEMBER = 49;

  private static final int LAYOUT_AMITYITEMSEPARATECONTENT = 50;

  private static final int LAYOUT_AMITYITEMSETTINGSHEADER = 51;

  private static final int LAYOUT_AMITYITEMSETTINGSMARGIN = 52;

  private static final int LAYOUT_AMITYITEMSETTINGSNAVCONTENT = 53;

  private static final int LAYOUT_AMITYITEMSETTINGSRADIOCONTENT = 54;

  private static final int LAYOUT_AMITYITEMSETTINGSTEXTCONTENT = 55;

  private static final int LAYOUT_AMITYITEMSETTINGSTOGGLECONTENT = 56;

  private static final int LAYOUT_AMITYITEMTRENDINGCOMMUNITYLIST = 57;

  private static final int LAYOUT_AMITYITEMVIEWPOSTFILE = 58;

  private static final int LAYOUT_AMITYVIEWADDEDMEMBERWITHCOUNT = 59;

  private static final int LAYOUT_AMITYVIEWGLOBALFEEDEMPTY = 60;

  private static final int LAYOUT_AMITYVIEWMAINSETTINGSCONTENT = 61;

  private static final int LAYOUT_AMITYVIEWMYTIMELINEFEEDEMPTY = 62;

  private static final int LAYOUT_AMITYVIEWOTHERUSERTIMELINEEMPTY = 63;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(63);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_category_list, LAYOUT_AMITYACTIVITYCATEGORYLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_community_detail, LAYOUT_AMITYACTIVITYCOMMUNITYDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_community_setting, LAYOUT_AMITYACTIVITYCOMMUNITYSETTING);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_create_community, LAYOUT_AMITYACTIVITYCREATECOMMUNITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_create_post, LAYOUT_AMITYACTIVITYCREATEPOST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_post_review_settings, LAYOUT_AMITYACTIVITYPOSTREVIEWSETTINGS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_select_members_list, LAYOUT_AMITYACTIVITYSELECTMEMBERSLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_comment_compose_bar, LAYOUT_AMITYCOMMENTCOMPOSEBAR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_base_feed, LAYOUT_AMITYFRAGMENTBASEFEED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_category_community_list, LAYOUT_AMITYFRAGMENTCATEGORYCOMMUNITYLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_category_list, LAYOUT_AMITYFRAGMENTCATEGORYLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_category_preview, LAYOUT_AMITYFRAGMENTCATEGORYPREVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_community_home_page, LAYOUT_AMITYFRAGMENTCOMMUNITYHOMEPAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_community_page, LAYOUT_AMITYFRAGMENTCOMMUNITYPAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_create_community, LAYOUT_AMITYFRAGMENTCREATECOMMUNITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_edit_comment, LAYOUT_AMITYFRAGMENTEDITCOMMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_edit_user_profile, LAYOUT_AMITYFRAGMENTEDITUSERPROFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_explore, LAYOUT_AMITYFRAGMENTEXPLORE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_members, LAYOUT_AMITYFRAGMENTMEMBERS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_moderators, LAYOUT_AMITYFRAGMENTMODERATORS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_my_community, LAYOUT_AMITYFRAGMENTMYCOMMUNITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_news_feed, LAYOUT_AMITYFRAGMENTNEWSFEED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_post_create, LAYOUT_AMITYFRAGMENTPOSTCREATE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_post_detail, LAYOUT_AMITYFRAGMENTPOSTDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_post_target_selection, LAYOUT_AMITYFRAGMENTPOSTTARGETSELECTION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_select_members_list, LAYOUT_AMITYFRAGMENTSELECTMEMBERSLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_user_profile_page, LAYOUT_AMITYFRAGMENTUSERPROFILEPAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_added_member, LAYOUT_AMITYITEMADDEDMEMBER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_category_community_list, LAYOUT_AMITYITEMCATEGORYCOMMUNITYLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_category_list, LAYOUT_AMITYITEMCATEGORYLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_comment_footer_news_feed, LAYOUT_AMITYITEMCOMMENTFOOTERNEWSFEED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_comment_news_feed, LAYOUT_AMITYITEMCOMMENTNEWSFEED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_comment_post, LAYOUT_AMITYITEMCOMMENTPOST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_community, LAYOUT_AMITYITEMCOMMUNITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_community_category, LAYOUT_AMITYITEMCOMMUNITYCATEGORY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_community_membership, LAYOUT_AMITYITEMCOMMUNITYMEMBERSHIP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_community_selection_list, LAYOUT_AMITYITEMCOMMUNITYSELECTIONLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_create_post_file, LAYOUT_AMITYITEMCREATEPOSTFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_create_post_image, LAYOUT_AMITYITEMCREATEPOSTIMAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_deleted_comment_post, LAYOUT_AMITYITEMDELETEDCOMMENTPOST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_deleted_news_feed_reply, LAYOUT_AMITYITEMDELETEDNEWSFEEDREPLY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_header_select_member, LAYOUT_AMITYITEMHEADERSELECTMEMBER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_multiple_create_post_image, LAYOUT_AMITYITEMMULTIPLECREATEPOSTIMAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_my_community, LAYOUT_AMITYITEMMYCOMMUNITY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_post_footer, LAYOUT_AMITYITEMPOSTFOOTER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_post_header, LAYOUT_AMITYITEMPOSTHEADER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_recomm_comm, LAYOUT_AMITYITEMRECOMMCOMM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_select_member, LAYOUT_AMITYITEMSELECTMEMBER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_selected_member, LAYOUT_AMITYITEMSELECTEDMEMBER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_separate_content, LAYOUT_AMITYITEMSEPARATECONTENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_header, LAYOUT_AMITYITEMSETTINGSHEADER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_margin, LAYOUT_AMITYITEMSETTINGSMARGIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_nav_content, LAYOUT_AMITYITEMSETTINGSNAVCONTENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_radio_content, LAYOUT_AMITYITEMSETTINGSRADIOCONTENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_text_content, LAYOUT_AMITYITEMSETTINGSTEXTCONTENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_toggle_content, LAYOUT_AMITYITEMSETTINGSTOGGLECONTENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_trending_community_list, LAYOUT_AMITYITEMTRENDINGCOMMUNITYLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_view_post_file, LAYOUT_AMITYITEMVIEWPOSTFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_added_member_with_count, LAYOUT_AMITYVIEWADDEDMEMBERWITHCOUNT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_global_feed_empty, LAYOUT_AMITYVIEWGLOBALFEEDEMPTY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_main_settings_content, LAYOUT_AMITYVIEWMAINSETTINGSCONTENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_my_timeline_feed_empty, LAYOUT_AMITYVIEWMYTIMELINEFEEDEMPTY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_other_user_timeline_empty, LAYOUT_AMITYVIEWOTHERUSERTIMELINEEMPTY);
  }

  private final ViewDataBinding internalGetViewDataBinding0(DataBindingComponent component,
      View view, int internalId, Object tag) {
    switch(internalId) {
      case  LAYOUT_AMITYACTIVITYCATEGORYLIST: {
        if ("layout/amity_activity_category_list_0".equals(tag)) {
          return new AmityActivityCategoryListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_activity_category_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYACTIVITYCOMMUNITYDETAIL: {
        if ("layout/amity_activity_community_detail_0".equals(tag)) {
          return new AmityActivityCommunityDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_activity_community_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYACTIVITYCOMMUNITYSETTING: {
        if ("layout/amity_activity_community_setting_0".equals(tag)) {
          return new AmityActivityCommunitySettingBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_activity_community_setting is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYACTIVITYCREATECOMMUNITY: {
        if ("layout/amity_activity_create_community_0".equals(tag)) {
          return new AmityActivityCreateCommunityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_activity_create_community is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYACTIVITYCREATEPOST: {
        if ("layout/amity_activity_create_post_0".equals(tag)) {
          return new AmityActivityCreatePostBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_activity_create_post is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYACTIVITYPOSTREVIEWSETTINGS: {
        if ("layout/amity_activity_post_review_settings_0".equals(tag)) {
          return new AmityActivityPostReviewSettingsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_activity_post_review_settings is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYACTIVITYSELECTMEMBERSLIST: {
        if ("layout/amity_activity_select_members_list_0".equals(tag)) {
          return new AmityActivitySelectMembersListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_activity_select_members_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYCOMMENTCOMPOSEBAR: {
        if ("layout/amity_comment_compose_bar_0".equals(tag)) {
          return new AmityCommentComposeBarBindingImpl(component, new View[]{view});
        }
        throw new IllegalArgumentException("The tag for amity_comment_compose_bar is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTBASEFEED: {
        if ("layout/amity_fragment_base_feed_0".equals(tag)) {
          return new AmityFragmentBaseFeedBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_base_feed is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTCATEGORYCOMMUNITYLIST: {
        if ("layout/amity_fragment_category_community_list_0".equals(tag)) {
          return new AmityFragmentCategoryCommunityListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_category_community_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTCATEGORYLIST: {
        if ("layout/amity_fragment_category_list_0".equals(tag)) {
          return new AmityFragmentCategoryListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_category_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTCATEGORYPREVIEW: {
        if ("layout/amity_fragment_category_preview_0".equals(tag)) {
          return new AmityFragmentCategoryPreviewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_category_preview is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTCOMMUNITYHOMEPAGE: {
        if ("layout/amity_fragment_community_home_page_0".equals(tag)) {
          return new AmityFragmentCommunityHomePageBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_community_home_page is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTCOMMUNITYPAGE: {
        if ("layout/amity_fragment_community_page_0".equals(tag)) {
          return new AmityFragmentCommunityPageBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_community_page is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTCREATECOMMUNITY: {
        if ("layout/amity_fragment_create_community_0".equals(tag)) {
          return new AmityFragmentCreateCommunityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_create_community is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTEDITCOMMENT: {
        if ("layout/amity_fragment_edit_comment_0".equals(tag)) {
          return new AmityFragmentEditCommentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_edit_comment is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTEDITUSERPROFILE: {
        if ("layout/amity_fragment_edit_user_profile_0".equals(tag)) {
          return new AmityFragmentEditUserProfileBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_edit_user_profile is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTEXPLORE: {
        if ("layout/amity_fragment_explore_0".equals(tag)) {
          return new AmityFragmentExploreBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_explore is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTMEMBERS: {
        if ("layout/amity_fragment_members_0".equals(tag)) {
          return new AmityFragmentMembersBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_members is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTMODERATORS: {
        if ("layout/amity_fragment_moderators_0".equals(tag)) {
          return new AmityFragmentModeratorsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_moderators is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTMYCOMMUNITY: {
        if ("layout/amity_fragment_my_community_0".equals(tag)) {
          return new AmityFragmentMyCommunityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_my_community is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTNEWSFEED: {
        if ("layout/amity_fragment_news_feed_0".equals(tag)) {
          return new AmityFragmentNewsFeedBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_news_feed is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTPOSTCREATE: {
        if ("layout/amity_fragment_post_create_0".equals(tag)) {
          return new AmityFragmentPostCreateBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_post_create is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTPOSTDETAIL: {
        if ("layout/amity_fragment_post_detail_0".equals(tag)) {
          return new AmityFragmentPostDetailBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_post_detail is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTPOSTTARGETSELECTION: {
        if ("layout/amity_fragment_post_target_selection_0".equals(tag)) {
          return new AmityFragmentPostTargetSelectionBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_post_target_selection is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTSELECTMEMBERSLIST: {
        if ("layout/amity_fragment_select_members_list_0".equals(tag)) {
          return new AmityFragmentSelectMembersListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_select_members_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYFRAGMENTUSERPROFILEPAGE: {
        if ("layout/amity_fragment_user_profile_page_0".equals(tag)) {
          return new AmityFragmentUserProfilePageBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_fragment_user_profile_page is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMADDEDMEMBER: {
        if ("layout/amity_item_added_member_0".equals(tag)) {
          return new AmityItemAddedMemberBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_added_member is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCATEGORYCOMMUNITYLIST: {
        if ("layout/amity_item_category_community_list_0".equals(tag)) {
          return new AmityItemCategoryCommunityListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_category_community_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCATEGORYLIST: {
        if ("layout/amity_item_category_list_0".equals(tag)) {
          return new AmityItemCategoryListBindingImpl(component, new View[]{view});
        }
        throw new IllegalArgumentException("The tag for amity_item_category_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCOMMENTFOOTERNEWSFEED: {
        if ("layout/amity_item_comment_footer_news_feed_0".equals(tag)) {
          return new AmityItemCommentFooterNewsFeedBindingImpl(component, new View[]{view});
        }
        throw new IllegalArgumentException("The tag for amity_item_comment_footer_news_feed is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCOMMENTNEWSFEED: {
        if ("layout/amity_item_comment_news_feed_0".equals(tag)) {
          return new AmityItemCommentNewsFeedBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_comment_news_feed is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCOMMENTPOST: {
        if ("layout/amity_item_comment_post_0".equals(tag)) {
          return new AmityItemCommentPostBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_comment_post is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCOMMUNITY: {
        if ("layout/amity_item_community_0".equals(tag)) {
          return new AmityItemCommunityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_community is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCOMMUNITYCATEGORY: {
        if ("layout/amity_item_community_category_0".equals(tag)) {
          return new AmityItemCommunityCategoryBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_community_category is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCOMMUNITYMEMBERSHIP: {
        if ("layout/amity_item_community_membership_0".equals(tag)) {
          return new AmityItemCommunityMembershipBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_community_membership is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCOMMUNITYSELECTIONLIST: {
        if ("layout/amity_item_community_selection_list_0".equals(tag)) {
          return new AmityItemCommunitySelectionListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_community_selection_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCREATEPOSTFILE: {
        if ("layout/amity_item_create_post_file_0".equals(tag)) {
          return new AmityItemCreatePostFileBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_create_post_file is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMCREATEPOSTIMAGE: {
        if ("layout/amity_item_create_post_image_0".equals(tag)) {
          return new AmityItemCreatePostImageBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_create_post_image is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMDELETEDCOMMENTPOST: {
        if ("layout/amity_item_deleted_comment_post_0".equals(tag)) {
          return new AmityItemDeletedCommentPostBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_deleted_comment_post is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMDELETEDNEWSFEEDREPLY: {
        if ("layout/amity_item_deleted_news_feed_reply_0".equals(tag)) {
          return new AmityItemDeletedNewsFeedReplyBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_deleted_news_feed_reply is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMHEADERSELECTMEMBER: {
        if ("layout/amity_item_header_select_member_0".equals(tag)) {
          return new AmityItemHeaderSelectMemberBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_header_select_member is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMMULTIPLECREATEPOSTIMAGE: {
        if ("layout/amity_item_multiple_create_post_image_0".equals(tag)) {
          return new AmityItemMultipleCreatePostImageBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_multiple_create_post_image is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMMYCOMMUNITY: {
        if ("layout/amity_item_my_community_0".equals(tag)) {
          return new AmityItemMyCommunityBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_my_community is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMPOSTFOOTER: {
        if ("layout/amity_item_post_footer_0".equals(tag)) {
          return new AmityItemPostFooterBindingImpl(component, new View[]{view});
        }
        throw new IllegalArgumentException("The tag for amity_item_post_footer is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMPOSTHEADER: {
        if ("layout/amity_item_post_header_0".equals(tag)) {
          return new AmityItemPostHeaderBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_post_header is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMRECOMMCOMM: {
        if ("layout/amity_item_recomm_comm_0".equals(tag)) {
          return new AmityItemRecommCommBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_recomm_comm is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMSELECTMEMBER: {
        if ("layout/amity_item_select_member_0".equals(tag)) {
          return new AmityItemSelectMemberBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_select_member is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMSELECTEDMEMBER: {
        if ("layout/amity_item_selected_member_0".equals(tag)) {
          return new AmityItemSelectedMemberBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_selected_member is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMSEPARATECONTENT: {
        if ("layout/amity_item_separate_content_0".equals(tag)) {
          return new AmityItemSeparateContentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_separate_content is invalid. Received: " + tag);
      }
    }
    return null;
  }

  private final ViewDataBinding internalGetViewDataBinding1(DataBindingComponent component,
      View view, int internalId, Object tag) {
    switch(internalId) {
      case  LAYOUT_AMITYITEMSETTINGSHEADER: {
        if ("layout/amity_item_settings_header_0".equals(tag)) {
          return new AmityItemSettingsHeaderBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_settings_header is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMSETTINGSMARGIN: {
        if ("layout/amity_item_settings_margin_0".equals(tag)) {
          return new AmityItemSettingsMarginBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_settings_margin is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMSETTINGSNAVCONTENT: {
        if ("layout/amity_item_settings_nav_content_0".equals(tag)) {
          return new AmityItemSettingsNavContentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_settings_nav_content is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMSETTINGSRADIOCONTENT: {
        if ("layout/amity_item_settings_radio_content_0".equals(tag)) {
          return new AmityItemSettingsRadioContentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_settings_radio_content is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMSETTINGSTEXTCONTENT: {
        if ("layout/amity_item_settings_text_content_0".equals(tag)) {
          return new AmityItemSettingsTextContentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_settings_text_content is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMSETTINGSTOGGLECONTENT: {
        if ("layout/amity_item_settings_toggle_content_0".equals(tag)) {
          return new AmityItemSettingsToggleContentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_settings_toggle_content is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMTRENDINGCOMMUNITYLIST: {
        if ("layout/amity_item_trending_community_list_0".equals(tag)) {
          return new AmityItemTrendingCommunityListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_trending_community_list is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYITEMVIEWPOSTFILE: {
        if ("layout/amity_item_view_post_file_0".equals(tag)) {
          return new AmityItemViewPostFileBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_item_view_post_file is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYVIEWADDEDMEMBERWITHCOUNT: {
        if ("layout/amity_view_added_member_with_count_0".equals(tag)) {
          return new AmityViewAddedMemberWithCountBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_view_added_member_with_count is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYVIEWGLOBALFEEDEMPTY: {
        if ("layout/amity_view_global_feed_empty_0".equals(tag)) {
          return new AmityViewGlobalFeedEmptyBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_view_global_feed_empty is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYVIEWMAINSETTINGSCONTENT: {
        if ("layout/amity_view_main_settings_content_0".equals(tag)) {
          return new AmityViewMainSettingsContentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_view_main_settings_content is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYVIEWMYTIMELINEFEEDEMPTY: {
        if ("layout/amity_view_my_timeline_feed_empty_0".equals(tag)) {
          return new AmityViewMyTimelineFeedEmptyBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_view_my_timeline_feed_empty is invalid. Received: " + tag);
      }
      case  LAYOUT_AMITYVIEWOTHERUSERTIMELINEEMPTY: {
        if ("layout/amity_view_other_user_timeline_empty_0".equals(tag)) {
          return new AmityViewOtherUserTimelineEmptyBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for amity_view_other_user_timeline_empty is invalid. Received: " + tag);
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      // find which method will have it. -1 is necessary becausefirst id starts with 1;
      int methodIndex = (localizedLayoutId - 1) / 50;
      switch(methodIndex) {
        case 0: {
          return internalGetViewDataBinding0(component, view, localizedLayoutId, tag);
        }
        case 1: {
          return internalGetViewDataBinding1(component, view, localizedLayoutId, tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case LAYOUT_AMITYCOMMENTCOMPOSEBAR: {
          if("layout/amity_comment_compose_bar_0".equals(tag)) {
            return new AmityCommentComposeBarBindingImpl(component, views);
          }
          throw new IllegalArgumentException("The tag for amity_comment_compose_bar is invalid. Received: " + tag);
        }
        case LAYOUT_AMITYITEMCATEGORYLIST: {
          if("layout/amity_item_category_list_0".equals(tag)) {
            return new AmityItemCategoryListBindingImpl(component, views);
          }
          throw new IllegalArgumentException("The tag for amity_item_category_list is invalid. Received: " + tag);
        }
        case LAYOUT_AMITYITEMCOMMENTFOOTERNEWSFEED: {
          if("layout/amity_item_comment_footer_news_feed_0".equals(tag)) {
            return new AmityItemCommentFooterNewsFeedBindingImpl(component, views);
          }
          throw new IllegalArgumentException("The tag for amity_item_comment_footer_news_feed is invalid. Received: " + tag);
        }
        case LAYOUT_AMITYITEMPOSTFOOTER: {
          if("layout/amity_item_post_footer_0".equals(tag)) {
            return new AmityItemPostFooterBindingImpl(component, views);
          }
          throw new IllegalArgumentException("The tag for amity_item_post_footer is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(2);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new com.ekoapp.ekosdk.uikit.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(44);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "addBottomSpace");
      sKeys.put(2, "alertColor");
      sKeys.put(3, "avatarUrl");
      sKeys.put(4, "clickListener");
      sKeys.put(5, "community");
      sKeys.put(6, "communityCategory");
      sKeys.put(7, "communityMemberShip");
      sKeys.put(8, "delete");
      sKeys.put(9, "description");
      sKeys.put(10, "disable");
      sKeys.put(11, "edited");
      sKeys.put(12, "ekoCommunity");
      sKeys.put(13, "isCheck");
      sKeys.put(14, "isCommunity");
      sKeys.put(15, "isJoined");
      sKeys.put(16, "isLoggedInUser");
      sKeys.put(17, "isModerator");
      sKeys.put(18, "isMyUser");
      sKeys.put(19, "isReplyComment");
      sKeys.put(20, "isSender");
      sKeys.put(21, "leftDrawable");
      sKeys.put(22, "leftString");
      sKeys.put(23, "listener");
      sKeys.put(24, "menuItem");
      sKeys.put(25, "name");
      sKeys.put(26, "postCount");
      sKeys.put(27, "readOnly");
      sKeys.put(28, "replyingToUser");
      sKeys.put(29, "rightDrawable");
      sKeys.put(30, "rightString");
      sKeys.put(31, "rightStringActive");
      sKeys.put(32, "showFeedAction");
      sKeys.put(33, "showLoadingComment");
      sKeys.put(34, "showProgressBar");
      sKeys.put(35, "showReplying");
      sKeys.put(36, "showReplyingTo");
      sKeys.put(37, "showShareButton");
      sKeys.put(38, "showViewAllComment");
      sKeys.put(39, "showViewMoreRepliesButton");
      sKeys.put(40, "showViewRepliesButton");
      sKeys.put(41, "viewModel");
      sKeys.put(42, "visibilityDescription");
      sKeys.put(43, "vm");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(63);

    static {
      sKeys.put("layout/amity_activity_category_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_category_list);
      sKeys.put("layout/amity_activity_community_detail_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_community_detail);
      sKeys.put("layout/amity_activity_community_setting_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_community_setting);
      sKeys.put("layout/amity_activity_create_community_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_create_community);
      sKeys.put("layout/amity_activity_create_post_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_create_post);
      sKeys.put("layout/amity_activity_post_review_settings_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_post_review_settings);
      sKeys.put("layout/amity_activity_select_members_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_activity_select_members_list);
      sKeys.put("layout/amity_comment_compose_bar_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_comment_compose_bar);
      sKeys.put("layout/amity_fragment_base_feed_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_base_feed);
      sKeys.put("layout/amity_fragment_category_community_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_category_community_list);
      sKeys.put("layout/amity_fragment_category_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_category_list);
      sKeys.put("layout/amity_fragment_category_preview_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_category_preview);
      sKeys.put("layout/amity_fragment_community_home_page_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_community_home_page);
      sKeys.put("layout/amity_fragment_community_page_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_community_page);
      sKeys.put("layout/amity_fragment_create_community_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_create_community);
      sKeys.put("layout/amity_fragment_edit_comment_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_edit_comment);
      sKeys.put("layout/amity_fragment_edit_user_profile_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_edit_user_profile);
      sKeys.put("layout/amity_fragment_explore_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_explore);
      sKeys.put("layout/amity_fragment_members_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_members);
      sKeys.put("layout/amity_fragment_moderators_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_moderators);
      sKeys.put("layout/amity_fragment_my_community_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_my_community);
      sKeys.put("layout/amity_fragment_news_feed_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_news_feed);
      sKeys.put("layout/amity_fragment_post_create_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_post_create);
      sKeys.put("layout/amity_fragment_post_detail_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_post_detail);
      sKeys.put("layout/amity_fragment_post_target_selection_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_post_target_selection);
      sKeys.put("layout/amity_fragment_select_members_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_select_members_list);
      sKeys.put("layout/amity_fragment_user_profile_page_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_fragment_user_profile_page);
      sKeys.put("layout/amity_item_added_member_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_added_member);
      sKeys.put("layout/amity_item_category_community_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_category_community_list);
      sKeys.put("layout/amity_item_category_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_category_list);
      sKeys.put("layout/amity_item_comment_footer_news_feed_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_comment_footer_news_feed);
      sKeys.put("layout/amity_item_comment_news_feed_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_comment_news_feed);
      sKeys.put("layout/amity_item_comment_post_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_comment_post);
      sKeys.put("layout/amity_item_community_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_community);
      sKeys.put("layout/amity_item_community_category_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_community_category);
      sKeys.put("layout/amity_item_community_membership_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_community_membership);
      sKeys.put("layout/amity_item_community_selection_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_community_selection_list);
      sKeys.put("layout/amity_item_create_post_file_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_create_post_file);
      sKeys.put("layout/amity_item_create_post_image_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_create_post_image);
      sKeys.put("layout/amity_item_deleted_comment_post_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_deleted_comment_post);
      sKeys.put("layout/amity_item_deleted_news_feed_reply_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_deleted_news_feed_reply);
      sKeys.put("layout/amity_item_header_select_member_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_header_select_member);
      sKeys.put("layout/amity_item_multiple_create_post_image_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_multiple_create_post_image);
      sKeys.put("layout/amity_item_my_community_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_my_community);
      sKeys.put("layout/amity_item_post_footer_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_post_footer);
      sKeys.put("layout/amity_item_post_header_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_post_header);
      sKeys.put("layout/amity_item_recomm_comm_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_recomm_comm);
      sKeys.put("layout/amity_item_select_member_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_select_member);
      sKeys.put("layout/amity_item_selected_member_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_selected_member);
      sKeys.put("layout/amity_item_separate_content_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_separate_content);
      sKeys.put("layout/amity_item_settings_header_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_header);
      sKeys.put("layout/amity_item_settings_margin_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_margin);
      sKeys.put("layout/amity_item_settings_nav_content_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_nav_content);
      sKeys.put("layout/amity_item_settings_radio_content_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_radio_content);
      sKeys.put("layout/amity_item_settings_text_content_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_text_content);
      sKeys.put("layout/amity_item_settings_toggle_content_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_settings_toggle_content);
      sKeys.put("layout/amity_item_trending_community_list_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_trending_community_list);
      sKeys.put("layout/amity_item_view_post_file_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_view_post_file);
      sKeys.put("layout/amity_view_added_member_with_count_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_added_member_with_count);
      sKeys.put("layout/amity_view_global_feed_empty_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_global_feed_empty);
      sKeys.put("layout/amity_view_main_settings_content_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_main_settings_content);
      sKeys.put("layout/amity_view_my_timeline_feed_empty_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_my_timeline_feed_empty);
      sKeys.put("layout/amity_view_other_user_timeline_empty_0", com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_other_user_timeline_empty);
    }
  }
}
