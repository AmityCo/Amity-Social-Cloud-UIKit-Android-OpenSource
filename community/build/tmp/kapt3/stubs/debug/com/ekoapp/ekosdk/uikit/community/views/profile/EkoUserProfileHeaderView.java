package com.ekoapp.ekosdk.uikit.community.views.profile;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u0013\u001a\u00020\u0014H\u0002J\u000e\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001aR\u001a\u0010\u000b\u001a\u00020\fX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/views/profile/EkoUserProfileHeaderView;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "btnUserProfileAction", "Lcom/google/android/material/button/MaterialButton;", "getBtnUserProfileAction", "()Lcom/google/android/material/button/MaterialButton;", "setBtnUserProfileAction", "(Lcom/google/android/material/button/MaterialButton;)V", "mBinding", "Lcom/ekoapp/ekosdk/uikit/databinding/AmityViewUserProfileHeaderBinding;", "init", "", "setIsLoggedInUser", "isLoggedInUser", "", "setUserData", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "community_debug"})
public final class EkoUserProfileHeaderView extends androidx.constraintlayout.widget.ConstraintLayout {
    private com.ekoapp.ekosdk.uikit.databinding.AmityViewUserProfileHeaderBinding mBinding;
    @org.jetbrains.annotations.NotNull()
    public com.google.android.material.button.MaterialButton btnUserProfileAction;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.android.material.button.MaterialButton getBtnUserProfileAction() {
        return null;
    }
    
    public final void setBtnUserProfileAction(@org.jetbrains.annotations.NotNull()
    com.google.android.material.button.MaterialButton p0) {
    }
    
    public final void setUserData(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser user) {
    }
    
    public final void setIsLoggedInUser(boolean isLoggedInUser) {
    }
    
    private final void init() {
    }
    
    public EkoUserProfileHeaderView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    public EkoUserProfileHeaderView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    public EkoUserProfileHeaderView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
}