package com.djambulat69.developerslife

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.djambulat69.developerslife.api.DevLifePost
import com.djambulat69.developerslife.databinding.FragmentTabBinding


private const val ARG_CATEGORY = "category"
private const val TAG = "TabFragment"

class TabFragment : Fragment() {

    private var category: String? = null

    private var _binding: FragmentTabBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TabFragmentViewModel by viewModels{
        TabFragmentViewModel.Factory(category!!, requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.descriptionTextView.text = category

        setupObserver()

        with(binding) {
            nextButton.setOnClickListener {
                viewModel.nextPost()
            }
            prevButton.setOnClickListener {
                viewModel.prevPost()
            }
            postRetryButton.setOnClickListener {
                viewModel.post.removeObservers(viewLifecycleOwner)
                viewModel.retryPost()
                setupObserver()
                hideErrorUI()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideUI(){
        with(binding) {
            val gone = View.GONE
            gifImageView.visibility = gone
            descriptionTextView.visibility = gone
            nextButton.visibility = gone
            if (viewModel.currentPost == 0 && viewModel.currentPage == 0) {
                prevButton.visibility = gone
            }
            postRetryButton.visibility = gone
            postErrorTextView.visibility = gone
        }
    }
    private fun showUI(){
        with(binding) {
            val visible = View.VISIBLE
            gifImageView.visibility = visible
            gifProgressBar.visibility = visible
            descriptionTextView.visibility = visible
            nextButton.visibility = visible
            if (viewModel.currentPost != 0 || viewModel.currentPage != 0) {
                prevButton.visibility = visible
            }
        }
    }

    private fun showErrorUI(){
        with(binding){
            val visible = View.VISIBLE
            postRetryButton.visibility = visible
            postErrorTextView.visibility = visible
        }
    }
    private fun hideErrorUI(){
        with(binding){
            val gone = View.GONE
            postRetryButton.visibility = gone
            postErrorTextView.visibility = gone
        }
    }

    private fun loadGif(url: String){
        Glide.with(this)
                .load(url)
                .addListener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?,
                                              model: Any?, target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?,
                                                 model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?,
                                                 isFirstResource: Boolean): Boolean {
                        binding.gifProgressBar.visibility = View.GONE
                        return false
                    }

                })
                .into(binding.gifImageView)
    }

    private fun setupObserver(){
        binding.postProgressBar.visibility = View.VISIBLE
        viewModel.post.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading<DevLifePost> -> {
                    hideUI()
                    binding.postProgressBar.visibility = View.VISIBLE
                }
                is Resource.Success<DevLifePost> -> {
                    Log.d(TAG, "got data: ${it.data}")
                    showUI()
                    binding.postProgressBar.visibility = View.GONE

                    it.data?.let { post ->
                        loadGif(post.gifURL)
                        binding.descriptionTextView.text = post.description
                    }
                }
                is Resource.Error<DevLifePost> -> {
                    Log.d(TAG, "Error: ${it.message}")
                    hideUI()
                    showErrorUI()
                    with(binding) {
                        postErrorTextView.text = it.message
                        postProgressBar.visibility = View.GONE
                        if (viewModel.currentPost != 0 || viewModel.currentPage != 0) {
                            prevButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    companion object {

        fun newInstance(category: String) =
            TabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, category)
                }
            }
    }
}