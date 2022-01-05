package com.ekoapp.ekosdk.uikit.community.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000b\u001a\u00020\fH\u0002J\"\u0010\r\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00120\u0011J\"\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00120\u0011J\"\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00120\u0011J\u0010\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u000e\u0010\u0018\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u001bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u001c"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/utils/EkoSharePostBottomSheetDialog;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoShareMenuViewModel;", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "(Lcom/ekoapp/ekosdk/feed/EkoPost;)V", "fragment", "Lcom/ekoapp/ekosdk/uikit/common/views/dialog/EkoBottomSheetDialogFragment;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostShareListener;", "getPost", "()Lcom/ekoapp/ekosdk/feed/EkoPost;", "getMenu", "", "observeShareToExternalApp", "lifecycleOwner", "Landroidx/lifecycle/LifecycleOwner;", "callback", "Lkotlin/Function1;", "", "observeShareToGroup", "observeShareToMyTimeline", "renderMenuItem", "menu", "Landroid/view/Menu;", "setNavigationListener", "show", "childFragmentManager", "Landroidx/fragment/app/FragmentManager;", "community_debug"})
public final class EkoSharePostBottomSheetDialog extends com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoShareMenuViewModel {
    private final com.ekoapp.ekosdk.uikit.common.views.dialog.EkoBottomSheetDialogFragment fragment = null;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostShareListener listener;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.feed.EkoPost post = null;
    
    public final void show(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentManager childFragmentManager) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.utils.EkoSharePostBottomSheetDialog setNavigationListener(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostShareListener listener) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.utils.EkoSharePostBottomSheetDialog observeShareToMyTimeline(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LifecycleOwner lifecycleOwner, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.ekoapp.ekosdk.feed.EkoPost, kotlin.Unit> callback) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.utils.EkoSharePostBottomSheetDialog observeShareToGroup(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LifecycleOwner lifecycleOwner, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.ekoapp.ekosdk.feed.EkoPost, kotlin.Unit> callback) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.utils.EkoSharePostBottomSheetDialog observeShareToExternalApp(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LifecycleOwner lifecycleOwner, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.ekoapp.ekosdk.feed.EkoPost, kotlin.Unit> callback) {
        return null;
    }
    
    private final void renderMenuItem(android.view.Menu menu) {
    }
    
    private final int getMenu() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.feed.EkoPost getPost() {
        return null;
    }
    
    public EkoSharePostBottomSheetDialog(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post) {
        super(null);
    }
}