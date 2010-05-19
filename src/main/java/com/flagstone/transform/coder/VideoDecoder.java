/*
 * VideoDecoder.java
 * Transform
 *
 * Copyright (c) 2001-2010 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.flagstone.transform.coder;

import com.flagstone.transform.video.AudioData;
import com.flagstone.transform.video.VideoData;
import com.flagstone.transform.video.VideoMetaData;

/**
 * VideoDecoder is responsible for decoding the binary Flash Video data into
 * the different instances of VideoTag objects.
*/
public final class VideoDecoder implements FLVFactory<VideoTag> {
    /** {@inheritDoc} */
    public VideoTag getObject(final FLVDecoder coder) throws CoderException {

        VideoTag object;

        switch (coder.scanByte()) {
        case VideoTypes.AUDIO_DATA:
            object = new AudioData(coder);
            break;
        case VideoTypes.VIDEO_DATA:
            object = new VideoData(coder);
            break;
        case VideoTypes.META_DATA:
            object = new VideoMetaData(coder);
            break;
        default:
            throw new AssertionError();
        }
        coder.readUI32(); // previous length
        return object;
    }
}
