package io.lunarchain.lunarcoin.android.ui.wallet

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import io.lunarchain.R
import io.lunarchain.lunarcoin.android.base.BaseFragment
import io.lunarchain.lunarcoin.android.model.wallet.Record
import io.lunarchain.lunarcoin.android.widgets.Formatter
import io.lunarchain.lunarcoin.android.widgets.SimpleRecyclerViewAdapter
import io.lunarchain.lunarcoin.android.widgets.SimpleRecyclerViewHolder
import io.lunarchain.lunarcoin.util.CryptoUtil
import kotlinx.android.synthetic.main.fragment_wallet.*
import org.slf4j.LoggerFactory
import java.util.*

class WalletFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun getRootContentViewResId(): Int = R.layout.fragment_wallet

    override fun getContentViewResId(): Int = 0

    override fun init(savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity.setTitle(R.string.title_wallet)

        val dataSource: ArrayList<Record> = ArrayList()
        // TODO
        for (i in 0..25) {
            var type = if (Random().nextBoolean()) "received" else "sent"
            dataSource.add(Record(i.toString(), type, type, i.toLong(), System.currentTimeMillis() + i * 100))
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter =
                SimpleRecyclerViewAdapter(dataSource, this, R.layout.list_item_wallet_activity, ViewHolder::class.java)

        val kp = CryptoUtil.generateKeyPair()
        val address = CryptoUtil.generateAddress(kp.public)
        val logger = LoggerFactory.getLogger(javaClass)
        logger.debug(address.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.wallet_qrcode, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.menuAddress) {
            showQRCode()
        }
        return super.onOptionsItemSelected(item)
    }

    inner class ViewHolder(val itemRoot: View) : SimpleRecyclerViewHolder<Record>(itemRoot), View.OnClickListener {

        private var receivedDrawable: Drawable = ContextCompat.getDrawable(itemRoot.context, R.drawable.ic_received)
        private var sentDrawable: Drawable = ContextCompat.getDrawable(itemRoot.context, R.drawable.ic_sent)
        private lateinit var itemData: Record

        init {
            itemRoot.setOnClickListener(this)
        }

        override fun bindData(itemData: Record) {
            this.itemData = itemData

            val tvType = itemRoot.findViewById<TextView>(R.id.tvType)
            val tvAmount = itemRoot.findViewById<TextView>(R.id.tvAmount)
            val tvTime = itemRoot.findViewById<TextView>(R.id.tvTime)

            var d: Drawable
            if (itemData.type != null && itemData.type == "sent") {// TODO
                d = sentDrawable
                tvAmount.text = itemRoot.context.getString(R.string.wallet_bits_sent, Formatter.formatNumber(itemData.amount))
            } else {
                d = receivedDrawable
                tvAmount.text = itemRoot.context.getString(R.string.wallet_bits_received, Formatter.formatNumber(itemData.amount))
            }
            d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
            tvType.apply {
                text = itemData.typeName
                setCompoundDrawables(d, null, null, null)
            }
            tvTime.text = Formatter.formatTime(itemData.time)
        }

        override fun onClick(v: View?) {
            Toast.makeText(itemRoot.context, "Detail " + itemData.id, Toast.LENGTH_SHORT).show()
        }

    }

    private fun showQRCode() {
        activity.supportFragmentManager.beginTransaction().replace(R.id.content, QRCodeFragment()).addToBackStack("qrcode").commit()
    }

}
