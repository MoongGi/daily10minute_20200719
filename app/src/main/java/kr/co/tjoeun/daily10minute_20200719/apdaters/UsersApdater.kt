package kr.co.tjoeun.daily10minute_20200719.apdaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kr.co.tjoeun.daily10minute_20200719.R
import kr.co.tjoeun.daily10minute_20200719.datas.Project
import kr.co.tjoeun.daily10minute_20200719.datas.Users


class UsersApdater (val mContext: Context, resId: Int, val mList: List<Users>)
    : ArrayAdapter<Users>(mContext, resId, mList)
{
    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var tempRow = convertView

        if (tempRow == null)
        {
            tempRow = inf.inflate(R.layout.ongoing_user_list_item, null)
        }

        val row = tempRow!!

        val onGogingDaysTxt = row.findViewById<TextView>(R.id.onGogingDaysTxt)
        val onGogingEmailTxt = row.findViewById<TextView>(R.id.onGogingEmailTxt)
        val onGogingNickTxt = row.findViewById<TextView>(R.id.onGogingNickTxt)
        val onGogingImg = row.findViewById<ImageView>(R.id.onGogingImg)

        val data = mList[position]

        onGogingDaysTxt.text = "도전 ${data.projectDays.toString()} 일차"
        onGogingEmailTxt.text = data.email
        onGogingNickTxt.text = data.nickName

        //프사를 띄어주자 => 0번째 프사가 제일 최근 업로드 프사 => 0번째 사진을 무조건 보여줌
        Glide.with(mContext).load(data.profileImageList[0].imageUrl).into(onGogingImg)

        return row
    }
}