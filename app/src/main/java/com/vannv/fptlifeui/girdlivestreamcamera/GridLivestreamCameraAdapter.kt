package com.vannv.fptlifeui.girdlivestreamcamera

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.vannv.fptlifeui.R


/**
Created by vannv8@fpt.com on 11/15/2023.
Copyright (c) 2023 FPT Telecom. All rights reserved.
 */
class GridLivestreamCameraAdapter(
  private val cameras: List<Camera>,
  private val listener: OnClickCamera,
  private val gridMode: Int
) : Adapter<GridLivestreamCameraAdapter.ViewHolder>() {

  fun updateUiSelectPosition(positionSelect: Int) {
    cameras.forEach {
      it.check = false
    }
    cameras[if (cameras[positionSelect].isEnable()) positionSelect else 0].check = true
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      LayoutInflater.from(parent.context)
        .inflate(R.layout.item_livestream_camera, parent, false)
    )
  }


  override fun getItemCount(): Int = cameras.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindData(position, cameras[position])
  }

  inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val txtNameCamera by lazy { view.findViewById<TextView>(R.id.text_name_camera) }
    private val viewBorderSelect by lazy { view.findViewById<View>(R.id.view_border_select) }

    fun bindData(index: Int, camera: Camera) {
      txtNameCamera.text = camera.name ?: "Không có camera"
      viewBorderSelect.visibility =
        if (gridMode != GridMode.GRID_ONE.value && camera.check) View.VISIBLE else View.GONE

      if (camera.isEnable() && gridMode != GridMode.GRID_ONE.value) {
        view.setOnClickListener(object : OnDoubleClickListener() {
          override fun onDoubleTap() {
            listener.onDoubleTap(index, camera)
          }

          override fun onSingleTap() {
            cameras.forEach {
              it.check = false
            }
            camera.check = true
            notifyDataSetChanged()
            listener.onSingleTap(index, camera)
          }

        })
      }
    }
  }
}

data class Camera(val id: Int? = null, val name: String? = null, var check: Boolean = false) {
  fun isEnable() = name != null
}

data class PagerItem(val id: Int)

abstract class OnDoubleClickListener : View.OnClickListener {
  private val doubleClickTimeout: Int = ViewConfiguration.getDoubleTapTimeout()
  private val handler: Handler = Handler(Looper.getMainLooper())
  private var firstClickTime = 0L

  override fun onClick(v: View) {
    val now = System.currentTimeMillis()
    if (now - firstClickTime < doubleClickTimeout) {
      handler.removeCallbacksAndMessages(null)
      firstClickTime = 0L
      onDoubleTap()
    } else {
      firstClickTime = now
      handler.postDelayed({
                            onSingleTap()
                            firstClickTime = 0L
                          }, doubleClickTimeout.toLong())
    }
  }

  abstract fun onDoubleTap()
  abstract fun onSingleTap()
  fun reset() {
    handler.removeCallbacksAndMessages(null)
  }
}