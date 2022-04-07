package tech.dojo.pay.sdk.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.R

class Dojo3DSFragment : Fragment() {

    private val viewModel: DojoCardPaymentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dojo_3ds, container, false)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(1000) //Wait for 3ds completion
            viewModel.on3DSCompleted()
        }
    }
}