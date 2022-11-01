package org.szkubisznekk.audio;

import org.lwjgl.openal.*;

import java.util.ArrayList;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class Audio
{
	private static long s_device;
	private static long s_context;
	private static float s_volume = 1.0f;
	private static ArrayList<AudioSource> s_sources = new ArrayList<>();

	public static void init()
	{
		s_device = alcOpenDevice((java.lang.CharSequence)null);
		s_context = alcCreateContext(s_device, (int[])null);
		alcMakeContextCurrent(s_context);
		ALCCapabilities alcCapabilities = ALC.createCapabilities(s_device);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

		alListener3f(AL_POSITION, 0.0f, 0.0f, 0.0f);
		alListener3f(AL_VELOCITY, 0.0f, 0.0f, 0.0f);
	}

	public static void destruct()
	{
		for(AudioSource source : s_sources)
		{
			source.destruct();
		}
	}

	public static void setVolume(float volume)
	{
		s_volume = volume;
		for(AudioSource source : s_sources)
		{
			source.setVolume(s_volume);
		}
	}

	public static void play(AudioClip clip, boolean loop)
	{
		int i = 0;
		while(i < s_sources.size() && s_sources.get(i).isPlaying())
		{
			i++;
		}

		if(i < s_sources.size())
		{
			s_sources.get(i).play(clip, loop);
		}
		else
		{
			AudioSource newSource = new AudioSource(s_volume);
			s_sources.add(newSource);
			newSource.play(clip, loop);
		}
	}

	private static class AudioSource
	{
		private final int m_handle;

		public AudioSource(float volume)
		{
			m_handle = alGenSources();
			alSourcef(m_handle, AL_GAIN, volume);
			alSourcef(m_handle, AL_PITCH, 1.0f);
			alSource3f(m_handle, AL_POSITION, 0.0f, 0.0f, 0.0f);
		}

		public void destruct()
		{
			alDeleteSources(m_handle);
		}

		public void setVolume(float volume)
		{
			alSourcef(m_handle, AL_GAIN, volume);
		}

		public void play(AudioClip clip, boolean loop)
		{
			if(loop)
			{
				alSourcei(m_handle, AL_LOOPING, AL_TRUE);
			}
			alSourcei(m_handle, AL_BUFFER, clip.getBuffer());
			alSourcePlay(m_handle);
		}

		public boolean isPlaying()
		{
			return alGetSourcei(m_handle, AL_SOURCE_STATE) == AL_PLAYING;
		}
	}
}
