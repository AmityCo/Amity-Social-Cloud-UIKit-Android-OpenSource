package com.amity.socialcloud.uikit.common.eventbus

import com.amity.socialcloud.sdk.core.session.eventbus.BaseStateEventBus
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent

object NetworkConnectionEventBus : BaseStateEventBus<NetworkConnectionEvent>()