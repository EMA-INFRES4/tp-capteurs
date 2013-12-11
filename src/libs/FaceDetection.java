package libs;
/*
* TP VAP ASR - CSC5004 
* Copyright (C) 2009-2010  Institut TELECOM ; TELECOM Sudparis
* All rights reserved.
* Author: S. LERICHE - sebastien.leriche@it-sudparis.eu
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the Institut TELECOM, TELECOM SudParis nor the
*       names of its contributors may be used to endorse or promote products
*       derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import hypermedia.video.OpenCV;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.MemoryImageSource;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.event.EventListenerList;

public class FaceDetection implements Runnable {

    private static final long serialVersionUID = 1L;

    
    public Boolean thereisSomebody = false;
    // program execution frame rate (millisecond)
    final int FRAME_RATE = 200;

    OpenCV cv = null; // OpenCV Object

    Thread t = null; // the sample thread

    private Image icon;

    // the input video stream image
    Image frame = null;

    // list of all face detected area
    Rectangle[] squares = new Rectangle[0];

    private int iw;

    private int ih;

    private Font font = new Font("Arial", Font.BOLD, 18);

    private BufferStrategy strategy;

    private Graphics2D buffer;

    /**
     * Setup Frame and Object(s).
     */
    public FaceDetection() {


        // OpenCV setup
        cv = new OpenCV();
        cv.capture(320, 240);
        cv.cascade(OpenCV.CASCADE_FRONTALFACE_ALT);
    }


	@Override
	public void run() {
    	int loop = 0; 
        while (cv != null) {
            // grab image from video stream
            cv.read();

            // create a new image from cv pixels data
            MemoryImageSource mis = new MemoryImageSource(cv.width, cv.height,
                    cv.pixels(), 0, cv.width);
            //frame = createImage(mis);

            // detect faces
            squares = cv.detect(1.2f, 2, OpenCV.HAAR_DO_CANNY_PRUNING, 20, 20);
            thereisSomebody = squares.length > 0;
            fireSomebodyAppear(squares.length);
            // of course, render
            //render();

        }
    }

	private final EventListenerList listeners = new EventListenerList();
    
	public void addWebcamListener(WebcamListener listener){
		listeners.add(WebcamListener.class, listener);
	}
	
	public void removeWebcamListener(WebcamListener listener){
		listeners.remove(WebcamListener.class, listener);
	}

	public WebcamListener[] getWebcamListeners(){
		return listeners.getListeners(WebcamListener.class);
	}

	private int nbPeople = 0;
	protected void fireSomebodyAppear(int numberOfPeople){
		if(nbPeople != numberOfPeople){
			for(WebcamListener listener : getWebcamListeners()){
				if(numberOfPeople == 0){
					listener.SomebodyDisappear();
				}else{
					listener.SomebodyAppear(numberOfPeople);
				}
			}
		}
		nbPeople = numberOfPeople;
	}
}