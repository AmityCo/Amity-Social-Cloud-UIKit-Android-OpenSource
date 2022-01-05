package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentNewsFeedBindingImpl extends AmityFragmentNewsFeedBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.appBar, 7);
        sViewsWithIds.put(R.id.collapsingToolbar, 8);
        sViewsWithIds.put(R.id.globalFeedContainer, 9);
        sViewsWithIds.put(R.id.ivIcon, 10);
        sViewsWithIds.put(R.id.btnExplore, 11);
        sViewsWithIds.put(R.id.tvCreateCommunity, 12);
        sViewsWithIds.put(R.id.fabCreatePost, 13);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.Group mboundView3;
    @NonNull
    private final androidx.constraintlayout.widget.Group mboundView4;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityFragmentNewsFeedBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private AmityFragmentNewsFeedBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (com.google.android.material.appbar.AppBarLayout) bindings[7]
            , (com.google.android.material.button.MaterialButton) bindings[11]
            , (com.google.android.material.appbar.CollapsingToolbarLayout) bindings[8]
            , (android.view.View) bindings[2]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[13]
            , (android.widget.FrameLayout) bindings[9]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.FrameLayout) bindings[1]
            , (androidx.swiperefreshlayout.widget.SwipeRefreshLayout) bindings[0]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[6]
            );
        this.divider.setTag(null);
        this.mboundView3 = (androidx.constraintlayout.widget.Group) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (androidx.constraintlayout.widget.Group) bindings[4];
        this.mboundView4.setTag(null);
        this.myCommunityContainer.setTag(null);
        this.refreshLayout.setTag(null);
        this.tvEmptyGlobalFeed.setTag(null);
        this.tvFindCommunity.setTag(null);
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
            setViewModel((com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoNewsFeedViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoNewsFeedViewModel ViewModel) {
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
                return onChangeViewModelEmptyCommunityList((androidx.databinding.ObservableBoolean) object, fieldId);
            case 1 :
                return onChangeViewModelEmptyGlobalFeed((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelEmptyCommunityList(androidx.databinding.ObservableBoolean ViewModelEmptyCommunityList, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelEmptyGlobalFeed(androidx.databinding.ObservableBoolean ViewModelEmptyGlobalFeed, int fieldId) {
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
        androidx.databinding.ObservableBoolean viewModelEmptyCommunityList = null;
        boolean viewModelEmptyCommunityListViewModelEmptyGlobalFeedBooleanFalse = false;
        int viewModelEmptyCommunityListViewModelEmptyGlobalFeedBooleanFalseViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean viewModelEmptyGlobalFeed = null;
        boolean viewModelEmptyGlobalFeedViewModelEmptyCommunityListBooleanFalse = false;
        boolean viewModelEmptyGlobalFeedGet = false;
        int viewModelEmptyCommunityListViewGONEViewVISIBLE = 0;
        boolean viewModelEmptyCommunityListGet = false;
        int viewModelEmptyGlobalFeedViewModelEmptyCommunityListBooleanFalseViewGONEViewVISIBLE = 0;
        com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoNewsFeedViewModel viewModel = mViewModel;

        if ((dirtyFlags & 0xfL) != 0) {



                if (viewModel != null) {
                    // read viewModel.emptyCommunityList
                    viewModelEmptyCommunityList = viewModel.getEmptyCommunityList();
                    // read viewModel.emptyGlobalFeed
                    viewModelEmptyGlobalFeed = viewModel.getEmptyGlobalFeed();
                }
                updateRegistration(0, viewModelEmptyCommunityList);
                updateRegistration(1, viewModelEmptyGlobalFeed);


                if (viewModelEmptyCommunityList != null) {
                    // read viewModel.emptyCommunityList.get()
                    viewModelEmptyCommunityListGet = viewModelEmptyCommunityList.get();
                }
            if((dirtyFlags & 0xfL) != 0) {
                if(viewModelEmptyCommunityListGet) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }
            if((dirtyFlags & 0xdL) != 0) {
                if(viewModelEmptyCommunityListGet) {
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x400L;
                }
            }
                if (viewModelEmptyGlobalFeed != null) {
                    // read viewModel.emptyGlobalFeed.get()
                    viewModelEmptyGlobalFeedGet = viewModelEmptyGlobalFeed.get();
                }
            if((dirtyFlags & 0xfL) != 0) {
                if(viewModelEmptyGlobalFeedGet) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }

            if ((dirtyFlags & 0xdL) != 0) {

                    // read viewModel.emptyCommunityList.get() ? View.GONE : View.VISIBLE
                    viewModelEmptyCommunityListViewGONEViewVISIBLE = ((viewModelEmptyCommunityListGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
        }
        // batch finished

        if ((dirtyFlags & 0xfL) != 0) {

                // read viewModel.emptyCommunityList.get() ? viewModel.emptyGlobalFeed.get() : false
                viewModelEmptyCommunityListViewModelEmptyGlobalFeedBooleanFalse = ((viewModelEmptyCommunityListGet) ? (viewModelEmptyGlobalFeedGet) : (false));
                // read viewModel.emptyGlobalFeed.get() ? viewModel.emptyCommunityList.get() : false
                viewModelEmptyGlobalFeedViewModelEmptyCommunityListBooleanFalse = ((viewModelEmptyGlobalFeedGet) ? (viewModelEmptyCommunityListGet) : (false));
            if((dirtyFlags & 0xfL) != 0) {
                if(viewModelEmptyCommunityListViewModelEmptyGlobalFeedBooleanFalse) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }
            if((dirtyFlags & 0xfL) != 0) {
                if(viewModelEmptyGlobalFeedViewModelEmptyCommunityListBooleanFalse) {
                        dirtyFlags |= 0x2000L;
                }
                else {
                        dirtyFlags |= 0x1000L;
                }
            }


                // read viewModel.emptyCommunityList.get() ? viewModel.emptyGlobalFeed.get() : false ? View.VISIBLE : View.GONE
                viewModelEmptyCommunityListViewModelEmptyGlobalFeedBooleanFalseViewVISIBLEViewGONE = ((viewModelEmptyCommunityListViewModelEmptyGlobalFeedBooleanFalse) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read viewModel.emptyGlobalFeed.get() ? viewModel.emptyCommunityList.get() : false ? View.GONE : View.VISIBLE
                viewModelEmptyGlobalFeedViewModelEmptyCommunityListBooleanFalseViewGONEViewVISIBLE = ((viewModelEmptyGlobalFeedViewModelEmptyCommunityListBooleanFalse) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            this.divider.setVisibility(viewModelEmptyCommunityListViewGONEViewVISIBLE);
            this.myCommunityContainer.setVisibility(viewModelEmptyCommunityListViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0xfL) != 0) {
            // api target 1

            this.mboundView3.setVisibility(viewModelEmptyGlobalFeedViewModelEmptyCommunityListBooleanFalseViewGONEViewVISIBLE);
            this.mboundView4.setVisibility(viewModelEmptyCommunityListViewModelEmptyGlobalFeedBooleanFalseViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvEmptyGlobalFeed, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvFindCommunity, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.emptyCommunityList
        flag 1 (0x2L): viewModel.emptyGlobalFeed
        flag 2 (0x3L): viewModel
        flag 3 (0x4L): null
        flag 4 (0x5L): viewModel.emptyCommunityList.get() ? viewModel.emptyGlobalFeed.get() : false
        flag 5 (0x6L): viewModel.emptyCommunityList.get() ? viewModel.emptyGlobalFeed.get() : false
        flag 6 (0x7L): viewModel.emptyCommunityList.get() ? viewModel.emptyGlobalFeed.get() : false ? View.VISIBLE : View.GONE
        flag 7 (0x8L): viewModel.emptyCommunityList.get() ? viewModel.emptyGlobalFeed.get() : false ? View.VISIBLE : View.GONE
        flag 8 (0x9L): viewModel.emptyGlobalFeed.get() ? viewModel.emptyCommunityList.get() : false
        flag 9 (0xaL): viewModel.emptyGlobalFeed.get() ? viewModel.emptyCommunityList.get() : false
        flag 10 (0xbL): viewModel.emptyCommunityList.get() ? View.GONE : View.VISIBLE
        flag 11 (0xcL): viewModel.emptyCommunityList.get() ? View.GONE : View.VISIBLE
        flag 12 (0xdL): viewModel.emptyGlobalFeed.get() ? viewModel.emptyCommunityList.get() : false ? View.GONE : View.VISIBLE
        flag 13 (0xeL): viewModel.emptyGlobalFeed.get() ? viewModel.emptyCommunityList.get() : false ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}