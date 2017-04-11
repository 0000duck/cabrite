package system.hmi;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.Method;

import com.imm.recorder.RecorderThread;
import com.keba.kemro.kvs.control.KvsFrame;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;
import com.keba.util.Utilities;

public class Main extends KvsFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Main() throws VartypeException, VarNotExistException, IOException, NetworkException {
		Thread recoderThread = new RecorderThread();
		recoderThread.start();
	}
	
	public void actionPerformed(final ActionEvent ev) {
		try {
			Utilities.invokeLater(new Invoker(this.getClass().getMethod(ev.getActionCommand(), null), this));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	class Invoker implements Runnable {
		private Method method;
		private Object target;
		/**
		 * Constructor
		 *
		 * @param m method to invoke
		 * @param target object on which the method should be invoced
		 */
		Invoker(Method m, Object target) {
			method = m;
			this.target = target;
		}
		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				method.invoke(target, null);
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}
}
