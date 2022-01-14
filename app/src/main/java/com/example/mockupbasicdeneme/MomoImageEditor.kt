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
        private const val FACEMODEL_CUSTOM = "facemodel_custom.onnx"
        private const val FACEMARK_MODEL = "facemark_model.onnx"
    }

    private val nativeLib: NativeLib

    init {
        val faceDir: File = context.getDir("facelib", Context.MODE_PRIVATE)
        val faceModel = File(faceDir, FACEMODEL_CUSTOM)
        val faceMarkModel = File(faceDir, FACEMARK_MODEL)

        val fModelInputStream: InputStream = context.resources
            .openRawResource(R.raw.facemodel_custom)
        val fMarkInputStream: InputStream = context.resources
            .openRawResource(R.raw.facemark_model)

        fileCopy(faceModel, fModelInputStream)
        fileCopy(faceMarkModel, fMarkInputStream)

        nativeLib = NativeLib()

        nativeLib.initLib(faceModel.absolutePath, faceMarkModel.absolutePath)
        nativeLib.faceDetection(bitmap) // TODO Ask SDK to set image with another function
    }

    fun isFaceDetected(): Boolean {
        return nativeLib.faceDetection(bitmap)
    }

    fun setNewImage(bitmap: Bitmap) {
        nativeLib.faceDetection(bitmap) // TODO Ask SDK to set image with another function
    }

    fun applyFilter(filter: KFilter.SliderFilter, filterLevel: Int): Bitmap {
        val result = filter.apply(nativeLib, filterLevel).also {
            bitmap = it
            nativeLib.faceDetection(bitmap) // TODO Ask SDK to set image with another function
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

class KFilter {

    companion object {
        private const val TAG = "Filter"
    }

    abstract class SliderFilter {

        abstract fun apply(nativeLib: NativeLib, filterLevel: Int): Bitmap

        class ChinWidth : SliderFilter() {

            override fun apply(nativeLib: NativeLib, filterLevel: Int): Bitmap {
                if (nativeLib.faceDetection(nativeLib.bitmapWithLandmarks).not()) {
                    Log.e(TAG, "Face is not detected")
                    return nativeLib.bitmapWithLandmarks
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

        val resultImage: Bitmap = imageEditor
            .applyFilter(KFilter.SliderFilter.ChinWidth(), sliderValue)
    }
}