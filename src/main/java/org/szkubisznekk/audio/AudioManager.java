package org.szkubisznekk.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class AudioManager
{
	private static class AudioClip
	{
		private final int m_buffer;

		AudioClip(String path)
		{
			m_buffer = alGenBuffers();

			IntBuffer channels = MemoryUtil.memAllocInt(1);
			IntBuffer sampleRate = MemoryUtil.memAllocInt(1);
			ShortBuffer data = STBVorbis.stb_vorbis_decode_filename(path, channels, sampleRate);

			if(data != null)
			{
				alBufferData(m_buffer, (channels.get(0) == 1) ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, data, sampleRate.get(0));
			}
			else
			{
				System.err.printf("Failed to load audio: (%s)\n", path);
			}
		}

		void destruct()
		{
			alDeleteBuffers(m_buffer);
		}

		int getBuffer()
		{
			return m_buffer;
		}
	}

	public static class AudioSource
	{
		private final int m_handle;

		AudioSource()
		{
			m_handle = alGenSources();
			alSourcef(m_handle, AL_PITCH, 1.0f);
			alSource3f(m_handle, AL_POSITION, 0.0f, 0.0f, 0.0f);
			setVolume(1.0f);
		}

		void destruct()
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

		public void stop()
		{
			alSourceStop(m_handle);
		}

		public boolean isPlaying()
		{
			return alGetSourcei(m_handle, AL_SOURCE_STATE) == AL_PLAYING;
		}
	}

	private static AudioManager s_instance;

	private final long m_device;
	private final long m_context;
	private final HashMap<String, AudioClip> m_clips = new HashMap<>();
	private final ArrayList<AudioSource> m_sources = new ArrayList<>();

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
		setVolume(1.0f);
	}

	public void destruct()
	{
		for(var source : m_sources)
		{
			source.destruct();
		}

		for(var entry : m_clips.entrySet())
		{
			entry.getValue().destruct();
		}
	}

	public static AudioManager get()
	{
		return s_instance;
	}

	public void setVolume(float volume)
	{
		alListenerf(AL_GAIN, volume);
	}

	public AudioSource play(String path, float volume, boolean loop)
	{

		AudioClip clip;
		if(m_clips.containsKey(path))
		{
			clip = m_clips.get(path);
		}
		else
		{
			clip = new AudioClip(path);
			m_clips.put(path, clip);
		}

		int i = 0;
		while(i < m_sources.size() && m_sources.get(i).isPlaying())
		{
			i++;
		}

		AudioSource source;
		if(i < m_sources.size())
		{
			source = m_sources.get(i);
		}
		else
		{
			source = new AudioSource();
			m_sources.add(source);

		}
		source.setVolume(volume);
		source.play(clip, loop);

		return source;
	}
}
