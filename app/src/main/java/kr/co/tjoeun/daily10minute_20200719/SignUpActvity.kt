package kr.co.tjoeun.daily10minute_20200719

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_sign_up_actvity.*

class SignUpActvity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_actvity)
        setValues()
        setupEvents()
    }

    override fun setValues()
    {
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
    }
}
