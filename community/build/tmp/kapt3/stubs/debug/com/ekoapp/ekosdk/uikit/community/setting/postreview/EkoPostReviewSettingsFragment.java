package com.ekoapp.ekosdk.uikit.community.setting.postreview;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u001eB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0002J\u0018\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0002J\b\u0010\u000f\u001a\u00020\nH\u0002J\u001a\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u0016\u0010\u0015\u001a\u00020\n2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0002J\b\u0010\u0019\u001a\u00020\nH\u0002J\u0015\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u001cH\u0000\u00a2\u0006\u0002\b\u001dR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/postreview/EkoPostReviewSettingsFragment;", "Lcom/trello/rxlifecycle3/components/support/RxFragment;", "()V", "essentialViewModel", "Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingEssentialViewModel;", "settingsListAdapter", "Lcom/ekoapp/ekosdk/uikit/community/setting/EkoSettingsItemAdapter;", "viewModel", "Lcom/ekoapp/ekosdk/uikit/community/setting/postreview/EkoPostReviewSettingsViewModel;", "confirmTurnOffPostReview", "", "errorDialog", "title", "", "description", "getPostReviewItems", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "renderItems", "items", "", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "setupPostReviewListRecyclerView", "toggleApproveMemberPostEvent", "isChecked", "", "toggleApproveMemberPostEvent$community_debug", "Builder", "community_debug"})
public final class EkoPostReviewSettingsFragment extends com.trello.rxlifecycle3.components.support.RxFragment {
    private final com.ekoapp.ekosdk.uikit.community.setting.EkoSettingsItemAdapter settingsListAdapter = null;
    private com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingEssentialViewModel essentialViewModel;
    private com.ekoapp.ekosdk.uikit.community.setting.postreview.EkoPostReviewSettingsViewModel viewModel;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public final void toggleApproveMemberPostEvent$community_debug(boolean isChecked) {
    }
    
    private final void setupPostReviewListRecyclerView() {
    }
    
    private final void getPostReviewItems() {
    }
    
    private final void renderItems(java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem> items) {
    }
    
    private final void confirmTurnOffPostReview() {
    }
    
    private final void errorDialog(int title, int description) {
    }
    
    public EkoPostReviewSettingsFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\u0003\u001a\u00020\u00002\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0006R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/postreview/EkoPostReviewSettingsFragment$Builder;", "", "()V", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "communityId", "", "build", "Lcom/ekoapp/ekosdk/uikit/community/setting/postreview/EkoPostReviewSettingsFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "id", "community_debug"})
    public static final class Builder {
        private java.lang.String communityId;
        private com.ekoapp.ekosdk.community.EkoCommunity community;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.setting.postreview.EkoPostReviewSettingsFragment.Builder communityId(@org.jetbrains.annotations.NotNull()
        java.lang.String id) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.setting.postreview.EkoPostReviewSettingsFragment.Builder community(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.community.EkoCommunity community) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.setting.postreview.EkoPostReviewSettingsFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}