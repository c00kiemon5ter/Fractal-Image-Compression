package fic;

import lib.Compress;
import lib.Decompress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * 
 */
class FicTask {

	private ExecutorService executor;

	public FicTask() {
		this.executor = Executors.newSingleThreadExecutor();
	}

	public void compress(String imagefilename, String compressedfilename) {
		RunnableFuture<Void> task = new FutureTask<Void>(new Compress(), null);
		executor.execute(task);
	}

	public void decompress(String compressedfilename, String imagefilename) {
		RunnableFuture<Void> task = new FutureTask<Void>(new Decompress(), null);
		executor.execute(task);
	}
}
