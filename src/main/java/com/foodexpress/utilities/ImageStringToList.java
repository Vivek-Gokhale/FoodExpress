package com.foodexpress.utilities;

import java.util.Arrays;
import java.util.List;

public class ImageStringToList {
	public static List<String> convertStringToList(String imagesString) {
        // Remove square brackets and split by comma
        imagesString = imagesString.replace("[", "").replace("]", "").replace("'", "").trim();
        return Arrays.asList(imagesString.split(",\\s*"));
    }
}
