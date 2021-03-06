package priv.chunyu.moto.DeviceDiscoveryCommands;

import java.io.IOException;
import java.util.logging.Logger;
import priv.chunyu.moto.DataProcesss.DataProcess;
import priv.chunyu.moto.XCMP.XCMPsocket;

/*
 * 1.Flag 要一樣 (data[5])  2.id要一樣(data[10]) 3.確保回正確的ack
 * 
*/
public class ESN extends XCMPsocket {
	public ESN() throws IOException, InterruptedException {
		super();
	}

	public static byte id[] = { (byte) 0x00, (byte) 0x04 };
	static String strClassName = ESN.class.getName();
	static Logger logger = Logger.getLogger(strClassName);
	static String esn;

	public static String request() throws IOException, InterruptedException {
		synchronized (lock) {
			checkflag();
			byte data[] = { (byte) 0x00, (byte) 0x0F, (byte) 0X00, (byte) 0x0B, (byte) 0X01, (byte) flag,
					(byte) RadioAddress[0], (byte) RadioAddress[1], (byte) MasterAddress[0], (byte) MasterAddress[1],
					(byte) id[0], (byte) id[1], (byte) 0X00, (byte) 0x03, (byte) 0X00, (byte) 0X0E, (byte) 0X09 };
			output.write(data);
			logger.info("Sending ESN Request");
			lock.wait(500);
		}
		return esn;
	}

	public static void reply(byte[] data) throws IOException, InterruptedException {
		synchronized (lock) {
			esn = DataProcess.ReadingData(14, data.length, data);
			logger.info("Electric Serial Number: " + esn);
			lock.notify();
		}
	}
}
