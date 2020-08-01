package kr.co.tjoeun.daily10minute_20200719.datas

import android.util.Log
import org.json.JSONObject

class Project
{
    //15.164.153.174/api/docs
    var id = 0
    var title = ""
    var imageUrl = ""
    var description = ""
    var onGoingUserCount = ""
    var proofMethod = ""
    var completeDays = 0
    var proofCount = 0

    // 내 진행 상태를 표시하는 변수 : null => 신청 혹은 참가하지 않은 상태
    var myLastStatus : String? = null



    companion object
    {
        // 적절한 JSONObject 를 재료로 받아서 => Project 객체로 뽑아주는 기능
        fun getProjectFromJson(json: JSONObject) : Project {
            val p = Project()
            Log.d("테스트",json.toString())
            // json에 들어 있는 데이터들을 이용해서 => p의 데이터로 대입
            p.id = json.getInt("id")
            p.title = json.getString("title")
            p.imageUrl = json.getString("img_url")
            p.description = json.getString("description")
            p.onGoingUserCount = json.getString("ongoing_users_count")
            p.completeDays = json.getInt("complete_days")

            // 내 진행상태는 null이 아닐때만 파싱하자.
            if (!json.isNull("my_last_status"))
            {
                // 파싱 진행
                p.myLastStatus = json.getString("my_last_status")
            }

            //null 체크 확인
            if (!json.isNull("proof_count")) {
                p.proofCount = json.getInt("proof_count")
            }


            return p
        }
    }
}