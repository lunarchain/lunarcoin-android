package io.lunarchain.lunarcoin.android.ui.wallet

import android.graphics.Bitmap
import android.os.Bundle
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import io.lunarchain.R
import io.lunarchain.lunarcoin.android.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_qrcode.*
import java.util.*

class QRCodeFragment : BaseFragment() {

    override fun getContentViewResId(): Int = R.layout.fragment_qrcode

    override fun init(savedInstanceState: Bundle?) {
        tvNewQRCode.setOnClickListener { newQRCode() }
        imgQRCode.setImageBitmap(generateBitmap("https://www.baidu.com", 500, 500))
    }

    private fun generateBitmap(content: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hints = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.MARGIN] = 0
        try {
            val encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000
                    } else {
                        pixels[i * width + j] = -0x1
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return null
    }

    private fun newQRCode() {
        imgQRCode.setImageBitmap(generateBitmap("https://www.baidu.com?" + Random().nextInt(), 500, 500))
    }

}