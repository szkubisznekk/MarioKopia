package org.szkubisznekk.audio;

import static org.lwjgl.openal.AL10.*;

import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;

public class AudioClip
{
	private final int m_buffer;

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

	public void destruct()
	{
		alDeleteBuffers(m_buffer);
	}

	public int getBuffer()
	{
		return m_buffer;
	}
}
