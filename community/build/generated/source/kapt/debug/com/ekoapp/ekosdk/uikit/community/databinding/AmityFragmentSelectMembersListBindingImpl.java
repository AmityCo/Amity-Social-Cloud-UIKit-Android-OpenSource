package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentSelectMembersListBindingImpl extends AmityFragmentSelectMembersListBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.rvSelectedMembers, 7);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView3;
    @NonNull
    private final android.widget.TextView mboundView6;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener etSearchandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.searchString.get()
            //         is viewModel.searchString.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(etSearch);
            // localize variables for thread safety
            // viewModel.searchString
            androidx.databinding.ObservableField<java.lang.String> viewModelSearchString = null;
            // viewModel.searchString.get()
            java.lang.String viewModelSearchStringGet = null;
            // viewModel.searchString != null
            boolean viewModelSearchStringJavaLangObjectNull = false;
            // viewModel
            com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoSelectMembersViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {


                viewModelSearchString = viewModel.getSearchString();

                viewModelSearchStringJavaLangObjectNull = (viewModelSearchString) != (null);
                if (viewModelSearchStringJavaLangObjectNull) {




                    viewModelSearchString.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };

    public AmityFragmentSelectMembersListBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds));
    }
    private AmityFragmentSelectMembersListBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (com.google.android.material.textfield.TextInputEditText) bindings[1]
            , (androidx.recyclerview.widget.RecyclerView) bindings[2]
            , (androidx.recyclerview.widget.RecyclerView) bindings[5]
            , (androidx.recyclerview.widget.RecyclerView) bindings[7]
            , (android.widget.TextView) bindings[4]
            );
        this.etSearch.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView3 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView6 = (android.widget.TextView) bindings[6];
        this.mboundView6.setTag(null);
        this.rvMembersList.setTag(null);
        this.rvSearchResults.setTag(null);
        this.tvSearchResult.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
            setViewModel((com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoSelectMembersViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoSelectMembersViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewModelIsSearchUser((androidx.databinding.ObservableBoolean) object, fieldId);
            case 1 :
                return onChangeViewModelSearchString((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelIsSearchUser(androidx.databinding.ObservableBoolean ViewModelIsSearchUser, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelSearchString(androidx.databinding.ObservableField<java.lang.String> ViewModelSearchString, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
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
        boolean viewModelIsSearchUserGet = false;
        int textUtilsIsEmptyViewModelSearchStringViewGONEViewVISIBLE = 0;
        int viewModelIsSearchUserViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean viewModelIsSearchUser = null;
        boolean textUtilsIsEmptyViewModelSearchString = false;
        androidx.databinding.ObservableField<java.lang.String> viewModelSearchString = null;
        int viewModelIsSearchUserViewGONEViewVISIBLE = 0;
        int textUtilsIsEmptyViewModelSearchStringViewVISIBLEViewGONE = 0;
        java.lang.String viewModelSearchStringGet = null;
        com.ekoapp.ekosdk.uikit.community.ui.viewModel.EkoSelectMembersViewModel viewModel = mViewModel;

        if ((dirtyFlags & 0xfL) != 0) {


            if ((dirtyFlags & 0xdL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isSearchUser()
                        viewModelIsSearchUser = viewModel.isSearchUser();
                    }
                    updateRegistration(0, viewModelIsSearchUser);


                    if (viewModelIsSearchUser != null) {
                        // read viewModel.isSearchUser().get()
                        viewModelIsSearchUserGet = viewModelIsSearchUser.get();
                    }
                if((dirtyFlags & 0xdL) != 0) {
                    if(viewModelIsSearchUserGet) {
                            dirtyFlags |= 0x80L;
                            dirtyFlags |= 0x200L;
                    }
                    else {
                            dirtyFlags |= 0x40L;
                            dirtyFlags |= 0x100L;
                    }
                }


                    // read viewModel.isSearchUser().get() ? View.VISIBLE : View.GONE
                    viewModelIsSearchUserViewVISIBLEViewGONE = ((viewModelIsSearchUserGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                    // read viewModel.isSearchUser().get() ? View.GONE : View.VISIBLE
                    viewModelIsSearchUserViewGONEViewVISIBLE = ((viewModelIsSearchUserGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0xeL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.searchString
                        viewModelSearchString = viewModel.getSearchString();
                    }
                    updateRegistration(1, viewModelSearchString);


                    if (viewModelSearchString != null) {
                        // read viewModel.searchString.get()
                        viewModelSearchStringGet = viewModelSearchString.get();
                    }


                    // read TextUtils.isEmpty(viewModel.searchString.get())
                    textUtilsIsEmptyViewModelSearchString = android.text.TextUtils.isEmpty(viewModelSearchStringGet);
                if((dirtyFlags & 0xeL) != 0) {
                    if(textUtilsIsEmptyViewModelSearchString) {
                            dirtyFlags |= 0x20L;
                            dirtyFlags |= 0x800L;
                    }
                    else {
                            dirtyFlags |= 0x10L;
                            dirtyFlags |= 0x400L;
                    }
                }


                    // read TextUtils.isEmpty(viewModel.searchString.get()) ? View.GONE : View.VISIBLE
                    textUtilsIsEmptyViewModelSearchStringViewGONEViewVISIBLE = ((textUtilsIsEmptyViewModelSearchString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                    // read TextUtils.isEmpty(viewModel.searchString.get()) ? View.VISIBLE : View.GONE
                    textUtilsIsEmptyViewModelSearchStringViewVISIBLEViewGONE = ((textUtilsIsEmptyViewModelSearchString) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished
        if ((dirtyFlags & 0xeL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.etSearch, viewModelSearchStringGet);
            this.mboundView3.setVisibility(textUtilsIsEmptyViewModelSearchStringViewGONEViewVISIBLE);
            this.rvMembersList.setVisibility(textUtilsIsEmptyViewModelSearchStringViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.etSearch, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, etSearchandroidTextAttrChanged);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.mboundView6, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.tvSearchResult, getColorFromResource(tvSearchResult, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvSearchResult, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            this.mboundView6.setVisibility(viewModelIsSearchUserViewGONEViewVISIBLE);
            this.rvSearchResults.setVisibility(viewModelIsSearchUserViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.isSearchUser()
        flag 1 (0x2L): viewModel.searchString
        flag 2 (0x3L): viewModel
        flag 3 (0x4L): null
        flag 4 (0x5L): TextUtils.isEmpty(viewModel.searchString.get()) ? View.GONE : View.VISIBLE
        flag 5 (0x6L): TextUtils.isEmpty(viewModel.searchString.get()) ? View.GONE : View.VISIBLE
        flag 6 (0x7L): viewModel.isSearchUser().get() ? View.VISIBLE : View.GONE
        flag 7 (0x8L): viewModel.isSearchUser().get() ? View.VISIBLE : View.GONE
        flag 8 (0x9L): viewModel.isSearchUser().get() ? View.GONE : View.VISIBLE
        flag 9 (0xaL): viewModel.isSearchUser().get() ? View.GONE : View.VISIBLE
        flag 10 (0xbL): TextUtils.isEmpty(viewModel.searchString.get()) ? View.VISIBLE : View.GONE
        flag 11 (0xcL): TextUtils.isEmpty(viewModel.searchString.get()) ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}