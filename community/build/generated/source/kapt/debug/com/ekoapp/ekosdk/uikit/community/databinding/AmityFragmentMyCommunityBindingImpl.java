package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentMyCommunityBindingImpl extends AmityFragmentMyCommunityBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.rvMyCommunities, 3);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView2;
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
            com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel.EkoMyCommunityListViewModel viewModel = mViewModel;
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

    public AmityFragmentMyCommunityBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private AmityFragmentMyCommunityBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (com.google.android.material.textfield.TextInputEditText) bindings[1]
            , (androidx.recyclerview.widget.RecyclerView) bindings[3]
            );
        this.etSearch.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
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
            setViewModel((com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel.EkoMyCommunityListViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel.EkoMyCommunityListViewModel ViewModel) {
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
                return onChangeViewModelSearchString((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeViewModelEmptyCommunity((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelSearchString(androidx.databinding.ObservableField<java.lang.String> ViewModelSearchString, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelEmptyCommunity(androidx.databinding.ObservableBoolean ViewModelEmptyCommunity, int fieldId) {
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
        boolean viewModelEmptyCommunityGet = false;
        androidx.databinding.ObservableField<java.lang.String> viewModelSearchString = null;
        java.lang.String viewModelSearchStringGet = null;
        com.ekoapp.ekosdk.uikit.community.mycommunity.viewmodel.EkoMyCommunityListViewModel viewModel = mViewModel;
        androidx.databinding.ObservableBoolean viewModelEmptyCommunity = null;
        int viewModelEmptyCommunityViewVISIBLEViewGONE = 0;

        if ((dirtyFlags & 0xfL) != 0) {


            if ((dirtyFlags & 0xdL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.searchString
                        viewModelSearchString = viewModel.getSearchString();
                    }
                    updateRegistration(0, viewModelSearchString);


                    if (viewModelSearchString != null) {
                        // read viewModel.searchString.get()
                        viewModelSearchStringGet = viewModelSearchString.get();
                    }
            }
            if ((dirtyFlags & 0xeL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.emptyCommunity
                        viewModelEmptyCommunity = viewModel.getEmptyCommunity();
                    }
                    updateRegistration(1, viewModelEmptyCommunity);


                    if (viewModelEmptyCommunity != null) {
                        // read viewModel.emptyCommunity.get()
                        viewModelEmptyCommunityGet = viewModelEmptyCommunity.get();
                    }
                if((dirtyFlags & 0xeL) != 0) {
                    if(viewModelEmptyCommunityGet) {
                            dirtyFlags |= 0x20L;
                    }
                    else {
                            dirtyFlags |= 0x10L;
                    }
                }


                    // read viewModel.emptyCommunity.get() ? View.VISIBLE : View.GONE
                    viewModelEmptyCommunityViewVISIBLEViewGONE = ((viewModelEmptyCommunityGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.etSearch, viewModelSearchStringGet);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.etSearch, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, etSearchandroidTextAttrChanged);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.mboundView2, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0xeL) != 0) {
            // api target 1

            this.mboundView2.setVisibility(viewModelEmptyCommunityViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.searchString
        flag 1 (0x2L): viewModel.emptyCommunity
        flag 2 (0x3L): viewModel
        flag 3 (0x4L): null
        flag 4 (0x5L): viewModel.emptyCommunity.get() ? View.VISIBLE : View.GONE
        flag 5 (0x6L): viewModel.emptyCommunity.get() ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}