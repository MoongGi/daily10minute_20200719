package kr.co.tjoeun.daily10minute_20200719.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ServerUtil
{
    // 서버의 응답이 돌아올때 실행할 내용을 담아둘 목적의 인터페이스
    interface JsonResponseHandler {
        fun onResponse(json: JSONObject)
    }

    //ServerUtil클래스의 Static 함수 / 변수들을 저장할 공간.
    companion object {

        //접근할 서버를 담는 변수
        private val BASE_URL = "http://15.164.153.174"

        //로그인 요청을 해주는 기능
        fun postRequsetLogin(context:Context, email:String, pw:String, handler: JsonResponseHandler?)
        {
            val client = OkHttpClient()

            //어떤 주소로 가야하는지 URL 을 후스트 주소 + 기능주소의 결합
            val urlString = "${BASE_URL}/user"

            //서버에 실제로 전달할 데이터를 폼바디에 담아주자. (POST - 폼 데이터)
            val formData = FormBody.Builder()
                .add("email", email)
                .add("pw", pw)
                .build()

            //서버에 요청할 모든 정보를 담아주는 request 변수 생성
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                //.header() API헤더 정보 필요할경우 첨부
                .build()

            //완성한 Request를 가지고 실제로 서버로 출발
            //서버는 request를 받으면 => Response를 내려줌.
            //그에 대한 처리도 필요함
            client.newCall(request).enqueue(object : Callback
            {
                override fun onFailure(call: Call, e: IOException)
                {
                    //서버에 연결 자체를 실패한 경우
                }

                override fun onResponse(call: Call, response: Response)
                {
                    //서버에 연결 성공 했을 경우
                    val bodyString = response.body!!.string()

                    //String을 => 분석하기 쉬운 Json 클래스로 변환.
                    val json = JSONObject(bodyString)
                    Log.d("서버응답", json.toString())

                    //완성된 json을 액티비티에서 처리하도록 전달
                    handler?.onResponse(json)
                }
            })
        }
    }
}