package com.example.roomwithaview

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "ToDoItemNotificationChannel"

    private val newWordActivityRequestCode = 1
    private val toDoListViewModel: ToDoListViewModel by viewModels {
        ToDoListViewModel.ToDoListViewModelFactory((application as ToDoListApplication).repository)
    }

    fun recyclerAdapterItemClicked(itemId:Int){
        Log.d("MainActivity","Item Clicked: " + itemId )
    }
    fun recyclerAdapterItemCheckboxClicked(itemId:Int,isChecked:Boolean){
        if (isChecked) {
            Log.d("MainActivity", "Item " + itemId + " is now complete")
        }else{
            Log.d("MainActivity", "Item " + itemId + " is not completed")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ToDoListAdapter(this::recyclerAdapterItemClicked,this::recyclerAdapterItemCheckboxClicked)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        toDoListViewModel.allToDoItems.observe(this) { todoitems ->
            // Update the cached copy of the words in the adapter.
            todoitems.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            var itemTitle:String = ""
            var itemContent:String = ""
            intentData?.getStringExtra(NewWordActivity.EXTRA_TITLE)?.let { title ->
                 itemTitle = title
            }
            intentData?.getStringExtra(NewWordActivity.EXTRA_CONTENT)?.let { content ->
                itemContent = content
            }
            val toDoItem:ToDoItem = ToDoItem(null,itemTitle,itemContent,0,0)
            toDoListViewModel.insert(toDoItem)
            createNotification(toDoItem)

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = "ToDoNotifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun createNotification(toDoItem: ToDoItem) {
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("ToDo Item: " +toDoItem.title+" is Due!")
            .setContentText(toDoItem.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(toDoItem.completed,builder.build())
        }
    }
}
