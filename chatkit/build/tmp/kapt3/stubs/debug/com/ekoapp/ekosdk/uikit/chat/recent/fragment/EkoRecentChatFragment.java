package com.ekoapp.ekosdk.uikit.chat.recent.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\u001dB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\b\u0010\f\u001a\u00020\rH\u0002J\b\u0010\u000e\u001a\u00020\rH\u0002J&\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\b\u0010\u0017\u001a\u00020\rH\u0016J\u0010\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u001a\u0010\u001b\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/recent/fragment/EkoRecentChatFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/ekoapp/ekosdk/uikit/chat/home/callback/IRecentChatItemClickListener;", "()V", "mAdapter", "Lcom/ekoapp/ekosdk/uikit/chat/recent/adapter/EkoRecentChatAdapter;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/chat/databinding/AmityFragmentRecentChatBinding;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/chat/recent/fragment/EkoRecentChatViewModel;", "recentChatDisposable", "Lio/reactivex/disposables/Disposable;", "getRecentChatData", "", "initRecyclerView", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onRecentChatItemClick", "channelId", "", "onViewCreated", "view", "Builder", "chatkit_debug"})
public final class EkoRecentChatFragment extends androidx.fragment.app.Fragment implements com.ekoapp.ekosdk.uikit.chat.home.callback.IRecentChatItemClickListener {
    private com.ekoapp.ekosdk.uikit.chat.recent.fragment.EkoRecentChatViewModel mViewModel;
    private com.ekoapp.ekosdk.uikit.chat.recent.adapter.EkoRecentChatAdapter mAdapter;
    private io.reactivex.disposables.Disposable recentChatDisposable;
    private com.ekoapp.ekosdk.uikit.chat.databinding.AmityFragmentRecentChatBinding mBinding;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initRecyclerView() {
    }
    
    private final void getRecentChatData() {
    }
    
    @java.lang.Override()
    public void onRecentChatItemClick(@org.jetbrains.annotations.NotNull()
    java.lang.String channelId) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private EkoRecentChatFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/recent/fragment/EkoRecentChatFragment$Builder;", "", "()V", "mListener", "Lcom/ekoapp/ekosdk/uikit/chat/home/callback/IRecentChatItemClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/chat/recent/fragment/EkoRecentChatFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "recentChatItemClickListener", "listener", "chatkit_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.uikit.chat.home.callback.IRecentChatItemClickListener mListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.chat.recent.fragment.EkoRecentChatFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.chat.recent.fragment.EkoRecentChatFragment.Builder recentChatItemClickListener(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.chat.home.callback.IRecentChatItemClickListener listener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}