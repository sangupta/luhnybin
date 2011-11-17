package com.sangupta.luhnybin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Luhn {
	
	private Reader reader;

	private StringBuilder buffer = new StringBuilder();
	
	private StringBuilder digits = new StringBuilder();
	
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

	private String filter(String line) {
		
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
			
			if(digitsLength(digits) > 16 && !toBeReplaced) {
				buffer.append(digits.charAt(0));
				digits.delete(0, 1);
			}

			if(digitsLength(digits) >= 14) {
				if(isLuhn(digits)) {
					toBeReplaced = true;
				} else {
					if(toBeReplaced) {
						// the last string was a luhn
						// do the same
						digits.delete(digits.length() - 1, digits.length());
						replaceDigits(digits);
						
						buffer.append(digits.toString());
						digits.setLength(0);
						digits.append(c);
						
						toBeReplaced = false;
					}
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

	private int digitsLength(StringBuilder digits) {
		int len = 0;
		for(int index = 0; index < digits.length(); index++) {
			if(isDigit(digits.charAt(index))) {
				len++;
			}
		}
		return len;
	}

	private void replaceDigits(StringBuilder digits) {
		final int len = digits.length();
		
//		if(len < 14 || len > 16) {
//			return;
//		}
		
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
				twice = !twice;

			} else {
				continue;
			}
			
		}
		return sum % 10 == 0;
	}

	private boolean isValid(char c) {
		return isDigit(c) || c == ' ' || c == '-';
	}
	
	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

}
