package kr.co.tjoeun.daily10minute_20200719

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_project_detail.*
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
    }

    override fun setValues() {

    }

    override fun setupEvents() {
        mProjectId = intent.getIntExtra("projectId", 0)
        getProjectDetailFromServer()
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

                    // 프로젝트 정보 화면 반영 (UI)
                    runOnUiThread {
                        Glide.with(mContext).load(mProject.imageUrl).into(projectImg)
                        projectTitleTxt.text = mProject.title
                        projectDescrptionTxt.text = mProject.description
                        projectCompleteTxt.text = "${mProject.completeDays}명 도전 진행중"
                        projectAuthTxt.text = mProject.proofMethod

                    }
                }

            })
    }
}