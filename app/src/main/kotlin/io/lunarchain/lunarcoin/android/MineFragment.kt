package io.lunarchain.lunarcoin.android

import android.os.Bundle
import io.lunarchain.R
import io.lunarchain.lunarcoin.android.base.BaseFragment
import io.lunarchain.lunarcoin.core.BlockChainManager
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    override fun getContentViewResId() = R.layout.fragment_mine

    override fun init(savedInstanceState: Bundle?) {
        activity.setTitle(R.string.title_mine)
        switchMining.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) BlockChainManager.INSTANCE.startMining() else BlockChainManager.INSTANCE.stopMining()
        }
    }

}
