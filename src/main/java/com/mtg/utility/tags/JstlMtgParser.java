package com.mtg.utility.tags;


public class JstlMtgParser {

	public static String parseManaCost(String manaCost) {
		String[] arrayOfMana = manaCost.split(" ");
		String firstElement = arrayOfMana[0].trim();
		if (arrayOfMana.length == 1 && !firstElement.contains("/")) {
			return "<i class=\"ms ms-" + firstElement + " \" style=\"font-size: 1.4em;\"></i>";
		} else {
			String result = "";
			for (String anArrayOfMana : arrayOfMana) {
				String[] multicoloredMana = anArrayOfMana.trim().split("/");
				if (multicoloredMana.length == 1) {
					result = result + "<i class=\"ms ms-" + anArrayOfMana.trim() + " \" style=\"font-size: 1.4em;\"></i>";
				} else {
					result = result + "<i class=\"ms ms-" + multicoloredMana[0].trim() + multicoloredMana[1].trim() + " ms-split ms-cost\" style=\"font-size: 1.4em;\"> </i>";
				}
			}
			return result;
		}

	}

}
