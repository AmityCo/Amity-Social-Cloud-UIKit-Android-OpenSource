package com.ekoapp.ekosdk.uikit.community.newsfeed.activity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0002\n\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\tH\u0016R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/activity/EkoEditCommentActivity;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseToolbarFragmentContainerActivity;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "getContentFragment", "Landroidx/fragment/app/Fragment;", "initToolbar", "", "EkoAddCommentActivityContract", "EkoEditCommentActivityContract", "community_debug"})
public final class EkoEditCommentActivity extends com.ekoapp.ekosdk.uikit.base.EkoBaseToolbarFragmentContainerActivity {
    private final java.lang.String TAG = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void initToolbar() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.fragment.app.Fragment getContentFragment() {
        return null;
    }
    
    public EkoEditCommentActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001a\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u0002H\u0016J\u001c\u0010\t\u001a\u0004\u0018\u00010\u00022\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u0016\u00a8\u0006\r"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/activity/EkoEditCommentActivity$EkoEditCommentActivityContract;", "Landroidx/activity/result/contract/ActivityResultContract;", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "()V", "createIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "input", "parseResult", "resultCode", "", "intent", "community_debug"})
    public static final class EkoEditCommentActivityContract extends androidx.activity.result.contract.ActivityResultContract<com.ekoapp.ekosdk.comment.EkoComment, com.ekoapp.ekosdk.comment.EkoComment> {
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public android.content.Intent createIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.comment.EkoComment input) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        @java.lang.Override()
        public com.ekoapp.ekosdk.comment.EkoComment parseResult(int resultCode, @org.jetbrains.annotations.Nullable()
        android.content.Intent intent) {
            return null;
        }
        
        public EkoEditCommentActivityContract() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0001B\u0005\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0002H\u0016J!\u0010\n\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0016\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/activity/EkoEditCommentActivity$EkoAddCommentActivityContract;", "Landroidx/activity/result/contract/ActivityResultContract;", "Landroid/os/Bundle;", "", "()V", "createIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "input", "parseResult", "resultCode", "", "intent", "(ILandroid/content/Intent;)Ljava/lang/Boolean;", "community_debug"})
    public static final class EkoAddCommentActivityContract extends androidx.activity.result.contract.ActivityResultContract<android.os.Bundle, java.lang.Boolean> {
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public android.content.Intent createIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        android.os.Bundle input) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        @java.lang.Override()
        public java.lang.Boolean parseResult(int resultCode, @org.jetbrains.annotations.Nullable()
        android.content.Intent intent) {
            return null;
        }
        
        public EkoAddCommentActivityContract() {
            super();
        }
    }
}