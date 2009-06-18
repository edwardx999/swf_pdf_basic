package com.flagstone.transform.util.sound;

/** TODO(class). */
enum SoundEncoding {

    /** TODO(method). */
    MP3("audio/mpeg", new MP3Decoder()),
    /** TODO(method). */
    WAV("audio/x-wav", new WAVDecoder());

    private final String mimeType;
    private final SoundProvider provider;

    private SoundEncoding(final String mimeType, final SoundProvider provider) {
        this.mimeType = mimeType;
        this.provider = provider;
    }

    /** TODO(method). */
    public String getMimeType() {
        return mimeType;
    }

    /** TODO(method). */
    public SoundProvider getProvider() {
        return provider;
    }
}