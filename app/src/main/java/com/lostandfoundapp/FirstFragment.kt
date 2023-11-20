package com.lostandfoundapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.lostandfoundapp.databinding.FragmentFirstBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val db = FirebaseFirestore.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    //TODO: If there is no Item, display a message

    // Use a suspend function to fetch data asynchronously
    private suspend fun fetchFirestoreData(): List<CardViewItem> {
        val cardViewItems = mutableListOf<CardViewItem>()
        try {
            val querySnapshot: QuerySnapshot = db.collection("items").get().await()

            for (document in querySnapshot.documents) {

                val cardViewItem = CardViewItem(
                    documentID = document.getString("documentID")!!,
                    itemPhoto = document.getString("photoUrl")!!,
                    itemName = document.getString("name")!!,
                    itemCategory = document.getString("category")!!,
                    itemDateandTime = document.getString("dateTime")!!,
                    itemLocation = document.getString("location")!!,
                    itemDetails = document.getString("details")!!,
                )

                cardViewItems.add(cardViewItem)
            }

            // Sort the list based on dateTime in descending order
            cardViewItems.sortByDescending { it.itemDateandTime }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching data from Firestore: ${e.message}")
        }
        return cardViewItems
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use a coroutine to fetch data asynchronously
        lifecycleScope.launch {
            val cardViewItems = fetchFirestoreData()

            val recyclerView: RecyclerView = view.findViewById(R.id.itemcardcontainer)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = CardViewAdapter(cardViewItems)
        }

        val cardViewItems = emptyList<CardViewItem>()
        val recyclerView: RecyclerView = view.findViewById(R.id.itemcardcontainer)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CardViewAdapter(cardViewItems)
    }


    fun addCard(item: CardViewItem) {
        // Add the new item to the list and notify the adapter
        Log.d("AddCard", "Adding card: $item")
        Toast.makeText(requireContext(), "Adding card: $item", Toast.LENGTH_SHORT).show()
        (binding.itemcardcontainer.adapter as CardViewAdapter).addItem(item)
    }

    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }


    data class CardViewItem(
        val documentID: String,
        val itemPhoto: String,
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

        // Add this function to your CardViewAdapter
        fun addItem(item: CardViewItem) {
            cardViewItems.toMutableList().add(item)
            notifyDataSetChanged() // Notify that the data set has changed
        }

        class CardViewItemDiffCallback(
            private val oldList: List<CardViewItem>,
            private val newList: List<CardViewItem>
        ) : DiffUtil.Callback() {

            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].documentID == newList[newItemPosition].documentID
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        }
    }

private fun <E> List<E>.addAll(newItems: List<E>) {
    (this as? MutableList)?.addAll(newItems)
}

private fun <E> List<E>.clear() {
    (this as? MutableList)?.clear()
}

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CardViewItem) {

            // Bind data to CardView elements here
            //TODO: Add Multiple Images as Carousel in Item Photo
            //Load Image using Glide
            Glide.with(itemView.context)
                .load(item.itemPhoto)
                .into(itemView.findViewById(R.id.ItemPhoto))

            itemView.findViewById<TextView>(R.id.ItemName).text = item.itemName
            itemView.findViewById<TextView>(R.id.ItemCategory).text = item.itemCategory
            itemView.findViewById<TextView>(R.id.ItemDateandTime).text = item.itemDateandTime
            itemView.findViewById<TextView>(R.id.ItemLocation).text = item.itemLocation
            itemView.findViewById<TextView>(R.id.ItemDetails).text = item.itemDetails

            //Buttons
            val itemPhoto = itemView.findViewById<ImageView>(R.id.ItemPhoto)
            val detailsContainer = itemView.findViewById<View>(R.id.details_container)
            val dropDownButton = itemView.findViewById<ToggleButton>(R.id.dropdownbutton)
            val claimButton = itemView.findViewById<Button>(R.id.claim_button)


            //Preview Function
            //TODO: Fix Preview Function
            itemPhoto.setOnClickListener{
                val context = itemView.context
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_item_photo, null)
                val previewImageView = dialogView.findViewById<ImageView>(R.id.preview_image_view)
//                previewImageView.setImageResource(item.itemPhoto)
                //Load Image using Glide
                Glide.with(itemView.context)
                    .load(item.itemPhoto)
                    .into(previewImageView)

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

                val intent = Intent(itemView.context, ClaimProceedActivity::class.java)
                itemView.context.startActivity(intent)

            }


        }
    }