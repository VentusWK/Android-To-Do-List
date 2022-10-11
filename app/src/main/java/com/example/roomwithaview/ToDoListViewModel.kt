package com.example.roomwithaview

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ToDoListViewModel(private val repository: ToDoItemRepository): ViewModel() {

    val allToDoItems: LiveData<List<ToDoItem>> = repository.allToDoItems.asLiveData()

    fun insert(toDoItem: ToDoItem) = viewModelScope.launch {
        repository.insert(toDoItem)
    }

    class ToDoListViewModelFactory(private val repository: ToDoItemRepository) : ViewModelProvider.Factory{
        override fun <T: ViewModel> create(modelClass: Class<T>): T{
            if(modelClass.isAssignableFrom(ToDoListViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ToDoListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }


}