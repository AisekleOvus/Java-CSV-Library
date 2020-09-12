package com.ovus.aisekle.csv;

public class AcsvlException extends Exception {
	public AcsvlException(String msg)  {
		super(msg);
	}
	public AcsvlException(String msg, Exception e)  {
		super(msg, e);
	}
}

class AcsvlFileFormatException extends Exception {
}
class AcsvlInputFormatException extends Exception {
}
