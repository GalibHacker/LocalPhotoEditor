package com.openroom.render

import android.opengl.GLES30

object ShaderLoader {
    fun compile(type: Int, source: String): Int =
        GLES30.glCreateShader(type).also {
            GLES30.glShaderSource(it, source)
            GLES30.glCompileShader(it)
        }

    const val VERTEX = """
        #version 300 es
        in vec2 aPosition;
        out vec2 vUv;
        void main() { vUv = aPosition * 0.5 + 0.5; gl_Position = vec4(aPosition, 0.0, 1.0); }
    """

    const val FRAGMENT = """
        #version 300 es
        precision mediump float;
        in vec2 vUv;
        uniform sampler2D uTexture;
        uniform float uExposure;
        uniform float uTemperature;
        uniform float uTint;
        uniform float uContrast;
        uniform float uHighlights;
        uniform float uShadows;
        uniform float uWhites;
        uniform float uBlacks;
        uniform float uVibrance;
        uniform float uSaturation;
        out vec4 outColor;
        void main() {
            vec3 color = texture(uTexture, vUv).rgb;
            color *= exp2(uExposure);
            color = (color - 0.5) * (1.0 + uContrast) + 0.5;
            color.r += uTemperature * 0.15;
            color.b -= uTemperature * 0.15;
            color.g += uTint * 0.15;
            float luminance = dot(color, vec3(0.2126, 0.7152, 0.0722));
            color += (luminance - 0.5) * uHighlights * 0.25;
            color += (0.5 - luminance) * uShadows * 0.25;
            color += uWhites * 0.1;
            color -= uBlacks * 0.1;
            color = mix(vec3(luminance), color, 1.0 + uSaturation);
            outColor = vec4(clamp(color, 0.0, 1.0), 1.0);
        }
    """
}
