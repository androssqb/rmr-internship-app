package com.redmadrobot.app.utils.providers

import androidx.annotation.IdRes
import com.redmadrobot.app.R
import com.redmadrobot.mapmemory.MapMemory
import com.redmadrobot.mapmemory.sharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFromPostNavigator @Inject constructor(memory: MapMemory) {

    var previousGraphId: Int by memory

    val invalidatePostList by memory.sharedFlow<Unit>()

    suspend fun setCurrentDestination(@IdRes graphId: Int) {
        previousGraphId = graphId
        if (graphId == R.id.feed_graph) invalidatePostList.emit(Unit)
    }
}
