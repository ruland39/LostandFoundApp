package com.lostandfoundapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lostandfoundapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

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
                R.drawable.iphone,
                "iPhone 15 Pro Max",
                "Electronics",
                "24/11/2023 07:33 PM",
                "The Core",
                "Metallic Grey iPhone 15 Pro Max with a black case",
            ),

            CardViewItem(
                R.drawable.wallet,
                "Brown Leather Wallet",
                "Accessories",
                "09/11/2023 09:42 PM",
                "Cafeteria",
                "Brown Leather Wallet with a few cards and a picture of a dog",
            ),

            CardViewItem(
                R.drawable.roomkey,
                "Room Keys",
                "Essentials",
                "10/11/2023 03:19 PM",
                "Library",
                "Room Keys with a house keychain",
            ),

            CardViewItem(
                R.drawable.feature_one,
                "Feature One",
                "Item Category",
                "Date and Time",
                "Location",
                "Item Details",
            ),

            CardViewItem(
                R.drawable.feature_two,
                "Feature Two",
                "Item Category",
                "Date and Time",
                "Location",
                "Item Details",
            ),

            CardViewItem(
                R.drawable.feature_three,
                "Feature Three",
                "Item Category",
                "Date and Time",
                "Location",
                "Item Details",
            ),
        )



        val recyclerView: RecyclerView = view.findViewById(R.id.itemcardcontainer)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CardViewAdapter(cardViewItems)
        binding.claimButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

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
                LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_card, parent, false)
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
            //TODO: Add Multiple Images as Carousel in Item Photo
            itemView.findViewById<ImageView>(R.id.ItemPhoto).setImageResource(item.itemPhoto)
            itemView.findViewById<TextView>(R.id.ItemName).text = item.itemName
            itemView.findViewById<TextView>(R.id.ItemCategory).text = item.itemCategory
            itemView.findViewById<TextView>(R.id.ItemCategory).text = item.itemCategory
            itemView.findViewById<TextView>(R.id.ItemDateandTime).text = item.itemDateandTime
            itemView.findViewById<TextView>(R.id.ItemDetails).text = item.itemDetails

            //Buttons
            val itemPhoto = itemView.findViewById<ImageView>(R.id.ItemPhoto)
            val detailsContainer = itemView.findViewById<View>(R.id.details_container)
            val dropDownButton = itemView.findViewById<ToggleButton>(R.id.dropdownbutton)
            val claimButton = itemView.findViewById<Button>(R.id.claim_button)


            itemPhoto.setOnClickListener{
                val context = itemView.context
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_item_photo, null)
                val previewImageView = dialogView.findViewById<ImageView>(R.id.preview_image_view)
                previewImageView.setImageResource(item.itemPhoto)

                MaterialAlertDialogBuilder(context)
                    .setView(dialogView)
                    .show()

            }

            dropDownButton.setOnClickListener{


                if(dropDownButton.isChecked){
                    detailsContainer.visibility = View.VISIBLE
                    dropDownButton.animate().rotationBy(180f).setDuration(300).start()

                }else{
                    detailsContainer.visibility = View.GONE
                    dropDownButton.animate().rotationBy(-180f).setDuration(300).start()

                }
            }

            claimButton.setOnClickListener {
//                Toast.makeText(itemView.context, item.itemName + " has been claimed", Toast.LENGTH_SHORT).show()

                val intent = Intent(itemView.context, ClaimProceedActivity::class.java)
                itemView.context.startActivity(intent)

            }


        }
    }