package kr.co.tjoeun.daily10minute_20200719

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_view_project_proof_list.*
import kotlinx.android.synthetic.main.activity_view_proof_detail.*
import kr.co.tjoeun.daily10minute_20200719.apdaters.ProofAdapter
import kr.co.tjoeun.daily10minute_20200719.apdaters.ReplyAdapter
import kr.co.tjoeun.daily10minute_20200719.datas.Project
import kr.co.tjoeun.daily10minute_20200719.datas.Proof
import kr.co.tjoeun.daily10minute_20200719.datas.Reply
import kr.co.tjoeun.daily10minute_20200719.utils.ContextUtil
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import kr.co.tjoeun.daily10minute_20200719.utils.TimeUtil
import org.json.JSONObject

class ViewProofDetailActvitiy : BaseActivity() {

    // 목록에서 넘겨주는 인증글의 id 값
    var mProofId = 0

    // 이 화면에서 보여줄 함수
    lateinit var mProof: Proof


    val mReplyList = ArrayList<Reply>()

    lateinit var  mReplyAdapter : ReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_proof_detail)
        setValues()
        setupEvents()
    }

    override fun setValues()
    {
        mProofId = intent.getIntExtra("proof_id",0)
        getProofDataFromServer()

        mReplyAdapter = ReplyAdapter(mContext, R.layout.proof_list_item, mReplyList)
        replyListView.adapter = mReplyAdapter
    }

    override fun setupEvents()
    {
        postReplyBtn.setOnClickListener {

            val edit = replyContentEdt.text

            if (edit.length < 5)
            {
                Toast.makeText(mContext, "댓글은 최소 5자 이상이어야 합니다." , Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }
            else
            {
                setPostReply()
            }
        }
    }

    fun getProofDataFromServer()
    {
        // GET - /project_proof/id 값 api 호출
        ServerUtil.getRequestProofDetail(mContext, mProofId, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")
                val proof = data.getJSONObject("project")

                // project
                mProof = Proof.getProofFromJson(proof)

                // 같이 담겨오는 댓글 목록을 처리 => 기존에 담겨있던 댓글들을 삭제하고 처리 진행
                val replies = proof.getJSONArray("replies")

                mReplyList.clear()

                for (i in 0 until replies.length()) {
                    // 댓글 JSONObejct 추출 => Reply 형태로 변환 => mReplayList 에 추가
                    mReplyList.add(Reply.getReplyFromJson(replies.getJSONObject(i)))
                }

                runOnUiThread {
                    writerNickNameTxt.text = mProof.user.nickName
                    createAtTxt.text = TimeUtil.getTimeAgoStringFromCalendar(mProof.proofTime)
                    contentTxt.text = mProof.content
                    mReplyAdapter.notifyDataSetChanged()

                    //댓글을 불러오면 => 맨 밑으로 리스트뷰를 끌어 내려주자.
                    //만약에 댓글이 댓글이 10개 => 0~9 로 이동 : 9번째로 이동
                    replyListView.smoothScrollToPosition(mReplyList.size - 1)
                }
            }
        })
    }

    fun setPostReply()
    {
        ServerUtil.postRequestProofReply(mContext, mProofId, replyContentEdt.text.toString(), object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                getProofDataFromServer()

                replyContentEdt.setText("")
            }
        })
    }
}