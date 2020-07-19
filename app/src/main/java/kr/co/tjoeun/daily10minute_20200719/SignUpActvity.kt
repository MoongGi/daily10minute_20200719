package kr.co.tjoeun.daily10minute_20200719

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_sign_up_actvity.*
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject
import java.util.*

class SignUpActvity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_actvity)
        setValues()
        setupEvents()
    }

    override fun setValues()
    {

    }

    fun checkPasswords()
    {
        //입력한 비밀번호
        val inputPw = pwEdt.text.toString()

        //글자수 => 0자 : 비밀번호를 입력해주세요.
        //1~7자 : 비밀번호가 너무 짧습니다.
        //8자 이상 사용해도 좋은 비밀번호 입니다.

        if (inputPw.length == 0 || inputPw.isEmpty())
        {
            pwCheckResult.text = "비밀번호를 입력해주세요."
        }
        else if (inputPw.length < 8)
        {
            pwCheckResult.text = "비밀번호가 너무 짧습니다."
        }
        else
        {
            pwCheckResult.text = "사용해도 좋은 비밀번호 입니다."
        }
    }

    override fun setupEvents() {

        //회원 가입 버튼 누르면 빈 입력값 있는지 확인후
        //괜찮으면 실제로 데이터 요청

        okBtn.setOnClickListener {

            val inputEmail = emailEdt.text.toString()
            val inputPw = pwEdt.text.toString()
            val inputNickName = nickEdt.text.toString()

            if (inputEmail.isEmpty()) {
                Toast.makeText(mContext, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                // 가입 절차 강제 종료
                return@setOnClickListener
            } else if (!inputEmail.contains("@")) {
                Toast.makeText(mContext, "이메일 양식이 아닙니다. ", Toast.LENGTH_SHORT).show()
                // 가입 절차 강제 종료
                return@setOnClickListener
            }

            if (inputPw.length < 8) {
                Toast.makeText(mContext, "비밀번호가 너무 짧습니다. ", Toast.LENGTH_SHORT).show()
                // 가입 절차 강제 종료
                return@setOnClickListener
            }

            if (inputNickName.isEmpty())
            {
                Toast.makeText(mContext, "닉네임을 입력해주세요. ", Toast.LENGTH_SHORT).show()
                // 가입 절차 강제 종료
                return@setOnClickListener
            }

            //이메일 / 비번 / 닉네임 검사를 모두 통과한 상황
            //서버에 실제로 가입 신청
            ServerUtil.putRequsetSignUp(mContext, inputEmail, inputPw, inputNickName, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    // 서버가 알려주는 코드값이 200이면 가입 성공처리
                    // 아니라면 가입 실패처ㅣ

                    val code = json.getInt("code")
                    if (code == 200)
                    {
                        runOnUiThread {
                            Toast.makeText(mContext,"회원가입 성공",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    else
                    {
                        // 가입 실패 상황 => 왜 실패했는지 출력
                        val message = json.getString("message")
                        runOnUiThread {
                            Toast.makeText(mContext, "회원가입 실패 :${message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            })

        }

        //EditText (비번 입력칸) 에 글자를 타이핑하는 이벤트 체크
        pwEdt.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
                // TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswords()
            }
        })
    }
}
