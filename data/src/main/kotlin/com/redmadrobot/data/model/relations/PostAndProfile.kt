package com.redmadrobot.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.redmadrobot.data.model.PostDbEntity
import com.redmadrobot.data.model.ProfileDbEntity

data class PostAndProfile(
    @Embedded val post: PostDbEntity,
    @Relation(
        parentColumn = "author_id",
        entityColumn = "id"
    )
    val profile: ProfileDbEntity
)
