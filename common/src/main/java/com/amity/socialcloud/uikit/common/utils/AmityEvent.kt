/*
 * Copyright ©2018 Pretlist. All Rights Reserved.
 *
 * This file has trade secrets and proprietary information of Pretlist India Private Limited.
 *
 * This file and the information contained herein are the subject of copyright and intellectual property rights
 * under international conventions.
 *
 * No part of this file may be reproduced, stored in a retrieval system, or sent in any form by any means,
 * electronic, mechanical, or optical, in whole or in part, without the prior written permission of
 * Pretlist India Private Limited.
 *
 * Unless expressly shown or the context requires otherwise, the terms “Pretlist,” “Company,” “we,” “us,” and “our”
 * in this file refer to Pretlist India Private Limited. The term “Pretlist” may also refer to our products,
 * regardless of the way they are accessed. Pretlist, pretlist, Prêtlist, prêtlist, the Pretlist logo,
 * and our other registered or common law trademarks, service marks, or trade names in this file are the property
 * of Pretlist. Other trademarks, service marks, or trade names in this file are the property of their owners.
 *
 * Pretlist makes no warranties, express, implied, or statutory, as to the information in this file.
 * This file has information about Pretlist that is not available to the public and should be treated as
 * confidential information. No one shall disclose any confidential information to any third parties without express
 * written authorization from Pretlist.
 *
 * This file and the information contained herein are the subject of copyright and intellectual property rights
 * under international conventions.
 */
package com.amity.socialcloud.uikit.common.utils

/**
 * Class that defines reusable component Event used throughout the app.
 * @author sumitlakra
 * @date 06/01/2020
 */
class AmityEvent<T> {

    private val handlers = arrayListOf<(AmityEvent<T>.(T) -> Unit)>()

    /**
     * Function to overload '+' operator to add new event.
     * @param [handler] Event of any generic type
     * @author sumitlakra
     * @date 06/01/2020
     */
    operator fun plusAssign(handler: AmityEvent<T>.(T) -> Unit) {
        handlers.add(handler)
    }

    /**
     * Function to overload '-' operator to remove event.
     * @param [handler] Event of any generic type
     * @author sumitlakra
     * @date 06/01/2020
     */
    operator fun minusAssign(handler: AmityEvent<T>.(T) -> Unit) {
        handlers.remove(handler)
    }

    /**
     * Function to overload '[]' operator to invoke any particular event.
     * @param [value] Any generic type
     * @author sumitlakra
     * @date 06/01/2020
     */
    operator fun invoke(value: T) {
        for (handler in handlers) handler(value)
    }

    /**
     * Function to remove all events.
     * @author sumitlakra
     * @date 06/01/2020
     */
    fun removeAllhandlers() {
        handlers.clear()
    }
}
