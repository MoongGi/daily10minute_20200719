package kr.co.tjoeun.daily10minute_20200719

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.tjoeun.daily10minute_20200719.apdaters.ProjectApdaters
import kr.co.tjoeun.daily10minute_20200719.datas.Project
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject

class MainActvity : BaseActivity() {

    val mProjectList = ArrayList<Project>()

    lateinit var mProjectAdapter : ProjectApdaters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setValues()
        setupEvents()
    }

    override fun setValues() {
        // 서버에서 받아오는 기능 실행
        getProjectListFromServer()

        //서버에서 받아오고 난후에 어댑터 연결
        mProjectAdapter = ProjectApdaters(mContext, R.layout.project_list_view, mProjectList)
        projcetListView.adapter = mProjectAdapter
    }

    override fun setupEvents() {
        projcetListView.setOnItemClickListener { parent, view, position, id ->
          val clickedProject = mProjectList [position]
            val myIntent = Intent(mContext, ViewProjectDetailActivity::class.java)
            Log.d("눌렸나", clickedProject.id.toString())
            myIntent.putExtra("projectId", clickedProject.id)
            //몇번 프로젝트가 눌렸는지 프로젝트의 id 값만 전달
            startActivity(myIntent)
        }
    }

    //서버에서 프로젝트 목록 가져오는 함수작성
    fun getProjectListFromServer()
    {
        ServerUtil.getRequestProjectList(mContext, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")
                val projects = data.getJSONArray("projects")

                // projects JSONArray 내부의 데이터들을 추출
                // 반복분 i 로 돌면서 하나하나 가져옴
                // until (for문 0 부터 project.length 까지 가겠다 (...) 생략 )
                for (i in 0 until projects.length())
                {
                    // i번째 JSONObject 를 추출
                    val projectObj = projects.getJSONObject(i)

                    // 프로젝트 정보가 JSONObject => Project Class 형태의 인스턴스로 변환 => 목록에 추가

                    // JSON => project 로 변환
                    val project = Project.getProjectFromJson(projectObj)

                    //프로젝트 목록 변수에 추가
                    mProjectList.add(project)
                }

                // 목록이 추가 되는 사람이 => 어댑터 연결 이후 일수도 있다.
                // 어댑터가 연결되고 나서 => 내용이 추가 될수 있다.
                runOnUiThread {
                    mProjectAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}
