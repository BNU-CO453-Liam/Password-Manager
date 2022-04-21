package com.passwordmanager.adapters

import com.passwordmanager.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.passwordmanager.models.AccModelClass

class ItemAdapter(val context: Context, val items: ArrayList<AccModelClass>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var mListener : onItemClickListener

    // interface for recycler view listener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    // set on click listener for each recycler view item
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val itemView = LayoutInflater.from(context).inflate(R.layout.item_custom_row,
                parent,
                false)
        return ViewHolder(itemView,mListener)
    }

    /**
     * Called when RecyclerView to bind each item in the ArrayList to a view
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvItem.text = item.accName.toString()
        holder.tvItem.id = item.id

        // Set background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.cardViewItem.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGray
                )
            )
        } else {
            holder.cardViewItem.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorWhite
                )
            )
        }
    }

    /**
     * Get the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {

        // Holds the TextView that will add each item to
        val tvItem = view.findViewById<TextView>(R.id.tv_item_name)
        val cardViewItem = view.findViewById<CardView>(R.id.card_view_item)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}