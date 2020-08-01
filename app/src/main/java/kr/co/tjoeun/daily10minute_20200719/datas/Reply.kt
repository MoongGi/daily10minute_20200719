package kr.co.tjoeun.daily10minute_20200719.datas

import org.json.JSONObject

class Reply
{
    var id = 0
    var content = ""
    lateinit var writer : Users
    var likeCount = 0

    companion object
    {
        // 적절한 JSONObject 를 재료로 받아서 => Project 객체로 뽑아주는 기능
        fun getReplyFromJson(json: JSONObject) : Reply {
            val p = Reply()

            // json에 들어 있는 데이터들을 이용해서 => p의 데이터로 대입
            p.id = json.getInt("id")
            p.content = json.getString("content")
            p.likeCount = json.getInt("like_count")

            // userJSONObject 추출하여 => User 클래스로 변환 => r.writer 에 대입
            p.writer = Users.getUserFromJson(json.getJSONObject("user"))

            return p
        }
    }
}