/*
 * Copyright 2019 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.esri.arcgisruntime.sample.integratedwindowsauthentication

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esri.arcgisruntime.portal.PortalItem
import kotlinx.android.synthetic.main.portal_item_row.view.*

class PortalItemAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<PortalItemAdapter.PortalItemViewHolder>() {

    // List of PortalItems to display
    private var portalItems: MutableList<PortalItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortalItemViewHolder {
        with(LayoutInflater.from(parent.context).inflate(R.layout.portal_item_row, parent, false)) {
            return PortalItemViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: PortalItemViewHolder, position: Int) {
        holder.bind(portalItems?.get(position), onItemClickListener)
    }

    override fun getItemCount() = portalItems?.size ?: 0

    class PortalItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemTextView = itemView.itemTextView

        fun bind(portalItem: PortalItem?, onItemClickListener: OnItemClickListener) {
            portalItem?.let {
                itemTextView.text = it.title
                itemView.setOnClickListener { _ ->
                    onItemClickListener.onPortalItemClick(it)
                }
            }
        }
    }

    fun updatePortalItems(portalItems: List<PortalItem>) {
        if (this.portalItems == null) {
            this.portalItems = ArrayList()
        }

        DiffUtil.calculateDiff(PortalItemsDiffUtilCallback(this.portalItems, portalItems)).let {
            this.portalItems?.clear()
            this.portalItems?.addAll(portalItems)
            it.dispatchUpdatesTo(this)
        }
    }

    interface OnItemClickListener {
        fun onPortalItemClick(portalItem: PortalItem)
    }
}

class PortalItemsDiffUtilCallback(private val oldPortalItems: List<PortalItem>?, private val newPortalItems: List<PortalItem>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldPortalItems?.get(oldItemPosition)?.itemId == newPortalItems[newItemPosition].itemId

    override fun getOldListSize() = oldPortalItems?.size ?: 0

    override fun getNewListSize() = newPortalItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldPortalItems?.get(oldItemPosition) == newPortalItems[newItemPosition]

}