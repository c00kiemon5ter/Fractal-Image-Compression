package app.task;

import java.util.Properties;

/**
 *
 */
public abstract class Task implements Runnable {

	protected Properties properties;

	Task(Properties properties) {
		this.properties = properties;
	}
}
