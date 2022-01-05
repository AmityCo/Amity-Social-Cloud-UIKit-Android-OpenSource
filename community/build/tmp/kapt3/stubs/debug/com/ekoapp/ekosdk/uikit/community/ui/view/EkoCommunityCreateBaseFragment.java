package com.ekoapp.ekosdk.uikit.community.ui.view;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001cH\u0002J\b\u0010\u001e\u001a\u00020\u001cH\u0002J\b\u0010\u001f\u001a\u00020\u001cH\u0002J\u0006\u0010 \u001a\u00020\u000fJ\u0010\u0010!\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020\u001aH\u0002J&\u0010#\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020&2\b\u0010\'\u001a\u0004\u0018\u00010(2\b\u0010)\u001a\u0004\u0018\u00010*H\u0016J\b\u0010+\u001a\u00020\u001cH\u0016J\u0006\u0010,\u001a\u00020\u001cJ\u001a\u0010-\u001a\u00020\u001c2\u0006\u0010.\u001a\u00020$2\b\u0010)\u001a\u0004\u0018\u00010*H\u0016J\b\u0010\u0016\u001a\u00020\u001cH\u0002J\b\u0010/\u001a\u00020\u001cH\u0002J\b\u00100\u001a\u00020\u001cH\u0002J\b\u00101\u001a\u00020\u001cH\u0002J\u000e\u00102\u001a\u00020\u001c2\u0006\u00103\u001a\u000204J\b\u00105\u001a\u00020\u001cH\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0010\u001a\u00020\u00118FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0012\u0010\u0013R\u001c\u0010\u0016\u001a\u0010\u0012\f\u0012\n \u0005*\u0004\u0018\u00010\u00040\u00040\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0018\u001a\u0010\u0012\f\u0012\n \u0005*\u0004\u0018\u00010\u00040\u00040\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0019\u001a\u0010\u0012\f\u0012\n \u0005*\u0004\u0018\u00010\u001a0\u001a0\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00066"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/view/EkoCommunityCreateBaseFragment;", "Landroidx/fragment/app/Fragment;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "disposable", "Lio/reactivex/disposables/CompositeDisposable;", "getDisposable", "()Lio/reactivex/disposables/CompositeDisposable;", "setDisposable", "(Lio/reactivex/disposables/CompositeDisposable;)V", "imageUri", "Landroid/net/Uri;", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentCreateCommunityBinding;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/ui/viewModel/EkoCreateCommunityViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/ui/viewModel/EkoCreateCommunityViewModel;", "mViewModel$delegate", "Lkotlin/Lazy;", "pickImage", "Landroidx/activity/result/ActivityResultLauncher;", "pickImagePermission", "selectCategoryContract", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectCategoryItem;", "addInitialStateChangeListener", "", "checkUserRole", "createCommunity", "editCommunity", "getBindingVariable", "launchCategorySelection", "preSelectedCategory", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onLeftIconClick", "onViewCreated", "view", "setAvatar", "setUpBackPress", "showDialog", "uploadImage", "isEditCommunity", "", "uploadImageAndCreateCommunity", "community_debug"})
public abstract class EkoCommunityCreateBaseFragment extends androidx.fragment.app.Fragment {
    private final java.lang.String TAG = null;
    @org.jetbrains.annotations.NotNull()
    private io.reactivex.disposables.CompositeDisposable disposable;
    private android.net.Uri imageUri;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy mViewModel$delegate = null;
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCreateCommunityBinding mBinding;
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> pickImage = null;
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> pickImagePermission = null;
    private androidx.activity.result.ActivityResultLauncher<com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem> selectCategoryContract;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.disposables.CompositeDisposable getDisposable() {
        return null;
    }
    
    public final void setDisposable(@org.jetbrains.annotations.NotNull()
    io.reactivex.disposables.CompositeDisposable p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel getMViewModel() {
        return null;
    }
    
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
    
    private final void uploadImageAndCreateCommunity() {
    }
    
    private final void addInitialStateChangeListener() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCreateCommunityBinding getBindingVariable() {
        return null;
    }
    
    private final void pickImage() {
    }
    
    private final void launchCategorySelection(com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem preSelectedCategory) {
    }
    
    private final void setAvatar() {
    }
    
    private final void createCommunity() {
    }
    
    public final void onLeftIconClick() {
    }
    
    private final void setUpBackPress() {
    }
    
    private final void showDialog() {
    }
    
    public final void uploadImage(boolean isEditCommunity) {
    }
    
    private final void editCommunity() {
    }
    
    private final void checkUserRole() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public EkoCommunityCreateBaseFragment() {
        super();
    }
}