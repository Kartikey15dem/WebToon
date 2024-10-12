package com.example.webtoon

import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.webtoon.databinding.ActivityInfoBinding
import com.squareup.picasso.Picasso
import room.Manhwa
import room.ManhwaViewModel


class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    private lateinit var manhwaViewModel: ManhwaViewModel
    private var manhwaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.info)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.favBut.drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)

        // Initialize ViewModel
        manhwaViewModel = ViewModelProvider(this).get(ManhwaViewModel::class.java)
        // Get the passed manhwa ID from intent
        manhwaId = intent.getIntExtra("MANHWA_ID", 0)

        // Fetch the manhwa from the Room database
        manhwaViewModel.getManhwaById(manhwaId).observe(this, Observer { manhwa ->

            Picasso.get()
                .load(Uri.parse(manhwa.image))
                .into(binding.manImg)
            binding.name.text = manhwa.name
            binding.rating.text = manhwa.rating.toString()
            binding.year.text = manhwa.year
            binding.writer.text = manhwa.creator
            binding.reads.text = manhwa.reads
            binding.des.text = manhwa.des

            if(manhwa.isfav){binding.favBut.drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN) }

            binding.ratingBar.rating = manhwa.MyRating

            binding.favBut.setOnClickListener() {
                manhwa.isfav=!manhwa.isfav
                if (manhwa.isfav) {
                    binding.favBut.drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN)
                    Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show()
                }else{
                    binding.favBut.drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
                    Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show()
                }
                manhwaViewModel.updateManhwa(manhwa)
            }

            binding.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
                // 'rating' is the number of stars (float)
                if (fromUser) {

                    manhwa.MyRating = rating
                    manhwaViewModel.updateManhwa(manhwa)
                }
            }


        })





    }
}