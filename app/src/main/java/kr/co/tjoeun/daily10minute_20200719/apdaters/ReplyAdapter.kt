package kr.co.tjoeun.daily10minute_20200719.apdaters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.reply_list_item.view.*
import kr.co.tjoeun.daily10minute_20200719.R
import kr.co.tjoeun.daily10minute_20200719.datas.Reply
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject

class ReplyAdapter (val mContext: Context, resId: Int, val mList: List<Reply>)
    : ArrayAdapter<Reply>(mContext, resId, mList)
{
    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var tempRow = convertView

        if (tempRow == null)
        {
            tempRow = inf.inflate(R.layout.reply_list_item, null)
        }

        val row = tempRow!!

        val writerNickNameTxt = row.findViewById<TextView>(R.id.writerNickNameTxt)
        val contentDetailTxt = row.findViewById<TextView>(R.id.contentDetailTxt)
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)

        val data = mList[position]
        writerNickNameTxt.text = data.writer.nickName
        contentDetailTxt.text = data.content


        likeBtn.setOnClickListener {
            ServerUtil.postRequestLikeReply(mContext, data.id, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    val message = json.getString("message")
                    val uiHandler = Handler(Looper.getMainLooper())

                    uiHandler.post {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        notifyDataSetChanged()
                    }

                }

            })
        }

//        if
//        {
//            likeBtn.text = "좋아요 취소"
//            likeBtn.setBackgroundResource(R.drawable.red_border_color)
//            likeBtn.setTextColor(mContext.resources.getColor(R.color.naverRed))
//
//        }
//        else
//        {
//            likeBtn.text = "좋아요"
//            likeBtn.setBackgroundResource(R.drawable.gray_border_box)
//            likeBtn.setTextColor(mContext.resources.getColor(R.color.black))
//        }

        return row
    }
}