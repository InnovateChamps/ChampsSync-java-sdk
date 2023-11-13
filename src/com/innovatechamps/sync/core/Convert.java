package com.innovatechamps.sync.core;

public class Convert {

	public static final Double UNI_BITS = 0.125;
	public static final Double UNI_MB = 1000d;

	public static Double convertGbWithMb(double params1) {
		return params1 * UNI_MB;
	}

	public static Double convertMbWithGb(double params1) {
		return params1 / UNI_MB;
	}

}
