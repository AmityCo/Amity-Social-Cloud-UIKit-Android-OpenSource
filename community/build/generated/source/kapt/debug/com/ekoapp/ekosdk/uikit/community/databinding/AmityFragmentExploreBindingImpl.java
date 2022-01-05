package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentExploreBindingImpl extends AmityFragmentExploreBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityFragmentExploreBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private AmityFragmentExploreBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 3
            , (android.widget.FrameLayout) bindings[3]
            , (android.widget.FrameLayout) bindings[1]
            , (androidx.swiperefreshlayout.widget.SwipeRefreshLayout) bindings[0]
            , (android.widget.FrameLayout) bindings[2]
            );
        this.categoryContainer.setTag(null);
        this.recommendedContainer.setTag(null);
        this.refreshLayout.setTag(null);
        this.trendingContainer.setTag(null);
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
            setViewModel((com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoExploreCommunityViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoExploreCommunityViewModel ViewModel) {
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
                return onChangeViewModelEmptyCategoryList((androidx.databinding.ObservableBoolean) object, fieldId);
            case 1 :
                return onChangeViewModelEmptyRecommendedList((androidx.databinding.ObservableBoolean) object, fieldId);
            case 2 :
                return onChangeViewModelEmptyTrendingList((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelEmptyCategoryList(androidx.databinding.ObservableBoolean ViewModelEmptyCategoryList, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelEmptyRecommendedList(androidx.databinding.ObservableBoolean ViewModelEmptyRecommendedList, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelEmptyTrendingList(androidx.databinding.ObservableBoolean ViewModelEmptyTrendingList, int fieldId) {
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
        androidx.databinding.ObservableBoolean viewModelEmptyCategoryList = null;
        boolean viewModelEmptyCategoryListGet = false;
        boolean viewModelEmptyRecommendedListGet = false;
        androidx.databinding.ObservableBoolean viewModelEmptyRecommendedList = null;
        int viewModelEmptyCategoryListViewGONEViewVISIBLE = 0;
        int viewModelEmptyRecommendedListViewGONEViewVISIBLE = 0;
        boolean viewModelEmptyTrendingListGet = false;
        com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoExploreCommunityViewModel viewModel = mViewModel;
        androidx.databinding.ObservableBoolean viewModelEmptyTrendingList = null;
        int viewModelEmptyTrendingListViewGONEViewVISIBLE = 0;

        if ((dirtyFlags & 0x1fL) != 0) {


            if ((dirtyFlags & 0x19L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.emptyCategoryList
                        viewModelEmptyCategoryList = viewModel.getEmptyCategoryList();
                    }
                    updateRegistration(0, viewModelEmptyCategoryList);


                    if (viewModelEmptyCategoryList != null) {
                        // read viewModel.emptyCategoryList.get()
                        viewModelEmptyCategoryListGet = viewModelEmptyCategoryList.get();
                    }
                if((dirtyFlags & 0x19L) != 0) {
                    if(viewModelEmptyCategoryListGet) {
                            dirtyFlags |= 0x40L;
                    }
                    else {
                            dirtyFlags |= 0x20L;
                    }
                }


                    // read viewModel.emptyCategoryList.get() ? View.GONE : View.VISIBLE
                    viewModelEmptyCategoryListViewGONEViewVISIBLE = ((viewModelEmptyCategoryListGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0x1aL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.emptyRecommendedList
                        viewModelEmptyRecommendedList = viewModel.getEmptyRecommendedList();
                    }
                    updateRegistration(1, viewModelEmptyRecommendedList);


                    if (viewModelEmptyRecommendedList != null) {
                        // read viewModel.emptyRecommendedList.get()
                        viewModelEmptyRecommendedListGet = viewModelEmptyRecommendedList.get();
                    }
                if((dirtyFlags & 0x1aL) != 0) {
                    if(viewModelEmptyRecommendedListGet) {
                            dirtyFlags |= 0x100L;
                    }
                    else {
                            dirtyFlags |= 0x80L;
                    }
                }


                    // read viewModel.emptyRecommendedList.get() ? View.GONE : View.VISIBLE
                    viewModelEmptyRecommendedListViewGONEViewVISIBLE = ((viewModelEmptyRecommendedListGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0x1cL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.emptyTrendingList
                        viewModelEmptyTrendingList = viewModel.getEmptyTrendingList();
                    }
                    updateRegistration(2, viewModelEmptyTrendingList);


                    if (viewModelEmptyTrendingList != null) {
                        // read viewModel.emptyTrendingList.get()
                        viewModelEmptyTrendingListGet = viewModelEmptyTrendingList.get();
                    }
                if((dirtyFlags & 0x1cL) != 0) {
                    if(viewModelEmptyTrendingListGet) {
                            dirtyFlags |= 0x400L;
                    }
                    else {
                            dirtyFlags |= 0x200L;
                    }
                }


                    // read viewModel.emptyTrendingList.get() ? View.GONE : View.VISIBLE
                    viewModelEmptyTrendingListViewGONEViewVISIBLE = ((viewModelEmptyTrendingListGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x19L) != 0) {
            // api target 1

            this.categoryContainer.setVisibility(viewModelEmptyCategoryListViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0x1aL) != 0) {
            // api target 1

            this.recommendedContainer.setVisibility(viewModelEmptyRecommendedListViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0x10L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.recommendedContainer, getColorFromResource(recommendedContainer, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
        }
        if ((dirtyFlags & 0x1cL) != 0) {
            // api target 1

            this.trendingContainer.setVisibility(viewModelEmptyTrendingListViewGONEViewVISIBLE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.emptyCategoryList
        flag 1 (0x2L): viewModel.emptyRecommendedList
        flag 2 (0x3L): viewModel.emptyTrendingList
        flag 3 (0x4L): viewModel
        flag 4 (0x5L): null
        flag 5 (0x6L): viewModel.emptyCategoryList.get() ? View.GONE : View.VISIBLE
        flag 6 (0x7L): viewModel.emptyCategoryList.get() ? View.GONE : View.VISIBLE
        flag 7 (0x8L): viewModel.emptyRecommendedList.get() ? View.GONE : View.VISIBLE
        flag 8 (0x9L): viewModel.emptyRecommendedList.get() ? View.GONE : View.VISIBLE
        flag 9 (0xaL): viewModel.emptyTrendingList.get() ? View.GONE : View.VISIBLE
        flag 10 (0xbL): viewModel.emptyTrendingList.get() ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}