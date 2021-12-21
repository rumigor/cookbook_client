package ru.rumigor.cookbook.ui.tagFilter

import moxy.viewstate.strategy.alias.SingleState
import moxy.viewstate.strategy.alias.Skip
import ru.rumigor.cookbook.ui.ScreenView
import ru.rumigor.cookbook.ui.TagViewModel

interface TagsView: ScreenView {
    @Skip
    fun loadTags(tags: List<TagViewModel>)
}
