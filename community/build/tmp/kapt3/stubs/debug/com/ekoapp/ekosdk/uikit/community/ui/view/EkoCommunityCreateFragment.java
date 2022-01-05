package com.ekoapp.ekosdk.uikit.community.ui.view;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\u001aB\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\b\u0010\u000e\u001a\u00020\fH\u0016J\b\u0010\u000f\u001a\u00020\fH\u0016J\u0010\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\tH\u0016J\u001a\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\b\u0010\u0017\u001a\u00020\fH\u0002J\b\u0010\u0018\u001a\u00020\fH\u0002J\b\u0010\u0019\u001a\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R(\u0010\u0006\u001a\u001c\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\t \n*\n\u0012\u0004\u0012\u00020\t\u0018\u00010\b0\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/view/EkoCommunityCreateFragment;", "Lcom/ekoapp/ekosdk/uikit/community/ui/view/EkoCommunityCreateBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/ui/clickListener/EkoAddedMemberClickListener;", "()V", "mAdapter", "Lcom/ekoapp/ekosdk/uikit/community/ui/adapter/EkoAddedMembersAdapter;", "selectMembers", "Landroidx/activity/result/ActivityResultLauncher;", "Ljava/util/ArrayList;", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectMemberItem;", "kotlin.jvm.PlatformType", "addMembers", "", "goToAddMembersActivity", "onAddButtonClicked", "onMemberCountClicked", "onMemberRemoved", "item", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "setAddMemberVisibility", "setAddedMemberRecyclerView", "setCount", "Builder", "community_debug"})
public final class EkoCommunityCreateFragment extends com.ekoapp.ekosdk.uikit.community.ui.view.EkoCommunityCreateBaseFragment implements com.ekoapp.ekosdk.uikit.community.ui.clickListener.EkoAddedMemberClickListener {
    private com.ekoapp.ekosdk.uikit.community.ui.adapter.EkoAddedMembersAdapter mAdapter;
    private final androidx.activity.result.ActivityResultLauncher<java.util.ArrayList<com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem>> selectMembers = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void addMembers() {
    }
    
    private final void setAddedMemberRecyclerView() {
    }
    
    @java.lang.Override()
    public void onAddButtonClicked() {
    }
    
    @java.lang.Override()
    public void onMemberCountClicked() {
    }
    
    @java.lang.Override()
    public void onMemberRemoved(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem item) {
    }
    
    private final void setCount() {
    }
    
    private final void goToAddMembersActivity() {
    }
    
    private final void setAddMemberVisibility() {
    }
    
    public EkoCommunityCreateFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/view/EkoCommunityCreateFragment$Builder;", "", "()V", "build", "Lcom/ekoapp/ekosdk/uikit/community/ui/view/EkoCommunityCreateFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "community_debug"})
    public static final class Builder {
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.ui.view.EkoCommunityCreateFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}