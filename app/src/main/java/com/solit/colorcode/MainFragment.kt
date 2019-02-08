package com.solit.colorcode

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.solit.colorcode.databinding.FragmentMainBinding
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {
    lateinit var binding: com.solit.colorcode.databinding.FragmentMainBinding
    val mainViewModel: MainViewModel by inject()
    val clipBoard by lazy { activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.color = mainViewModel.color
        binding.callback = mainViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length == 6) {
                    mainViewModel.updateColor(s.toString())
                    binding.seekBarR.progress = Integer.parseInt(mainViewModel.color.r, 16)
                    binding.seekBarG.progress = Integer.parseInt(mainViewModel.color.g, 16)
                    binding.seekBarB.progress = Integer.parseInt(mainViewModel.color.b, 16)
                    binding.invalidateAll()
                }
            }
        }
        binding.inputText.addTextChangedListener(textWatcher)

        binding.seekBarR.setOnSeekBarChangeListener(object : OnSeekBarChangeListener(binding) {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                super.onProgressChanged(seekBar, progress, fromUser)
                mainViewModel.updateSeekBar(progress, Color.Model.R)
            }

        })

        binding.seekBarG.setOnSeekBarChangeListener(object : OnSeekBarChangeListener(binding) {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                super.onProgressChanged(seekBar, progress, fromUser)
                mainViewModel.updateSeekBar(progress, Color.Model.G)
            }
        })

        binding.seekBarB.setOnSeekBarChangeListener(object : OnSeekBarChangeListener(binding) {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                super.onProgressChanged(seekBar, progress, fromUser)
                mainViewModel.updateSeekBar(progress, Color.Model.B)
            }
        })

        mainViewModel.clipBoardLiveData.observe(viewLifecycleOwner, Observer {
            clipBoard.primaryClip = ClipData.newPlainText("coloCode", it)
            Toast.makeText(context, "$it をコピーしました", Toast.LENGTH_SHORT).show()
        })
        mainViewModel.updateColorLiveData.observe(viewLifecycleOwner, Observer {
            activity?.window?.statusBarColor = mainViewModel.color.color
        })
    }

    abstract class OnSeekBarChangeListener(val binding: FragmentMainBinding) : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            binding.invalidateAll()
        }
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.saveColor()
    }
}