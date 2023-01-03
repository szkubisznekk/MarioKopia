package org.szkubisznekk.audio;

import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.*;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

/**
 * Manages audio sources and clips.
 */
public class AudioManager
{
	/**
	 * Wraps an OpenAl source.
	 */
	public static class AudioSource
	{
		private final int m_handle;

		/**
		 * Creates an OpenAL source.
		 */
		AudioSource()
		{
			m_handle = alGenSources();
			alSourcef(m_handle, AL_PITCH, 1.0f);
			alSource3f(m_handle, AL_POSITION, 0.0f, 0.0f, 0.0f);
			setVolume(1.0f);
		}

		/**
		 * Destructs the OpenAL source.
		 */
		void destruct()
		{
			alDeleteSources(m_handle);
		}

		/**
		 * Sets the volume of this source.
		 *
		 * @param volume The volume.
		 */
		public void setVolume(float volume)
		{
			alSourcef(m_handle, AL_GAIN, volume);
		}

		/**
		 * Plays an audio clip.
		 *
		 * @param clip The clip.
		 * @param loop Should the clip loop.
		 */
		public void play(AudioClip clip, boolean loop)
		{
			if(loop)
			{
				alSourcei(m_handle, AL_LOOPING, AL_TRUE);
			}
			alSourcei(m_handle, AL_BUFFER, clip.getBuffer());
			alSourcePlay(m_handle);
		}

		/**
		 * Stops thw currently playing clip.
		 */
		public void stop()
		{
			alSourceStop(m_handle);
		}

		/**
		 * Returns whether the last played clip is still playing.
		 *
		 * @return Whether the last played clip is still playing.
		 */
		public boolean isPlaying()
		{
			return alGetSourcei(m_handle, AL_SOURCE_STATE) == AL_PLAYING;
		}
	}

	/**
	 * Holds an audio clip file data. Wraps an OpenAL buffer.
	 */
	private static class AudioClip
	{
		private final int m_buffer;

		/**
		 * Loads an audio clip from file.
		 *
		 * @param path The path to the file.
		 */
		public AudioClip(String path)
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

		/**
		 * Destructs the audio clip's buffer.
		 */
		public void destruct()
		{
			alDeleteBuffers(m_buffer);
		}

		/**
		 * Returns the handle to the audio clip's OpenAL buffer.
		 *
		 * @return The handle to the audio clip's OpenAL buffer.
		 */
		public int getBuffer()
		{
			return m_buffer;
		}
	}

	private static AudioManager s_instance;

	private final long m_device;
	private final long m_context;
	private final HashMap<String, AudioClip> m_clips = new HashMap<>();
	private final ArrayList<AudioSource> m_sources = new ArrayList<>();

	/**
	 * Creates an OpenAL context.
	 */
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

	/**
	 * Destruct all sources and clips.
	 */
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

	/**
	 * Returns the only instance of audio manager.
	 *
	 * @return The only instance of audio manager.
	 */
	public static AudioManager get()
	{
		return s_instance;
	}

	/**
	 * Set global volume.
	 *
	 * @param volume The global volume.
	 */
	public void setVolume(float volume)
	{
		alListenerf(AL_GAIN, volume);
	}

	/**
	 * Plays an audio clip and returns the audio source used to play it.
	 *
	 * @param path   The path to the audio clip.
	 * @param volume The volume.
	 * @param loop   Should the clip loop.
	 * @return The audio source used to play it.
	 */
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
