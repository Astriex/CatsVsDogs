package com.astriex.catsvsdogs.ui.fragments.home.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.databinding.FragmentResultsBinding
import com.astriex.catsvsdogs.db.Vote
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultsFragment : Fragment(R.layout.fragment_results) {
    private var _binding: FragmentResultsBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModels<ResultsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResultsBinding.bind(view)
    }

    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.IO) {
            getVotes()
        }
    }

    @InternalCoroutinesApi
    private suspend fun getVotes() {
        val flatList: List<Vote> = viewModel.loadVotes().flattenToList()
        drawPie(flatList[0], flatList[1])
    }

    private fun drawPie(catVotes: Vote, dogVotes: Vote) {
        val config = AnimatedPieViewConfig()
        config.startAngle((-90).toFloat())// Starting angle offset
            .addData(
                SimplePieInfo(
                    catVotes.count.toDouble(),
                    getColor(requireContext(), R.color.pieChartCats),
                    "cats"
                )
            )//Data (bean that implements the IPieInfo interface)
            .addData(
                SimplePieInfo(
                    dogVotes.count.toDouble(),
                    getColor(requireContext(), R.color.pieChartDogs),
                    "dogs"
                )
            )
            .pieRadius(220.0f)
            .drawText(true)
            .textSize(50.toFloat())
            .canTouch(true)
            .strokeWidth(60)
            .duration(2000);// draw pie animation duration

        binding.pieView.apply {
            applyConfig(config)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private suspend fun <T> Flow<List<T>>.flattenToList() = flatMapConcat {
    it.asFlow()
}.toList()
