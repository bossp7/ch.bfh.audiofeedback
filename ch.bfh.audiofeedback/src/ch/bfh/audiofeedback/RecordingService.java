package ch.bfh.audiofeedback;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.ProgressBar;

public class RecordingService extends Thread implements Runnable {
	private static String TAG = "RecordingService";
	private final ArrayBlockingQueue<double[]> queue;
	int sampleRate, bufferSize;
	
	boolean suspendFlag;
	
	public RecordingService(ArrayBlockingQueue<double[]> q, int sr, int bs){
		queue = q;		
		sampleRate = sr;
		bufferSize = bs;
		suspendFlag = false;
	}
       	

	@Override
	public void run() {
		
		int audioBufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		if (bufferSize < audioBufferSize){
			bufferSize = audioBufferSize;
		}
				
		short[] mBuffer = new short[bufferSize];
		Log.d(TAG, "Buffersize from Audiorecord: " + Integer.toString(audioBufferSize));
		Log.d(TAG, "given Buffersize ." + Integer.toString(bufferSize));
		AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, sampleRate, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, audioBufferSize);
		
		
		while(true){
			int readShorts = 0;
			recorder.startRecording();
	
			Log.d(TAG, "getting some Data");
				
			// read data until mBuffer is full up
			for(int i = 0; i < mBuffer.length/1024; i++){
				readShorts = recorder.read(mBuffer, i*1024 , 1024);
				Log.d(TAG,"Read data: " + Integer.toString(readShorts));
//				Log.d(TAG, Integer.toString(mBuffer.length/1024));
//				Log.d(TAG, "Index: " + i);
				
		 	}
			recorder.stop();
		
			
	//		for (int i = 0; i < mBuffer.length; i ++){
	//			Log.d(TAG, "Nr. " + i + " : " + Short.toString(mBuffer[i]));
	//		}
			
			// cast the shorts to doubles
			double[] result = new double[bufferSize];
			for(int i = 0; i < mBuffer.length; i++){
				result[i] = (double)mBuffer[i];
			}
			try {
				queue.put(result);
				Log.d(TAG, "Put Item in queue");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			synchronized(this) {
	            while(suspendFlag) {
	               try {
					wait();
					Log.d(TAG, "Thread waiting");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	          }
			
				
		
		}// while
		
	
	}// run()
	
	
	public void mysuspend() {
	   suspendFlag = true;
	   Log.d(TAG, "Suspend Task");
	}
	
	public synchronized void myresume() {
      suspendFlag = false;
      notify();
      Log.d(TAG, "Resume Task");
	}

}
