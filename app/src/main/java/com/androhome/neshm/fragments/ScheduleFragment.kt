package com.androhome.neshm.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androhome.neshm.R
import com.androhome.neshm.adpters.ScheduleAdapter
import com.androhome.neshm.model.DateModel
import com.androhome.neshm.model.UserModel
import com.androhome.neshm.model.scheduledataModel
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 *
 */
class ScheduleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var time: ArrayList<String>
    private lateinit var adapter: ScheduleAdapter
    private lateinit var calendarView: CompactCalendarView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var dateTextView: TextView
    private var dateFormatForMonth: SimpleDateFormat = SimpleDateFormat("MMM - yyyy", Locale.getDefault())
    private lateinit var database: DatabaseReference
    private lateinit var userModel: UserModel
    private lateinit var dateModel: DateModel
    private lateinit var scheduleDataList: ArrayList<scheduledataModel>
    private var flag: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_schedule, container, false)
        initViews(view)
        listeners()
        return view
    }

    private fun listeners() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (flag && !calendarView.isAnimating) {
                        calendarView.hideCalendar()
                        flag = false
                    }
                } else if (dy < 1 && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    if (!flag && !calendarView.isAnimating) {
                        calendarView.showCalendar()
                        flag = true
                    }
                }
                Log.e("Schedule fragment", dy.toString())
                super.onScrolled(recyclerView, dx, dy)
            }
        })


        calendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                clearData()
                scheduleCallFromFirebase(dateClicked?.time as Long)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                dateTextView.text = dateFormatForMonth.format(firstDayOfNewMonth)
            }

        })
    }

    fun initViews(view: View) {
        dateModel = DateModel()
        database = FirebaseDatabase.getInstance().reference
        recyclerView = view.findViewById(R.id.schedule_list)
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        userModel = UserModel(requireContext())
        calendarView = view.findViewById(R.id.calendar_view) as CompactCalendarView
        dateTextView = view.findViewById(R.id.date_text)
        dateTextView.text = dateFormatForMonth.format(calendarView.firstDayOfCurrentMonth)
        scheduleDataList = ArrayList()
        scheduleCallFromFirebase(dateModel.presentTimestamp)
    }

    private fun scheduleCallFromFirebase(timestamp: Long) {
        var tempEmail = userModel.email.replace("@", "*").replace(".", "+")
        val userNameRef = database.child(tempEmail)
        userNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        var key: HashMap<*, *> = data.value as HashMap<*, *>
                        var tempStartTimeStamp: Long = (key.get("startTimeStamp")) as Long
                        var tempendTimeStamp: Long = (key.get("endtimeStamp")) as Long
                        if (timestamp >= tempStartTimeStamp && timestamp <= tempendTimeStamp) {
                            var tempStartTime: String = (key.get("startTime")) as String
                            var tempEndTime: String = (key.get("endTime")) as String
                            var tempSubCat: String = (key.get("subcat")) as String
                            scheduleDataList.add(scheduledataModel(tempStartTime, tempEndTime, tempSubCat))
                            adapter = ScheduleAdapter(requireContext(), scheduleDataList)
                            recyclerView.adapter = adapter
                        }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun clearData() {
        scheduleDataList.clear()
        adapter.notifyDataSetChanged() // let your adapter know about the changes and reload view.
    }
}
