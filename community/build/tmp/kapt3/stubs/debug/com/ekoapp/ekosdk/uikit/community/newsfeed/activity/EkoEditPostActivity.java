package com.ekoapp.ekosdk.uikit.community.newsfeed.activity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\b\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/activity/EkoEditPostActivity;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseToolbarFragmentContainerActivity;", "()V", "getContentFragment", "Landroidx/fragment/app/Fragment;", "initToolbar", "", "EkoEditPostActivityContract", "community_debug"})
public final class EkoEditPostActivity extends com.ekoapp.ekosdk.uikit.base.EkoBaseToolbarFragmentContainerActivity {
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void initToolbar() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.fragment.app.Fragment getContentFragment() {
        return null;
    }
    
    public EkoEditPostActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0001B\u0005\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0002H\u0016J\u001c\u0010\n\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0016\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/activity/EkoEditPostActivity$EkoEditPostActivityContract;", "Landroidx/activity/result/contract/ActivityResultContract;", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "", "()V", "createIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "input", "parseResult", "resultCode", "", "intent", "community_debug"})
    public static final class EkoEditPostActivityContract extends androidx.activity.result.contract.ActivityResultContract<com.ekoapp.ekosdk.feed.EkoPost, java.lang.String> {
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public android.content.Intent createIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.feed.EkoPost input) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        @java.lang.Override()
        public java.lang.String parseResult(int resultCode, @org.jetbrains.annotations.Nullable()
        android.content.Intent intent) {
            return null;
        }
        
        public EkoEditPostActivityContract() {
            super();
        }
    }
}