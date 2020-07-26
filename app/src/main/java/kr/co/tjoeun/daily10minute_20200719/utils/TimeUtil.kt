package kr.co.tjoeun.daily10minute_20200719.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtil
{
    companion object
    {
        //시간의 재료로 넣으면 시차를 보정해서 상황에 맞는 문구로 알려줌
        fun getTimeAgoStringFromCalendar(cal: Calendar) : String
        {
            // cal 을 => 핸드폰에 설정된 시간에 맞는 시차 계산
            // cal 내부에는, 어느 시간대 (서울, 영국, 뉴옥) 인지 정보가 저장되어 있다.
            // 시간 값만 서버에서 준 그대로 받아 적어둔 상태

            // 핸드폰의 시간대와 => 표전 시간대의 시차를 구해서
            // cal의 시간을 보정 해주자.
            val myPhoneTimeZone = cal.timeZone // 한국폰 : 한국 시간대 , 영국 : 영국 시간대

            // 표준 시간대 (UTC/ GMT + 00:00) 과의 몇시간 차이나는지 계산
            // rawOffset : 시차를 밀리초로 단위까지 기록. => 시간 단위로 변환
            // (1000 초) / (60 분) / (60 시)
            val timeOffSet = myPhoneTimeZone.rawOffset / 1000 / 60 / 60
            //재료로 들어온 cal에게 시간값을 증가시키자
            cal.add(Calendar.HOUR, timeOffSet)

            //양식을 가공해서 String으로 리턴
            val sdf = SimpleDateFormat("yyyy년 M월 d일 a h시 m분")

            return sdf.format(cal.time)
        }
    }
}