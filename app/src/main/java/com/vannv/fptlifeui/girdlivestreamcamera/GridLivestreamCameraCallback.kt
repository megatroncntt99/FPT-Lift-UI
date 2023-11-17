package com.vannv.fptlifeui.girdlivestreamcamera

/**
Created by vannv8@fpt.com on 11/15/2023.
Copyright (c) 2023 FPT Telecom. All rights reserved.
 */
interface GridLivestreamCameraCallback {
  fun onChangeGridOneViewPager(index: Int)
  fun onSelectIndexCamera(index: Int)
  fun onRefreshFragment()
}