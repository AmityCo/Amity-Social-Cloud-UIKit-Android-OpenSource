package com.amity.socialcloud.uikit.community.compose.screenshot.fakes

import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.fixtures.FakePostFactory
import com.amity.socialcloud.uikit.community.compose.screenshot.fixtures.FakeUserFactory
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake [AmityPostDetailPageViewModel] for screenshot tests.
 * No SDK calls. No RxJava schedulers. Exposes hardcoded StateFlows.
 *
 * @param fakePost  Post to emit. Pass null to simulate persistent loading (shimmer).
 * @param fakeInternetState  Use [NetworkConnectionEvent.Disconnected] to test offline banner.
 * @param fakePostError  Set true to test error UI.
 */
class FakeAmityPostDetailPageViewModel(
    private val fakePost: AmityPost? = FakePostFactory.textPost(),
    private val fakeInternetState: NetworkConnectionEvent = NetworkConnectionEvent.Connected,
    private val fakePostError: Boolean = false,
    private val fakeUser: AmityUser = FakeUserFactory.currentUser(),
) : AmityPostDetailPageViewModel() {

    override val internetState: StateFlow<NetworkConnectionEvent> =
        MutableStateFlow(fakeInternetState).asStateFlow()

    override val postErrorState: StateFlow<Boolean> =
        MutableStateFlow(fakePostError).asStateFlow()

    override fun getFetchErrorState(): StateFlow<Throwable?> =
        MutableStateFlow(null).asStateFlow()

    override fun getCurrentUser(): Flowable<AmityUser> = Flowable.just(fakeUser)

    override fun getPost(postId: String): Flow<AmityPost> =
        if (fakePost != null) flowOf(fakePost) else flow { }

    override fun subscribePostRT(post: AmityPost) = Unit
    override fun unSubscribePostRT(post: AmityPost) = Unit
}
