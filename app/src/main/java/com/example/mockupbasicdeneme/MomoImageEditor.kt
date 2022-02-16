package com.example.mockupbasicdeneme

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.momolib.NativeLib
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MomoImageEditor private constructor(
    context: Context,
    private var bitmap: Bitmap
) {

    companion object {
        private const val TAG = "MomoImageEditor"
        private const val FACEMODEL_CUSTOM = "fdet_128_r_fbepoch_50_loss_3.onnx"
        private const val FACEMARK_MODEL =
            "wo_dsnt_skn36_mltp2_98points_add_layer_dsnt_rmse_fm_12x12x288_grayimg_shape_192x192_data_300wv_43720plus_waug_20052.onnx"
    }

    private val nativeLib: NativeLib

    init {
        val faceDir: File = context.getDir("facelib", Context.MODE_PRIVATE)
        val faceModel = File(faceDir, FACEMODEL_CUSTOM)
        val faceMarkModel = File(faceDir, FACEMARK_MODEL)

        val fModelInputStream: InputStream = context.resources
            .openRawResource(R.raw.fdet_128_r_fbepoch_50_loss_3)
        val fMarkInputStream: InputStream = context.resources
            .openRawResource(R.raw.wo_dsnt_skn36_mltp2_98points_add_layer_dsnt_rmse_fm_12x12x288_grayimg_shape_192x192_data_300wv_43720plus_waug_20052)

        fileCopy(faceModel, fModelInputStream)
        fileCopy(faceMarkModel, fMarkInputStream)

        nativeLib = NativeLib()

        nativeLib.initLib(faceModel.absolutePath, faceMarkModel.absolutePath)
        nativeLib.setImage(bitmap)
    }

    fun isFaceDetected(): Boolean {
        return nativeLib.isFaceDetected
    }

    fun setNewImage(bitmap: Bitmap) {
        nativeLib.setImage(bitmap)
    }

    fun applyFilter(filter: Filter.SliderFilter, filterLevel: Int): Bitmap? {
        val result = filter.apply(nativeLib, filterLevel)?.also {
            bitmap = it
            nativeLib.setImage(bitmap)
        }
        return result
    }

    private fun fileCopy(file: File, inputStream: InputStream) {
        try {
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var byteRead = inputStream.read(buffer)
            while (byteRead != -1) {
                outputStream.write(buffer, 0, byteRead)
                byteRead = inputStream.read(buffer)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error while reading onnx models", e)
        }
    }

    // Context is injected, Builder can get injected too
    class Builder(private val context: Context) {

        private var bitmap: Bitmap? = null

        fun setBitmap(bitmap: Bitmap): Builder {
            this.bitmap = bitmap
            return this
        }

        //@Throws(IllegalArgumentException::class)
        fun build(): MomoImageEditor {
            if (bitmap == null) {
                Log.e(TAG, "Set bitmap image before calling MomoImageEditor.build()")
                throw IllegalArgumentException("Bitmap image is not provided to MomoImageEditor.Builder")
            }

            return MomoImageEditor(context, bitmap!!)
        }
    }
}

class Filter {

    companion object {
        private const val TAG = "Filter"
    }

    abstract class SliderFilter {

        abstract fun apply(nativeLib: NativeLib, filterLevel: Int): Bitmap?

        class ChinWidth : SliderFilter() {

            override fun apply(nativeLib: NativeLib, filterLevel: Int): Bitmap? {
                if (nativeLib.isFaceDetected.not()) {
                    Log.e(TAG, "Face is not detected")
                    return null
                }

                return nativeLib.setChinWidth(filterLevel)
            }
        }
    }
}

class TestUsage(private val injectedBuilder: MomoImageEditor.Builder) {

    companion object {
        private const val TAG = "TestUsage"
    }

    private lateinit var imageEditor: MomoImageEditor

    fun onImageSelected(bitmap: Bitmap) {
        imageEditor = injectedBuilder.setBitmap(bitmap).build()

        if (imageEditor.isFaceDetected().not()) {
            Log.e(TAG, "Face is not detected. Please select another image")
        }
    }

    fun onSliderChanged(sliderValue: Int) {
        if (::imageEditor.isInitialized.not()) {
            Log.e(TAG, "Image editor is not initialized")
            return
        }

        val resultImage: Bitmap? = imageEditor
            .applyFilter(Filter.SliderFilter.ChinWidth(), sliderValue)
    }
}
