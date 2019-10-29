package vn.zuni.findpurpose.adapters

import android.view.View
import kotlinx.android.synthetic.main.layout_survey_item.view.*
import vn.zuni.findpurpose.*
import vn.zuni.findpurpose.extensions.ExtraUtils
import vn.zuni.findpurpose.extensions.L
import vn.zuni.findpurpose.extensions.startForResult
import vn.zuni.findpurpose.models.SurveyProperties

/**
 *
 * Created by namnd on 10-Jan-18.
 */
class SurveyAdapter : BaseAdapter<SurveyProperties>(emptyList(), L.layout_survey_item) {

    override fun View.bind(item: SurveyProperties, position: Int) {
        titleItem.text = item.title
        itemIndicator.text = item.id.toString()
        if (item.id == 0) {
            itemProgressBar.max = 1
            itemProgressBar.progress = if (context.isIntroRead(item.id)) 1 else 0
        } else {
            val questCount = context.getQuestionCount(item.id)
            if (questCount > 0) {
                itemProgressBar.max = questCount
                var progress = 0
                (0..(questCount - 1))
                        .map { context.getAnswer(item.id, it) }
                        .filterNot { it.isEmpty() }
                        .forEach { progress += 1 }
                itemProgressBar.progress = progress
            } else {
                itemProgressBar.progress = 0
            }
        }
    }


    override fun onItemClick(itemView: View, position: Int) {
        val item = itemList[position]
        (itemView.context as MainActivity).startForResult<SurveyActivity>(MainActivity.SURVEY_REQUEST) {
            putExtra(ExtraUtils.EXTRA_SURVEY_PROPERTIES, item)
        }
    }
}