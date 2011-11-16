package com.sangupta.luhnybin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Luhn {
	
	Reader reader;

	public static void main(String[] args) throws IOException {
		Luhn luhn = new Luhn();
		luhn.reader = new InputStreamReader(System.in);
		
		luhn.execute();
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

	private String filter(String line) {
		StringBuilder buffer = new StringBuilder();
		
		StringBuilder digits = new StringBuilder();
		
		boolean toBeReplaced = false;
		for(int index = 0; index < line.length(); index++) {
			char c = line.charAt(index);
			if(!isValid(c)) {
				if(digits.length() > 0) {
					if(toBeReplaced) {
						replaceDigits(digits);
					}
					
					buffer.append(digits.toString());
					digits.setLength(0);
				}
				
				buffer.append(c);
				toBeReplaced = false;
			} else {
				digits.append(c);
			}

			if(digits.length() >= 14) {
				if(isLuhn(digits)) {
					toBeReplaced = true;
				}
			}
		}
		
		if(digits.length() > 0) {
			if(toBeReplaced) {
				replaceDigits(digits);
			}
			
			buffer.append(digits.toString());
			digits.setLength(0);
		}
		
		return buffer.toString();
	}

	private void replaceDigits(StringBuilder digits) {
		final int len = digits.length();
		
		if(len < 14 || len > 16) {
			return;
		}
		
		for(int index = 0; index < len; index++) {
			char c = digits.charAt(index);
			if(isDigit(c)) {
				digits.setCharAt(index, 'X');
			}
		}
	}

	private boolean isLuhn(StringBuilder digits) {
		final int len = digits.length();
		
		int sum = 0;
		boolean twice = false;
		for(int index = len - 1; index >=0; index--) {
			char c = digits.charAt(index);
			if(isDigit(c)) {
				int digit = c - '0';
				
				if(twice) {
					digit *= 2;
				}
				
				sum += (digit / 10) + (digit % 10);
			} else {
				index--;
			}
			
			twice = !twice;
		}
		return sum % 10 == 0;
	}

	private boolean isValid(char c) {
		return isDigit(c) || c == ' ' || c == '_';
	}
	
	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

}
