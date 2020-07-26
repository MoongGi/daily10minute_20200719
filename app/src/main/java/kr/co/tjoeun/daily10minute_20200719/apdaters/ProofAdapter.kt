package kr.co.tjoeun.daily10minute_20200719.apdaters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import kr.co.tjoeun.daily10minute_20200719.R
import kr.co.tjoeun.daily10minute_20200719.datas.Project
import kr.co.tjoeun.daily10minute_20200719.datas.Proof
import kr.co.tjoeun.daily10minute_20200719.utils.ContextUtil
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import kr.co.tjoeun.daily10minute_20200719.utils.TimeUtil
import org.json.JSONObject
import java.text.SimpleDateFormat

class ProofAdapter (val mContext: Context, resId: Int, val mList: List<Proof>)
    : ArrayAdapter<Proof>(mContext, resId, mList)
{
    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var tempRow = convertView

        if (tempRow == null)
        {
            tempRow = inf.inflate(R.layout.proof_list_item, null)
        }

        val row = tempRow!!

        val proofTitleImg = row.findViewById<ImageView>(R.id.proofTitleImg)
        val proofTitleTxt = row.findViewById<TextView>(R.id.proofTitleTxt)
        val proofContentTxt = row.findViewById<TextView>(R.id.proofContentTxt)
        val proofContentImg = row.findViewById<ImageView>(R.id.proofContentImg)
        val proofNickNameTxt  = row.findViewById<TextView>(R.id.proofNickNameTxt)
        val proofTimeTxt = row.findViewById<TextView>(R.id.proofTimeTxt)
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)
        val replyBtn = row.findViewById<Button>(R.id.replyBtn)

        val data = mList[position]

        proofContentTxt.text = data.content
        proofNickNameTxt.text = data.user.nickName
        Glide.with(mContext).load(data.user.profileImageList[0].imageUrl).into(proofTitleImg)

        proofTimeTxt.text = TimeUtil.getTimeAgoStringFromCalendar(data.proofTime)

        val sdf = SimpleDateFormat("yyyy년 M월 d일 a h시")
        proofTimeTxt.text = sdf.format(data.proofTime.time)

        //그림이 있느냐 없느냐를 구별함 => How? data의 이미지 주소목록의 크기값을 확인한다.
        if(data.imageUrlList.size == 0)
        {
            //그림이 첨부가 안된경우
            proofContentImg.visibility = View.GONE
        }
        else
        {
            // 한장 이상의 그림이 있는경우
            proofContentImg.visibility = View.VISIBLE

            Glide.with(mContext).load(data.imageUrlList[0]).into(proofContentImg)
        }

        //좋아요, 답글 버튼 문구 수정
        likeBtn.text = "좋아요 ${data.likeCount}개"
        replyBtn.text = "답글 ${data.replyCount}개"

        likeBtn.setOnClickListener {
            ServerUtil.postRequestLikeProof(mContext, data.id, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                    val message = json.getString("message")

                    //변경된 좋아요 갯수 / 내 좋아요 여부를 data의 변수에 반영
                    val dataObj = json.getJSONObject("data")
                    val like = dataObj.getJSONObject("like")

                    data.likeCount = like.getInt("like_count")
                    data.myLike = like.getBoolean("my_like")

                    val myHandler = Handler(Looper.getMainLooper())
                    //어댑터에는 runOnUiThread가 없다.
                    //그래도 어떻게든 UIThread를 UI반영을 해야 앱이 돌아감.
                    myHandler.post {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

                        //리스트 어댑터 새로고침
                        //얘자체가 어댑터라
                        notifyDataSetChanged()
                    }
                }
            })
        }

        return row
    }
}