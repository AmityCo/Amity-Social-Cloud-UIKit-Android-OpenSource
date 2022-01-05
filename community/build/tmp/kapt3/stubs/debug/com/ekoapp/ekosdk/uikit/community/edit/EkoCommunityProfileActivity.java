package com.ekoapp.ekosdk.uikit.community.edit;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00162\u00020\u00012\u00020\u0002:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u000fH\u0002J\u0012\u0010\u0011\u001a\u00020\u000f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\b\u0010\u0014\u001a\u00020\u000fH\u0016J\b\u0010\u0015\u001a\u00020\u000fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\b\u001a\u00020\t8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0017"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/edit/EkoCommunityProfileActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/ekoapp/ekosdk/uikit/components/EkoToolBarClickListener;", "()V", "communityId", "", "mFragment", "Lcom/ekoapp/ekosdk/uikit/community/edit/EkoCommunityProfileEditFragment;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/ui/viewModel/EkoCreateCommunityViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/ui/viewModel/EkoCreateCommunityViewModel;", "mViewModel$delegate", "Lkotlin/Lazy;", "leftIconClick", "", "loadFragment", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "rightIconClick", "setUpToolbar", "Companion", "community_debug"})
public final class EkoCommunityProfileActivity extends androidx.appcompat.app.AppCompatActivity implements com.ekoapp.ekosdk.uikit.components.EkoToolBarClickListener {
    private com.ekoapp.ekosdk.uikit.community.edit.EkoCommunityProfileEditFragment mFragment;
    private final kotlin.Lazy mViewModel$delegate = null;
    private java.lang.String communityId = "";
    private static final java.lang.String COMMUNITY_ID = "COMMUNITY_ID";
    public static final com.ekoapp.ekosdk.uikit.community.edit.EkoCommunityProfileActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel getMViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpToolbar() {
    }
    
    private final void loadFragment() {
    }
    
    @java.lang.Override()
    public void leftIconClick() {
    }
    
    @java.lang.Override()
    public void rightIconClick() {
    }
    
    public EkoCommunityProfileActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/edit/EkoCommunityProfileActivity$Companion;", "", "()V", "COMMUNITY_ID", "", "newIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "communityId", "community_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.Intent newIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String communityId) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}