//Shader stolen from online, needs a rewrite, used just for test purposes here....
#version 120

uniform sampler2D texture;
uniform vec4 color;
uniform vec4 brightColor;
uniform int ticks;

void main() {
    vec4 sum = vec4(0);
    vec2 texcoord = vec2(gl_TexCoord[0]);
    float brightness = sin(ticks / 12.0) * 0.5 + 0.5;

    float multFactor = 0.3;
    vec4 mixedColor = texture2D(texture, texcoord) * (color * 0.9);

    for (int i = -4; i < 4; i++) {
        for (int j = -3; j < 3; j++) {
            sum += texture2D(texture, texcoord + vec2(j, i) * 0.004) * 0.25 * brightColor;
        }
    }

    if (mixedColor.r < 0.3) {
        gl_FragColor = sum * sum * 0.012 + mixedColor;
    } else {
        if (mixedColor.r < 0.5) {
            gl_FragColor = sum * sum * 0.009 * brightness + mixedColor;
        } else {
            gl_FragColor = sum * sum * 0.0075 * brightness + mixedColor;
        }
    }
}