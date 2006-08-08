package net.diaperrush.utils.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * This class keeps track of any files that could change during the execution of an application. 
 * 
 * @author mkhumri
 */
public class FileMonitor
{
    private static final Logger logger = Logger.getLogger( FileMonitor.class );
	private Timer timer;

	private Hashtable<String, FileMonitorTask> timerEntries;

	public FileMonitor()
	{
		// Create timer, run timer thread as daemon.
		timer = new Timer(true);
		timerEntries = new Hashtable<String, FileMonitorTask>();
	}

	/**
	 * Add a monitored file with a FileChangeListener.
	 * 
	 * @param listener
	 *            listener to notify when the file changed.
	 * @param fileName
	 *            name of the file to monitor.
	 * @param period
	 *            polling period in milliseconds.
	 */
	public void addFileChangeListener(FileChangeListener listener,
			String fileName, long period) throws FileNotFoundException
	{
		removeFileChangeListener(listener, fileName);
		FileMonitorTask task = new FileMonitorTask(listener, fileName);
		timerEntries.put(fileName + listener.hashCode(), task);
		timer.schedule(task, period, period);
	}

	/**
	 * Remove the listener from the notification list.
	 * 
	 * @param listener
	 *            the listener to be removed.
	 */
	public void removeFileChangeListener(FileChangeListener listener,
			String fileName)
	{
		FileMonitorTask task = timerEntries.remove(fileName
				+ listener.hashCode());
		if (task != null)
		{
			task.cancel();			
		}
	}

	public void close()	
	{
		Iterator it = timerEntries.keySet().iterator();
		
		while(it.hasNext())
		{
			String key = (String)it.next();
			FileMonitorTask task = timerEntries.get(key);
			if (task != null)
			{
				task.cancel();
				logger.debug("FileMonitor: close: task.cancel() was just called task = " + task.toString() + " fileName = " + task.fileName);
			}
			
			timerEntries.clear();
		}
		
		if(timer != null)
		{
			timer.cancel();
			logger.debug("FileMonitor: close: timer.cancel() was just called");
		}
	}
	
	protected void fireFileChangeEvent(FileChangeListener listener,
			String fileName)
	{
		listener.fileChanged(fileName);
		//logger.debug("fireFileChangeEvent - filename = " + fileName + " just changed.");
	}

	class FileMonitorTask extends TimerTask
	{
		private FileChangeListener listener;

		private String fileName;

		private File monitoredFile;

		private long lastModified;

		public FileMonitorTask(FileChangeListener listener, String fileName)
				throws FileNotFoundException
		{
			this.listener = listener;
			this.fileName = fileName;
			this.lastModified = 0;

			monitoredFile = new File(fileName);
			if (!monitoredFile.exists())
			{ 
				URL fileURL = listener.getClass().getClassLoader().getResource(fileName);
				if (fileURL != null)
				{
					monitoredFile = new File(fileURL.getFile());
				}
				else
				{
					throw new FileNotFoundException("File Not Found: "
							+ fileName);
				}
			}
			this.lastModified = monitoredFile.lastModified();
		}

        @Override
		public void run()
		{
			long lastModified = monitoredFile.lastModified();
			
			if (lastModified != this.lastModified)
			{
				this.lastModified = lastModified;
				fireFileChangeEvent(this.listener, this.fileName);
			}
		}
	}
}
