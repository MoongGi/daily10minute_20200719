package kr.co.tjoeun.daily10minute_20200719

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity()
{
    var mContext = this

    abstract fun setValues()
    abstract fun setupEvents()
}