package com.redmadrobot.data.model

import com.redmadrobot.domain.model.Error
import java.io.IOException

class ServerException(error: Error?) : IOException() {

    override val message: String? = error?.message

    val code: String? = error?.code
}
