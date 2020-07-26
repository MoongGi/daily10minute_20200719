package kr.co.tjoeun.daily10minute_20200719.datas

import org.json.JSONObject

class Project
{
    //15.164.153.174/api/docs
    var id = 0
    var title = ""
    var imageUrl = ""
    var description = ""
    var completeDays = ""
    var proofMethod = ""
    var ongoingUserCount = 0

    companion object
    {
        // 적절한 JSONObject 를 재료로 받아서 => Project 객체로 뽑아주는 기능
        fun getProjectFromJson(json: JSONObject) : Project {
            val p = Project()

            // json에 들어 있는 데이터들을 이용해서 => p의 데이터로 대입
            p.id = json.getInt("id")
            p.title = json.getString("title")
            p.imageUrl = json.getString("img_url")
            p.description = json.getString("description")
            p.completeDays = json.getString("ongoing_users_count")

           return p
        }
    }
}