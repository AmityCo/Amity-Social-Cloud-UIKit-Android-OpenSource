package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentCommunityPageBindingImpl extends AmityFragmentCommunityPageBinding implements com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.layout_parent, 12);
        sViewsWithIds.put(R.id.appBar, 13);
        sViewsWithIds.put(R.id.collapsingToolbar, 14);
        sViewsWithIds.put(R.id.communityNameContainer, 15);
        sViewsWithIds.put(R.id.tvPost, 16);
        sViewsWithIds.put(R.id.tvMembers, 17);
        sViewsWithIds.put(R.id.view_container_action_buttons, 18);
        sViewsWithIds.put(R.id.ccDetailTab, 19);
    }
    // views
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback2;
    @Nullable
    private final android.view.View.OnClickListener mCallback4;
    @Nullable
    private final android.view.View.OnClickListener mCallback3;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityFragmentCommunityPageBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 20, sIncludes, sViewsWithIds));
    }
    private AmityFragmentCommunityPageBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 11
            , (com.google.android.material.appbar.AppBarLayout) bindings[13]
            , (com.google.android.material.button.MaterialButton) bindings[10]
            , (com.google.android.material.button.MaterialButton) bindings[7]
            , (com.google.android.material.button.MaterialButton) bindings[8]
            , (com.google.android.material.button.MaterialButton) bindings[9]
            , (com.ekoapp.ekosdk.uikit.components.EkoTabLayout) bindings[19]
            , (com.google.android.material.appbar.CollapsingToolbarLayout) bindings[14]
            , (android.widget.FrameLayout) bindings[15]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[11]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            , (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[12]
            , (androidx.swiperefreshlayout.widget.SwipeRefreshLayout) bindings[0]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[4]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[18]
            );
        this.btnJoin.setTag(null);
        this.buttonEditProfile.setTag(null);
        this.buttonMessage.setTag(null);
        this.buttonSecondaryMessage.setTag(null);
        this.fabCreatePost.setTag(null);
        this.ivAvatar.setTag(null);
        this.refreshLayout.setTag(null);
        this.tvCategory.setTag(null);
        this.tvDescription.setTag(null);
        this.tvMembersCount.setTag(null);
        this.tvName.setTag(null);
        this.tvPostCount.setTag(null);
        setRootTag(root);
        // listeners
        mCallback2 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 1);
        mCallback4 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 3);
        mCallback3 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 2);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1000L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.viewModel == variableId) {
            setViewModel((com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityDetailViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityDetailViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x800L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewModelIsModerator((androidx.databinding.ObservableBoolean) object, fieldId);
            case 1 :
                return onChangeViewModelCategory((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeViewModelIsOfficial((androidx.databinding.ObservableBoolean) object, fieldId);
            case 3 :
                return onChangeViewModelPosts((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 4 :
                return onChangeViewModelIsPublic((androidx.databinding.ObservableBoolean) object, fieldId);
            case 5 :
                return onChangeViewModelDescription((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 6 :
                return onChangeViewModelIsMember((androidx.databinding.ObservableBoolean) object, fieldId);
            case 7 :
                return onChangeViewModelAvatarUrl((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 8 :
                return onChangeViewModelIsModerator1((androidx.databinding.ObservableBoolean) object, fieldId);
            case 9 :
                return onChangeViewModelName((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 10 :
                return onChangeViewModelMembers((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelIsModerator(androidx.databinding.ObservableBoolean ViewModelIsModerator, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelCategory(androidx.databinding.ObservableField<java.lang.String> ViewModelCategory, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsOfficial(androidx.databinding.ObservableBoolean ViewModelIsOfficial, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelPosts(androidx.databinding.ObservableField<java.lang.String> ViewModelPosts, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsPublic(androidx.databinding.ObservableBoolean ViewModelIsPublic, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelDescription(androidx.databinding.ObservableField<java.lang.String> ViewModelDescription, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsMember(androidx.databinding.ObservableBoolean ViewModelIsMember, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelAvatarUrl(androidx.databinding.ObservableField<java.lang.String> ViewModelAvatarUrl, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsModerator1(androidx.databinding.ObservableBoolean ViewModelIsModerator, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelName(androidx.databinding.ObservableField<java.lang.String> ViewModelName, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelMembers(androidx.databinding.ObservableField<java.lang.String> ViewModelMembers, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x400L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        boolean viewModelIsModeratorViewModelIsMessageVisibleBooleanFalse = false;
        androidx.databinding.ObservableBoolean viewModelIsModerator = null;
        boolean viewModelIsMessageVisibleViewModelIsModeratorBooleanFalse = false;
        boolean viewModelIsModeratorViewModelIsMessageVisibleBooleanFalseViewModelIsMemberBooleanFalse = false;
        androidx.databinding.ObservableField<java.lang.String> viewModelCategory = null;
        int viewModelIsModeratorViewVISIBLEViewGONE = 0;
        java.lang.String viewModelDescriptionGet = null;
        java.lang.String viewModelMembersGet = null;
        boolean viewModelIsModeratorGet = false;
        androidx.databinding.ObservableBoolean viewModelIsOfficial = null;
        androidx.databinding.ObservableField<java.lang.String> viewModelPosts = null;
        androidx.databinding.ObservableBoolean viewModelIsPublic = null;
        java.lang.String viewModelCategoryGet = null;
        androidx.databinding.ObservableField<java.lang.String> viewModelDescription = null;
        androidx.databinding.ObservableBoolean viewModelIsMember = null;
        android.graphics.drawable.Drawable viewModelIsOfficialTvNameAndroidDrawableAmityIcVerifiedJavaLangObjectNull = null;
        boolean viewModelIsOfficialGet = false;
        boolean viewModelIsPublicGet = false;
        boolean ViewModelIsModerator1 = false;
        boolean ViewModelIsModeratorGet1 = false;
        android.graphics.drawable.Drawable viewModelIsPublicJavaLangObjectNullTvNameAndroidDrawableAmityIcLock2 = null;
        java.lang.String viewModelNameGet = null;
        java.lang.String viewModelPostsGet = null;
        androidx.databinding.ObservableField<java.lang.String> viewModelAvatarUrl = null;
        int viewModelIsModeratorViewModelIsMessageVisibleBooleanFalseViewModelIsMemberBooleanFalseViewVISIBLEViewGONE = 0;
        int viewModelIsMemberViewVISIBLEViewGONE = 0;
        java.lang.String viewModelAvatarUrlGet = null;
        int viewModelIsMemberViewGONEViewVISIBLE = 0;
        int viewModelIsMessageVisibleViewModelIsModeratorBooleanFalseViewVISIBLEViewGONE = 0;
        boolean viewModelIsMemberGet = false;
        androidx.databinding.ObservableBoolean ViewModelIsModerator2 = null;
        boolean viewModelIsMessageVisible = false;
        androidx.databinding.ObservableField<java.lang.String> viewModelName = null;
        com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityDetailViewModel viewModel = mViewModel;
        androidx.databinding.ObservableField<java.lang.String> viewModelMembers = null;

        if ((dirtyFlags & 0x1fffL) != 0) {


            if ((dirtyFlags & 0x1801L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isModerator()
                        viewModelIsModerator = viewModel.isModerator();
                        // read viewModel.isMessageVisible
                        viewModelIsMessageVisible = viewModel.isMessageVisible();
                    }
                    updateRegistration(0, viewModelIsModerator);
                if((dirtyFlags & 0x1801L) != 0) {
                    if(viewModelIsMessageVisible) {
                            dirtyFlags |= 0x10000L;
                    }
                    else {
                            dirtyFlags |= 0x8000L;
                    }
                }


                    if (viewModelIsModerator != null) {
                        // read viewModel.isModerator().get()
                        viewModelIsModeratorGet = viewModelIsModerator.get();
                    }
                if((dirtyFlags & 0x1801L) != 0) {
                    if(viewModelIsModeratorGet) {
                            dirtyFlags |= 0x100000L;
                    }
                    else {
                            dirtyFlags |= 0x80000L;
                    }
                }


                    // read viewModel.isModerator().get() ? View.VISIBLE : View.GONE
                    viewModelIsModeratorViewVISIBLEViewGONE = ((viewModelIsModeratorGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x1802L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.category
                        viewModelCategory = viewModel.getCategory();
                    }
                    updateRegistration(1, viewModelCategory);


                    if (viewModelCategory != null) {
                        // read viewModel.category.get()
                        viewModelCategoryGet = viewModelCategory.get();
                    }
            }
            if ((dirtyFlags & 0x1804L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isOfficial()
                        viewModelIsOfficial = viewModel.isOfficial();
                    }
                    updateRegistration(2, viewModelIsOfficial);


                    if (viewModelIsOfficial != null) {
                        // read viewModel.isOfficial().get()
                        viewModelIsOfficialGet = viewModelIsOfficial.get();
                    }
                if((dirtyFlags & 0x1804L) != 0) {
                    if(viewModelIsOfficialGet) {
                            dirtyFlags |= 0x400000L;
                    }
                    else {
                            dirtyFlags |= 0x200000L;
                    }
                }


                    // read viewModel.isOfficial().get() ? @android:drawable/amity_ic_verified : null
                    viewModelIsOfficialTvNameAndroidDrawableAmityIcVerifiedJavaLangObjectNull = ((viewModelIsOfficialGet) ? (androidx.appcompat.content.res.AppCompatResources.getDrawable(tvName.getContext(), R.drawable.amity_ic_verified)) : (null));
            }
            if ((dirtyFlags & 0x1808L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.posts
                        viewModelPosts = viewModel.getPosts();
                    }
                    updateRegistration(3, viewModelPosts);


                    if (viewModelPosts != null) {
                        // read viewModel.posts.get()
                        viewModelPostsGet = viewModelPosts.get();
                    }
            }
            if ((dirtyFlags & 0x1810L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isPublic()
                        viewModelIsPublic = viewModel.isPublic();
                    }
                    updateRegistration(4, viewModelIsPublic);


                    if (viewModelIsPublic != null) {
                        // read viewModel.isPublic().get()
                        viewModelIsPublicGet = viewModelIsPublic.get();
                    }
                if((dirtyFlags & 0x1810L) != 0) {
                    if(viewModelIsPublicGet) {
                            dirtyFlags |= 0x1000000L;
                    }
                    else {
                            dirtyFlags |= 0x800000L;
                    }
                }


                    // read viewModel.isPublic().get() ? null : @android:drawable/amity_ic_lock2
                    viewModelIsPublicJavaLangObjectNullTvNameAndroidDrawableAmityIcLock2 = ((viewModelIsPublicGet) ? (null) : (androidx.appcompat.content.res.AppCompatResources.getDrawable(tvName.getContext(), R.drawable.amity_ic_lock2)));
            }
            if ((dirtyFlags & 0x1820L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.description
                        viewModelDescription = viewModel.getDescription();
                    }
                    updateRegistration(5, viewModelDescription);


                    if (viewModelDescription != null) {
                        // read viewModel.description.get()
                        viewModelDescriptionGet = viewModelDescription.get();
                    }
            }
            if ((dirtyFlags & 0x1840L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isMember()
                        viewModelIsMember = viewModel.isMember();
                    }
                    updateRegistration(6, viewModelIsMember);


                    if (viewModelIsMember != null) {
                        // read viewModel.isMember().get()
                        viewModelIsMemberGet = viewModelIsMember.get();
                    }
                if((dirtyFlags & 0x1840L) != 0) {
                    if(viewModelIsMemberGet) {
                            dirtyFlags |= 0x10000000L;
                            dirtyFlags |= 0x40000000L;
                    }
                    else {
                            dirtyFlags |= 0x8000000L;
                            dirtyFlags |= 0x20000000L;
                    }
                }


                    // read viewModel.isMember().get() ? View.VISIBLE : View.GONE
                    viewModelIsMemberViewVISIBLEViewGONE = ((viewModelIsMemberGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                    // read viewModel.isMember().get() ? View.GONE : View.VISIBLE
                    viewModelIsMemberViewGONEViewVISIBLE = ((viewModelIsMemberGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0x1880L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.avatarUrl
                        viewModelAvatarUrl = viewModel.getAvatarUrl();
                    }
                    updateRegistration(7, viewModelAvatarUrl);


                    if (viewModelAvatarUrl != null) {
                        // read viewModel.avatarUrl.get()
                        viewModelAvatarUrlGet = viewModelAvatarUrl.get();
                    }
            }
            if ((dirtyFlags & 0x1940L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isModerator
                        ViewModelIsModerator2 = viewModel.isModerator();
                    }
                    updateRegistration(8, ViewModelIsModerator2);


                    if (ViewModelIsModerator2 != null) {
                        // read viewModel.isModerator.get()
                        ViewModelIsModeratorGet1 = ViewModelIsModerator2.get();
                    }


                    // read !viewModel.isModerator.get()
                    ViewModelIsModerator1 = !ViewModelIsModeratorGet1;
                if((dirtyFlags & 0x1940L) != 0) {
                    if(ViewModelIsModerator1) {
                            dirtyFlags |= 0x4000L;
                    }
                    else {
                            dirtyFlags |= 0x2000L;
                    }
                }
            }
            if ((dirtyFlags & 0x1a00L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.name
                        viewModelName = viewModel.getName();
                    }
                    updateRegistration(9, viewModelName);


                    if (viewModelName != null) {
                        // read viewModel.name.get()
                        viewModelNameGet = viewModelName.get();
                    }
            }
            if ((dirtyFlags & 0x1c00L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.members
                        viewModelMembers = viewModel.getMembers();
                    }
                    updateRegistration(10, viewModelMembers);


                    if (viewModelMembers != null) {
                        // read viewModel.members.get()
                        viewModelMembersGet = viewModelMembers.get();
                    }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x4000L) != 0) {

                if (viewModel != null) {
                    // read viewModel.isMessageVisible
                    viewModelIsMessageVisible = viewModel.isMessageVisible();
                }
            if((dirtyFlags & 0x1801L) != 0) {
                if(viewModelIsMessageVisible) {
                        dirtyFlags |= 0x10000L;
                }
                else {
                        dirtyFlags |= 0x8000L;
                }
            }
        }

        if ((dirtyFlags & 0x1940L) != 0) {

                // read !viewModel.isModerator.get() ? viewModel.isMessageVisible : false
                viewModelIsModeratorViewModelIsMessageVisibleBooleanFalse = ((ViewModelIsModerator1) ? (viewModelIsMessageVisible) : (false));
            if((dirtyFlags & 0x1940L) != 0) {
                if(viewModelIsModeratorViewModelIsMessageVisibleBooleanFalse) {
                        dirtyFlags |= 0x40000L;
                }
                else {
                        dirtyFlags |= 0x20000L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x1801L) != 0) {

                // read viewModel.isMessageVisible ? viewModel.isModerator().get() : false
                viewModelIsMessageVisibleViewModelIsModeratorBooleanFalse = ((viewModelIsMessageVisible) ? (viewModelIsModeratorGet) : (false));
            if((dirtyFlags & 0x1801L) != 0) {
                if(viewModelIsMessageVisibleViewModelIsModeratorBooleanFalse) {
                        dirtyFlags |= 0x100000000L;
                }
                else {
                        dirtyFlags |= 0x80000000L;
                }
            }


                // read viewModel.isMessageVisible ? viewModel.isModerator().get() : false ? View.VISIBLE : View.GONE
                viewModelIsMessageVisibleViewModelIsModeratorBooleanFalseViewVISIBLEViewGONE = ((viewModelIsMessageVisibleViewModelIsModeratorBooleanFalse) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0x40000L) != 0) {

                if (viewModel != null) {
                    // read viewModel.isMember()
                    viewModelIsMember = viewModel.isMember();
                }
                updateRegistration(6, viewModelIsMember);


                if (viewModelIsMember != null) {
                    // read viewModel.isMember().get()
                    viewModelIsMemberGet = viewModelIsMember.get();
                }
            if((dirtyFlags & 0x1840L) != 0) {
                if(viewModelIsMemberGet) {
                        dirtyFlags |= 0x10000000L;
                        dirtyFlags |= 0x40000000L;
                }
                else {
                        dirtyFlags |= 0x8000000L;
                        dirtyFlags |= 0x20000000L;
                }
            }
        }

        if ((dirtyFlags & 0x1940L) != 0) {

                // read !viewModel.isModerator.get() ? viewModel.isMessageVisible : false ? viewModel.isMember().get() : false
                viewModelIsModeratorViewModelIsMessageVisibleBooleanFalseViewModelIsMemberBooleanFalse = ((viewModelIsModeratorViewModelIsMessageVisibleBooleanFalse) ? (viewModelIsMemberGet) : (false));
            if((dirtyFlags & 0x1940L) != 0) {
                if(viewModelIsModeratorViewModelIsMessageVisibleBooleanFalseViewModelIsMemberBooleanFalse) {
                        dirtyFlags |= 0x4000000L;
                }
                else {
                        dirtyFlags |= 0x2000000L;
                }
            }


                // read !viewModel.isModerator.get() ? viewModel.isMessageVisible : false ? viewModel.isMember().get() : false ? View.VISIBLE : View.GONE
                viewModelIsModeratorViewModelIsMessageVisibleBooleanFalseViewModelIsMemberBooleanFalseViewVISIBLEViewGONE = ((viewModelIsModeratorViewModelIsMessageVisibleBooleanFalseViewModelIsMemberBooleanFalse) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x1840L) != 0) {
            // api target 1

            this.btnJoin.setVisibility(viewModelIsMemberViewGONEViewVISIBLE);
            this.fabCreatePost.setVisibility(viewModelIsMemberViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1000L) != 0) {
            // api target 1

            this.buttonEditProfile.setOnClickListener(mCallback2);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setBackgroundAlpha(this.buttonEditProfile, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.buttonMessage.setOnClickListener(mCallback3);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setBackgroundAlpha(this.buttonMessage, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.buttonSecondaryMessage.setOnClickListener(mCallback4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvCategory, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x1801L) != 0) {
            // api target 1

            this.buttonEditProfile.setVisibility(viewModelIsModeratorViewVISIBLEViewGONE);
            this.buttonSecondaryMessage.setVisibility(viewModelIsMessageVisibleViewModelIsModeratorBooleanFalseViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1940L) != 0) {
            // api target 1

            this.buttonMessage.setVisibility(viewModelIsModeratorViewModelIsMessageVisibleBooleanFalseViewModelIsMemberBooleanFalseViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1880L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.ivAvatar, viewModelAvatarUrlGet, androidx.appcompat.content.res.AppCompatResources.getDrawable(ivAvatar.getContext(), R.drawable.amity_ic_default_community_avatar));
        }
        if ((dirtyFlags & 0x1802L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvCategory, viewModelCategoryGet);
        }
        if ((dirtyFlags & 0x1820L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvDescription, viewModelDescriptionGet);
        }
        if ((dirtyFlags & 0x1c00L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvMembersCount, viewModelMembersGet);
        }
        if ((dirtyFlags & 0x1a00L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setText(this.tvName, viewModelNameGet);
        }
        if ((dirtyFlags & 0x1810L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setDrawableStart(this.tvName, viewModelIsPublicJavaLangObjectNullTvNameAndroidDrawableAmityIcLock2);
        }
        if ((dirtyFlags & 0x1804L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setDrawableEnd(this.tvName, viewModelIsOfficialTvNameAndroidDrawableAmityIcVerifiedJavaLangObjectNull);
        }
        if ((dirtyFlags & 0x1808L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvPostCount, viewModelPostsGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 1: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityDetailViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.onEditProfileButtonClick();
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityDetailViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.onMessageButtonClick();
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.community.detailpage.EkoCommunityDetailViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.onMessageButtonClick();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.isModerator()
        flag 1 (0x2L): viewModel.category
        flag 2 (0x3L): viewModel.isOfficial()
        flag 3 (0x4L): viewModel.posts
        flag 4 (0x5L): viewModel.isPublic()
        flag 5 (0x6L): viewModel.description
        flag 6 (0x7L): viewModel.isMember()
        flag 7 (0x8L): viewModel.avatarUrl
        flag 8 (0x9L): viewModel.isModerator
        flag 9 (0xaL): viewModel.name
        flag 10 (0xbL): viewModel.members
        flag 11 (0xcL): viewModel
        flag 12 (0xdL): null
        flag 13 (0xeL): !viewModel.isModerator.get() ? viewModel.isMessageVisible : false
        flag 14 (0xfL): !viewModel.isModerator.get() ? viewModel.isMessageVisible : false
        flag 15 (0x10L): viewModel.isMessageVisible ? viewModel.isModerator().get() : false
        flag 16 (0x11L): viewModel.isMessageVisible ? viewModel.isModerator().get() : false
        flag 17 (0x12L): !viewModel.isModerator.get() ? viewModel.isMessageVisible : false ? viewModel.isMember().get() : false
        flag 18 (0x13L): !viewModel.isModerator.get() ? viewModel.isMessageVisible : false ? viewModel.isMember().get() : false
        flag 19 (0x14L): viewModel.isModerator().get() ? View.VISIBLE : View.GONE
        flag 20 (0x15L): viewModel.isModerator().get() ? View.VISIBLE : View.GONE
        flag 21 (0x16L): viewModel.isOfficial().get() ? @android:drawable/amity_ic_verified : null
        flag 22 (0x17L): viewModel.isOfficial().get() ? @android:drawable/amity_ic_verified : null
        flag 23 (0x18L): viewModel.isPublic().get() ? null : @android:drawable/amity_ic_lock2
        flag 24 (0x19L): viewModel.isPublic().get() ? null : @android:drawable/amity_ic_lock2
        flag 25 (0x1aL): !viewModel.isModerator.get() ? viewModel.isMessageVisible : false ? viewModel.isMember().get() : false ? View.VISIBLE : View.GONE
        flag 26 (0x1bL): !viewModel.isModerator.get() ? viewModel.isMessageVisible : false ? viewModel.isMember().get() : false ? View.VISIBLE : View.GONE
        flag 27 (0x1cL): viewModel.isMember().get() ? View.VISIBLE : View.GONE
        flag 28 (0x1dL): viewModel.isMember().get() ? View.VISIBLE : View.GONE
        flag 29 (0x1eL): viewModel.isMember().get() ? View.GONE : View.VISIBLE
        flag 30 (0x1fL): viewModel.isMember().get() ? View.GONE : View.VISIBLE
        flag 31 (0x20L): viewModel.isMessageVisible ? viewModel.isModerator().get() : false ? View.VISIBLE : View.GONE
        flag 32 (0x21L): viewModel.isMessageVisible ? viewModel.isModerator().get() : false ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}