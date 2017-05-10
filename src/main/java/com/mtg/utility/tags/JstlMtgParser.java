package com.mtg.utility.tags;


import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.regex.Pattern;

public class JstlMtgParser {

	private static Logger loger = Logger.getLogger(JstlMtgParser.class);

	public static String parseManaCost(String manaCost) {
		String[] arrayOfMana = manaCost.split(" ");
		String firstElement = arrayOfMana[0].trim();
		if (arrayOfMana.length == 1 && !firstElement.contains("/")) {
			return "<i class=\"ms ms-" + firstElement + " \" style=\"font-size: 1.4em;\"></i>";
		} else {
			StringBuilder result = new StringBuilder();
			for (String anArrayOfMana : arrayOfMana) {
				String[] multicoloredMana = anArrayOfMana.trim().split("/");
				if (multicoloredMana.length == 1) {
					result.append("<i class=\"ms ms-").append(anArrayOfMana.trim()).append(" \" style=\"font-size: 1.4em;\"></i>");
				} else {
					result.append("<i class=\"ms ms-").append(multicoloredMana[0].trim()).append(multicoloredMana[1].trim()).append(" ms-split ms-cost\" style=\"font-size: 1.4em;\"> </i>");
				}
			}
			return result.toString();
		}

	}


	public static String parseCardText(String cardText) {
		loger.info("-----------------------------------");
		//Splits text to array
		String[] cardTextArray = (cardText.replaceAll("\n", ", ")).split(" ");
		loger.info("card text array: " + Arrays.toString(cardTextArray));
		StringBuilder stringBuilder = new StringBuilder();
		for (String line : cardTextArray) {
			int start = line.indexOf("{");
			int end = line.lastIndexOf("}");
			if (line.contains("{")) {
				String[] splitedLine = line.split("[}][{]");
				if (start == 0) {
					if (splitedLine.length == 1) {
						parseTextWithCSSIcon(line, stringBuilder);
						addLatsPartOfParsedLine(stringBuilder, line, end);
					} else {
						loger.info("splited line: " + Arrays.toString(splitedLine));
						parseSplitedLineWithCSSIcons(stringBuilder, splitedLine);
						addLatsPartOfParsedLine(stringBuilder, line, end);
					}

				} else {
					stringBuilder.append(line.substring(0, start));
					if (splitedLine.length == 1) {
						parseTextWithCSSIcon(line.substring(start, end), stringBuilder);
						addLatsPartOfParsedLine(stringBuilder, line, end);
					}
					else {
						loger.info("splited line: " + Arrays.toString(splitedLine));
						parseSplitedLineWithCSSIcons(stringBuilder, splitedLine);
						addLatsPartOfParsedLine(stringBuilder, line, end);
					}

				}

			} else stringBuilder.append(line).append(" ");
		}
		loger.info("-----------------------------------");
		return stringBuilder.toString();
	}

	private static void parseSplitedLineWithCSSIcons(StringBuilder stringBuilder, String[] splitedLine) {
		for (String string : splitedLine) {
			parseTextWithCSSIcon(string, stringBuilder);
		}
	}

	private static void addLatsPartOfParsedLine(StringBuilder stringBuilder, String line, int end) {
		if (end < line.length()) {
			stringBuilder.append(line.substring(end + 1)).append(" ");
		}
	}

	private static void parseTextWithCSSIcon(String line, StringBuilder stringBuilder) {
		String[] splitedLine = line.replaceAll("[{};:,.]", "").split(" ");
		System.out.println("splited line is: " + Arrays.toString(splitedLine));
		Pattern pattern = Pattern.compile("\\d+");
		for (String clearLine : splitedLine) {
			if (pattern.matcher(clearLine).find()) {
				stringBuilder.append(clearLine);
				continue;
			}
			if (line.contains("/")) {
				stringBuilder.append("<i class=\"ms ms-").append(clearLine.replaceAll("/", "")).append(" ms-split ms-cost \"  style=\"font-size: 0.9em;\"> </i>");
			} else {
				stringBuilder.append("<i class=\"ms ms-").append(clearLine).append("\" style=\"font-size: 0.9em;\"> </i>");
			}
		}

	}


}
