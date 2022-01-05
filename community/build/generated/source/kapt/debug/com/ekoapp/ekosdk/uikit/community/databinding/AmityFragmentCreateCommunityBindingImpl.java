package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentCreateCommunityBindingImpl extends AmityFragmentCreateCommunityBinding implements com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.lAvatar, 28);
        sViewsWithIds.put(R.id.icCamera, 29);
        sViewsWithIds.put(R.id.tvCommName, 30);
        sViewsWithIds.put(R.id.tv_about, 31);
        sViewsWithIds.put(R.id.tvCategory, 32);
        sViewsWithIds.put(R.id.categoryArrow, 33);
        sViewsWithIds.put(R.id.tvAdmin, 34);
        sViewsWithIds.put(R.id.tv_public, 35);
        sViewsWithIds.put(R.id.tv_private, 36);
        sViewsWithIds.put(R.id.tv_add_members, 37);
        sViewsWithIds.put(R.id.rvAddedMembers, 38);
        sViewsWithIds.put(R.id.barrier, 39);
    }
    // views
    @NonNull
    private final androidx.core.widget.NestedScrollView mboundView0;
    @NonNull
    private final androidx.constraintlayout.widget.Group mboundView23;
    @NonNull
    private final android.widget.TextView mboundView4;
    @NonNull
    private final android.widget.TextView mboundView6;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback8;
    @Nullable
    private final android.view.View.OnClickListener mCallback7;
    @Nullable
    private final android.view.View.OnClickListener mCallback6;
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener ccNameandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.communityName.get()
            //         is viewModel.communityName.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(ccName);
            // localize variables for thread safety
            // viewModel.communityName
            androidx.databinding.ObservableField<java.lang.String> viewModelCommunityName = null;
            // viewModel.communityName.get()
            java.lang.String viewModelCommunityNameGet = null;
            // viewModel
            com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;
            // viewModel.communityName != null
            boolean viewModelCommunityNameJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {


                viewModelCommunityName = viewModel.getCommunityName();

                viewModelCommunityNameJavaLangObjectNull = (viewModelCommunityName) != (null);
                if (viewModelCommunityNameJavaLangObjectNull) {




                    viewModelCommunityName.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };
    private androidx.databinding.InverseBindingListener etDescriptionandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.description.get()
            //         is viewModel.description.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(etDescription);
            // localize variables for thread safety
            // viewModel.description != null
            boolean viewModelDescriptionJavaLangObjectNull = false;
            // viewModel.description.get()
            java.lang.String viewModelDescriptionGet = null;
            // viewModel
            com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel viewModel = mViewModel;
            // viewModel.description
            androidx.databinding.ObservableField<java.lang.String> viewModelDescription = null;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {


                viewModelDescription = viewModel.getDescription();

                viewModelDescriptionJavaLangObjectNull = (viewModelDescription) != (null);
                if (viewModelDescriptionJavaLangObjectNull) {




                    viewModelDescription.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };

    public AmityFragmentCreateCommunityBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 40, sIncludes, sViewsWithIds));
    }
    private AmityFragmentCreateCommunityBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 9
            , (androidx.constraintlayout.widget.Barrier) bindings[39]
            , (com.google.android.material.switchmaterial.SwitchMaterial) bindings[13]
            , (com.google.android.material.button.MaterialButton) bindings[27]
            , (android.widget.TextView) bindings[10]
            , (android.widget.ImageView) bindings[33]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[2]
            , (com.google.android.material.textfield.TextInputEditText) bindings[3]
            , (android.view.View) bindings[1]
            , (android.view.View) bindings[5]
            , (android.view.View) bindings[9]
            , (android.view.View) bindings[11]
            , (android.view.View) bindings[14]
            , (android.view.View) bindings[18]
            , (android.view.View) bindings[22]
            , (android.view.View) bindings[26]
            , (com.google.android.material.textfield.TextInputEditText) bindings[8]
            , (androidx.constraintlayout.widget.Group) bindings[24]
            , (android.widget.ImageView) bindings[29]
            , (android.widget.ImageView) bindings[25]
            , (android.widget.ImageView) bindings[15]
            , (android.widget.ImageView) bindings[19]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[28]
            , (android.widget.RadioButton) bindings[21]
            , (android.widget.RadioButton) bindings[17]
            , (androidx.recyclerview.widget.RecyclerView) bindings[38]
            , (android.widget.TextView) bindings[31]
            , (android.widget.TextView) bindings[37]
            , (android.widget.TextView) bindings[34]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[32]
            , (android.widget.TextView) bindings[30]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[36]
            , (android.widget.TextView) bindings[20]
            , (android.widget.TextView) bindings[35]
            , (android.widget.TextView) bindings[16]
            );
        this.btnAdminSwitch.setTag(null);
        this.btnCreateCommunity.setTag(null);
        this.category.setTag(null);
        this.ccAvatar.setTag(null);
        this.ccName.setTag(null);
        this.divider1.setTag(null);
        this.divider2.setTag(null);
        this.divider3.setTag(null);
        this.divider4.setTag(null);
        this.divider5.setTag(null);
        this.divider6.setTag(null);
        this.divider7.setTag(null);
        this.divider8.setTag(null);
        this.etDescription.setTag(null);
        this.groupAddMembers.setTag(null);
        this.ivAdd.setTag(null);
        this.ivGlobe.setTag(null);
        this.ivLock.setTag(null);
        this.mboundView0 = (androidx.core.widget.NestedScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView23 = (androidx.constraintlayout.widget.Group) bindings[23];
        this.mboundView23.setTag(null);
        this.mboundView4 = (android.widget.TextView) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView6 = (android.widget.TextView) bindings[6];
        this.mboundView6.setTag(null);
        this.rbPrivate.setTag(null);
        this.rbPublic.setTag(null);
        this.tvAdminDescription.setTag(null);
        this.tvCount.setTag(null);
        this.tvPrivateDescription.setTag(null);
        this.tvPublicDescription.setTag(null);
        setRootTag(root);
        // listeners
        mCallback8 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 3);
        mCallback7 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 2);
        mCallback6 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x400L;
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
            setViewModel((com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x200L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewModelCategory((androidx.databinding.ObservableParcelable<com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem>) object, fieldId);
            case 1 :
                return onChangeViewModelNameError((androidx.databinding.ObservableBoolean) object, fieldId);
            case 2 :
                return onChangeViewModelCommunityId((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeViewModelAddMemberVisible((androidx.databinding.ObservableBoolean) object, fieldId);
            case 4 :
                return onChangeViewModelCommunityName((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 5 :
                return onChangeViewModelIsPublic((androidx.databinding.ObservableBoolean) object, fieldId);
            case 6 :
                return onChangeViewModelDescription((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 7 :
                return onChangeViewModelAvatarUrl((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 8 :
                return onChangeViewModelIsAdmin((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelCategory(androidx.databinding.ObservableParcelable<com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem> ViewModelCategory, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelNameError(androidx.databinding.ObservableBoolean ViewModelNameError, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelCommunityId(androidx.databinding.ObservableField<java.lang.String> ViewModelCommunityId, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelAddMemberVisible(androidx.databinding.ObservableBoolean ViewModelAddMemberVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelCommunityName(androidx.databinding.ObservableField<java.lang.String> ViewModelCommunityName, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsPublic(androidx.databinding.ObservableBoolean ViewModelIsPublic, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelDescription(androidx.databinding.ObservableField<java.lang.String> ViewModelDescription, int fieldId) {
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
    private boolean onChangeViewModelIsAdmin(androidx.databinding.ObservableBoolean ViewModelIsAdmin, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
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
        boolean textUtilsIsEmptyViewModelCategoryName = false;
        androidx.databinding.ObservableParcelable<com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem> viewModelCategory = null;
        androidx.databinding.ObservableBoolean viewModelNameError = null;
        androidx.databinding.ObservableField<java.lang.String> viewModelCommunityId = null;
        int viewModelAddMemberVisibleViewVISIBLEViewGONE = 0;
        com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem viewModelCategoryGet = null;
        boolean viewModelNameErrorGet = false;
        java.lang.String viewModelDescriptionTrimLengthJavaLangString180 = null;
        boolean viewModelIsAdminGet = false;
        int viewModelNameErrorDivider2AndroidColorAmityColorAlertDivider2AndroidColorAmityColorBase = 0;
        int viewModelDescriptionTrimLength = 0;
        java.lang.String viewModelCommunityNameTrim = null;
        int viewModelCommunityIdEmptyViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean viewModelAddMemberVisible = null;
        java.lang.String viewModelCategoryName = null;
        int viewModelNameErrorViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableField<java.lang.String> viewModelCommunityName = null;
        java.lang.String viewModelCommunityIdGet = null;
        boolean textUtilsIsEmptyViewModelCategoryNameTextUtilsIsEmptyViewModelCommunityNameTrimBooleanFalse = false;
        boolean textUtilsIsEmptyViewModelCategoryNameTextUtilsIsEmptyViewModelCommunityNameTrimBooleanFalseViewModelIsPublicBooleanTrueViewModelAddMemberVisibleBooleanFalse = false;
        int viewModelCommunityNameTrimLength = 0;
        int viewModelIsPublicViewModelCommunityIdEmptyBooleanFalseViewVISIBLEViewGONE = 0;
        boolean viewModelAddMemberVisibleGet = false;
        boolean viewModelCommunityIdEmpty = false;
        boolean viewModelIsPublic = false;
        java.lang.String viewModelDescriptionGet = null;
        androidx.databinding.ObservableBoolean ViewModelIsPublic1 = null;
        androidx.databinding.ObservableField<java.lang.String> viewModelDescription = null;
        boolean viewModelIsPublicGet = false;
        boolean textUtilsIsEmptyViewModelCommunityNameTrim = false;
        boolean viewModelIsPublicViewModelCommunityIdEmptyBooleanFalse = false;
        boolean TextUtilsIsEmptyViewModelCategoryName1 = false;
        androidx.databinding.ObservableField<java.lang.String> viewModelAvatarUrl = null;
        boolean viewModelIsPublicBooleanTrueViewModelAddMemberVisible = false;
        boolean ViewModelAddMemberVisible1 = false;
        java.lang.String viewModelCommunityNameTrimLengthJavaLangString30 = null;
        java.lang.String viewModelCategoryNameEmptyCategoryAndroidStringAmityPleaseSelectCategoryViewModelCategoryName = null;
        java.lang.String viewModelCommunityNameGet = null;
        boolean viewModelCategoryNameEmpty = false;
        java.lang.String viewModelAvatarUrlGet = null;
        boolean TextUtilsIsEmptyViewModelCommunityNameTrim1 = false;
        java.lang.String viewModelDescriptionTrim = null;
        androidx.databinding.ObservableBoolean viewModelIsAdmin = null;
        com.ekoapp.ekosdk.uikit.common.views.ColorShade viewModelNameErrorColorShadeDEFAULTColorShadeSHADE4 = null;
        com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel viewModel = mViewModel;

        if ((dirtyFlags & 0x7ffL) != 0) {


            if ((dirtyFlags & 0x639L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.category
                        viewModelCategory = viewModel.getCategory();
                    }
                    updateRegistration(0, viewModelCategory);


                    if (viewModelCategory != null) {
                        // read viewModel.category.get()
                        viewModelCategoryGet = viewModelCategory.get();
                    }


                    if (viewModelCategoryGet != null) {
                        // read viewModel.category.get().name
                        viewModelCategoryName = viewModelCategoryGet.getName();
                    }


                    // read TextUtils.isEmpty(viewModel.category.get().name)
                    TextUtilsIsEmptyViewModelCategoryName1 = android.text.TextUtils.isEmpty(viewModelCategoryName);


                    // read !TextUtils.isEmpty(viewModel.category.get().name)
                    textUtilsIsEmptyViewModelCategoryName = !TextUtilsIsEmptyViewModelCategoryName1;
                if((dirtyFlags & 0x639L) != 0) {
                    if(textUtilsIsEmptyViewModelCategoryName) {
                            dirtyFlags |= 0x100000L;
                    }
                    else {
                            dirtyFlags |= 0x80000L;
                    }
                }
                if ((dirtyFlags & 0x601L) != 0) {

                        if (viewModelCategoryName != null) {
                            // read viewModel.category.get().name.empty
                            viewModelCategoryNameEmpty = viewModelCategoryName.isEmpty();
                        }
                    if((dirtyFlags & 0x601L) != 0) {
                        if(viewModelCategoryNameEmpty) {
                                dirtyFlags |= 0x40000000L;
                        }
                        else {
                                dirtyFlags |= 0x20000000L;
                        }
                    }
                }
            }
            if ((dirtyFlags & 0x602L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.nameError
                        viewModelNameError = viewModel.getNameError();
                    }
                    updateRegistration(1, viewModelNameError);


                    if (viewModelNameError != null) {
                        // read viewModel.nameError.get()
                        viewModelNameErrorGet = viewModelNameError.get();
                    }
                if((dirtyFlags & 0x602L) != 0) {
                    if(viewModelNameErrorGet) {
                            dirtyFlags |= 0x4000L;
                            dirtyFlags |= 0x40000L;
                            dirtyFlags |= 0x100000000L;
                    }
                    else {
                            dirtyFlags |= 0x2000L;
                            dirtyFlags |= 0x20000L;
                            dirtyFlags |= 0x80000000L;
                    }
                }


                    // read viewModel.nameError.get() ? @android:color/amityColorAlert : @android:color/amityColorBase
                    viewModelNameErrorDivider2AndroidColorAmityColorAlertDivider2AndroidColorAmityColorBase = ((viewModelNameErrorGet) ? (getColorFromResource(divider2, R.color.amityColorAlert)) : (getColorFromResource(divider2, R.color.amityColorBase)));
                    // read viewModel.nameError.get() ? View.VISIBLE : View.GONE
                    viewModelNameErrorViewVISIBLEViewGONE = ((viewModelNameErrorGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                    // read viewModel.nameError.get() ? ColorShade.DEFAULT : ColorShade.SHADE4
                    viewModelNameErrorColorShadeDEFAULTColorShadeSHADE4 = ((viewModelNameErrorGet) ? (com.ekoapp.ekosdk.uikit.common.views.ColorShade.DEFAULT) : (com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4));
            }
            if ((dirtyFlags & 0x604L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.communityId
                        viewModelCommunityId = viewModel.getCommunityId();
                    }
                    updateRegistration(2, viewModelCommunityId);


                    if (viewModelCommunityId != null) {
                        // read viewModel.communityId.get()
                        viewModelCommunityIdGet = viewModelCommunityId.get();
                    }


                    if (viewModelCommunityIdGet != null) {
                        // read viewModel.communityId.get().empty
                        viewModelCommunityIdEmpty = viewModelCommunityIdGet.isEmpty();
                    }
                if((dirtyFlags & 0x604L) != 0) {
                    if(viewModelCommunityIdEmpty) {
                            dirtyFlags |= 0x10000L;
                    }
                    else {
                            dirtyFlags |= 0x8000L;
                    }
                }


                    // read viewModel.communityId.get().empty ? View.VISIBLE : View.GONE
                    viewModelCommunityIdEmptyViewVISIBLEViewGONE = ((viewModelCommunityIdEmpty) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x608L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.addMemberVisible
                        viewModelAddMemberVisible = viewModel.getAddMemberVisible();
                    }
                    updateRegistration(3, viewModelAddMemberVisible);


                    if (viewModelAddMemberVisible != null) {
                        // read viewModel.addMemberVisible.get()
                        viewModelAddMemberVisibleGet = viewModelAddMemberVisible.get();
                    }
                if((dirtyFlags & 0x608L) != 0) {
                    if(viewModelAddMemberVisibleGet) {
                            dirtyFlags |= 0x1000L;
                    }
                    else {
                            dirtyFlags |= 0x800L;
                    }
                }


                    // read viewModel.addMemberVisible.get() ? View.VISIBLE : View.GONE
                    viewModelAddMemberVisibleViewVISIBLEViewGONE = ((viewModelAddMemberVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x610L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.communityName
                        viewModelCommunityName = viewModel.getCommunityName();
                    }
                    updateRegistration(4, viewModelCommunityName);


                    if (viewModelCommunityName != null) {
                        // read viewModel.communityName.get()
                        viewModelCommunityNameGet = viewModelCommunityName.get();
                    }


                    if (viewModelCommunityNameGet != null) {
                        // read viewModel.communityName.get().trim()
                        viewModelCommunityNameTrim = viewModelCommunityNameGet.trim();
                    }


                    if (viewModelCommunityNameTrim != null) {
                        // read viewModel.communityName.get().trim().length()
                        viewModelCommunityNameTrimLength = viewModelCommunityNameTrim.length();
                    }


                    // read (viewModel.communityName.get().trim().length()) + ("/30")
                    viewModelCommunityNameTrimLengthJavaLangString30 = (viewModelCommunityNameTrimLength) + ("/30");
            }
            if ((dirtyFlags & 0x624L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isPublic()
                        ViewModelIsPublic1 = viewModel.isPublic();
                    }
                    updateRegistration(5, ViewModelIsPublic1);


                    if (ViewModelIsPublic1 != null) {
                        // read viewModel.isPublic().get()
                        viewModelIsPublicGet = ViewModelIsPublic1.get();
                    }
                if((dirtyFlags & 0x400000L) != 0) {
                    if(viewModelIsPublicGet) {
                            dirtyFlags |= 0x10000000L;
                    }
                    else {
                            dirtyFlags |= 0x8000000L;
                    }
                }


                    // read !viewModel.isPublic().get()
                    viewModelIsPublic = !viewModelIsPublicGet;
                if((dirtyFlags & 0x624L) != 0) {
                    if(viewModelIsPublic) {
                            dirtyFlags |= 0x4000000L;
                    }
                    else {
                            dirtyFlags |= 0x2000000L;
                    }
                }
            }
            if ((dirtyFlags & 0x640L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.description
                        viewModelDescription = viewModel.getDescription();
                    }
                    updateRegistration(6, viewModelDescription);


                    if (viewModelDescription != null) {
                        // read viewModel.description.get()
                        viewModelDescriptionGet = viewModelDescription.get();
                    }


                    if (viewModelDescriptionGet != null) {
                        // read viewModel.description.get().trim()
                        viewModelDescriptionTrim = viewModelDescriptionGet.trim();
                    }


                    if (viewModelDescriptionTrim != null) {
                        // read viewModel.description.get().trim().length()
                        viewModelDescriptionTrimLength = viewModelDescriptionTrim.length();
                    }


                    // read (viewModel.description.get().trim().length()) + ("/180")
                    viewModelDescriptionTrimLengthJavaLangString180 = (viewModelDescriptionTrimLength) + ("/180");
            }
            if ((dirtyFlags & 0x680L) != 0) {

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
            if ((dirtyFlags & 0x700L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isAdmin()
                        viewModelIsAdmin = viewModel.isAdmin();
                    }
                    updateRegistration(8, viewModelIsAdmin);


                    if (viewModelIsAdmin != null) {
                        // read viewModel.isAdmin().get()
                        viewModelIsAdminGet = viewModelIsAdmin.get();
                    }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x601L) != 0) {

                // read viewModel.category.get().name.empty ? @android:string/amity_please_select_category : viewModel.category.get().name
                viewModelCategoryNameEmptyCategoryAndroidStringAmityPleaseSelectCategoryViewModelCategoryName = ((viewModelCategoryNameEmpty) ? (category.getResources().getString(R.string.amity_please_select_category)) : (viewModelCategoryName));
        }
        if ((dirtyFlags & 0x4100000L) != 0) {


            if ((dirtyFlags & 0x4000000L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.communityId
                        viewModelCommunityId = viewModel.getCommunityId();
                    }
                    updateRegistration(2, viewModelCommunityId);


                    if (viewModelCommunityId != null) {
                        // read viewModel.communityId.get()
                        viewModelCommunityIdGet = viewModelCommunityId.get();
                    }


                    if (viewModelCommunityIdGet != null) {
                        // read viewModel.communityId.get().empty
                        viewModelCommunityIdEmpty = viewModelCommunityIdGet.isEmpty();
                    }
                if((dirtyFlags & 0x604L) != 0) {
                    if(viewModelCommunityIdEmpty) {
                            dirtyFlags |= 0x10000L;
                    }
                    else {
                            dirtyFlags |= 0x8000L;
                    }
                }
            }
            if ((dirtyFlags & 0x100000L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.communityName
                        viewModelCommunityName = viewModel.getCommunityName();
                    }
                    updateRegistration(4, viewModelCommunityName);


                    if (viewModelCommunityName != null) {
                        // read viewModel.communityName.get()
                        viewModelCommunityNameGet = viewModelCommunityName.get();
                    }


                    if (viewModelCommunityNameGet != null) {
                        // read viewModel.communityName.get().trim()
                        viewModelCommunityNameTrim = viewModelCommunityNameGet.trim();
                    }


                    // read TextUtils.isEmpty(viewModel.communityName.get().trim())
                    TextUtilsIsEmptyViewModelCommunityNameTrim1 = android.text.TextUtils.isEmpty(viewModelCommunityNameTrim);


                    // read !TextUtils.isEmpty(viewModel.communityName.get().trim())
                    textUtilsIsEmptyViewModelCommunityNameTrim = !TextUtilsIsEmptyViewModelCommunityNameTrim1;
            }
        }

        if ((dirtyFlags & 0x639L) != 0) {

                // read !TextUtils.isEmpty(viewModel.category.get().name) ? !TextUtils.isEmpty(viewModel.communityName.get().trim()) : false
                textUtilsIsEmptyViewModelCategoryNameTextUtilsIsEmptyViewModelCommunityNameTrimBooleanFalse = ((textUtilsIsEmptyViewModelCategoryName) ? (textUtilsIsEmptyViewModelCommunityNameTrim) : (false));
            if((dirtyFlags & 0x639L) != 0) {
                if(textUtilsIsEmptyViewModelCategoryNameTextUtilsIsEmptyViewModelCommunityNameTrimBooleanFalse) {
                        dirtyFlags |= 0x400000L;
                }
                else {
                        dirtyFlags |= 0x200000L;
                }
            }
        }
        if ((dirtyFlags & 0x624L) != 0) {

                // read !viewModel.isPublic().get() ? viewModel.communityId.get().empty : false
                viewModelIsPublicViewModelCommunityIdEmptyBooleanFalse = ((viewModelIsPublic) ? (viewModelCommunityIdEmpty) : (false));
            if((dirtyFlags & 0x624L) != 0) {
                if(viewModelIsPublicViewModelCommunityIdEmptyBooleanFalse) {
                        dirtyFlags |= 0x1000000L;
                }
                else {
                        dirtyFlags |= 0x800000L;
                }
            }


                // read !viewModel.isPublic().get() ? viewModel.communityId.get().empty : false ? View.VISIBLE : View.GONE
                viewModelIsPublicViewModelCommunityIdEmptyBooleanFalseViewVISIBLEViewGONE = ((viewModelIsPublicViewModelCommunityIdEmptyBooleanFalse) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished

        if ((dirtyFlags & 0x400000L) != 0) {



                if (viewModel != null) {
                    // read viewModel.isPublic()
                    ViewModelIsPublic1 = viewModel.isPublic();
                }
                updateRegistration(5, ViewModelIsPublic1);


                if (ViewModelIsPublic1 != null) {
                    // read viewModel.isPublic().get()
                    viewModelIsPublicGet = ViewModelIsPublic1.get();
                }
            if((dirtyFlags & 0x400000L) != 0) {
                if(viewModelIsPublicGet) {
                        dirtyFlags |= 0x10000000L;
                }
                else {
                        dirtyFlags |= 0x8000000L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x8000000L) != 0) {

                if (viewModel != null) {
                    // read viewModel.addMemberVisible
                    viewModelAddMemberVisible = viewModel.getAddMemberVisible();
                }
                updateRegistration(3, viewModelAddMemberVisible);


                if (viewModelAddMemberVisible != null) {
                    // read viewModel.addMemberVisible.get()
                    viewModelAddMemberVisibleGet = viewModelAddMemberVisible.get();
                }
            if((dirtyFlags & 0x608L) != 0) {
                if(viewModelAddMemberVisibleGet) {
                        dirtyFlags |= 0x1000L;
                }
                else {
                        dirtyFlags |= 0x800L;
                }
            }


                // read !viewModel.addMemberVisible.get()
                ViewModelAddMemberVisible1 = !viewModelAddMemberVisibleGet;
        }

        if ((dirtyFlags & 0x400000L) != 0) {

                // read viewModel.isPublic().get() ? true : !viewModel.addMemberVisible.get()
                viewModelIsPublicBooleanTrueViewModelAddMemberVisible = ((viewModelIsPublicGet) ? (true) : (ViewModelAddMemberVisible1));
        }

        if ((dirtyFlags & 0x639L) != 0) {

                // read !TextUtils.isEmpty(viewModel.category.get().name) ? !TextUtils.isEmpty(viewModel.communityName.get().trim()) : false ? viewModel.isPublic().get() ? true : !viewModel.addMemberVisible.get() : false
                textUtilsIsEmptyViewModelCategoryNameTextUtilsIsEmptyViewModelCommunityNameTrimBooleanFalseViewModelIsPublicBooleanTrueViewModelAddMemberVisibleBooleanFalse = ((textUtilsIsEmptyViewModelCategoryNameTextUtilsIsEmptyViewModelCommunityNameTrimBooleanFalse) ? (viewModelIsPublicBooleanTrueViewModelAddMemberVisible) : (false));
        }
        // batch finished
        if ((dirtyFlags & 0x700L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.btnAdminSwitch, viewModelIsAdminGet);
        }
        if ((dirtyFlags & 0x400L) != 0) {
            // api target 1

            this.btnAdminSwitch.setOnClickListener(mCallback6);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ccAvatar, getColorFromResource(ccAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.ccName, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, ccNameandroidTextAttrChanged);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider1, getColorFromResource(divider1, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider3, getColorFromResource(divider3, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider4, getColorFromResource(divider4, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider5, getColorFromResource(divider5, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider6, getColorFromResource(divider6, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider7, getColorFromResource(divider7, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider8, getColorFromResource(divider8, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.etDescription, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, etDescriptionandroidTextAttrChanged);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedImageView(this.ivAdd, getColorFromResource(ivAdd, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedImageView(this.ivGlobe, getColorFromResource(ivGlobe, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedImageView(this.ivLock, getColorFromResource(ivLock, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.mboundView4, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            this.rbPrivate.setOnClickListener(mCallback8);
            this.rbPublic.setOnClickListener(mCallback7);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvAdminDescription, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvCount, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvPrivateDescription, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvPublicDescription, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x639L) != 0) {
            // api target 1

            this.btnCreateCommunity.setEnabled(textUtilsIsEmptyViewModelCategoryNameTextUtilsIsEmptyViewModelCommunityNameTrimBooleanFalseViewModelIsPublicBooleanTrueViewModelAddMemberVisibleBooleanFalse);
        }
        if ((dirtyFlags & 0x601L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.category, viewModelCategoryNameEmptyCategoryAndroidStringAmityPleaseSelectCategoryViewModelCategoryName);
        }
        if ((dirtyFlags & 0x680L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.ccAvatar, viewModelAvatarUrlGet, androidx.appcompat.content.res.AppCompatResources.getDrawable(ccAvatar.getContext(), R.drawable.amity_ic_default_community_avatar));
        }
        if ((dirtyFlags & 0x610L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.ccName, viewModelCommunityNameGet);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView4, viewModelCommunityNameTrimLengthJavaLangString30);
        }
        if ((dirtyFlags & 0x602L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider2, viewModelNameErrorDivider2AndroidColorAmityColorAlertDivider2AndroidColorAmityColorBase, viewModelNameErrorColorShadeDEFAULTColorShadeSHADE4);
            this.mboundView6.setVisibility(viewModelNameErrorViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x640L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.etDescription, viewModelDescriptionGet);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvCount, viewModelDescriptionTrimLengthJavaLangString180);
        }
        if ((dirtyFlags & 0x624L) != 0) {
            // api target 1

            this.groupAddMembers.setVisibility(viewModelIsPublicViewModelCommunityIdEmptyBooleanFalseViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x608L) != 0) {
            // api target 1

            this.ivAdd.setVisibility(viewModelAddMemberVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x604L) != 0) {
            // api target 1

            this.mboundView23.setVisibility(viewModelCommunityIdEmptyViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x620L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.rbPrivate, viewModelIsPublic);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.rbPublic, viewModelIsPublicGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 3: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {



                    viewModel.changePostType(false);
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {



                    viewModel.changePostType(true);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoCreateCommunityViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.changeAdminPost();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.category
        flag 1 (0x2L): viewModel.nameError
        flag 2 (0x3L): viewModel.communityId
        flag 3 (0x4L): viewModel.addMemberVisible
        flag 4 (0x5L): viewModel.communityName
        flag 5 (0x6L): viewModel.isPublic()
        flag 6 (0x7L): viewModel.description
        flag 7 (0x8L): viewModel.avatarUrl
        flag 8 (0x9L): viewModel.isAdmin()
        flag 9 (0xaL): viewModel
        flag 10 (0xbL): null
        flag 11 (0xcL): viewModel.addMemberVisible.get() ? View.VISIBLE : View.GONE
        flag 12 (0xdL): viewModel.addMemberVisible.get() ? View.VISIBLE : View.GONE
        flag 13 (0xeL): viewModel.nameError.get() ? @android:color/amityColorAlert : @android:color/amityColorBase
        flag 14 (0xfL): viewModel.nameError.get() ? @android:color/amityColorAlert : @android:color/amityColorBase
        flag 15 (0x10L): viewModel.communityId.get().empty ? View.VISIBLE : View.GONE
        flag 16 (0x11L): viewModel.communityId.get().empty ? View.VISIBLE : View.GONE
        flag 17 (0x12L): viewModel.nameError.get() ? View.VISIBLE : View.GONE
        flag 18 (0x13L): viewModel.nameError.get() ? View.VISIBLE : View.GONE
        flag 19 (0x14L): !TextUtils.isEmpty(viewModel.category.get().name) ? !TextUtils.isEmpty(viewModel.communityName.get().trim()) : false
        flag 20 (0x15L): !TextUtils.isEmpty(viewModel.category.get().name) ? !TextUtils.isEmpty(viewModel.communityName.get().trim()) : false
        flag 21 (0x16L): !TextUtils.isEmpty(viewModel.category.get().name) ? !TextUtils.isEmpty(viewModel.communityName.get().trim()) : false ? viewModel.isPublic().get() ? true : !viewModel.addMemberVisible.get() : false
        flag 22 (0x17L): !TextUtils.isEmpty(viewModel.category.get().name) ? !TextUtils.isEmpty(viewModel.communityName.get().trim()) : false ? viewModel.isPublic().get() ? true : !viewModel.addMemberVisible.get() : false
        flag 23 (0x18L): !viewModel.isPublic().get() ? viewModel.communityId.get().empty : false ? View.VISIBLE : View.GONE
        flag 24 (0x19L): !viewModel.isPublic().get() ? viewModel.communityId.get().empty : false ? View.VISIBLE : View.GONE
        flag 25 (0x1aL): !viewModel.isPublic().get() ? viewModel.communityId.get().empty : false
        flag 26 (0x1bL): !viewModel.isPublic().get() ? viewModel.communityId.get().empty : false
        flag 27 (0x1cL): viewModel.isPublic().get() ? true : !viewModel.addMemberVisible.get()
        flag 28 (0x1dL): viewModel.isPublic().get() ? true : !viewModel.addMemberVisible.get()
        flag 29 (0x1eL): viewModel.category.get().name.empty ? @android:string/amity_please_select_category : viewModel.category.get().name
        flag 30 (0x1fL): viewModel.category.get().name.empty ? @android:string/amity_please_select_category : viewModel.category.get().name
        flag 31 (0x20L): viewModel.nameError.get() ? ColorShade.DEFAULT : ColorShade.SHADE4
        flag 32 (0x21L): viewModel.nameError.get() ? ColorShade.DEFAULT : ColorShade.SHADE4
    flag mapping end*/
    //end
}