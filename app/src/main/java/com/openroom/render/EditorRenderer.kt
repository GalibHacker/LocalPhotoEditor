package com.openroom.render

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
    private val vertices = floatArrayOf(-1f, -1f, 1f, -1f, -1f, 1f, 1f, 1f)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vertex = ShaderLoader.compile(GLES30.GL_VERTEX_SHADER, ShaderLoader.VERTEX)
        val fragment = ShaderLoader.compile(GLES30.GL_FRAGMENT_SHADER, ShaderLoader.FRAGMENT)
        program = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertex)
            GLES30.glAttachShader(it, fragment)
            GLES30.glLinkProgram(it)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) = GLES30.glViewport(0, 0, width, height)

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUseProgram(program)
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
}
