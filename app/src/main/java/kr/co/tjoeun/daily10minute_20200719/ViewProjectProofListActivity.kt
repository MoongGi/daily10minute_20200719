package kr.co.tjoeun.daily10minute_20200719

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_view_project_proof_list.*
import kr.co.tjoeun.daily10minute_20200719.apdaters.ProofAdapter
import kr.co.tjoeun.daily10minute_20200719.datas.Project
import kr.co.tjoeun.daily10minute_20200719.datas.Proof
import kr.co.tjoeun.daily10minute_20200719.utils.ContextUtil
import kr.co.tjoeun.daily10minute_20200719.utils.ServerUtil
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ViewProjectProofListActivity : BaseActivity() {

    // 인증 확인할 날짜를 저장 해주는 변수
    val mPfoorDate = Calendar.getInstance()

    val mProofList = ArrayList<Proof>()

    var mProjectId = 0

    lateinit var mProject : Project

    lateinit var  mProofAdapter : ProofAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_project_proof_list)
        setValues()
        setupEvents()

    }

    override fun setValues() {
        mProjectId = intent.getIntExtra("projectId", 0)

        //  2020년 6월 9일 형태로 출력
        nowDate()
        getProofListFromServer()

        mProofAdapter = ProofAdapter(mContext, R.layout.proof_list_item, mProofList)
        proofListView.adapter = mProofAdapter
    }

    override fun setupEvents()
    {
        //날짜 변경 버튼 누르면 proofDate에 저장된 날짜를 변경
        chargeProofDateBtn.setOnClickListener {
            // 날짜 선택하는 팝업창을 띄우자.
            val datePickerDialog = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // 인증 날짜를 선택된 날짜로 변경
                mPfoorDate.set(Calendar.YEAR, year)
                mPfoorDate.set(Calendar.MONTH, month)
                mPfoorDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                //변경된 인증 날짜를 화면에 반영
                nowDate()
                getProofListFromServer()

            }, mPfoorDate.get(Calendar.YEAR), mPfoorDate.get(Calendar.MONTH), mPfoorDate.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
    }

    fun getProofListFromServer()
    {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val proofStr = sdf.format(mPfoorDate.time)

        ServerUtil.getRequestProjectDetailWithProofList(mContext, mProjectId, proofStr, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {
                //날짜가 바뀌어서 인증글 목록을 새로 받아오면
                //기존에 보여주던 인증글들은 모두 날려주고, 다시 불러오자

                mProofList.clear()

                val data = json.getJSONObject("data")

                Log.d("데이터 proofs : ", data.toString())

                val projectObj = data.getJSONObject("project")

                Log.d("데이터 proofs : ", projectObj.toString())


//                이 화면에서 보는 프로젝트 정보 대입
                mProject = Project.getProjectFromJson(projectObj)

//                인증글 목록을 파싱해서 => 멤버 목록에 반영

                val proofs = projectObj.getJSONArray("proofs")

                Log.d("데이터 proofs : ", proofs.toString())

                for (i in 0 until proofs.length()) {
                    val proofObj = proofs.getJSONObject(i)
//                    JSONObject => Proof로 변환 (우리가 직접 만든 클래스 고유 기능 사용)
                    val proof = Proof.getProofFromJson(proofObj)
//                    뽑힌 Proof 타입의 변수를 목록에 추가
                    Log.d("데이터 proof : ", proof.toString())
                    mProofList.add(proof)
                }

                //프로젝트 제목 등 UI 반영 작업
                runOnUiThread {
                    proofTitleTxt.text = mProject.title
                    mProofAdapter.notifyDataSetChanged()
                }
            }

        })
    }

    fun nowDate()
    {
        val sdf = SimpleDateFormat("yyyy년 M월 d일")
        val proofStr = sdf.format(mPfoorDate.time)
        proofDateTxt.text = proofStr
    }

}