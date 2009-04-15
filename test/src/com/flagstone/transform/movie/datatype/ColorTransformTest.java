/*
 * ColorTransformTest.java
 * Transform
 *
 * Copyright (c) 2001-2009 Flagstone Software Ltd. All rights reserved.
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
package com.flagstone.transform.movie.datatype;

import org.junit.Test;

import com.flagstone.transform.coder.CoderException;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

@SuppressWarnings({
	"PMD.TooManyMethods", 
    "PMD.LocalVariableCouldBeFinal",
	"PMD.JUnitAssertionsShouldIncludeMessage"
})
public final class ColorTransformTest
{
    private transient ColorTransform fixture;
    
	private transient SWFEncoder encoder;
	private transient SWFDecoder decoder;
	private transient byte[] data;

    @Test
    public void encodeMultiplyWithoutAlpha() throws CoderException
    {
    	data = new byte[] { 108, -128, 32, 6, 0};   	
    	encoder = new SWFEncoder(data.length);
     	
    	fixture = new ColorTransform(1.0f,2.0f,3.0f,4.0f);
    	
    	assertEquals(5, fixture.prepareToEncode(encoder));
    	fixture.encode(encoder);
    	
    	assertEquals(40, encoder.getPointer());
    	assertArrayEquals(data, encoder.getData());
    }
    
    @Test
    public void encodeMultiplyWithAlpha() throws CoderException
    {
    	data = new byte[] { 112, 64, 8, 0, -64, 16, 0};   	
    	encoder = new SWFEncoder(data.length);
    	encoder.getContext().setTransparent(true);
    	
    	fixture = new ColorTransform(1.0f,2.0f,3.0f,4.0f);
    	assertEquals(7, fixture.prepareToEncode(encoder));
    	fixture.encode(encoder);
    	
    	assertEquals(56, encoder.getPointer());
    	assertArrayEquals(data, encoder.getData());
    }
    
    @Test
    public void encodeAddWithoutAlpha() throws CoderException
    {
    	data = new byte[] { -116, -90};   	
    	encoder = new SWFEncoder(data.length);
     	
    	fixture = new ColorTransform(1,2,3,4);
    	
    	assertEquals(2, fixture.prepareToEncode(encoder));
    	fixture.encode(encoder);
    	
    	assertEquals(16, encoder.getPointer());
    	assertArrayEquals(data, encoder.getData());
    }
    
    @Test
    public void encodeAddWithAlpha() throws CoderException
    {
    	data = new byte[] { -112, 72, -48};   	
    	encoder = new SWFEncoder(data.length);
    	encoder.getContext().setTransparent(true);
    	
    	fixture = new ColorTransform(1,2,3,4);
    	assertEquals(3, fixture.prepareToEncode(encoder));
    	fixture.encode(encoder);
    	
    	assertEquals(24, encoder.getPointer());
    	assertArrayEquals(data, encoder.getData());
    }
    
    @Test
    public void encodeWithoutAlpha() throws CoderException
    {
    	data = new byte[] { -20, -128, 32, 6, 0, 0, 64, 16, 3};   	
    	encoder = new SWFEncoder(data.length);
     	
    	fixture = new ColorTransform(1.0f,2.0f,3.0f,4.0f);
    	fixture.setAddTerms(1,2,3,4);
    	
    	assertEquals(9, fixture.prepareToEncode(encoder));
    	fixture.encode(encoder);
    	
    	assertEquals(72, encoder.getPointer());
    	assertArrayEquals(data, encoder.getData());
    }
    
    @Test
    public void encodeWithAlpha() throws CoderException
    {
    	data = new byte[] { -16, 64, 8, 0, -64, 16, 0, 0, 64, 8, 0, -64, 16};   	
    	encoder = new SWFEncoder(data.length);
    	encoder.getContext().setTransparent(true);
    	
    	fixture = new ColorTransform(1.0f,2.0f,3.0f,4.0f);
    	fixture.setAddTerms(1,2,3,4);

    	assertEquals(13, fixture.prepareToEncode(encoder));
    	fixture.encode(encoder);
    	
    	assertEquals(104, encoder.getPointer());
    	assertArrayEquals(data, encoder.getData());
    }

    @Test
    public void decodeMultiplyWithoutAlpha() throws CoderException
    {
    	data = new byte[] { 108, -128, 32, 6, 0};   	
    	decoder = new SWFDecoder(data);
     	
       	fixture = new ColorTransform(decoder);
            	
    	assertEquals(40, decoder.getPointer());
     	assertEquals(1.0f, fixture.getMultiplyRed());
    	assertEquals(2.0f, fixture.getMultiplyGreen());
    	assertEquals(3.0f, fixture.getMultiplyBlue());
    	assertEquals(1.0f, fixture.getMultiplyAlpha());
    }
    
    @Test
    public void decodeMultiplyWithAlpha() throws CoderException
    {
    	data = new byte[] { 112, 64, 8, 0, -64, 16, 0};   	
    	decoder = new SWFDecoder(data);
     	decoder.getContext().setTransparent(true);
     	
    	fixture = new ColorTransform(decoder);
    	
    	assertEquals(56, decoder.getPointer());
    	assertEquals(1.0f, fixture.getMultiplyRed());
    	assertEquals(2.0f, fixture.getMultiplyGreen());
    	assertEquals(3.0f, fixture.getMultiplyBlue());
    	assertEquals(4.0f, fixture.getMultiplyAlpha());
   }
    
    @Test
    public void decodeAddWithoutAlpha() throws CoderException
    {
    	data = new byte[] { -116, -90};   	
    	decoder = new SWFDecoder(data);
     	
       	fixture = new ColorTransform(decoder);
            	
    	assertEquals(16, decoder.getPointer());
    	assertEquals(1, fixture.getAddRed());
    	assertEquals(2, fixture.getAddGreen());
    	assertEquals(3, fixture.getAddBlue());
    	assertEquals(0, fixture.getAddAlpha());
    }
    
    @Test
    public void decodeAddWithAlpha() throws CoderException
    {
    	data = new byte[] { -112, 72, -48};   	
    	decoder = new SWFDecoder(data);
    	decoder.getContext().setTransparent(true);
    	
       	fixture = new ColorTransform(decoder);
            	
    	assertEquals(24, decoder.getPointer());
    	assertEquals(1, fixture.getAddRed());
    	assertEquals(2, fixture.getAddGreen());
    	assertEquals(3, fixture.getAddBlue());
    	assertEquals(4, fixture.getAddAlpha());
    }
    
    @Test
    public void decodeWithoutAlpha() throws CoderException
    {
    	data = new byte[] { -20, -128, 32, 6, 0, 0, 64, 16, 3};   	
    	decoder = new SWFDecoder(data);

       	fixture = new ColorTransform(decoder);
        
    	assertEquals(72, decoder.getPointer());
    	assertEquals(1, fixture.getAddRed());
    	assertEquals(2, fixture.getAddGreen());
    	assertEquals(3, fixture.getAddBlue());
    	assertEquals(0, fixture.getAddAlpha());
    	assertEquals(1.0f, fixture.getMultiplyRed());
    	assertEquals(2.0f, fixture.getMultiplyGreen());
    	assertEquals(3.0f, fixture.getMultiplyBlue());
    	assertEquals(1.0f, fixture.getMultiplyAlpha());
    }
    
    @Test
    public void decodeWithAlpha() throws CoderException
    {
    	data = new byte[] { -16, 64, 8, 0, -64, 16, 0, 0, 64, 8, 0, -64, 16};   	
    	decoder = new SWFDecoder(data);
    	decoder.getContext().setTransparent(true);
     	
       	fixture = new ColorTransform(decoder);
        
    	assertEquals(104, decoder.getPointer());
    	assertEquals(1, fixture.getAddRed());
    	assertEquals(2, fixture.getAddGreen());
    	assertEquals(3, fixture.getAddBlue());
    	assertEquals(4, fixture.getAddAlpha());
    	assertEquals(1.0f, fixture.getMultiplyRed());
    	assertEquals(2.0f, fixture.getMultiplyGreen());
    	assertEquals(3.0f, fixture.getMultiplyBlue());
    	assertEquals(4.0f, fixture.getMultiplyAlpha());
    }
}
