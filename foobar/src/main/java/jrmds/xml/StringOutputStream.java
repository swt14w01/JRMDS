package jrmds.xml;

import java.io.IOException;
import java.io.OutputStream;

public class StringOutputStream
	extends OutputStream {

	private StringBuilder _buffer = new StringBuilder();
	

	@Override
	public void write(int charAsByte) throws IOException {
		_buffer.append((char)charAsByte);
	}

	public StringBuilder GetStringBuilder()
	{
		return _buffer;
	}
	
	@Override
	public String toString()
	{
		return _buffer.toString();
	}
}
