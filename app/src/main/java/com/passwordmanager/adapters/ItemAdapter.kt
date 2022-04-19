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

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    //var onItemClick : ((AccModelClass) -> Unit)? = null

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
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)
        val account = items[position]

        holder.tvItem.text = item.accName.toString()
        holder.tvItem.id = item.id



        // set on click for holder view
        //holder.itemView.setOnClickListener{
           // onItemClick?.invoke(account)
        //}

        // Updating the background color according to the odd/even positions in list.
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
     * Gets the number of items in the list
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

    // get item names in list

    fun getItemDetails(): ArrayList<String> {
        val itemList = ArrayList<String>()
        for (item in items) {
            itemList.add(item.accName.toString())
            itemList.add(item.username.toString())
            itemList.add(item.passwd.toString())
        }
        return itemList
    }
}