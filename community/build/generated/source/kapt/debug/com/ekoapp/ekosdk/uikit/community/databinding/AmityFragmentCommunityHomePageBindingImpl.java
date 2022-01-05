package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentCommunityHomePageBindingImpl extends AmityFragmentCommunityHomePageBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final android.view.View mboundView3;
    @NonNull
    private final android.widget.TextView mboundView4;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityFragmentCommunityHomePageBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds));
    }
    private AmityFragmentCommunityHomePageBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 3
            , (androidx.recyclerview.widget.RecyclerView) bindings[2]
            , (com.ekoapp.ekosdk.uikit.components.EkoTabLayout) bindings[1]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView3 = (android.view.View) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (android.widget.TextView) bindings[4];
        this.mboundView4.setTag(null);
        this.rvCommunitySearch.setTag(null);
        this.tabLayout.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10L;
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
            setViewModel((com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomeViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomeViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewModelIsSearchMode((androidx.databinding.ObservableBoolean) object, fieldId);
            case 1 :
                return onChangeViewModelEmptySearchString((androidx.databinding.ObservableBoolean) object, fieldId);
            case 2 :
                return onChangeViewModelEmptySearch((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelIsSearchMode(androidx.databinding.ObservableBoolean ViewModelIsSearchMode, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelEmptySearchString(androidx.databinding.ObservableBoolean ViewModelEmptySearchString, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelEmptySearch(androidx.databinding.ObservableBoolean ViewModelEmptySearch, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
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
        int viewModelIsSearchModeViewGONEViewVISIBLE = 0;
        int viewModelEmptySearchViewModelIsSearchModeBooleanFalseViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean viewModelIsSearchMode = null;
        boolean viewModelEmptySearchStringViewModelIsSearchMode = false;
        androidx.databinding.ObservableBoolean viewModelEmptySearchString = null;
        boolean viewModelEmptySearchStringGet = false;
        boolean viewModelIsSearchModeGet = false;
        int viewModelEmptySearchStringViewModelIsSearchModeViewVISIBLEViewGONE = 0;
        int viewModelIsSearchModeViewVISIBLEViewGONE = 0;
        com.ekoapp.ekosdk.uikit.community.home.fragments.EkoCommunityHomeViewModel viewModel = mViewModel;
        boolean viewModelEmptySearchGet = false;
        androidx.databinding.ObservableBoolean viewModelEmptySearch = null;
        boolean viewModelEmptySearchViewModelIsSearchModeBooleanFalse = false;

        if ((dirtyFlags & 0x1fL) != 0) {


            if ((dirtyFlags & 0x1bL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isSearchMode()
                        viewModelIsSearchMode = viewModel.isSearchMode();
                        // read viewModel.emptySearchString
                        viewModelEmptySearchString = viewModel.getEmptySearchString();
                    }
                    updateRegistration(0, viewModelIsSearchMode);
                    updateRegistration(1, viewModelEmptySearchString);


                    if (viewModelIsSearchMode != null) {
                        // read viewModel.isSearchMode().get()
                        viewModelIsSearchModeGet = viewModelIsSearchMode.get();
                    }
                if((dirtyFlags & 0x19L) != 0) {
                    if(viewModelIsSearchModeGet) {
                            dirtyFlags |= 0x40L;
                            dirtyFlags |= 0x1000L;
                    }
                    else {
                            dirtyFlags |= 0x20L;
                            dirtyFlags |= 0x800L;
                    }
                }
                    if (viewModelEmptySearchString != null) {
                        // read viewModel.emptySearchString.get()
                        viewModelEmptySearchStringGet = viewModelEmptySearchString.get();
                    }

                if ((dirtyFlags & 0x19L) != 0) {

                        // read viewModel.isSearchMode().get() ? View.GONE : View.VISIBLE
                        viewModelIsSearchModeViewGONEViewVISIBLE = ((viewModelIsSearchModeGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                        // read viewModel.isSearchMode().get() ? View.VISIBLE : View.GONE
                        viewModelIsSearchModeViewVISIBLEViewGONE = ((viewModelIsSearchModeGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                }

                    // read (viewModel.emptySearchString.get()) & (viewModel.isSearchMode().get())
                    viewModelEmptySearchStringViewModelIsSearchMode = (viewModelEmptySearchStringGet) & (viewModelIsSearchModeGet);
                if((dirtyFlags & 0x1bL) != 0) {
                    if(viewModelEmptySearchStringViewModelIsSearchMode) {
                            dirtyFlags |= 0x400L;
                    }
                    else {
                            dirtyFlags |= 0x200L;
                    }
                }


                    // read (viewModel.emptySearchString.get()) & (viewModel.isSearchMode().get()) ? View.VISIBLE : View.GONE
                    viewModelEmptySearchStringViewModelIsSearchModeViewVISIBLEViewGONE = ((viewModelEmptySearchStringViewModelIsSearchMode) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x1dL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.emptySearch
                        viewModelEmptySearch = viewModel.getEmptySearch();
                    }
                    updateRegistration(2, viewModelEmptySearch);


                    if (viewModelEmptySearch != null) {
                        // read viewModel.emptySearch.get()
                        viewModelEmptySearchGet = viewModelEmptySearch.get();
                    }
                if((dirtyFlags & 0x1dL) != 0) {
                    if(viewModelEmptySearchGet) {
                            dirtyFlags |= 0x4000L;
                    }
                    else {
                            dirtyFlags |= 0x2000L;
                    }
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x4000L) != 0) {

                if (viewModel != null) {
                    // read viewModel.isSearchMode()
                    viewModelIsSearchMode = viewModel.isSearchMode();
                }
                updateRegistration(0, viewModelIsSearchMode);


                if (viewModelIsSearchMode != null) {
                    // read viewModel.isSearchMode().get()
                    viewModelIsSearchModeGet = viewModelIsSearchMode.get();
                }
            if((dirtyFlags & 0x19L) != 0) {
                if(viewModelIsSearchModeGet) {
                        dirtyFlags |= 0x40L;
                        dirtyFlags |= 0x1000L;
                }
                else {
                        dirtyFlags |= 0x20L;
                        dirtyFlags |= 0x800L;
                }
            }
        }

        if ((dirtyFlags & 0x1dL) != 0) {

                // read viewModel.emptySearch.get() ? viewModel.isSearchMode().get() : false
                viewModelEmptySearchViewModelIsSearchModeBooleanFalse = ((viewModelEmptySearchGet) ? (viewModelIsSearchModeGet) : (false));
            if((dirtyFlags & 0x1dL) != 0) {
                if(viewModelEmptySearchViewModelIsSearchModeBooleanFalse) {
                        dirtyFlags |= 0x100L;
                }
                else {
                        dirtyFlags |= 0x80L;
                }
            }


                // read viewModel.emptySearch.get() ? viewModel.isSearchMode().get() : false ? View.VISIBLE : View.GONE
                viewModelEmptySearchViewModelIsSearchModeBooleanFalseViewVISIBLEViewGONE = ((viewModelEmptySearchViewModelIsSearchModeBooleanFalse) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x1bL) != 0) {
            // api target 1

            this.mboundView3.setVisibility(viewModelEmptySearchStringViewModelIsSearchModeViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1dL) != 0) {
            // api target 1

            this.mboundView4.setVisibility(viewModelEmptySearchViewModelIsSearchModeBooleanFalseViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x10L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.mboundView4, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x19L) != 0) {
            // api target 1

            this.rvCommunitySearch.setVisibility(viewModelIsSearchModeViewVISIBLEViewGONE);
            this.tabLayout.setVisibility(viewModelIsSearchModeViewGONEViewVISIBLE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.isSearchMode()
        flag 1 (0x2L): viewModel.emptySearchString
        flag 2 (0x3L): viewModel.emptySearch
        flag 3 (0x4L): viewModel
        flag 4 (0x5L): null
        flag 5 (0x6L): viewModel.isSearchMode().get() ? View.GONE : View.VISIBLE
        flag 6 (0x7L): viewModel.isSearchMode().get() ? View.GONE : View.VISIBLE
        flag 7 (0x8L): viewModel.emptySearch.get() ? viewModel.isSearchMode().get() : false ? View.VISIBLE : View.GONE
        flag 8 (0x9L): viewModel.emptySearch.get() ? viewModel.isSearchMode().get() : false ? View.VISIBLE : View.GONE
        flag 9 (0xaL): (viewModel.emptySearchString.get()) & (viewModel.isSearchMode().get()) ? View.VISIBLE : View.GONE
        flag 10 (0xbL): (viewModel.emptySearchString.get()) & (viewModel.isSearchMode().get()) ? View.VISIBLE : View.GONE
        flag 11 (0xcL): viewModel.isSearchMode().get() ? View.VISIBLE : View.GONE
        flag 12 (0xdL): viewModel.isSearchMode().get() ? View.VISIBLE : View.GONE
        flag 13 (0xeL): viewModel.emptySearch.get() ? viewModel.isSearchMode().get() : false
        flag 14 (0xfL): viewModel.emptySearch.get() ? viewModel.isSearchMode().get() : false
    flag mapping end*/
    //end
}