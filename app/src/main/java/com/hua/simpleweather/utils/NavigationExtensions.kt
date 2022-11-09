package com.hua.simpleweather.utils

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.navigation.NavController

fun NavController.setStartDestination(
    @NavigationRes navGraphId: Int,
    startDestinationId: Int,
    args: Bundle ?= null
) {
    val graph = navInflater.inflate(navGraphId)
    graph.setStartDestination(startDestinationId)
    setGraph(graph, args)
}