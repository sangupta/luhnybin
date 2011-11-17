package com.sangupta.luhnybin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Luhn {
	
	private Reader reader;

	private StringBuilder buffer = new StringBuilder();
	
	private StringBuilder digits = new StringBuilder();
	
	private int digitsLength = 0;
	
	private boolean toBeReplaced = false;
	
	public static void main(String[] args) throws IOException {
		Luhn luhn = new Luhn();
		luhn.reader = new InputStreamReader(System.in);
		
		luhn.execute();
		//System.out.println(luhn.filter("7288-8379-3639-2755\n"));
	}

	public void execute() throws IOException {
		int chr;
		StringBuffer l = new StringBuffer();
		while ((chr = reader.read()) != -1) {
			l.append(Character.valueOf((char) chr));
		}

		String line = l.toString();
		line = filter(line);
		System.out.write(line.getBytes());
	}
	
	/**
	 * Empty digits into buffer
	 */
	private void emptyDigits() {
		buffer.append(digits.toString());
		digits.setLength(0);
		digitsLength = 0;
	}
	
	/**
	 * Check if replacement is necessary, and then empty digits into buffer
	 * 
	 */
	private void replaceAndEmptyIfNeeded() {
		if(digits.length() > 0) {
			if(toBeReplaced) {
				replaceDigits(digits);
			}
	
			emptyDigits();
		}
	}
	
	private void addDigit(char c) {
		digits.append(c);
		if(isDigit(c)) {
			digitsLength++;
		}
	}

	/**
	 * Filter the given line per LUHN rules
	 * 
	 * @param line
	 * @return
	 */
	private String filter(String line) {
		
		for(int index = 0; index < line.length(); index++) {
			char character = line.charAt(index);
			if(!isValidCharacter(character)) {
				replaceAndEmptyIfNeeded();
				
				buffer.append(character);
				toBeReplaced = false;
			} else {
				addDigit(character);
			}
			
			if(digitsLength > 16 && !toBeReplaced) {
				buffer.append(digits.charAt(0));
				digits.delete(0, 1);
			}

			if(digitsLength >= 14) {
				if(isLuhn()) {
					toBeReplaced = true;
				} else {
					if(toBeReplaced) {
						// the last string was a luhn
						// do the same
						digits.delete(digits.length() - 1, digits.length());
						replaceDigits(digits);
						
						emptyDigits();
						addDigit(character);
						
						toBeReplaced = false;
					}
				}
			}
		}
		
		replaceAndEmptyIfNeeded();
		
		return buffer.toString();
	}

	/**
	 * Replace the digits in the string with an 'X'
	 * 
	 * @param digits
	 */
	private void replaceDigits(StringBuilder digits) {
		final int length = digits.length();
		
		for(int index = 0; index < length; index++) {
			char character = digits.charAt(index);
			if(isDigit(character)) {
				digits.setCharAt(index, 'X');
			}
		}
	}

	/**
	 * Check if the given digits represent a LUHN number
	 * 
	 * @param digits
	 * @return
	 */
	private boolean isLuhn() {
		final int length = digits.length();
		
		int sum = 0;
		boolean twice = false;
		for(int index = length - 1; index >= 0; index--) {
			char c = digits.charAt(index);
			if(isDigit(c)) {
				int digit = c - '0';
				
				if(twice) {
					digit *= 2;
				}
				
				sum += (digit / 10) + (digit % 10);
				twice = !twice;

			} else {
				continue;
			}
			
		}
		return sum % 10 == 0;
	}

	/**
	 * Check if the character is a valid digit, or space or a dash/hyphen.
	 * 
	 * @param c
	 * @return
	 */
	private boolean isValidCharacter(char c) {
		return isDigit(c) || c == ' ' || c == '-';
	}
	
	/**
	 * Check if the character is a valid digit or not.
	 * 
	 * @param c
	 * @return
	 */
	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

}
