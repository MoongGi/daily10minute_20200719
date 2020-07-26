package kr.co.tjoeun.daily10minute_20200719

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_project_detail.*
import kr.co.tjoeun.daily10minute_20200719.apdaters.ProjectApdaters
import kr.co.tjoeun.daily10minute_20200719.datas.Project
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject

class ViewProjectDetailActivity : BaseActivity() {

    var mProjectId = 0

    // 이 화면에서 보여줄 함수
    lateinit var mProject: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_project_detail)
        setupEvents()
        setValues()
    }

    override fun setValues() {
        mProjectId = intent.getIntExtra("projectId", 0)
        getProjectDetailFromServer()

    }

    override fun setupEvents() {
        // 다른 사람들의 참여 인증 확인하는 버튼
        viewOtherProofBtn.setOnClickListener {
            // 인증 확인 화면 진입 => 어떤 프로젝트에 대해서 인지 id 값만 전달
            val myIntnet = Intent(mContext, ViewProjectProofListActivity::class.java)
            myIntnet.putExtra("projectId", mProjectId)
            startActivity(myIntnet)
        }

        // 프로젝트 별 참여 명부 화면으로 이동 => 명단 확인
        viewOngoingUserBtn.setOnClickListener {
            val myIntnet = Intent(mContext, ViewOngoingUserActivity::class.java)
            myIntnet.putExtra("projectId", mProjectId)
            startActivity(myIntnet)
        }

        joinProjectBtn.setOnClickListener {
            val alert = AlertDialog.Builder(mContext)
                .setTitle("프로젝트 참여 신청")
                .setMessage("정말 프로젝트에 참여 하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    //실제 서버 참여하기
                    ServerUtil.postRequestProjectJoin(mContext, mProjectId, object : ServerUtil.JsonResponseHandler{
                        override fun onResponse(json: JSONObject) {
                            val code = json.getInt("code")

                            if (code == 200)
                            {
                                val data = json.getJSONObject("data")
                                val projectObj = data.getJSONObject("project")

//                        갱신된 프로젝트 정보를 새로 대입
                                mProject = Project.getProjectFromJson(projectObj)

//                        별도 기능으로 만들어진 Ui 데이터 세팅 기능 실행
                                setProjectDataToUI()

                                //서버가 최신 정보가 못내려줌 => 강제로 다시 불러오자. (임시방편)
                                getProjectDetailFromServer()

                                runOnUiThread {
                                    Toast.makeText(mContext, "프로젝트에 참여 완료 했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else {
                                val message = json.getString("message")

                                runOnUiThread {
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    })
                })
                .setNegativeButton("아니요", null)
                .show()
        }
    }

    fun getProjectDetailFromServer()
    {
        ServerUtil.getRequestProjectDetail(mContext, mProjectId, object : ServerUtil.JsonResponseHandler
            {
                override fun onResponse(json: JSONObject) {
                    val data = json.getJSONObject("data")
                    val projectObj = data.getJSONObject("project")

                    // project
                    mProject = Project.getProjectFromJson(projectObj)

                    setProjectDataToUI()
                }

            })
    }

    // mProject에 세팅된 데이터를 화면에 반영하는 기능 별도 분리
    fun setProjectDataToUI()
    {
        // 프로젝트 정보 화면 반영 (UI)
        runOnUiThread {
            Glide.with(mContext).load(mProject.imageUrl).into(projectImg)
            projectTitleTxt.text = mProject.title
            projectDescrptionTxt.text = mProject.description
            projectCompleteTxt.text = "${mProject.completeDays}명 도전 진행중"
            projectAuthTxt.text = mProject.proofMethod

            // 참여 중인지 아닌지 확인후 보여지는 버튼이 다르게 하자.

            if (mProject.myLastStatus == "ONGOING")
            {
                // 참여중 버튼들 표시, 참가 버튼 숨기기
                onGogingButtonLayout.visibility = View.VISIBLE
                joinProjectBtn.visibility = View.GONE
            }
            else
            {
                onGogingButtonLayout.visibility = View.GONE
                joinProjectBtn.visibility = View.VISIBLE
            }

        }
    }
}