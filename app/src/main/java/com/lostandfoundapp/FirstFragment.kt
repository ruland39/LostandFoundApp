package com.lostandfoundapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lostandfoundapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isDetailsVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    //TODO: Add functionalities to the item_Card
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Dummy data for testing
        val cardViewItems = listOf(
            CardViewItem(
                R.drawable.screenshot_2022_05_26_180310,
                "Item Name",
                "Item Category",
                "Date and Time",
                "Location",
                "Item Details",
            ),

            CardViewItem(
                R.drawable.ic_launcher_foreground,
                "Lost and Found",
                "Item Category",
                "Date and Time",
                "Location",
                "Item Details",
            ),

            CardViewItem(
                R.drawable.feature_one,
                "Basketball People",
                "Item Category",
                "Date and Time",
                "Location",
                "Item Details",
            ),

            CardViewItem(
                R.drawable.feature_two,
                "KFC People",
                "Item Category",
                "Date and Time",
                "Location",
                "Item Details",
            ),

            CardViewItem(
                R.drawable.feature_three,
                "Shadow People",
                "Item Category",
                "Date and Time",
                "Location",
                "Item Details",
            ),
        )



        val recyclerView: RecyclerView = view.findViewById(R.id.itemcardcontainer)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CardViewAdapter(cardViewItems)
    }


    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }


    data class CardViewItem(
        val itemPhoto: Int,
        val itemName: String,
        val itemCategory: String,
        val itemDateandTime: String,
        val itemLocation: String,
        val itemDetails: String,
    )

    class CardViewAdapter(private val cardViewItems: List<CardViewItem>) :
        RecyclerView.Adapter<CardViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
            return CardViewHolder(view)
        }

        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            val item = cardViewItems[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int {
            return cardViewItems.size
        }
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CardViewItem) {
            // Bind data to CardView elements here

            itemView.findViewById<ImageView>(R.id.ItemPhoto).setImageResource(item.itemPhoto)
            itemView.findViewById<TextView>(R.id.ItemName).text = item.itemName
            itemView.findViewById<TextView>(R.id.ItemCategory).text = item.itemCategory
            itemView.findViewById<TextView>(R.id.ItemCategory).text = item.itemCategory
            itemView.findViewById<TextView>(R.id.ItemDateandTime).text = item.itemDateandTime
            itemView.findViewById<TextView>(R.id.ItemDetails).text = item.itemDetails
            // Set the image resource using Glide or Picasso for better performance
        }
    }