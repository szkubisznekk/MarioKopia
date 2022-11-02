package org.szkubisznekk.audio;

import org.lwjgl.openal.*;

import java.util.ArrayList;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class AudioManager
{
	private static AudioManager s_instance;

	private long m_device;
	private long m_context;
	private float m_volume = 1.0f;
	private ArrayList<AudioSource> m_sources = new ArrayList<>();

	public AudioManager()
	{
		s_instance = this;

		m_device = alcOpenDevice((java.lang.CharSequence)null);
		m_context = alcCreateContext(m_device, (int[])null);
		alcMakeContextCurrent(m_context);
		ALCCapabilities alcCapabilities = ALC.createCapabilities(m_device);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

		alListener3f(AL_POSITION, 0.0f, 0.0f, 0.0f);
		alListener3f(AL_VELOCITY, 0.0f, 0.0f, 0.0f);
	}

	public void destruct()
	{
		for(AudioSource source : m_sources)
		{
			source.destruct();
		}
	}

	public static AudioManager get()
	{
		return s_instance;
	}

	public void setVolume(float volume)
	{
		m_volume = volume;
		for(AudioSource source : m_sources)
		{
			source.setVolume(m_volume);
		}
	}

	public void play(AudioClip clip, boolean loop)
	{
		int i = 0;
		while(i < m_sources.size() && m_sources.get(i).isPlaying())
		{
			i++;
		}

		if(i < m_sources.size())
		{
			m_sources.get(i).play(clip, loop);
		}
		else
		{
			AudioSource newSource = new AudioSource(m_volume);
			m_sources.add(newSource);
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
