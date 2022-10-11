package com.example.roomwithaview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewToDoItemActivity : AppCompatActivity() {

    private lateinit var editToDoItemTitle: EditText
    private lateinit var editTodoItemContent: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_todo_item)
        editToDoItemTitle = findViewById(R.id.todo_item_title)
        editTodoItemContent = findViewById(R.id.todo_item_content)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editToDoItemTitle.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = editToDoItemTitle.text.toString()
                val content = editTodoItemContent.text.toString()
                replyIntent.putExtra(EXTRA_TITLE, title)
                replyIntent.putExtra(EXTRA_CONTENT,content)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_TITLE = "com.example.android.besttodolist.TITLE"
        const val EXTRA_CONTENT = "com.example.android.besttodolist.CONTENT"


    }
}
