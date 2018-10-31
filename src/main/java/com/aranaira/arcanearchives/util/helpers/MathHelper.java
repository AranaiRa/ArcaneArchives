package com.aranaira.arcanearchives.util.helpers;

public class MathHelper {
	
	public static float Clamp(float in, float min, float max)
	{
		if(in < min)
			in = min;
		if(in > max)
			in = max;
		return in;
	}
	
	public static float LinearTween(float time, float startValue, float endValue, float duration)
	{
		time = Clamp(time, 0, duration);
		float r = -1;
		
		r = time / duration;
		
		return startValue + (endValue - startValue) * r;
	}
	
	public static float QuadraticTween(EaseMode mode, float time, float startValue, float endValue, float duration)
	{
		time = Clamp(time, 0, duration);
		float prog = time / duration;
		float delta = endValue - startValue;
		
		if(mode == EaseMode.IN)
		{
			return (prog * prog * delta) + startValue;
		}
		else if(mode == EaseMode.OUT)
		{
			return -delta * prog * (prog - 2) + startValue;
		}
		else
		{
			prog /= 2;
			if(prog < 1) return delta/2 * prog * prog + startValue;
			prog--;
			return -delta/2 * (prog * (prog - 2) - 1) + startValue;
		}
	}
	
	public enum EaseMode
	{
		IN, OUT, BOTH
	}
}
