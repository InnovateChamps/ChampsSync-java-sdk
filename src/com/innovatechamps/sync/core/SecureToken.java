package com.innovatechamps.sync.core;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class SecureToken {
	public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

// 2048 bit keys should be secure until 2030 - https://web.archive.org/web/20170417095741/https://www.emc.com/emc-plus/rsa-labs/historical/twirl-and-rsa-key-size.htm
	public static final int SECURE_TOKEN_LENGTH = 43;

	private static final SecureRandom random = new SecureRandom();

	private static final char[] symbols = CHARACTERS.toCharArray();

	private static final char[] buf = new char[SECURE_TOKEN_LENGTH];

	private static final long xFFFFFF = 0xFFFFFF;
	private static final int xFF = 0xFF;

	private static final DateFormat SDF_MED = SimpleDateFormat.getDateTimeInstance( //
			SimpleDateFormat.MEDIUM, //
			SimpleDateFormat.MEDIUM);
	private static final SecureToken[] INSTANCES = new SecureToken[xFF + 1];

	private AtomicLong INC = new AtomicLong();
	private int instanceId = 0; // instanceId for different applications

	static { // initiate 0-255 instances, to avoid duplication
		for (int i = 0; i <= xFF; i++) {
			INSTANCES[i] = new SecureToken(i);
		}
	}

	private SecureToken(int instanceId) {
		this.instanceId = instanceId;
	}

	private long next() {
		return ((System.currentTimeMillis() >> 10) << 32) // timestamp
				+ ((INC.incrementAndGet() & xFFFFFF) << 8) // auto incremental
				+ instanceId // instance id
		;
	}

	public static long id() {
		return INSTANCES[0].next();
	}

	public static long id(int instanceId) {
		if (instanceId < 0 || instanceId > xFF)
			return INSTANCES[0].next();
		else
			return INSTANCES[instanceId].next();
	}

	public static String toString(long id) {
		String hex = Long.toHexString(id);
		return hex.subSequence(0, 8) //
				+ "-" + hex.substring(8, 14) //
				+ "-" + hex.substring(14);
	}

	public static String toStringLong(long id) {
		long time = (System.currentTimeMillis() >> 42 << 42) + (id >> 22);
		long inc = (id >> 8) & xFFFFFF;
		long instanceId = id & xFF;

		return id + " (DEC)"//
				+ "\n" + toString(id) + "  (HEX)" //
				+ "\ntime=" + SDF_MED.format(new Date(time)) + ", instanceId=" + instanceId + ", inc=" + inc;
	}

	/**
	 * Generate the next secure random token in the series.
	 */
	public static String nextToken() {
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}

}