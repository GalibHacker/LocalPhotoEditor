package com.openroom.render

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.openroom.domain.EditParams
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class EditorRenderer : GLSurfaceView.Renderer {
    @Volatile var params = EditParams()
    private var program = 0
    private var texture = 0
    private var textureWidth = 0
    private var textureHeight = 0
    private var surfaceWidth = 0
    private var surfaceHeight = 0
    private var pendingUri: Uri? = null
    private var textureLoaded = false
    private val vertices = floatArrayOf(-1f, -1f, 1f, -1f, -1f, 1f, 1f, 1f)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vertex = ShaderLoader.compile(GLES30.GL_VERTEX_SHADER, ShaderLoader.VERTEX)
        val fragment = ShaderLoader.compile(GLES30.GL_FRAGMENT_SHADER, ShaderLoader.FRAGMENT)
        program = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertex)
            GLES30.glAttachShader(it, fragment)
            GLES30.glLinkProgram(it)
        }
        if (pendingUri != null) {
            textureLoaded = false
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        surfaceWidth = width
        surfaceHeight = height
        setFittedViewport()
    }

    override fun onDrawFrame(gl: GL10?) {
        if (!textureLoaded && pendingUri != null) {
            loadPendingTexture(pendingUri!!)
        }
        GLES30.glViewport(0, 0, surfaceWidth, surfaceHeight)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        setFittedViewport()
        GLES30.glUseProgram(program)
        if (texture != 0) {
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture)
            GLES30.glUniform1i(GLES30.glGetUniformLocation(program, "uTexture"), 0)
        }
        val buffer = ByteBuffer.allocateDirect(vertices.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        buffer.put(vertices).position(0)
        val position = GLES30.glGetAttribLocation(program, "aPosition")
        GLES30.glEnableVertexAttribArray(position)
        GLES30.glVertexAttribPointer(position, 2, GLES30.GL_FLOAT, false, 0, buffer)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uExposure"), params.exposure)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uTemperature"), params.temperature)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uTint"), params.tint)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uContrast"), params.contrast)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uHighlights"), params.highlights)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uShadows"), params.shadows)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uWhites"), params.whites)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uBlacks"), params.blacks)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uVibrance"), params.vibrance)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(program, "uSaturation"), params.saturation)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
        GLES30.glDisableVertexAttribArray(position)
    }

    private fun setFittedViewport() {
        if (surfaceWidth == 0 || surfaceHeight == 0 || textureWidth == 0 || textureHeight == 0) {
            if (surfaceWidth > 0 && surfaceHeight > 0) {
                GLES30.glViewport(0, 0, surfaceWidth, surfaceHeight)
            }
            return
        }
        val surfaceAspect = surfaceWidth.toFloat() / surfaceHeight
        val imageAspect = textureWidth.toFloat() / textureHeight
        if (surfaceAspect > imageAspect) {
            val fittedWidth = (surfaceHeight * imageAspect).toInt()
            GLES30.glViewport((surfaceWidth - fittedWidth) / 2, 0, fittedWidth, surfaceHeight)
        } else {
            val fittedHeight = (surfaceWidth / imageAspect).toInt()
            GLES30.glViewport(0, (surfaceHeight - fittedHeight) / 2, surfaceWidth, fittedHeight)
        }
    }

    private fun loadPendingTexture(uri: Uri) {
        val context = contextRef ?: return
        val input = context.contentResolver.openInputStream(uri) ?: return
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        input.use { BitmapFactory.decodeStream(it, null, options) }
        var sampleSize = 1
        while (options.outWidth / sampleSize > 4096 || options.outHeight / sampleSize > 4096) {
            sampleSize *= 2
        }
        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val bitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
        } ?: return

        textureWidth = bitmap.width
        textureHeight = bitmap.height
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        texture = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
        android.opengl.GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
        textureLoaded = true
        setFittedViewport()
    }

    private var contextRef: Context? = null

    fun loadTexture(context: Context, uri: String) {
        contextRef = context.applicationContext
        pendingUri = Uri.parse(uri)
    }
}
