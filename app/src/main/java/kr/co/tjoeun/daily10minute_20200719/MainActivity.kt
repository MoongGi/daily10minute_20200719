package kr.co.tjoeun.daily10minute_20200719

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setValues()
        setupEvents()
    }

    override fun setValues() {

        loginBtn.setOnClickListener {
            // 입력한 아이디 & 비번 받아오기
            val inputId = emailEdt.text.toString()
            val inputPw = pwEdt.text.toString()

            //서버에 로그인 요청
            ServerUtil.postRequsetLogin(mContext, inputId, inputPw, object : ServerUtil.JsonResponseHandler
            {
                override fun onResponse(json: JSONObject)
                {
                    // code에 적힌 숫자가 몇인지 확인하자
                    val codeNum = json.getInt("code")

                    val message = json.getString("message")

                    if (codeNum == 200)
                    {
                        //로그인 성공
                    }
                    else
                    {
                        //서버 통신중에 UI에 영향을 주려면 runOnUIThread 를 활용하자
                        runOnUiThread {
                            Toast.makeText(mContext, "로그인실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

    override fun setupEvents() {

    }
}
