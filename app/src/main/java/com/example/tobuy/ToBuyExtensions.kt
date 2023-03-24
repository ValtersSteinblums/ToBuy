package com.example.tobuy

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.ui.home.HomeEpoxyController

fun EpoxyController.addHeaderModel(headerText: String) {
    HomeEpoxyController.HeaderItemEpoxyModel(headerText).id(headerText).addTo(this)
}