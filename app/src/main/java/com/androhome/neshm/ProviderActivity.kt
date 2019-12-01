package com.androhome.neshm

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androhome.neshm.model.DateModel
import com.androhome.neshm.model.UserModel
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat


class ProviderActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var startDateLayout: LinearLayout
    private lateinit var endDateLayout: LinearLayout
    private lateinit var startTimeLayout: LinearLayout
    private lateinit var endTimeLayout: LinearLayout
    private lateinit var startDateText: TextView
    private lateinit var endDateText: TextView
    private lateinit var startTimeText: TextView
    private lateinit var endTimeText: TextView
    private lateinit var name: TextView
    private lateinit var genre: TextView
    private lateinit var subCategory: TextView
    private lateinit var phoneNumer: TextView
    private lateinit var fees: TextView
    private lateinit var email: TextView
    private lateinit var userModel: UserModel
    private var startDate: String = ""
    private var endDate: String = ""
    private var startTime: String = ""
    private var endTime: String = ""
    private lateinit var dateModel: DateModel
    private lateinit var bundle: Bundle
    private var startTimestamp: Long = 0
    private var endTimestamp: Long = 0
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var database: DatabaseReference
    private var ifExists: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)
        bundle = intent.extras as Bundle
        initViews()
        listeners()
    }

    private fun listeners() {
        button.setOnClickListener(View.OnClickListener {
            if (startDate != "--/--/----" && endDate != "--/--/----" && startTime != "--:--" && endTime != "--:--") {
                if (checkData()) {
                    val tempemail = email.text.toString().replace("@", "*").replace(".", "+")
                    val userdata = userdata(
                        endDate, startDate, endTime, startTime, endTimestamp, startTimestamp, name.text.toString(),
                        phoneNumer.text.toString(), "0", "0", subCategory.text.toString()
                    )
                    val providerdata =
                        providerdata(
                            endDate,
                            startDate,
                            endTime,
                            startTime,
                            endTimestamp,
                            startTimestamp,
                            userModel.name,
                            userModel.phoneNumber,
                            "0",
                            "0",
                            userModel.address,
                            latitude,
                            longitude,
                            userModel.landmark
                        )
                    val subcat: String = subCategory.text.toString()
                    val tempUserEmail: String = userModel.email.replace("@", "*").replace(".", "+")
                    if (!ifExists) {
                        database.child(tempemail).child(tempUserEmail).setValue(providerdata)
                        database.child(tempUserEmail).child(subcat).setValue(userdata)
                        finish()
                    } else {
                        AlertDialog.Builder(this)
                            .setMessage("You have already booked a tutor in same category")
                            .setNegativeButton("Ok", null).show()
                    }
                } else {
                    Toast.makeText(this, "Fill Data correctly :(", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all the data", Toast.LENGTH_SHORT).show()
            }
        })
        startDateLayout.setOnClickListener(View.OnClickListener {
            var datePickerDialog: DatePickerDialog =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth ->
                    view.minDate = dateModel.presentTimestamp - 1000
                    if (month + 1 < 10)
                        startDate = dayOfMonth.toString() + "/0" + (month + 1) + "/" + year
                    else
                        startDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                    startDateText.text = startDate
                }, dateModel.presentDay, dateModel.presentMonth, dateModel.presentYear)
            datePickerDialog.datePicker.minDate = dateModel.presentTimestamp - 1000
            datePickerDialog.show()
        })

        endDateLayout.setOnClickListener(View.OnClickListener {
            var datePickerDialog: DatePickerDialog =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth ->
                    view.minDate = dateModel.presentTimestamp - 1000
                    if (month + 1 < 10)
                        endDate = dayOfMonth.toString() + "/0" + (month + 1) + "/" + year
                    else
                        endDate = dayOfMonth.toString() + "/0" + (month + 1) + "/" + year
                    endDateText.text = endDate
                }, dateModel.presentDay, dateModel.presentMonth, dateModel.presentYear)
            datePickerDialog.datePicker.minDate = dateModel.presentTimestamp - 1000
            datePickerDialog.show()
        })

        startTimeLayout.setOnClickListener(View.OnClickListener {
            var timePicker: TimePickerDialog =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener() { view, hourOfDay, minute ->
                    startTime = hourOfDay.toString() + ":" + minute
                    startTimeText.text = hourOfDay.toString() + ":" + minute
                }, dateModel.presentHour, dateModel.presentMinute, false)
            timePicker.show()
        })

        endTimeLayout.setOnClickListener(View.OnClickListener {
            var timePicker: TimePickerDialog =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener() { view, hourOfDay, minute ->
                    endTime = hourOfDay.toString() + ":" + minute
                    endTimeText.text = endTime
                }, dateModel.presentHour, dateModel.presentMinute, false)
            timePicker.show()
        })
    }

    private fun checkifExists(subcat: String, tempUserEmail: String) {
        val userNameRef = database.child(tempUserEmail)
        userNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(subcat)) {
                    ifExists = true
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun checkData(): Boolean {
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        startTimestamp = formatter.parse("$startDate 00:00:00").time
        endTimestamp = formatter.parse("$endDate 00:00:00").time
        val starthour: Int = startTime.split(":")[0].toInt()
//        String inDate = "01-01-1990"
        val endhour: Int = endTime.split(":")[0].toInt()
        return startTimestamp < endTimestamp && starthour < endhour
    }

    private fun initViews() {
        dateModel = DateModel()
        button = findViewById(R.id.book_button)
        startDateLayout = findViewById(R.id.linearLayoutStartDate)
        endDateLayout = findViewById(R.id.linearLayoutEndDate)
        startTimeLayout = findViewById(R.id.linearLayoutStartTime)
        endTimeLayout = findViewById(R.id.linearLayoutEndTime)
        startDateText = findViewById(R.id.start_date_text)
        endDateText = findViewById(R.id.endDateText)
        startTimeText = findViewById(R.id.startTimeText)
        endTimeText = findViewById(R.id.endTimeText)
        startTime = startTimeText.text.toString()
        endTime = endTimeText.text.toString()
        startDate = startDateText.text.toString()
        endDate = endDateText.text.toString()
        email = findViewById(R.id.emailText)
        phoneNumer = findViewById(R.id.phoneNumberText)
        name = findViewById(R.id.nameText)
        genre = findViewById(R.id.genreText)
        subCategory = findViewById(R.id.textViewSubCat)
        fees = findViewById(R.id.feesText)
        name.text = bundle.getString("name")
        genre.text = bundle.getString("genre")
        subCategory.text = bundle.getString("subCat")
        phoneNumer.text = bundle.getLong("phoneNumber").toString()
        email.text = bundle.getString("email")
        fees.text = "₹" + bundle.getString("fees")
        userModel = UserModel(context = this)
        latitude = bundle.getDouble("latitude")
        longitude = bundle.getDouble("longitude")
        database = FirebaseDatabase.getInstance().reference
        val subcat: String = subCategory.text.toString()
        val tempUserEmail: String = userModel.email.replace("@", "*").replace(".", "+")
        checkifExists(subcat, tempUserEmail)
    }
}

data class userdata(
    val endDate: String = "", val startDate: String = "", val endTime: String = "", val startTime: String = "",
    val endtimeStamp: Long = 0, val startTimeStamp: Long = 0, val tutorName: String = "", val phoneNumber: String = "",
    val acceptance: String = "", val payment: String = "", val subcat: String = ""
)

data class providerdata(
    val endDate: String = "",
    val startDate: String = "",
    val endTime: String = "",
    val startTime: String = "",
    val endtimeStamp: Long = 0,
    val startTimeStamp: Long = 0,
    val studentName: String = "",
    val phoneNumber: String = "",
    val acceptance: String = "",
    val payment: String = "",
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val landmark: String = ""
)