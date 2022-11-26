package com.jokku.funapp.core

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int, targetViewId: Int = -1) = atPositionOnView(position, targetViewId)

    private fun atPositionOnView(position: Int, targetViewId: Int) =
        object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                if (this.resources != null) {
                    idDescription = try {
                        this.resources!!.getResourceName(recyclerViewId)
                    } catch (e: Resources.NotFoundException) {
                        String.format("%s (resource name not found)", recyclerViewId)
                    }
                }
                description.appendText("RecyclerView with id: $idDescription at position: $position")
            }

            override fun matchesSafely(item: View): Boolean {
                this.resources = item.resources

                if (childView == null) {
                    val recyclerView = item.rootView.findViewById<RecyclerView>(recyclerViewId)
                    if (recyclerView.id == recyclerViewId) {
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                        if (viewHolder != null) {
                            childView = viewHolder.itemView
                        }
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    item === childView
                } else {
                    val targetView = childView!!.findViewById<View>(targetViewId)
                    item === targetView
                }
            }
        }
}