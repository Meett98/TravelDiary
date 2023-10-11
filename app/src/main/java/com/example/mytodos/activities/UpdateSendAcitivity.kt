package com.example.mytodos.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mytodos.databinding.ActivityUpdateSendAcitivityBinding
import com.example.mytodos.db.TodoDAO
import com.example.mytodos.db.TodoDatabase
import com.example.mytodos.entity.Entity
import com.example.mytodos.repository.ToDoRepository
import com.example.mytodos.viewmodel.MainViewModel
import com.example.mytodos.viewmodel.MainViewModelFactory


class UpdateSendAcitivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateSendAcitivityBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var database: TodoDatabase
    private lateinit var username:String
    private lateinit var password:String
    private lateinit var toDoRepository: ToDoRepository
    private lateinit var todoDAO: TodoDAO
    private lateinit var todoTitle:String
    private lateinit var btn:String
    private val REQUEST_CONTACT_PICKER = 1
    private var id:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateSendAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database= TodoDatabase.getDatabase(this)
        todoDAO = TodoDatabase.getDatabase(this).todoDAO()
        toDoRepository = ToDoRepository(todoDAO)
        mainViewModel= ViewModelProvider(this, MainViewModelFactory(toDoRepository)).get(
            MainViewModel::class.java)


        val intent = intent     //getIntent()
        username = intent.getStringExtra("USERNAME").toString()
        password = intent.getStringExtra("PASSWORD").toString()
        id=intent.getIntExtra("ID",0)
        todoTitle = intent.getStringExtra("TITLE").toString()
        btn = intent.getStringExtra("BUTTON_TEXT").toString()



        binding.task.setText(todoTitle)


        binding.updateButton.setOnClickListener {
            todoTitle=binding.task.text.toString()
            val entity: Entity = Entity(id,todoTitle,username,password)
            mainViewModel.updateTodo(entity)
            Toast.makeText(this, "Todo Updated", Toast.LENGTH_LONG).show()
            val iNext= Intent(this, MainActivity::class.java)
            startActivity(iNext)
        }

        binding.sendButton.setOnClickListener{

            //openContactPicker()
            sendmessage(todoTitle)

        }

    }

    private fun sendmessage(messageText:String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_TEXT, messageText)

        val chooserIntent = Intent.createChooser(sendIntent, "Send Message via...")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (sendIntent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
        } else {
            Toast.makeText(this, "No messaging apps found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openContactPicker() {
        val contactPickerIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(contactPickerIntent, REQUEST_CONTACT_PICKER)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("IsitgoinginthisCondition","YESINAbove")

        if (requestCode == REQUEST_CONTACT_PICKER && resultCode == RESULT_OK) {
            // The user has selected a contact.
            val contactUri: Uri? = data?.data
            val phoneNumber: String? = contactUri?.let { getContactPhoneNumber(it) }
            Log.d("IsitgoinginthisCondition","YESInMiddle")

            if (phoneNumber != null) {
                // Send the SMS message.
                Log.d("IsitgoinginthisCondition","YES")
                sendSMS(phoneNumber)
            } else {
                Toast.makeText(this, "Selected contact has no phone number.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getContactPhoneNumber(contactUri: Uri): String? {
        var phoneNumber: String? = null
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val cursor = contentResolver.query(contactUri, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val phoneNumberColumnIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            phoneNumber = cursor.getString(phoneNumberColumnIndex)
            cursor.close()
        }
        return phoneNumber
    }

    private fun sendSMS(phoneNumber: String) {
        // Implement logic to send an SMS.
        // This could involve using an SMS manager or sending an SMS intent.
        // Here, we print the message to the console.
        Toast.makeText(this,"SMS Sent to the $phoneNumber",Toast.LENGTH_SHORT).show()
        Log.d("MessageIntent","SMS Sent to the $phoneNumber")
    }
}