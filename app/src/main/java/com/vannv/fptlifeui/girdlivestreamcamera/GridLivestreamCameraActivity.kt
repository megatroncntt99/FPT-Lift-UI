package com.vannv.fptlifeui.girdlivestreamcamera

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.vannv.fptlifeui.R

/**
Created by vannv8@fpt.com on 11/15/2023.
Copyright (c) 2023 FPT Telecom. All rights reserved.
 */
class GridLivestreamCameraActivity : AppCompatActivity(), GridLivestreamCameraCallback {

  private val btnGrid1 by lazy { findViewById<Button>(R.id.button_grid_1) }
  private val btnGrid4 by lazy { findViewById<Button>(R.id.button_grid_4) }
  private val btnGrid9 by lazy { findViewById<Button>(R.id.button_grid_9) }
  private val btnGrid16 by lazy { findViewById<Button>(R.id.button_grid_16) }
  private val btnGrid25 by lazy { findViewById<Button>(R.id.button_grid_25) }
  private val txtIndexPager by lazy { findViewById<TextView>(R.id.text_index_pager) }
  private val vpGridLivestream by lazy { findViewById<ViewPager2>(R.id.view_pager_livestream) }

  private val pagerAdapter by lazy { GridLivestreamPagerAdapter(this) }
  private val cameras = mockCameras()
  private var totalPager = getTotalPage(size = cameras.size, GridMode.GRID_ONE.value)
  private var currentPager: Int = 0
  private var indexCameraSelect = 4


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_grid_livestream_camera)
    changeModeGrid(GridMode.GRID_ONE.value)


    btnGrid1.setOnClickListener {
      changeModeGrid(GridMode.GRID_ONE.value)
    }
    btnGrid4.setOnClickListener {
      changeModeGrid(GridMode.GRID_FOUR.value)
    }
    btnGrid9.setOnClickListener {
      changeModeGrid(GridMode.GRID_NINE.value)
    }
    btnGrid16.setOnClickListener {
      changeModeGrid(GridMode.GRID_SIXTEEN.value)
    }
    btnGrid25.setOnClickListener {
      changeModeGrid(GridMode.GRID_TWENTY_FIVE.value)
    }
  }

  private fun changeModeGrid(modeGrid: Int) {
    totalPager = getTotalPage(size = cameras.size, modeGrid)
    pagerAdapter.positionSelectCurrentPager = indexCameraSelect - ((indexCameraSelect / modeGrid) * modeGrid)
    updatePager(
      pagerItems(totalPager),
      modeGrid,
      cameras
    )
    listenerChangeTabViewPager(totalPager)
    vpGridLivestream.currentItem = indexCameraSelect / modeGrid
  }

  private fun listenerChangeTabViewPager(pageTotal: Int) {
    vpGridLivestream.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        currentPager = position
        txtIndexPager.text = "${position + 1}/$pageTotal"
        indexCameraSelect = position * pagerAdapter.modeGrid + pagerAdapter.positionSelectCurrentPager
        pagerAdapter.notifyDataSetChanged()
        vpGridLivestream.currentItem = position
      }
    })
  }

  private fun updatePager(items: List<PagerItem>, modeGrid: Int, cameras: List<Camera>) {
    vpGridLivestream.adapter = pagerAdapter
    vpGridLivestream.isUserInputEnabled = true
    pagerAdapter.modeGrid = modeGrid
    pagerAdapter.cameras = cameras
    pagerAdapter.setItems(items)
  }

  private fun getTotalPage(size: Int, modeGrid: Int): Int {
    val division = size / modeGrid
    val remainder = size % modeGrid
    return if (remainder != 0) {
      division + 1
    } else division
  }

  private fun pagerItems(page: Int): List<PagerItem> {
    val itemList = arrayListOf<PagerItem>()
    for (i in 0 until page) {
      itemList.add(PagerItem(i))
    }
    return itemList
  }

  override fun onChangeGridOneViewPager(index: Int) {
    indexCameraSelect = (currentPager) * pagerAdapter.modeGrid + index
    changeModeGrid(GridMode.GRID_ONE.value)
    vpGridLivestream.currentItem = indexCameraSelect
  }

  override fun onSelectIndexCamera(index: Int) {
    indexCameraSelect = (currentPager) * pagerAdapter.modeGrid + index
    pagerAdapter.positionSelectCurrentPager = index
    pagerAdapter.cameras.forEach {
      it.check = false
    }
    pagerAdapter.cameras[indexCameraSelect].check = true
  }

  override fun onRefreshFragment() {

  }
}

fun mockCameras(): MutableList<Camera> {
  val cameras = mutableListOf<Camera>()
  for (i in 0..15) {
    cameras.add(Camera(i, "Van $i"))
  }
  return cameras
}

enum class GridMode(val value: Int) {
  GRID_ONE(1),
  GRID_FOUR(4),
  GRID_NINE(9),
  GRID_SIXTEEN(16),
  GRID_TWENTY_FIVE(25),
}

enum class SpanCount(val value: Int) {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
}

