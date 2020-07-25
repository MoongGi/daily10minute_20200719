package kr.co.tjoeun.daily10minute_20200719.apdaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kr.co.tjoeun.daily10minute_20200719.R
import kr.co.tjoeun.daily10minute_20200719.datas.Project

class ProjectApdaters (val mContext: Context, resId: Int, val mList: List<Project>)
    : ArrayAdapter<Project>(mContext, resId, mList)
{
    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var tempRow = convertView

        if (tempRow == null)
        {
            tempRow = inf.inflate(R.layout.project_list_view, null)
        }

        val row = tempRow!!

        val projectTitleTxt = row.findViewById<TextView>(R.id.projectTitleTxt)
        val projectdiscriptionTxt = row.findViewById<TextView>(R.id.projectdiscriptionTxt)
        val projectImage = row.findViewById<ImageView>(R.id.projectImage)

        val data = mList[position]

        return super.getView(position, convertView, parent)
    }
}