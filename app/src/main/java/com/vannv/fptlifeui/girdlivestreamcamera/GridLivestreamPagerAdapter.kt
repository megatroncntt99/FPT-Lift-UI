package com.vannv.fptlifeui.girdlivestreamcamera

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
Created by vannv8@fpt.com on 11/15/2023.
Copyright (c) 2023 FPT Telecom. All rights reserved.
 */
class GridLivestreamPagerAdapter(fragmentActivity: FragmentActivity) :
  FragmentStateAdapter(fragmentActivity) {

  private val _pageItems = arrayListOf<PagerItem>()
  var modeGrid: Int = 1
  var cameras: List<Camera> = arrayListOf()
  private val positionSelect= PositionSelect()
  var positionSelectCurrentPager: Int = 0
    set(value) {
      field = value
      positionSelect.value = value
      println("positionSelectCurrentPager $value")
    }


  private fun camerasInPage(pageIndex: Int): List<Camera> {
    val array = arrayListOf<Camera>()
    val offset = pageIndex * modeGrid
    val limit = (modeGrid + offset) - 1
    val size = cameras.size
    for (i in offset..limit) {
      if (size > i) {
        array.add(cameras[i].apply {
          this.check = positionSelectCurrentPager + offset == i
        })
      } else {
        array.add(Camera())
      }
    }
    if (array.find { it.check } == null) {
      positionSelectCurrentPager = 0
      array[positionSelectCurrentPager].check = true
    }
    return array
  }

  fun getItem(position: Int) = getItem(_pageItems, position)

  fun getItem(newItems: List<PagerItem>, position: Int) =
    if (position < 0 || newItems.size <= position) null else newItems[position]

  override fun createFragment(position: Int) =
    GridLivestreamCameraFragment.newInstance(
      camerasInPage(position),
      modeGrid,
      positionSelect
    )


  override fun getItemCount() = _pageItems.size

  override fun getItemId(position: Int) = getItem(position)?.id?.toLong() ?: 0

  override fun containsItem(itemId: Long): Boolean {
    var result = false
    for (pagerItem in _pageItems) {
      if (pagerItem.id.toLong() == itemId) {
        result = true
        break
      }
    }
    return result
  }


  fun setItems(newListItems: List<PagerItem>) {
    _pageItems.clear()
    _pageItems.addAll(newListItems)
    notifyDataSetChanged()
  }
}