package com.example.androidarchitecturepatterns.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidarchitecturepatterns.R
import com.example.androidarchitecturepatterns.adapter.UpComingAdapter
import com.example.androidarchitecturepatterns.databinding.FragmentListBinding
import com.example.androidarchitecturepatterns.model.UpcomingProduct
import com.example.androidarchitecturepatterns.repository.AppRepository
import com.example.androidarchitecturepatterns.utils.Resource
import com.example.androidarchitecturepatterns.utils.SharedPreferencesUtil
import com.example.androidarchitecturepatterns.viewmodel.LoginViewModel
import com.example.androidarchitecturepatterns.viewmodel.UpComingViewModel
import com.example.androidarchitecturepatterns.viewmodel.ViewModelProviderFactory


class ListFragment : Fragment() {
    private lateinit var binding : FragmentListBinding
    private lateinit var adapter : UpComingAdapter
    private lateinit var upComingViewModel: UpComingViewModel
    private var modelList: ArrayList<UpcomingProduct> = ArrayList()
    
    var userID =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        userID = SharedPreferencesUtil().getUserId(requireContext())!!
    }

    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        upComingViewModel = ViewModelProvider(requireActivity(), factory).get(UpComingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_list, container, false)

        binding = FragmentListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeProduct(userID)
    }

    private fun initializeProduct(userID: String) {

        upComingViewModel.upComingFun(userID)

        upComingViewModel.upComingResponse.observe(requireActivity(),{event ->
            event.getContentIfNotHandled()?.let { response ->
                when(response)
                {
                    is Resource.Success ->{
                        response.data?.let { upComingResponse ->
                            modelList = ArrayList(upComingResponse.upcomming_products)
                            adapter = UpComingAdapter(requireContext(),modelList)
                            val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
                            binding.recTab.layoutManager = mLayoutManager
                            binding.recTab.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(0), true))
                            binding.recTab.itemAnimator = DefaultItemAnimator()
                            binding.recTab.adapter = adapter
                        }

                    }

                    is Resource.Error -> {
                        response.message?.let { message ->

                        }
                    }

                    is Resource.Loading -> {
                    }
                }
            }
        })

    }


    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }



}