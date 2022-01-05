package com.ekoapp.ekosdk.uikit.community.newsfeed.listener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\rH\u0016J\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0016J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0016J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0016R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\u0006R\u0018\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostShareListener;", "", "shareToExternalAppActionRelay", "Lcom/ekoapp/ekosdk/uikit/utils/SingleLiveData;", "", "getShareToExternalAppActionRelay", "()Lcom/ekoapp/ekosdk/uikit/utils/SingleLiveData;", "shareToGroupActionRelay", "getShareToGroupActionRelay", "shareToMyTimelineActionRelay", "getShareToMyTimelineActionRelay", "navigateShareTo", "type", "Lcom/ekoapp/ekosdk/uikit/community/utils/ShareType;", "observeShareToExternalApp", "observeShareToMyTimelinePage", "observeShareToPage", "community_debug"})
public abstract interface IPostShareListener {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> getShareToMyTimelineActionRelay();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> getShareToGroupActionRelay();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> getShareToExternalAppActionRelay();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToMyTimelinePage();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToPage();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToExternalApp();
    
    public abstract void navigateShareTo(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.utils.ShareType type);
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3)
    public final class DefaultImpls {
        
        @org.jetbrains.annotations.NotNull()
        public static com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToMyTimelinePage(com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostShareListener $this) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public static com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToPage(com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostShareListener $this) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public static com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToExternalApp(com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostShareListener $this) {
            return null;
        }
        
        public static void navigateShareTo(com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostShareListener $this, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.utils.ShareType type) {
        }
    }
}