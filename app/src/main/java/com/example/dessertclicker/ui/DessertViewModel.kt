package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    //All desserts

    val allDesserts: List<Dessert> = Datasource.dessertList

    /**
     * Determine which dessert to show.
     */
    fun determineDessertToShow(
        dessertsSold: Int,
    ): Dessert {
        var dessertToShow = allDesserts.first()
        for (dessert in allDesserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }
        return dessertToShow
    }

    fun updateAndShowNextDessert() {  // Update the revenue
        val dessertToShow = determineDessertToShow(_uiState.value.dessertsSold)
        _uiState.update { currentState ->
            currentState.copy(
                revenue = currentState.currentDessertPrice,
                dessertsSold = currentState.dessertsSold + 1,
                currentDessertImageId = dessertToShow.imageId,
                currentDessertPrice = dessertToShow.price
            )

        }
    }
}