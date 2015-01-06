package com.example.classtable;

public class Lesson {
	private String name;
	private String rom;
	private int day;
	private int start;
	private int end;
	
	public Lesson(String n, String r, int d, int s, int e) {
		name = n;
		rom = r;
		day = d;
		start = s;
		end = e;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRom() {
		return rom;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
}
