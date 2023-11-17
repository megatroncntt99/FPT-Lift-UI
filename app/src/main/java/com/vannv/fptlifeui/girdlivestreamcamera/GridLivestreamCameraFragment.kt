package com.vannv.fptlifeui.girdlivestreamcamera

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vannv.fptlifeui.R

/**
Created by vannv8@fpt.com on 11/15/2023.
Copyright (c) 2023 FPT Telecom. All rights reserved.
 */
class GridLivestreamCameraFragment : Fragment() {
  var modeGrid: Int = 0
  val cameras = mutableListOf<Camera>()
  var positionSelect = PositionSelect()

  private val adapter by lazy {
    GridLivestreamCameraAdapter(
      cameras,
      onClickCameraListener,
      modeGrid
    )
  }
  private lateinit var _callback: GridLivestreamCameraCallback
  private val rvGridCamera by lazy { requireView().findViewById<RecyclerView>(R.id.rv_grid_camera) }

  private val onClickCameraListener = object : OnClickCamera {
    override fun onSingleTap(index: Int, camera: Camera) {
      _callback.onSelectIndexCamera(index)
    }

    override fun onDoubleTap(index: Int, camera: Camera) {
      _callback.onChangeGridOneViewPager(index)
    }
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is GridLivestreamCameraCallback) _callback = context
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return inflater.inflate(R.layout.fragment_grid_livestream_camera, container, false)
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rvGridCamera.let {
      it.itemAnimator = DefaultItemAnimator()
      it.adapter = adapter
      it.layoutManager = GridLayoutManager(requireContext(), spanCount().value)
    }
  }

  private fun spanCount() = when (modeGrid) {
    GridMode.GRID_ONE.value -> SpanCount.ONE
    GridMode.GRID_FOUR.value -> SpanCount.TWO
    GridMode.GRID_NINE.value -> SpanCount.THREE
    GridMode.GRID_SIXTEEN.value -> SpanCount.FOUR
    GridMode.GRID_TWENTY_FIVE.value -> SpanCount.FIVE

    else -> SpanCount.ONE
  }

  override fun onResume() {
    println("Position $positionSelect")
    adapter.updateUiSelectPosition(positionSelect.value)
    super.onResume()
  }

  companion object {
    fun newInstance(cameras: List<Camera>, mode: Int, positionSelect: PositionSelect) =
      GridLivestreamCameraFragment().apply {
        this.modeGrid = mode
        this.positionSelect = positionSelect
        this.cameras.clear()
        this.cameras.addAll(cameras)

      }
  }
}

data class PositionSelect(var value: Int =0)


interface OnClickCamera {
  fun onSingleTap(index: Int, camera: Camera)
  fun onDoubleTap(index: Int, camera: Camera)
}