package kr.co.tjoeun.daily10minute_20200719

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kr.co.tjoeun.daily10minute_20200719.utils.ContextUtil

class SplashActivity : BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setValues()
        setupEvents()
    }

    override fun setValues() {
        val myHandler = Handler()

        myHandler.postDelayed({
            // 로그인을

            if (ContextUtil.getLoginUserToken(mContext) == "")
            {
                // 로그인을 아직 안한경우
                val myIntent = Intent(mContext, LoginActvity::class.java)
                startActivity(myIntent)
            }
            else
            {
                // 로그인을 해둔 경우
                val myIntent = Intent(mContext, MainActvity::class.java)
                startActivity(myIntent)
            }

            finish()

        }, 2500)
    }

    override fun setupEvents() {
    }

}