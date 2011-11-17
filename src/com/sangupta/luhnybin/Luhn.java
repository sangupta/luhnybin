package com.sangupta.luhnybin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Luhn {
	
	private static final char HYPHEN = '-';

	private static final char SPACE = ' ';

	private static final char DIGIT_NINE = '9';

	private static final char DIGIT_ZERO = '0';

	private static final int MAX_LENGTH = 16;

	private static final char REPLACEMENT = 'X';

	private static final int MIN_LENGTH = 14;

	private InputStream reader;
	
	private OutputStream out;

	private StringBuilder buffer = new StringBuilder();
	
	private StringBuilder digits = new StringBuilder();
	
	public Luhn() {
	}

	public static void main(String[] args) throws IOException {
		Luhn luhn = new Luhn();
		
		luhn.reader = System.in;
		luhn.out = System.out;
		
		luhn.execute();
	}

	public void execute() throws IOException {
		int character;
		while ((character = reader.read()) != -1) {
			if(!isValidCharacter(character)) {
				emptyCurrentBuffer();
				out.write(character);
			} else {
				buffer.append((char) character);
				
				if(isDigit(character)) {
					digits.append((char) character);
				}
				
				if(digits.length() < MIN_LENGTH) {
					continue;
				}
	
				int luhnLength = getLuhnLength();
				if(luhnLength > 0) {
					replaceLastLuhnLengthDigits(luhnLength);
				}
			}
		}
	}
	
	private void emptyCurrentBuffer() throws IOException {
		out.write(buffer.toString().getBytes());
		buffer.setLength(0);
		digits.setLength(0);
	}

	/**
	 * Check if replacement is necessary, and then empty digits into buffer
	 * 
	 */
	private void replaceLastLuhnLengthDigits(int count) throws IOException {
		for (int index = buffer.length() - 1; index >= 0; index--) {
			if(count == 0) {
				break;
			}

			if (isDigit(buffer.charAt(index))) {
				buffer.setCharAt(index, REPLACEMENT);
				count--;
			}
		}
	}
	
	/**
	 * Check if the given digits represent a LUHN number
	 * 
	 * @param digits
	 * @return
	 */
	private int getLuhnLength() {
		int totalLength = digits.length();
		final int length = totalLength < MAX_LENGTH ? totalLength : MAX_LENGTH;
		
		int sum = 0, luhnLength = 0;
		for(int index = 1; index <= length; index++) {
			int digit = digits.charAt(totalLength - index) - DIGIT_ZERO;
			
			if(index % 2 == 0) {
				digit *= 2;
			}
			
			sum += (digit / 10) + (digit % 10);
			
			if(index >= MIN_LENGTH) {
				if(sum % 10 == 0) {
					luhnLength = index;
				}
			}
		}
		
		return luhnLength;
	}
	
	/**
	 * Check if the character is a valid digit, or space or a dash/hyphen.
	 * 
	 * @param c
	 * @return
	 */
	private boolean isValidCharacter(int c) {
		return isDigit(c) || c == SPACE || c == HYPHEN;
	}
	
	/**
	 * Check if the character is a valid digit or not.
	 * 
	 * @param c
	 * @return
	 */
	private boolean isDigit(int c) {
		return (c >= DIGIT_ZERO && c <= DIGIT_NINE);
	}

}
