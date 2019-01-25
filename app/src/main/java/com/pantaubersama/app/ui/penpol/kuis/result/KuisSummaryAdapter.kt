package com.pantaubersama.app.ui.penpol.kuis.result

import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.kuis.AnsweredQuestion
import com.pantaubersama.app.utils.HtmlTagHandler
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.spannable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_kuis_summary.*

class KuisSummaryAdapter : RecyclerView.Adapter<KuisSummaryAdapter.ViewHolder>() {

    var items: List<AnsweredQuestion> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_kuis_summary))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position + 1)
    }

    class ViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: AnsweredQuestion, questionNo: Int) {
            val answerA = item.answers.firstOrNull()
            val answerB = item.answers.getOrNull(1)
            tv_kuis_title.text = "Pertanyaan ke-%s".format(questionNo)
            tv_kuis_question.text = item.content
            tv_answer_a_team.text = answerA?.team?.title
            tv_answer_b_team.text = answerB?.team?.title

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tv_answer_a_content.text = Html.fromHtml(HtmlTagHandler.customizeListTags(answerA?.content), Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
                tv_answer_b_content.text = Html.fromHtml(HtmlTagHandler.customizeListTags(answerB?.content), Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
            } else {
                tv_answer_a_content.text = Html.fromHtml(HtmlTagHandler.customizeListTags(answerA?.content), null, HtmlTagHandler())
                tv_answer_b_content.text = Html.fromHtml(HtmlTagHandler.customizeListTags(answerB?.content), null, HtmlTagHandler())
            }

            tv_user_answer.text = spannable {
                +"Jawabanmu : "
                textColor(containerView.context.color(R.color.orange)) { +item.answered.team.title }
            }.toCharSequence()
        }
    }
}