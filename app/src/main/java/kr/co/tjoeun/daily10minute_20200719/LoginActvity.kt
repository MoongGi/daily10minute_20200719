package kr.co.tjoeun.daily10minute_20200719

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject

class LoginActvity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setValues()
        setupEvents()
    }

    override fun setValues() {

        signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActvity::class.java)
            startActivity(myIntent)
        }

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
                        //로그인 실패 사유를 서버가 알려주는 message에 출력
                        val fileReason = json.getString("message")

                        //서버 통신중에 UI에 영향을 주려면 runOnUIThread 를 활용하자
                        runOnUiThread {
                            Toast.makeText(mContext, "로그인실패 : ${fileReason}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

    override fun setupEvents() {

    }
}
