package com.gene.zebox.task.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import androidx.core.widget.PopupWindowCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.RoomSQLiteQuery
import com.gene.zebox.App
import com.gene.zebox.LetterUtil
import com.gene.zebox.R
import com.gene.zebox.task.model.BugItem
import kotlinx.android.synthetic.main.activity_new_task.*
import kotlinx.coroutines.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class NewTaskActivity : AppCompatActivity() {
    val popAdapter by lazy { MainAdapter() }
    lateinit var mainAdapter: MainAdapter
    val popupWindow by lazy {
        PopupWindow(
            RecyclerView(this@NewTaskActivity).apply {
                this.layoutManager = LinearLayoutManager(this@NewTaskActivity)
                this.adapter = popAdapter
                setBackgroundColor(Color.GREEN)
            },
            -1, 800
        ).apply {
            this.isOutsideTouchable = true
        }
    }
    private val list by lazy { App.DB.bugDao.query() }
    private val lock by lazy { ReentrantReadWriteLock(true) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = MainAdapter()
        mainAdapter = recycler.adapter as MainAdapter
        edit.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    if (popupWindow.isShowing) {
                        popupWindow.dismiss()
                    }
                    return
                } else
                    CoroutineScope(Dispatchers.Main).launch {
                        val str = s.trim()
                        val bugs = async(Dispatchers.IO) {
                            val sql = StringBuilder(
                                "SELECT * FROM bug_item WHERE text LIKE '%$str%' OR ("
                            )
                            str.forEachIndexed { index, c ->
                                sql.append("text LIKE ")
                                    .append("'%")
                                    .append(c)
                                    .append("%'")
                                if (index != str.lastIndex) {
                                    sql.append(" AND ")
                                } else {
                                    sql.append(" )")
                                }
                            }
                            val query = RoomSQLiteQuery.acquire(sql.toString(), 0)
                            lock.read {
                                App.DB.bugDao.query(query)
                            }

                        }
                        val result = bugs.await().asList()
                        popAdapter.submitList(result) {
                            if (!popupWindow.isShowing && !result.isNullOrEmpty()) {
                                PopupWindowCompat.showAsDropDown(
                                    popupWindow,
                                    edit,
                                    0,
                                    0,
                                    Gravity.CENTER
                                )
                            }
                        }
                    }
            }
        })

        edit.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.action == KeyEvent.ACTION_UP)
            ) CoroutineScope(Dispatchers.IO).launch {
                val string = edit.text.toString()
                try {
                    lock.write {
                        App.DB.bugDao.insert(
                            BugItem(
                                string,
                                LetterUtil.getSpells(string)
                            )
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return@setOnEditorActionListener true
        }

        list.observe(this, Observer { mainAdapter.submitList(it.asList()) })
    }
}




