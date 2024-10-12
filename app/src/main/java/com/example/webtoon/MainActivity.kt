package com.example.webtoon

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import room.Manhwa
import room.ManhwaDatabase
import room.ManhwaViewModel
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewAct: RecyclerView
    private lateinit var recyclerViewRom: RecyclerView
    private lateinit var recyclerViewAdv: RecyclerView
    private lateinit var layoutManagerAct: LinearLayoutManager
    private lateinit var layoutManagerRom: LinearLayoutManager
    private lateinit var layoutManagerAdv: LinearLayoutManager
    private lateinit var adapterAct: RecyclerViewAdapter
    private lateinit var adapterRom: RecyclerViewAdapter
    private lateinit var adapterAdv: RecyclerViewAdapter
    private val handler = Handler(Looper.getMainLooper()) // Handler to schedule scrolling
    private var currentPosition = 0 // Track current position
    private var isUserInteracting = false // Track if user is manually scrolling
    private val delay: Long = 4000 // 4 seconds delay for auto scroll
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var manhwaViewModel:ManhwaViewModel
    private lateinit var favButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewAct = findViewById(R.id.recycler_view_act)
        recyclerViewRom = findViewById(R.id.recycler_view_rom)
        recyclerViewAdv = findViewById(R.id.recycler_view_adv)
        favButton = findViewById(R.id.favbut)

        favButton.setOnClickListener(){
            startActivity(Intent(this,FavoriteActivity::class.java))
        }


        // Sample data and adapter setup
        val ManhwaList = listOf(
            Manhwa("Solo Leveling", "2017 – 2021", "Chugog", "2.5B", getString(R.string.des1), 4.8f, 4.0f,
                "file:///android_asset/img1.webp", "file:///android_asset/image1.webp", false, cat.ACT),
            Manhwa("Tower of God", "2010 – Present", "SIU (Lee Jong Hui)", "1.2B", getString(R.string.des2), 4.7f, 4.0f,
                "file:///android_asset/img2.webp", "file:///android_asset/image2.webp", false, cat.ACT),
            Manhwa("Hardcore Leveling Warrior", "2016 – Present", "Sehoon Kim", "233.3M", getString(R.string.des3), 4.5f, 4.0f,
                "file:///android_asset/img3.webp", "file:///android_asset/image3.jpg", false, cat.ACT),
            Manhwa("Noblesse", "2007 – 2019", "Jeho Son", "439.7M", getString(R.string.des4), 4.6f, 4.0f,
                "file:///android_asset/img4.webp", "file:///android_asset/image4.jpg", false, cat.ACT),
            Manhwa("The God of High School", "2014 – Present", "Yongje Park", "786.1M", getString(R.string.des5), 4.6f, 4.0f,
                "file:///android_asset/img5.webp", "file:///android_asset/image5.jpg", false, cat.ACT),
            Manhwa("Second Life Ranker", "2019 – Present", "Nong Nong", "631M", getString(R.string.des6), 4.7f, 4.0f,
                "file:///android_asset/img6.webp", "file:///android_asset/image6.jpg", false, cat.ACT),
            Manhwa("Eleceed", "2020 – Present", "Jeho Son", "328.2M", getString(R.string.des7), 4.8f, 4.0f,
                "file:///android_asset/img7.webp", "file:///android_asset/image7.jpg", false, cat.ACT),
            Manhwa("Lore Olympus", "2018 – Present", "Rachel Smythe", "1.4B", getString(R.string.des8), 4.9f, 4.0f,
                "file:///android_asset/img8.webp", "file:///android_asset/image8.jpg", false, cat.ROM),
            Manhwa("The Remarried Empress", "2019 – Present", "Alphatart", "436M", getString(R.string.des9), 4.8f, 4.0f,
                "file:///android_asset/img9.webp", "file:///android_asset/image9.jpg", false, cat.ROM),
            Manhwa("Leveling Up My Husband to the Max", "2020 – 2021", "Uginon, Nuova, Curlin", "83.1M", getString(R.string.des10), 4.7f, 4.0f,
                "file:///android_asset/img10.webp", "file:///android_asset/image10.jpg", false, cat.ROM),
            Manhwa("I’m the Queen in this Life", "2022 – Present", "Themis", "69.5M", getString(R.string.des11), 4.6f, 4.0f,
                "file:///android_asset/img11.webp", "file:///android_asset/image11.jpg", false, cat.ROM),
            Manhwa("The Witch and the Bull", "2019 – Present", "Moonsia", "66.5M", getString(R.string.des12), 4.5f, 4.0f,
                "file:///android_asset/img12.webp", "file:///android_asset/image12.jpg", false, cat.ROM),
            Manhwa("From A Knight to A Lady", "2020 – Present", "Ink, Hyerim Sung", "70.4M", getString(R.string.des13), 4.7f, 4.0f,
                "file:///android_asset/img13.webp", "file:///android_asset/image13.jpg", false, cat.ROM),
            Manhwa("A Returner’s Magic Should Be Special", "2018 – Present", "Usonan", "1.3B", getString(R.string.des14), 4.9f, 4.0f,
                "file:///android_asset/img14.webp", "file:///android_asset/image14.jpg", false, cat.ADV),
            Manhwa("Omniscient Reader’s Viewpoint", "2018 – 2020", "Sing N Song, Sleepy-C", "349.4M", getString(R.string.des15), 4.8f, 4.0f,
                "file:///android_asset/img15.webp", "file:///android_asset/image15.jpg", false, cat.ADV),
            Manhwa("The World After The Fall", "Status: Every Monday", "Sing N Song, Undead Gamja", "52.8M", getString(R.string.des16), 4.7f, 4.0f,
                "file:///android_asset/img16.webp", "file:///android_asset/image16.jpg", false, cat.ADV),
            Manhwa("The Greatest Estate Developer", "Status: Every Thursday", "Lee Hyunmin, Kim Hyunsoo", "87.3M", getString(R.string.des17), 4.6f, 4.0f,
                "file:///android_asset/img17.webp", "file:///android_asset/image17.jpg", false, cat.ADV),
            Manhwa("The Wrath & the Dawn", "Completed", "Renee Ahdieh", "73.6M", getString(R.string.des18), 4.7f, 4.0f,
                "file:///android_asset/img18.webp", "file:///android_asset/image18.jpg", false, cat.ADV),
            Manhwa("Tricked Into Becoming the Heroine’s Stepmother", "Completed", "Mokgamgi, Hariheen", "87.2M", getString(R.string.des19), 4.5f, 4.0f,
                "file:///android_asset/img19.webp", "file:///android_asset/image19.jpg", false, cat.ADV),
            Manhwa("The First Night With the Duke", "Completed", "Teava, MSG", "110.1M", getString(R.string.des20), 4.6f, 4.0f,
                "file:///android_asset/img20.webp", "file:///android_asset/image20.jpg", false, cat.ADV)
        )




        // Call the insert function from ViewModel
        manhwaViewModel = ViewModelProvider(this)[ManhwaViewModel::class.java]
        manhwaViewModel.insertAllManhwas(ManhwaList)

        //Setting up the recyclerViews
        layoutManagerAct = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        layoutManagerRom = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        layoutManagerAdv = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterAct = RecyclerViewAdapter(this, emptyList())
        adapterRom = RecyclerViewAdapter(this, emptyList())
        adapterAdv = RecyclerViewAdapter(this, emptyList())

        recyclerViewAct.adapter = adapterAct
        recyclerViewAct.layoutManager = layoutManagerAct

        recyclerViewRom.adapter = adapterRom
        recyclerViewRom.layoutManager = layoutManagerRom

        recyclerViewAdv.adapter = adapterAdv
        recyclerViewAdv.layoutManager = layoutManagerAdv

       // Observe data from ViewModel
        manhwaViewModel.getManhwasByCategory(cat.ACT).observe(this) { manhwaList ->
            adapterAct.setManhwaList(manhwaList)
        }

        manhwaViewModel.getManhwasByCategory(cat.ROM).observe(this) { manhwaList ->
            adapterRom.setManhwaList(manhwaList)
        }

        manhwaViewModel.getManhwasByCategory(cat.ADV).observe(this) { manhwaList ->
            adapterAdv.setManhwaList(manhwaList)
        }

        // Start auto-scroll
        startAutoScroll(recyclerViewAct,adapterAct)
        startAutoScroll(recyclerViewRom,adapterRom)
        startAutoScroll(recyclerViewAdv,adapterAdv)

        // Add a scroll listener to detect user interaction and pause auto-scroll
        recyclerViewAct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // If user is interacting with RecyclerView (flinging or dragging)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    isUserInteracting = true
                    stopAutoScroll() // Pause auto-scrolling when user interacts
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isUserInteracting = false
                    currentPosition = layoutManagerAct.findFirstVisibleItemPosition()
                    // Restart auto-scroll after user interaction ends
                    startAutoScroll(recyclerViewAct,adapterAct)
                }
            }
        })

        recyclerViewRom.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // If user is interacting with RecyclerView (flinging or dragging)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    isUserInteracting = true
                    stopAutoScroll() // Pause auto-scrolling when user interacts
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isUserInteracting = false
                    currentPosition = layoutManagerRom.findFirstVisibleItemPosition()
                    // Restart auto-scroll after user interaction ends
                    startAutoScroll(recyclerViewRom, adapterRom)
                }
            }
        })

        recyclerViewAdv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // If user is interacting with RecyclerView (flinging or dragging)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    isUserInteracting = true
                    stopAutoScroll() // Pause auto-scrolling when user interacts
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isUserInteracting = false
                    currentPosition = layoutManagerAdv.findFirstVisibleItemPosition()
                    // Restart auto-scroll after user interaction ends
                    startAutoScroll(recyclerViewAdv, adapterAdv)
                }
            }
        })

 //           }
//            val man = manhwaDao.getManhwaById(4)
//            if (man != null) {
//                Log.d("TG",man.creator)
//            }
//        }



    }

    // Function to start auto-scrolling
    private fun startAutoScroll(recyclerView: RecyclerView,adapter: RecyclerViewAdapter) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isUserInteracting && adapter.itemCount > 0) {
                    // Scroll to the next position
                    currentPosition = (currentPosition + 1) % adapter.itemCount
                    recyclerView.smoothScrollToPosition(currentPosition)

                    // Repeat this action every 'delay' milliseconds
                    handler.postDelayed(this, delay)
                }
            }
        }, delay)
    }

    // Function to stop auto-scrolling
    private fun stopAutoScroll() {
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoScroll() // Stop the auto-scroll when activity is destroyed
        executor.shutdown()
    }


}
