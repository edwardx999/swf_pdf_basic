/*
 *  FontFactory.java
 *  Transform Utilities
 *
 * Copyright (c) 2009 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.flagstone.transform.util.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.DataFormatException;

import com.flagstone.transform.Strings;
import com.flagstone.transform.coder.MovieTag;

/**
 * TODO(class).
 */
public final class FontFactory {
    
    private FontDecoder decoder;
    
    /**
     * TODO(method).
     */
    public void read(final File file) throws IOException, DataFormatException {
        
        String fontType;
        
        if (file.getName().endsWith("ttf")) {
            fontType = "ttf";
        } else if (file.getName().endsWith("swf")) {
            fontType = "swf";
        } else {
            throw new DataFormatException(Strings.INVALID_FORMAT);
        }

        decoder = FontRegistry.getFontProvider(fontType);
        decoder.read(file);
    }

    /**
      * TODO(method).
     */
    public void read(final URL url) throws IOException, DataFormatException {
        
        final URLConnection connection = url.openConnection();
        final int fileSize = connection.getContentLength();
 
        if (fileSize < 0) {
            throw new FileNotFoundException(url.getFile());
        }

        String mimeType = connection.getContentType();
        decoder = FontRegistry.getFontProvider(mimeType);
        
        if (decoder == null) {
            throw new DataFormatException(Strings.INVALID_IMAGE);
        }

        decoder.read(url);
    }

    /**
     * 
     * @return
     */
    Font[] getFonts() {
        return decoder.getFonts();
    }
}