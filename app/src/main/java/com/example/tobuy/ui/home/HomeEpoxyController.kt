package com.example.tobuy.ui.home

import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.R
import com.example.tobuy.addHeaderModel
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.ModelEmptyStateBinding
import com.example.tobuy.databinding.ModelHeaderItemBinding
import com.example.tobuy.databinding.ModelItemEntityBinding
import com.example.tobuy.ui.epoxy.LoadingEpoxyModel
import com.example.tobuy.ui.epoxy.ViewBindingKotlinModel

class HomeEpoxyController(
    private val itemEntityInterface: ItemEntityInterface
): EpoxyController() {

    var isLoading: Boolean = true
        set(value) {
            field = value
            if (field) {
                requestModelBuild()
            }
        }

    var itemEntityList = ArrayList<ItemEntity>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {
        if (isLoading) {
            LoadingEpoxyModel().id("loading_state").addTo(this)
            return
        }

        if (itemEntityList.isEmpty()) {
            EmptyStateEpoxyModel().id("empty_state").addTo(this)
            return
        }

        var currentPriority: Int = -1

        itemEntityList.sortedByDescending {
            it.priority
        }.forEach { item ->
            if (item.priority != currentPriority) {
                currentPriority = item.priority
                addHeaderModel(getHeaderTextForPriority(currentPriority))
            }
            ItemEntityEpoxyModel(item, itemEntityInterface).id(item.id).addTo(this)
        }
    }

    private fun getHeaderTextForPriority(priority: Int): String {
        return when (priority) {
            1 -> "Low"
            2 -> "Medium"
            else -> "High"
        }
    }

    data class ItemEntityEpoxyModel(
        val itemEntity: ItemEntity,
        val itemEntityInterface: ItemEntityInterface
    ): ViewBindingKotlinModel<ModelItemEntityBinding>(R.layout.model_item_entity) {
        override fun ModelItemEntityBinding.bind() {
            titleTextView.text = itemEntity.title

            if (itemEntity.description == null) {
                descriptionTextView.isGone = true
            } else {
                descriptionTextView.isVisible = true
                descriptionTextView.text = itemEntity.description
            }

            priorityTextView.setOnClickListener {
                itemEntityInterface.onBumpPriority(itemEntity)
            }

            val colorRes = when (itemEntity.priority) {
                1 -> android.R.color.holo_green_light
                2 -> android.R.color.holo_orange_light
                3 -> android.R.color.holo_red_light
                else -> android.R.color.holo_purple
            }

            priorityTextView.setBackgroundColor(ContextCompat.getColor(root.context, colorRes))

            root.setOnClickListener {
                itemEntityInterface.onItemSelected(itemEntity)
            }
        }

    }

    class EmptyStateEpoxyModel: ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state) {
        override fun ModelEmptyStateBinding.bind() {
            //nothing to do at the moment
        }
    }

    class HeaderItemEpoxyModel(
        val headerText: String
    ): ViewBindingKotlinModel<ModelHeaderItemBinding>(R.layout.model_header_item) {
        override fun ModelHeaderItemBinding.bind() {
            textView.text = headerText
        }

    }

}