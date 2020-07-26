package kr.co.tjoeun.daily10minute_20200719

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_ongoing_user.*
import kotlinx.android.synthetic.main.activity_view_project_detail.*
import kr.co.tjoeun.daily10minute_20200719.apdaters.ProjectApdaters
import kr.co.tjoeun.daily10minute_20200719.apdaters.UsersApdaters
import kr.co.tjoeun.daily10minute_20200719.datas.Project
import kr.co.tjoeun.daily10minute_20200719.datas.Users
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject

class ViewOngoingUserActivity : BaseActivity() {

    var mProjectId = 0
    lateinit var mProject: Project

    val mOngogingUserList = ArrayList<Users>()
    lateinit var mUsersApdater : UsersApdaters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ongoing_user)
        setValues()
    }

    override fun setValues() {
        mProjectId = intent.getIntExtra("projectId", 0)

        getOngoingUsersFromServer()

        //서버에서 받아오고 난후에 어댑터 연결
        mUsersApdater = UsersApdaters(mContext, R.layout.ongoing_user_list_item, mOngogingUserList)
        userListView.adapter = mUsersApdater

    }

    override fun setupEvents()
    {

    }

    // 진행중인 사람 명단 + 상세정보 불러오기
    fun getOngoingUsersFromServer()
    {
        ServerUtil.getRequestProjectDetailWithUsers(mContext, mProjectId, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject)
            {
                val data = json.getJSONObject("data")
                val projectObj = data.getJSONObject("project")

                mProject = Project.getProjectFromJson(projectObj)

                val ongoingUsers = projectObj.getJSONArray("ongoing_users")

                for (i in 0 until ongoingUsers.length())
                {
                    // i번째 JSONObject 를 추출
                    val projectObj = ongoingUsers.getJSONObject(i)

                    // 프로젝트 정보가 JSONObject => Project Class 형태의 인스턴스로 변환 => 목록에 추가

                    // JSON => project 로 변환
                    val user = Users.getUserFromJson(projectObj)

                    //프로젝트 목록 변수에 추가
                    mOngogingUserList.add(user)
                }

                runOnUiThread {
                    titleTxt.text = mProject.title
                    usercountTxt.text = "참여중 ${mProject.completeDays} 명"
                    mUsersApdater.notifyDataSetChanged()
                }
            }
        })
    }

}