package kr.co.tjoeun.daily10minute_20200719.datas

import org.json.JSONObject

class Users
{
    var id = 0
    var email = ""
    var nickName = ""
    var projectDays = 0
    var imgUrl = ""

    // 해당 사용자가 갖고 있는 프사들을 저장할 목록
    val profileImageList = ArrayList<ProfileImage>()

    companion object {
        // jsonObject를 적당히 넣어주면 => Users 로 변환하는 기능

        fun getUserFromJson(json: JSONObject) : Users {

            val u = Users()

            u.id = json.getInt("id")
            u.email = json.getString("email")
            u.nickName = json.getString("nick_name")

            //nullchk
            //projectDay는 특정 상황에서만 표시
            if (!json.isNull("project_days"))
            {
                u.projectDays = json.getInt("project_days")
            }

            //  사용자가 가지고 있는 프사 목록 채우기
            val profile_images = json.getJSONArray("profile_images")

            for (i in 0 until profile_images.length())
            {
                // 프사 JSONObject 가져오기
                val profile_image = profile_images.getJSONObject(i)

                // JSONObject => ProfileImage 변환후 목록에 추가
                val profileImage = ProfileImage.getProfileImageFromJson(profile_image)

                u.profileImageList.add(profileImage)
            }

            return u
        }
    }
}