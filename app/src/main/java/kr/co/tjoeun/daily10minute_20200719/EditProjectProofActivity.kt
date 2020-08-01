package kr.co.tjoeun.daily10minute_20200719

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_project_proof.*
import kotlinx.android.synthetic.main.activity_view_proof_detail.*
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject

class EditProjectProofActivity : BaseActivity() {

    //어떤 프로젝트에 대해 인증 할지 구별하는 id값
    var mProjectId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_project_proof)
        setValues()
        setupEvents()
    }

    override fun setValues() {
        var title = intent.getStringExtra("title")
        proofProjectNameTxt.text = title
        mProjectId = intent.getIntExtra("projectId", 0)
    }

    override fun setupEvents() {
        proofBtn.setOnClickListener {
            var message = messageEdit.text.toString()

            if (message.length < 5)
            {
                Toast.makeText(mContext, "메세지는 5글자 이상 입력 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val alert = AlertDialog.Builder(mContext)
                .setTitle("오늘의 인증 하기")
                .setMessage("인증글은 하루에 한번만 올릴 수 있습니다. 정말 인증 하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    // 의사가 확인 되었으니 => 서버에 게시글을 올리자
                    setPostProjectProof()
                })
                .setNegativeButton("취소", null)
        }
    }

    fun setPostProjectProof()
    {
        ServerUtil.postRequestProjectProof(mContext, mProjectId, replyContentEdt.text.toString(), object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {
                Log.d("결과","@${json}")

                //등록 성공 실패 여부 구별
                val code = json.getInt("code")

                if (code == 200)
                {
                    // 등록 성공 케이스 => 토스트로 격려 메세지
                    // 완주 했다면 격려x, 축하 => 완주 여부 파악 필요
                    val isProjectComplete = json.getBoolean("is_project_complete")

                    runOnUiThread {
                        if (isProjectComplete)
                        {
                            Toast.makeText(mContext, "축하합니다.", Toast.LENGTH_SHORT).show()
                        }
                        else
                        {

                        }
                    }
                }
                else
                {
                    val message = json.getString("message")

                    runOnUiThread {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}