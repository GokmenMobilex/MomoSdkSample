package com.example.mockupbasicdeneme;

public enum Filter {
    EyesHeight("set eyes height", 0),
    MouthHeight("set mouth height", 1),
    EyebrowHeight("set eyebrow height", 2),
    Brightness("set brightness", 3),
    ChinHeight("set chin height", 4),
    BlackNWhite("set black & white", 5),
    Contrast("set contrast", 6),
    ChinSize("set chin size", 7),
    ChinWidth("set chin width", 8),
    EyebrowLifting("set eyebrow lifting", 9),
    EyebrowRotation("set eyebrow rotation", 10),
    EyebrowShape("set eyebrow shape", 11),
    EyeSize("set eye size", 12),
    EyeWidth("set eye width", 13),
    EyeDistance("set eye distance", 14),
    EyebrowSingleLift("set eyebrow single lift", 15),
    Gamma("set gamma", 16),
    Vignette("set vignette", 17),
    Vibrance("set vibrance", 18),
    Temperature("set temperature", 19),
    Structure("set structure", 20),
    Smile("set smile", 21),
    Sharpen("set sharpen", 22),
    Saturation("set saturation", 23),
    NoseWidth("set nose width", 24),
    NoseTip("set nose tip", 25),
    NoseSize("set nose size", 26),
    NoseNarrow("set nose narrow", 27),
    NoseHeight("set nose height", 28),
    Lighten("set lighten", 29);

    String description;
    int filterId;

    Filter(String description, int filterId) {
        this.description = description;
        this.filterId = filterId;
    }

    public static Filter getFilterByDesc(String description) {
        for (Filter filter : Filter.values()) {
            if (filter.description.equals(description)) return filter;
        }

        return null;
    }
}
